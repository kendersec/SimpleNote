package org.kender.simplenote;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.kender.simplenote.exceptions.ConnectionFailed;
import org.kender.simplenote.util.Util;

import com.google.gson.Gson;

public class SimpleNote {
    
    static final String AUTH_URL = "https://simple-note.appspot.com/api/login";
    static final String DATA_URL = "https://simple-note.appspot.com/api2/data";
    static final String INDEX_URL = "https://simple-note.appspot.com/api2/index";
    
    private String mEmail;
    private String mPassword;
    private String mToken;
    
    public SimpleNote(String email, String password) {
        mEmail = email;
        mPassword = password;
        mToken = null;
    }
    
    public String getToken() throws ConnectionFailed {
        if (mToken == null) {
            mToken = authenticate(mEmail, mPassword);
        }
        return mToken;
    }
    
    public SNote getNote(String noteID) throws ConnectionFailed {
        String params = "/" + noteID + "?auth=" + getToken() +"&email=" + mEmail;
        HttpGet request = new HttpGet(DATA_URL + params);
        
        String contents = Util.executeResquest(request);
        
        return new Gson().fromJson(contents, SNote.class);
    }
    
    public SNote addNote(String content) throws ConnectionFailed {
        SNote dummyNote = new SNote();
        dummyNote.setContent(content);
        
        return addNote(dummyNote);
    }
    
    public SNote addNote(SNote note) throws ConnectionFailed {
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
    
    //TODO: is SNote[] the best to return?
    public SNote[] getNoteList(int limit) throws ConnectionFailed {
        String params = "?auth=" + getToken() + "&email=" + mEmail + "&length=" + limit;
        HttpGet request = new HttpGet(INDEX_URL + params);
        
        String contents = Util.executeResquest(request);
        
        System.out.println(contents); //XXX
        
        return new SNote[] {new SNote()};
    }
    
    private static String authenticate(String email, String password) throws ConnectionFailed {        
        HttpPost request = new HttpPost(AUTH_URL);
        String params = "email=" + email + "&password=" + password;
                
        request.setEntity(Util.encode64(params));
        
        return Util.executeResquest(request);
    }
}
