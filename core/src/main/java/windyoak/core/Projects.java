package windyoak.core;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felix Haller
 */
@XmlRootElement(name = "projects")
public class Projects {
    
    private List<Project> projects;

    // Diesen Konstruktor ist nötig, da das XML Framework das voraussetzt.
    public Projects()
    {
        
    }

    public Projects(List<Project> projects)
    {
        this.projects = projects;
    }

    @XmlElement(name = "project")
    public List<Project> getProjects()
    {
        return projects;
    }

    public void setPhoneUsers(List<Project> projects)
    {
        this.projects = projects;
    }

}