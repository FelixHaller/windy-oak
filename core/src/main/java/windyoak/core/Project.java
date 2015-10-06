package windyoak.core;

import java.net.URL;
import java.util.Date;
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
    private Members members;
    private Tags tags;
    private URL postsURL;

    
    public Project()
    {

    }
    
    public Project(String title)
    {
        this.title = title;
    }
    
    /**
     * Diese Methode liefert den relativen Pfad (URL) zu den Posts des Projektes.
     * 
     * 
     * @return
     */
    public String getPosts()
    {
        return "/projects/"+this.id+"/posts";
    }
    
    public void setPosts(String foobar)
    {
        //Dummy
    }
    
    /**
     * Diese Methode liefert den relativen Pfad (URL) zu den Kommentaren zum Projekt.
     * 
     * 
     * @return
     */
    public String getComments()
    {
        return "/projects/" + this.id + "/comments";
    }
    
    public void setComments(String foobar)
    {
        //Dummy
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


    @XmlElement(name = "tags")
    public Tags getTags()
    {
        return tags;
    }

    public void setTags(Tags tags)
    {
        this.tags = tags;
    }

    @XmlElement(name = "members")
    public Members getMembers()
    {
        return members;
    }

    public void setMembers(Members members)
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
