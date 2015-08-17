package windyoak.core.impl;

import windyoak.core.StoreService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import windyoak.core.Project;

/**
 *
 * @author fhaller1
 */
public class StoreServiceInMemory implements StoreService
{
    private HashMap<Integer, Project> projects;
    private int lastID;

    public StoreServiceInMemory()
    {
        this.lastID = 0;
        this.projects = new HashMap<>();
        
        this.projects.put(lastID++, new Project("FooBar"));
    }

    @Override
    public List<Project> fetchAllProjects()
    {
        return new ArrayList(projects.values());
    }

    @Override
    public Project getProjectByID(int projectID)
    {
        return this.projects.get(projectID);
    }

    @Override
    public void setProject(int projectID, Project project) {
        this.projects.put(projectID,project);
    }

    @Override
    public Project addProject(Project project) {
        Project _project = project; 
               
                _project.setId(lastID);
        this.projects.put(lastID, _project);
        return _project;
    }

    @Override
    public Project deleteProject(int prjectID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
}
