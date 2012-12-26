package org.kender.simplenote;

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
    
    private static final String AUTH_URL = "https://simple-note.appspot.com/api/login";
    private static final String DATA_URL = "https://simple-note.appspot.com/api2/data";
    private static final String INDEX_URL = "https://simple-note.appspot.com/api2/index";
    private static final int MAX_FETCH_NOTES = 20;
    
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
     * @param note object with (presumably) some values updated
     * @return the new note object
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
        
        // SimpleNote doesn't set the content of the new note, so we set it ourselves
        modifiedNote.setContent(note.getContent());
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
     * Obtain the list of notes (this includes the notes in the trash).
     * 
     * Limited to {@value #MAX_FETCH_NOTES} notes. Use {@link #getNoteList(int)} to change this.
     * 
     * @return
     * @throws ConnectionFailed
     */
    public SNote[] getNoteList() throws ConnectionFailed {
        return getNoteList(MAX_FETCH_NOTES);
    }
    
    /**
     * Obtain the list of notes (this includes the notes in the trash)
     * 
     * @param limit maximum number of notes to fetch
     * @return
     * @throws ConnectionFailed
     */
    public SNote[] getNoteList(int limit) throws ConnectionFailed {
        String params = "?auth=" + getToken() + "&email=" + mEmail + "&length=" + limit;
        HttpGet request = new HttpGet(INDEX_URL + params);
        
        String contents = Util.executeResquest(request);
        
        SNoteIndex index = new Gson().fromJson(contents, SNoteIndex.class);
        
        return index.getNotes();
    }
    
    /*
     * Private members and private static nested classes
     */
    
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
        private SNote[] data;
        
        public SNote[] getNotes() {
            return data;
        }
    }
}
