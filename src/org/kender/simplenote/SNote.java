package org.kender.simplenote;

import java.util.Arrays;
import java.util.Date;

/**
 * This class represents a note from SimpleNote with the following attributes
 * 
 * {
 *  key       : (string, note identifier, created by server),
 *  deleted   : (bool, whether or not note is in trash),
 *  modifydate: (last modified date, in seconds since epoch),
 *  createdate: (note created date, in seconds since epoch),
 *  syncnum   : (integer, number set by server, track note changes),
 *  version   : (integer, number set by server, track note content changes),
 *  minversion: (integer, number set by server, minimum version available for note),
 *  systemtags: [(Array of strings, some set by server)],
 *  tags      : [(Array of strings)],
 *  content   : (string, data content)
 * }
 * 
 * @author David Martinez del Corral
 *
 */

public class SNote {
    private String key;
    private int deleted;
    private String modifydate;
    private String createdate;
    private int syncnum;
    private int version;
    private int minversion;
    private String[] systemtags;
    private String[] tags;
    private String content;
    
    /**
     * Note identifier, created by server
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Whether or not note is in trash
     */
    public boolean getDeleted() {
        return deleted == 1;
    }
    
    /**
     * Whether or not note is in trash
     */
    public void setDeleted(boolean inTrash) {
        deleted = inTrash ? 1 : 0;
    }
    
    /**
     * Last modified date
     */
    public Date getModifyDate() {
        return new Date((long) (Float.parseFloat(modifydate) * 1000));
    }
    
    /**
     * Last modified date
     */
    public void setModifyDate(Date modifyDate) {
        this.modifydate = String.format("%d", modifyDate.getTime() / 1000);
    }
    
    /**
     * Note created date
     */
    public Date getCreateDate() {
        return new Date((long) (Float.parseFloat(createdate) * 1000));
    }
    
    /**
     * Note created date
     */
    public void setCreateDate(Date createDate) {
        this.createdate = String.format("%d", createDate.getTime() / 1000);
    }
    
    /**
     * @return the syncNum
     */
    public int getSyncNum() {
        return syncnum;
    }
    
    /**
     * @param syncNum the syncNum to set
     */
    public void setSyncNum(int syncNum) {
        this.syncnum = syncNum;
    }
    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }
    /**
     * @return the minversion
     */
    public int getMinversion() {
        return minversion;
    }
    /**
     * @param minversion the minversion to set
     */
    public void setMinversion(int minversion) {
        this.minversion = minversion;
    }
    
    /**
     * @return the systemTags
     */
    public String[] getSystemTags() {
        return systemtags;
    }
    /**
     * @param systemTags the systemTags to set
     */
    public void setSystemTags(String[] systemTags) {
        this.systemtags = systemTags;
    }
    /**
     * @return the tags
     */
    public String[] getTags() {
        return tags;
    }
    /**
     * @param tags the tags to set
     */
    public void setTags(String[] tags) {
        this.tags = tags;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String
                .format("SNote [key=%s, deleted=%s, modifydate=%s, createdate=%s, syncnum=%s, version=%s, minversion=%s, systemtags=%s, tags=%s, content=%s]",
                        key, getDeleted(), getModifyDate(), getCreateDate(), syncnum, version,
                        minversion, Arrays.toString(systemtags),
                        Arrays.toString(tags), content);
    }
}
