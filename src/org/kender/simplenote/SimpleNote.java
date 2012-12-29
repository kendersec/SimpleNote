package org.kender.simplenote;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.kender.simplenote.exceptions.ConnectionFailed;
import org.kender.simplenote.util.Util;

import com.google.gson.Gson;

/**
 * Class for interacting with the SimpleNote web service
 * simplenoteapp.com
 * 
 * @author David Martínez
 *
 */
public class SimpleNote {
    
    public static final int MAX_FETCH_NOTES = 20;
    public static final int MAX_FETCH_TAGS = 100;
    
    private static final String AUTH_URL = "https://simple-note.appspot.com/api/login";
    private static final String DATA_URL = "https://simple-note.appspot.com/api2/data";
    private static final String INDEX_URL = "https://simple-note.appspot.com/api2/index";
    private static final String TAGS_URL = "https://simple-note.appspot.com/api2/tags";
    
    private String mEmail;
    private String mPassword;
    private String mToken;
    
    public SimpleNote(String email, String password) {
        mEmail = email;
        mPassword = password;
        mToken = null;
    }
    
    /**
     * Method to retrieve an auth token.
     *
     *   The cached global token is looked up and returned if it exists. If it
     *   is null a new one is requested and returned.
     *   
     * @return SimpleNote API token
     * @throws ConnectionFailed
     */
    public String getToken() throws ConnectionFailed {
        if (mToken == null) {
            mToken = authenticate(mEmail, mPassword);
        }
        return mToken;
    }
    
    /**
     * Method to get a specific note
     * 
     * @param noteID ID of the note to fetch
     * @return the note object
     * @throws ConnectionFailed
     */
    public SNote getNote(String noteID) throws ConnectionFailed {
        String params = "/" + noteID + "?auth=" + getToken() +"&email=" + mEmail;
        HttpGet request = new HttpGet(DATA_URL + params);
        
        String contents = Util.executeResquest(request);
        
        return new Gson().fromJson(contents, SNote.class);
    }
    
    /**
     * Add a new note with some content
     * 
     * @param content the content of the new note
     * @return the new note object
     * @throws ConnectionFailed
     */
    public SNote addNote(String content) throws ConnectionFailed {
        SNote dummyNote = new SNote();
        dummyNote.setContent(content);
        
        return updateNote(dummyNote);
    }
    
    /**
     * Update the note
     * 
     * @param note object with (presumably) some values updated.
     * @return the new note object
     * 
     * If no changes have been made by other clients since the
     * last update, then content will not be included in the response
     * note object. If there have been other changes then content will
     * be returned and the client should update their local store
     * accordingly.
     * 
     * @throws ConnectionFailed
     */
    public SNote updateNote(SNote note) throws ConnectionFailed {
        String params;
        if (note.getKey() != null) { //Existing note
            params = "/" + note.getKey() + "?auth=" + getToken() +"&email=" + mEmail;
        } else { //New note
            params = "?auth=" + getToken() +"&email=" + mEmail;
        }
        
        HttpPost request = new HttpPost(DATA_URL + params);
        String noteDump = new Gson().toJson(note);
        request.setEntity(Util.toEntity(noteDump));
        
        String contents = Util.executeResquest(request);
        SNote modifiedNote = new Gson().fromJson(contents, SNote.class);
        
        return modifiedNote;
    }
    
    /**
     * Delete (obliterate as opposed to move to trash) a note with a certain ID
     * 
     *   Note: To move a note to trash flag the note as "trash()" and update
     *   the note on the server.
     * 
     * @param noteID ID of the note to delete
     * @throws ConnectionFailed
     */
    public void deleteNote(String noteID) throws ConnectionFailed {
        SNote note = getNote(noteID);
        deleteNote(note);
    }
    
