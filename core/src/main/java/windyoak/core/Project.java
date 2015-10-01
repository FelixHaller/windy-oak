package windyoak.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Repräsentiert ein Projekt.
 * 
 * Ein Projekt enthält u.a. Projektmitglieder(members) und wird mit Stichwörtern 
 * (tags) beschrieben.
 * 
 * @author felix
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
    private URL postsURL;


    public Project()
    {

    }
    
    @XmlTransient
    public URL getPostsURL()
    {
        return this.postsURL;
    }

    public void setPostsURL(URL postsURL)
    {
        this.postsURL = postsURL;
    }

    public Project(String title)
    {
        this.title = title;
    }

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
    
    @XmlAttribute 
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

    public void setDateUpdated(Date dateUpdated)
    {
        this.dateUpdated = dateUpdated;
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
    
    /**
     * Setze Status des Projektes
     * 
     * Mögliche Status sind: new, published, closed
     * 
     * new: Erstellt, aber noch nicht veröffentlicht
     * published: veröffentlicht
     * closed: abgeschlossen
     * 
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

}
