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
    
}
