package org.kender.simplenote;

import java.util.Arrays;

/**
 * This class represents a tag from SimpleNote
 * 
 * A class is identified by it's name. This identifier
 * is not case sensitive although the tags will retain the casing.
 * 
 * E.g.: "TaG" and "tAg" refer to the same note. However one can modify
 *       the tag between the two.
 * 
 * @author David Martínez
 *
 */

public class STag {
    private int index;
    private int version = -1; //set only by the server
    private String name;
    private String[] share;
    
    /**
     * Get the position where the tag should appear in the list
     * of tags
     * 
     * @return might be -1 or share the value with another tag
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * Set the position of the tag
     * 
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }
    
    /**
     * Obtain the version number of the tag
     * 
     * @return
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * Obtain the tag name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Change the name of the tag
     */
    public void setName(String name) {
        // Only allow new notes and case changes
        if (this.name == null ||
                (name.toLowerCase() == this.name.toLowerCase())) {
            this.name = name;
        }
    }
    
    /**
     * Obtain the emails of the users it's shared with
     */
    public String[] getShare() {
        return share;
    }

    @Override
    public String toString() {
        return String.format("STag [index=%s, version=%s, name=%s, share=%s]", index,
                version, name, Arrays.toString(share));
    }
}
