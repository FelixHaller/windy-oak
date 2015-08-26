package windyoak.core;

import java.util.List;

 /**
 *
 * @author fhaller1
 */
public interface StoreService
{
    //Project
    List<Project> fetchAllProjects();
    List<Project> fetchRecentProjects(int count);
    Project getProjectByID(int projectID);
    Project createProject(Project project);
    void updateProject(int projectID, Project project);
    Project deleteProject(int prjectID);
    
    //User
    List<User> fetchAllUsers();
    User getUser(String username);
    
    //Project/Comments
    List<Comment> fetchAllComments(int ProjectsID);
    Comment getCommentByID(int commentID);
    
}
