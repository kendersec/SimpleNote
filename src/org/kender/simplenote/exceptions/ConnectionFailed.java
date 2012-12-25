package org.kender.simplenote.exceptions;

public class ConnectionFailed extends Exception {
    
    private static final long serialVersionUID = -8804719209432764470L;
    
    public ConnectionFailed(String msg) {
        super(msg);
    }

}
