package org.kender.simplenote;

import java.util.Arrays;
import java.util.Date;

/**
 * This class represents a note from SimpleNote with the following attributes
 * 
 * @author David Martínez
 *
 */

public class SNote {
    private String key; //string, note identifier, created by server
    private int deleted; //int [bool], whether or not note is in trash
    private String modifydate; //string [float], last modified date, in seconds since epoch
    private String createdate; //string [float], note created date, in seconds since epoch
    private int syncnum; //integer, number set by server, track note changes
    private int version; //integer, number set by server, track note content changes
    private int minversion; //integer, number set by server, minimum version available for note
    private String sharekey; //string, shared note identifier, set by server
    private String publishkey; //string, published note identifier, set by server
    private String[] systemtags; //Array of strings, some set by server
    private String[] tags; //Array of strings
    private String content; //string, data content
    
    /**
     * Note identifier, created by server
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Whether or not note is in trash
     */
    public boolean inTrash() {
        return deleted == 1;
    }
    
    /**
     * Move the note to the trash
     */
    public void trash() {
        deleted = 1;
    }
    
    /**
     * Restore the note from the trash
     * (Move it back to the inbox)
     */
    public void restore() {
        deleted = 0;
    }
    
    /**
     * Last modified date
     */
    public Date getModifiedDate() {
        return new Date((long) (Float.parseFloat(modifydate) * 1000));
    }
    
    /**
     * Last modified date
     */
    public void setModifiedDate(Date modifyDate) {
        this.modifydate = String.format("%d", modifyDate.getTime() / 1000);
    }
    
    /**
     * Note creation date
     */
    public Date getCreationDate() {
        return new Date((long) (Float.parseFloat(createdate) * 1000));
    }
    
    /**
     * Note creation date
     */
    public void setCreationDate(Date createDate) {
        this.createdate = String.format("%d", createDate.getTime() / 1000);
    }
    
    /**
     * Number set by server, track note changes
     */
    public int getSyncNum() {
        return syncnum;
    }
    
    /**
     * Number set by server, track note content changes
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * Number set by server, minimum version available for note
     */
    public int getMinversion() {
        return minversion;
    }
    
    /**
     * Shared note identifier
     */
    public String getShareKey() {
        return sharekey;
    }
    
    /**
     * Published note identifier
     */
    public String getPublishKey() {
        return publishkey;
    }
    
    /**
     * Obtain system tags (some set by server)
     * E.g.: pinned
     */
    public String[] getSystemTags() {
        return systemtags;
    }
    
    /**
     * Set system tags (some set by server)
     * E.g.: pinned
     */
    public void setSystemTags(String[] systemTags) {
        this.systemtags = systemTags;
    }
    
    /**
     * List of tags this note belongs to
     */
    public String[] getTags() {
        return tags;
    }
    
    /**
     * Set the list of tags the note belongs to
     * @param tags the tags shouldn't contain spaces or commas
     * if they do, they will be trimmed
     */
    public void setTags(String[] tags) {
        // No commas or spaces on the tags
        for (String tag: tags) {
            tag = tag.replaceAll(",", "").replaceAll(" ", "");
        }
        this.tags = tags;
    }
    
    /**
     * Obtain the content of the note
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Set the content of the note (it will completely replace the old one)
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return String
                .format("SNote [key=%s, deleted=%s, modifydate=%s, createdate=%s, syncnum=%s, version=%s, minversion=%s, systemtags=%s, tags=%s, content=%s]",
                        key, inTrash(), getModifiedDate(), getCreationDate(), syncnum, version,
                        minversion, Arrays.toString(systemtags),
                        Arrays.toString(tags), content);
    }
}
