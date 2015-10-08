package windyoak.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
    
    /**
     * Diese Methode dient dazu ein bestimmtes Projekt aus den Projekten zu 
     * extrahieren. 
     * 
     * @param id Die ID des gesuchten Projektes
     * @return Das Projekt, wenn vorhanden, sonst null.
     */
    public Project getProject(int id)
    {
        for (Project project : this.getProjects())
        {
            if (project.getId() == id)
            {
                return project;
            }
        }
        return null;
    }

}
