package windyoak.core;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felix Haller
 */
@XmlRootElement(name = "Comment")
public class Comment {

    private int id;
    private User creator;
    private String title;
    private String content;
    private Date dateCreated;
    private Date dateUpdated;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private int projectID;

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    /**
     * Setzt das Datum anhand der Zeit seit dem 01.01.1970 in Millisekunden.
     *
     *
     * @param epochTime
     */
    public void setDateCreated(long epochTime) {
        this.dateCreated = new Date(epochTime);
    }

    /**
     * Setzt das Datum anhand der Zeit seit dem 01.01.1970 in Millisekunden.
     *
     * @param epochTime
     */
    public void setDateUpdated(long epochTime) {
        this.dateUpdated = new Date(epochTime);
    }

    

}
