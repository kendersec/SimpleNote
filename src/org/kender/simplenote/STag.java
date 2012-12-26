package org.kender.simplenote;

/**
 * This class represents a tag from SimpleNote with the following attributes
 * 
 * @author David Martínez
 *
 */

public class STag {
    private int index;
    private int version;
    private String name;
    
    /**
     * Obtain the tag name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("STag [index=%s, version=%s, name=%s]", index,
                version, name);
    }
}
