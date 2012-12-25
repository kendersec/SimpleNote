package org.kender.simplenote.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.kender.simplenote.exceptions.ConnectionFailed;

public class Util {

    /**
     * Encode the string into base64
     * @throws ConnectionFailed 
     */
    public static StringEntity encode64(String input) throws ConnectionFailed {
        Base64 enconder = new Base64();
        return toEntity(new String(enconder.encode(input.getBytes())));
    }
    
    public static StringEntity toEntity(String input) throws ConnectionFailed {
        try {
            return new StringEntity(input);
        } catch (UnsupportedEncodingException e) {
            throw new ConnectionFailed("Encoding exception on Base64 conversion.");
        }
    }
    
    public static String executeResquest(HttpUriRequest request) throws ConnectionFailed {
        HttpClient client = new DefaultHttpClient();
        
        try {
            HttpResponse response = client.execute(request);
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new ConnectionFailed("Status code: " + statusCode);
            }
            
            String token = null;
            if(response.getEntity() != null) {
                HttpEntity responseEntity = response.getEntity();
                token = EntityUtils.toString(responseEntity); // get body as a string into token
                response.getEntity().consumeContent();
             } 
             client.getConnectionManager().shutdown();
             
             return token;
        } catch (ConnectionFailed e) {
            throw e;
        } catch (Exception e) {
            throw new ConnectionFailed("Client couldn't execute the request.");
        }
    }
}
