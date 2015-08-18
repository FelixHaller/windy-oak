package windyoak.core.impl;

import windyoak.core.StoreService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import windyoak.core.Project;
import windyoak.core.User;

/**
 *
 * @author fhaller1
 */
public class StoreServiceInMemory implements StoreService
{
    private HashMap<Integer, Project> projects;
    private int lastProjectID;
    
    private HashMap<Integer, User> users;

    public StoreServiceInMemory()
    {
        this.lastProjectID = 0;
        this.projects = new HashMap<>();
        
        this.users = new HashMap<>();
        
        //Test
        
        User testuser=new User();
             testuser.setForename("Konstantin");
             testuser.setSurname("Lorenz");
             testuser.setUsername("klorenz1");
        User testuser2=new User();
             testuser2.setForename("Felix");
             testuser2.setSurname("Haller");
             testuser2.setUsername("fhaller1");
        this.users.put(0, testuser);
        this.users.put(1, testuser2);
        Project myproject = new Project("FooBar");
        myproject.setCreator(testuser);
        this.projects.put(lastProjectID++, myproject);
        //Test-Ende
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
               
                _project.setId(lastProjectID);
        this.projects.put(lastProjectID++, _project);
        return _project;
    }

    @Override
    public Project deleteProject(int projectID) {
        Project old = this.projects.get(projectID);
        this.projects.remove(projectID);
        return old;
    }

    @Override
    public User getUser(int userID) {
        return this.users.get(userID);
    }

    @Override
    public List<User> fetchAllUsers() {
        return new ArrayList(users.values());
    }

    
    
    
}
