package windyoak.core;

import java.net.URL;
import java.util.Date;

/**
 *
 * @author Felix Haller
 */
public class RSSPost {
    
    private String title;
    private URL url;
    private URL commentsURL;
    private Date pubDate;
    private String creator;
    private String id;
    private String description;

    public String getTitle()
    {
        return title;
    }

    public URL getUrl()
    {
        return url;
    }

    public URL getCommentsURL()
    {
        return commentsURL;
    }

    public Date getPubDate()
    {
        return pubDate;
    }

    public String getCreator()
    {
        return creator;
    }

    public String getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setUrl(URL url)
    {
        this.url = url;
    }

    public void setCommentsURL(URL commentsURL)
    {
        this.commentsURL = commentsURL;
    }

    public void setPubDate(Date pubDate)
    {
        this.pubDate = pubDate;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
    

}
