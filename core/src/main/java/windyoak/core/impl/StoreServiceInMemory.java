package windyoak.core.impl;

import windyoak.core.StoreService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import windyoak.core.Comment;
import windyoak.core.OakCoreException;
import windyoak.core.Project;
import windyoak.core.Projects;
import windyoak.core.Tag;
import windyoak.core.User;

/**
 *
 * @author fhaller1
 */
public class StoreServiceInMemory implements StoreService {

    private HashMap<Integer, Project> projects;
    private int lastProjectID;

    private HashMap<Integer, User> users;

    public StoreServiceInMemory() {
        this.lastProjectID = 0;
        this.projects = new HashMap<>();

        this.users = new HashMap<>();

        //Test
        User testuser = new User();
        testuser.setForename("Konstantin");
        testuser.setSurname("Lorenz");
        testuser.setUsername("klorenz1");
        User testuser2 = new User();
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
    public List<Project> fetchAllProjects() {
        return new ArrayList(projects.values());
    }

    @Override
    public Project getProjectByID(int projectID) {
        return this.projects.get(projectID);
    }

    @Override
    public Project createProject(Project project) {
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
    public User getUser(String username) {
        return this.users.get(username);
    }

    @Override
    public List<User> fetchAllUsers() {
        return new ArrayList(users.values());
    }

    @Override
    public List<Comment> fetchAllComments(int ProjectsID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comment getCommentByID(int commentID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Project> fetchRecentProjects(int n) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comment deleteComment(int commentID) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comment createComment(Comment comment) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Project updateProject(Project project) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comment updateComment(Comment comment) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Project> searchProjectByName(String ProjectName, boolean recent) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tag getTagByName(String tagName) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tag> getTags() throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tag updateTagDescription(Tag newtag) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tag deleteTag(String tagName) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
