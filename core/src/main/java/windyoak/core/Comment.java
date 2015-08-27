package windyoak.core;

import java.util.Date;

/**
 *
 * @author Felix Haller
 */
public class Comment {
    
    private int id;
    private User creator;
    private String title;
    private String content;
    private Date dateCreated;
    private Date dateUpdated;
    private boolean published;

    public int getId()
    {
        return id;
    }

    public User getCreator()
    {
        return creator;
    }

    public String getTitle()
    {
        return title;
    }

    public String getContent()
    {
        return content;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public Date getDateUpdated()
    {
        return dateUpdated;
    }

    public boolean isPublished()
    {
        return published;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCreator(User creator)
    {
        this.creator = creator;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public void setDateUpdated(Date dateUpdated)
    {
        this.dateUpdated = dateUpdated;
    }
    
    /**
     * Setzt das Datum anhand der Zeit seit dem 01.01.1970 in Millisekunden.
     * 
     * 
     * @param epochTime
     */
    public void setDateCreated(long epochTime)
    {
        this.dateCreated = new Date(epochTime);
    }

    /**
     * Setzt das Datum anhand der Zeit seit dem 01.01.1970 in Millisekunden.
     * 
     * @param epochTime
     */
    public void setDateUpdated(long epochTime)
    {
        this.dateUpdated = new Date(epochTime);
    }
    

    public void setPublished(boolean published)
    {
        this.published = published;
    }
    

}