    /**
     * Delete (obliterate as opposed to move to trash) a note
     * 
     *   Note: To move a note to trash flag the note as "trash()" and update
     *   the note on the server.
     * 
     * @param note note to delete
     * @throws ConnectionFailed
     */
    public void deleteNote(SNote note) throws ConnectionFailed {
        String params = "/" + note.getKey() + "?auth=" + getToken() + "&email=" + mEmail;
        HttpDelete request = new HttpDelete(DATA_URL + params);
        
        Util.executeResquest(request); // Empty response, not interesting
    }
    
    /**
     * Obtain the list of notes (this includes the notes in the trash) without content.
     * 
     * Limited to {@value #MAX_FETCH_NOTES} notes. Use {@link #getNoteList(int, boolean)} to change this.
     * 
     * @return
     * @throws ConnectionFailed
     */
    public SNote[] getNoteList() throws ConnectionFailed {
        return getNoteList(MAX_FETCH_NOTES, false);
    }
    
    /**
     * Obtain the list of notes (this includes the notes in the trash).
     * 
     * Limited to {@value #MAX_FETCH_NOTES} notes. Use {@link #getNoteList(int)} to change this.
     * 
     * @poram withContent Add the content of the notes on the response or not
     * @return
     * @throws ConnectionFailed
     */
    public SNote[] getNoteList(boolean withContent) throws ConnectionFailed {
        return getNoteList(MAX_FETCH_NOTES, withContent);
    }
    
    /**
     * Obtain the list of notes (this includes the notes in the trash)
     * 
     * @param limit maximum number of notes to fetch
     * @return
     * @throws ConnectionFailed
     */
    public SNote[] getNoteList(int limit, boolean withContent) throws ConnectionFailed {
        String params = "?auth=" + getToken() + "&email=" + mEmail + "&content=" + withContent;
        HttpGet request = new HttpGet(INDEX_URL + params + "&length=" + limit);
        
        String contents = Util.executeResquest(request);
        
        SNoteIndex index = new Gson().fromJson(contents, SNoteIndex.class);
        ArrayList<SNote> notes = index.getNotes();
        
        while (index.getMark() != null && notes.size() < limit) {
            if ((limit - notes.size()) < MAX_FETCH_NOTES) {
                request = new HttpGet(INDEX_URL + params + "&length=" + (limit - notes.size()));
            } else {
                request = new HttpGet(INDEX_URL + params + "&length=" + MAX_FETCH_NOTES);
            }
            contents = Util.executeResquest(request);
            index = new Gson().fromJson(contents, SNoteIndex.class);
            notes.addAll(index.getNotes());
        }
        
        return notes.toArray(new SNote[0]);
    }
    
    /*------------------------------------
     * Tags operations
     -------------------------------------*/
    
    /**
     * Get tag info from name
     * 
     * @param tagName
     * @return
     * @throws ConnectionFailed
     */
    public STag getTag(String tagName) throws ConnectionFailed {
        String params = "/" + tagName + "?auth=" + getToken() +"&email=" + mEmail;
        HttpGet request = new HttpGet(TAGS_URL + params);
        
        String contents = Util.executeResquest(request);
        
        return new Gson().fromJson(contents, STag.class);
    }
    
    /**
     * Add a new tag
     * 
     * This can also be done by adding the tag to the note
     * 
     * @param tag new tag name
     */
    public STag addTag(String tagName) throws ConnectionFailed {
        STag dummyTag = new STag();
        dummyTag.setName(tagName);
        
        return updateTag(dummyTag);
    }
    
    /**
     * Update the tag. Only the case of the tag or the index can be changed
     * 
     * @param tag object
     * @return the new tag object
     * 
     * @throws ConnectionFailed
     */
    public STag updateTag(STag tag) throws ConnectionFailed {
        String params;
        if (tag.getVersion() != -1) { //Existing tag
            params = "/" + tag.getName() + "?auth=" + getToken() +"&email=" + mEmail;
        } else { //New tag
            params = "?auth=" + getToken() +"&email=" + mEmail;
        }
        
        HttpPost request = new HttpPost(TAGS_URL + params);
        String tagDump = new Gson().toJson(tag);
        request.setEntity(Util.toEntity(tagDump));
        
        String contents = Util.executeResquest(request);
        STag modifiedTag = new Gson().fromJson(contents, STag.class);
        
        return modifiedTag;
    }
    
