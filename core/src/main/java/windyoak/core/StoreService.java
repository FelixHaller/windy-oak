package windyoak.core;

import java.util.List;

 /**
 *
 * @author fhaller1
 */
public interface StoreService
{
    //Project createProject(String name);
    List<Project> fetchAllProjects();
    Project getProjectByID(int projectID);
    void setProject(int projectID, Project project );
    Project addProject(Project project);
    Project deleteProject(int prjectID);
    //void deleteProject(int projectID);
    
    //Comment createComment();
    //Comment getCommentByID(int userID);
 
    
}
