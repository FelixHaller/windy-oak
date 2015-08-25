package windyoak.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
    private List<User> members;
    private ArrayList<Tag> tags;

    @XmlElement(name = "tag")
    @XmlElementWrapper(name = "tags")
    public ArrayList<Tag> getTags()
    {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags)
    {
        this.tags = tags;
    }
    
    @XmlElement(name = "member")
    @XmlElementWrapper(name = "members")
    public List<User> getMembers()
    {
        return members;
    }

    public void setMembers(List<User> members)
    {
        this.members = members;
    }
    
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

    public void setDateCreated(long epochTime)
    {
        this.dateCreated = new Date(epochTime);
    }

    public void setDateUpdated(Date dateUpdated)
    {
        this.dateUpdated = dateUpdated;
    }
    
    public void setDateUpdated(long epochTime)
    {   
        this.dateUpdated = new Date(epochTime);
    }
    

    public void setStatus(String status)
    {
        this.status = status;
    }
    

}
