package windyoak.core.impl;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import windyoak.core.Project;
import windyoak.core.StoreService;
import windyoak.core.Tag;
import windyoak.core.User;

/**
 *
 * @author Felix Haller
 */
public class StoreServiceInSQLite implements StoreService
{

    Connection connection;

    public StoreServiceInSQLite()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    @Override
    public List<Project> fetchAllProjects()
    {
        ArrayList<Project> projects = new ArrayList<>();

        try
        {
            this.connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            Statement statement = connection.createStatement();
            String sql = "select * from project";
            ResultSet resultset = statement.executeQuery(sql);
            while (resultset.next())
            {
                Project project = new Project(resultset.getString("title"));
                project.setId(resultset.getInt("projectID"));

                project.setDateCreated(resultset.getLong("dateCreated"));
                // Wir nehmen an, dass kein Projekt 1970 aktualisiert wurde
                // Diese Ausnahme ist möglich, da SQLite einen leeren Wert als 0
                // ausgibt und nicht als NULL
                if (resultset.getLong("dateUpdated") > 0)
                {
                    project.setDateUpdated(resultset.getLong("dateUpdated"));
                }
                project.setStatus(resultset.getString("status"));

                projects.add(project);
            }
            resultset.close();
            statement.close();
            connection.close();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }

        return projects;
    }

    @Override
    public Project getProjectByID(int projectID)
    {
        Project project = new Project();
        String sql;
        Statement statement;
        ResultSet resultset;
        try
        {
            /* Allgemeine Infos zum Projekt holen */
            connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            statement = connection.createStatement();
        }
        catch (SQLException e)
        {
            System.err.println("Fehler bei Verbindungsherstellung: " + e.getMessage());
            return project;
        }
        try
        {
            sql =   "select * from project, user " +
                    "where project.creator = user.username " +
                    "and project.projectID = " + projectID;
            resultset = statement.executeQuery(sql);
            project.setTitle(resultset.getString("title"));
            project.setId(resultset.getInt("projectID"));
            project.setDescription(resultset.getString("description"));
            project.setDateCreated(resultset.getLong("dateCreated"));
            // Wir nehmen an, dass kein Projekt 1970 aktualisiert wurde
            // Diese Ausnahme ist möglich, da SQLite einen leeren Wert als 0
            // ausgibt und nicht als NULL
            if (resultset.getLong("dateUpdated") > 0)
            {
                project.setDateUpdated(resultset.getLong("dateUpdated"));
            }
            project.setStatus(resultset.getString("status"));
            
            User user = new User(resultset.getString("creator"));
            user.setForename(resultset.getString("forename"));
            user.setSurname(resultset.getString("surname"));
            project.setCreator(user);
            
            /* Informationen zu Projektmitgliedern holen */
            sql =   "select user.* from user,project, projectmember " +
                    "where project.projectID = projectmember.projectID " +
                    "and projectmember.username = user.username " +
                    "and project.projectID= " + projectID;
            
            resultset = statement.executeQuery(sql);
            
            ArrayList<User> members = new ArrayList<>();
            while (resultset.next())
            {
                User member = new User(resultset.getString("username"));
                member.setForename(resultset.getString("forename"));
                member.setSurname(resultset.getString("surname"));
                members.add(member);
            }
            project.setMembers(members);
            
            /*Informationen zu ProjectTags holen*/
            sql =   "select tag.* from project, projecttag, tag "+
                    "where project.projectID = projecttag.projectID "+
                    "and projecttag.tagName = tag.tagName "+
                    "and project.projectID= " + projectID;
            resultset = statement.executeQuery(sql);
            
            ArrayList<Tag> tags = new ArrayList<>();
            while(resultset.next())
            {
                Tag tag = new Tag(  resultset.getString("tagName"), 
                                    resultset.getString("description"));
                tags.add(tag);
            }
            project.setTags(tags);
            
            resultset.close();
            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return project;
}

}
