package windyoak.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felix Haller
 */
@XmlRootElement(name = "projects")
public class Projects {
    
    private List<Project> projects = new ArrayList<>();

    // Diesen Konstruktor ist nötig, da das XML Framework das voraussetzt.
    public Projects()
    {
        
    }

    @XmlElement(name = "project")
    public List<Project> getProjects()
    {
        return projects;
    }

    public void setProjects(List<Project> projects)
    {
        this.projects = projects;
    }
    

}
