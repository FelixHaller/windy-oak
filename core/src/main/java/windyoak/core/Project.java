package windyoak.core;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felix Haller
 */
@XmlRootElement(name = "project")
public class Project
{
    private int id;
    private User creator;
    private String title;
    private String description;
    private Date dateCreated;
    private Date dateUpdated;
    private String status;
    
    public Project()
    {
        
    }
    
    public Project(String title)
    {
        this.title = title;
    }
    
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

    public String getDescription()
    {
        return description;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public Date getDateUpdated()
    {
        return dateUpdated;
    }

    public String getStatus()
    {
        return status;
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

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public void setDateUpdated(Date dateUpdated)
    {
        this.dateUpdated = dateUpdated;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    

}