    /**
     * Delete a tag. Will not currently remove the tag from notes that still have this tag
     * 
     * @param tag tag object
     * @throws ConnectionFailed
     */
    public STag deleteTag(STag tag) throws ConnectionFailed {
        return deleteTag(tag.getName());
    }
    
    /**
     * Delete a tag. Will not currently remove the tag from notes that still have this tag
     * 
     * @param tagName name of the tag to delete
     * @throws ConnectionFailed
     */
    public STag deleteTag(String tagName) throws ConnectionFailed {
        String params = "/" + tagName + "?auth=" + getToken() + "&email=" + mEmail;
        HttpDelete request = new HttpDelete(TAGS_URL + params);
        
        String contents = Util.executeResquest(request);
        STag deletedTag = new Gson().fromJson(contents, STag.class);
        
        return deletedTag;
    }
    
    /**
     * Get all the tags.
     * 
     * Limited to {@value #MAX_FETCH_TAGS} notes. Use {@link #getTags(int)} to change this.
     * 
     * @return
     * @throws ConnectionFailed
     */
    public STag[] getTags() throws ConnectionFailed {
        return getTags(MAX_FETCH_TAGS);
    }
    
    /**
     * Get all the tags
     * 
     * @param limit maximum number of tags to fetch
     * @return
     * @throws ConnectionFailed
     */
    public STag[] getTags(int limit) throws ConnectionFailed {
        String params = "?auth=" + getToken() + "&email=" + mEmail;
        HttpGet request = new HttpGet(TAGS_URL + params + "&length=" + limit);
        
        String contents = Util.executeResquest(request);
        
        TagIndex tagIndex = new Gson().fromJson(contents, TagIndex.class);
        ArrayList<STag> tags = tagIndex.getTags();
        
        while (tagIndex.getMark() != null && tags.size() < limit) {
            if ((limit - tags.size()) < MAX_FETCH_TAGS) {
                request = new HttpGet(INDEX_URL + params + "&length=" + (limit - tags.size()));
            } else {
                request = new HttpGet(INDEX_URL + params + "&length=" + MAX_FETCH_TAGS);
            }
            contents = Util.executeResquest(request);
            tagIndex = new Gson().fromJson(contents, TagIndex.class);
            tags.addAll(tagIndex.getTags());
        }
        
        return tags.toArray(new STag[0]);
    }
    
    /*-----------------------------------------------------
     * Private members and private static nested classes
     ------------------------------------------------------*/
    
    /**
     * Method to get simplenote auth token
     */
    private static String authenticate(String email, String password) throws ConnectionFailed {        
        HttpPost request = new HttpPost(AUTH_URL);
        String params = "email=" + email + "&password=" + password;
                
        request.setEntity(Util.encode64(params));
        
        return Util.executeResquest(request);
    }
    
    /**
     * Pojo class to read the JSON response when the index of notes
     * is requested.
     */
    private final static class SNoteIndex {
        private ArrayList<SNote> data;
        // When the response is too long we might need to make another request
        // with this mark as a parameter to continue fetching objects
        private String mark;
        
        public ArrayList<SNote> getNotes() {
            return data;
        }
        
        public String getMark() {
            return mark;
        }
    }
    
    /**
     * Pojo class to read the JSON response when the tags
     * are requested.
     */
    private final static class TagIndex {
        private ArrayList<STag> tags;
        // When the response is too long we might need to make another request
        // with this mark as a parameter to continue fetching objects
        private String mark;
        
        public ArrayList<STag> getTags() {
            return tags;
        }
        
        public String getMark() {
            return mark;
        }
    }
}
