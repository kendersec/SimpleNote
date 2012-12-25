package org.kender.simplenote;

import org.apache.http.client.methods.HttpPost;
import org.kender.simplenote.exceptions.ConnectionFailed;
import org.kender.simplenote.util.Util;

public class SimpleNote {
    
    static final String AUTH_URL = "https://simple-note.appspot.com/api/login";
    static final String DATA_URL = "https://simple-note.appspot.com/api2/data";
    static final String INDEX_URL = "https://simple-note.appspot.com/api2/index";
    
    private String mEmail;
    private String mPassword;
    private String mToken;
    
    public SimpleNote(String email, String password) {
        try {
            mToken = authenticate(email, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String authenticate(String email, String password) throws ConnectionFailed {
        mEmail = email;
        mPassword = password;
        
        HttpPost request = new HttpPost(AUTH_URL);
        String params = "email=" + email + "&password=" + password;
                
        request.setEntity(Util.encode64(params));
        
        return Util.executeResquest(request);
    }
}
