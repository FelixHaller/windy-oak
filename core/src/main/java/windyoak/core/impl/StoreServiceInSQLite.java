package windyoak.core.impl;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import windyoak.core.Comment;
import windyoak.core.OakCoreException;
import windyoak.core.Project;
import windyoak.core.StoreService;
import windyoak.core.Tag;
import windyoak.core.User;

/**
 * Verwaltet die Projektdaten in einer SQLite Datenbank.
 * 
 * 
 * @author Felix Haller
 */
public class StoreServiceInSQLite implements StoreService
{

    Connection connection;
    Statement statement;
    String sql;
    
    public StoreServiceInSQLite()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, "SQLite JDBC Treiber konnte nicht gefunden werden.", ex);
            
        }
        
    }
    
    private List<Project> fetchProjects(boolean recent, int count) throws OakCoreException
    {   
        ArrayList<Project> projects = new ArrayList<>();
        try
        {
            this.connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            statement = connection.createStatement();
            if (recent)
            {
                sql = String.format("select * from project "
                    + "where status = 'published' "
                    + "order by dateCreated desc limit %d", count);
            }
            else
            {
                sql = "select * from project "
                    + "where status = 'published' "
                    + "or status = 'closed' "
                    + "order by projectID";
            }
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
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        }        
        finally
        {
            try
            {
                statement.close();
                connection.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
                throw new OakCoreException("Fehler beim Schließen der Datenbankverbindung");
            }
        }

        return projects;
    }
    
    
    @Override
    public List<Project> fetchAllProjects() throws OakCoreException
    {
        return this.fetchProjects(false, 0);
    }
    
    @Override
    public List<Project> fetchRecentProjects(int n) throws OakCoreException
    {
        return this.fetchProjects(true, n);
    }
    
    private Project getProject(int projectID, boolean showDeleted) throws OakCoreException
    {
        Project project = new Project();
        ResultSet resultset;
        try
        {
            /* Allgemeine Infos zum Projekt holen */
            connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            statement = connection.createStatement();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler beim Verbinden zur Datenbank");
        }

        try
        {
            if (showDeleted)
            {
                sql =   "select count(*) count, * from project, user " +
                        "where project.creator = user.username " +
                        "and project.projectID = " + projectID;
            }
            else
            {
                sql =   "select count(*) count, * from project, user " +
                        "where project.creator = user.username " +
                        "and status != 'deleted' " +
                        "and project.projectID = " + projectID;
            }
            
            resultset = statement.executeQuery(sql);
            
            if (resultset.getInt("count") == 0)
            {
                return null;
            }
            
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
            
        }
        catch (SQLException ex)        
        {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei Datenbankabfrage.");
        }        
        finally
        {
            try
            {
                statement.close();
                connection.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
                throw new OakCoreException("Fehler beim Schließen der Datenbank-Verbindung");
            }
        }
        return project;
    }
    
    @Override
    public Project getProjectByID(int projectID) throws OakCoreException
    {
        return this.getProject(projectID, false);
    }

    @Override
    public List<User> fetchAllUsers() throws OakCoreException
    {
        ArrayList<User> users = new ArrayList<>();
        try
        {
            this.connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            statement = connection.createStatement();
        }
        catch (SQLException ex)
        {
            throw new OakCoreException("Fehler beim Verbinden zur Datenbank");
        }
        try
        {
            sql = "select * from user";
            ResultSet resultset = statement.executeQuery(sql);
            while (resultset.next())
            {
                User user = new User(resultset.getString("username"));
                user.setForename(resultset.getString("forename"));
                user.setSurname(resultset.getString("surname"));
                
                users.add(user);
            }
            resultset.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage.");
        }
        finally
        {
            try
            {
                statement.close();
                connection.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
                throw new OakCoreException("Fehler beim Schließen der Datenbankverbindung");
            }
        }

        return users;
    }

    @Override
    public User getUser(String username) throws OakCoreException
    {
        User user = new User();
        
        try
        {
            this.connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            statement = connection.createStatement();
            sql = String.format("select count(*) count, * from user where username='%s'", username);
            ResultSet resultset = statement.executeQuery(sql);
            if (resultset.getInt("count") == 0)
            {
                return null;
            }
            user.setUsername(resultset.getString("username"));
            user.setForename(resultset.getString("forename"));
            user.setSurname(resultset.getString("surname"));

            resultset.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        }
        finally
        {
            
            try
            {
                statement.close();
                connection.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
                throw new OakCoreException("Fehler beim schließen der Datenbankverbindung");
            }
        }

        return user;
    }


    @Override
    public Project deleteProject(int projectID) throws OakCoreException
    {
        try
        {
            this.connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            statement = connection.createStatement();
            String sql = String.format( "update project set status = 'deleted' " +
                                        "where projectID = %d", projectID);
            statement.executeUpdate(sql);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        }
        finally
        {
            try{
                statement.close();
                connection.close();
            }
            catch (SQLException ex)
            {
                
                throw new OakCoreException("Fehler beim Schließen der Datenbankverbindung");
            }
        }
        return this.getProject(projectID, true);
    }


    @Override
    public Project createProject(Project project) throws OakCoreException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateProject(int projectID, Project project) throws OakCoreException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public List<Comment> fetchAllComments(int projectID) throws OakCoreException
    {
        ArrayList<Comment> comments = new ArrayList<>();

        try
        {
            this.connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            statement = connection.createStatement();
            sql =    String.format(
                            "select * from comment, user " +
                            "where projectID=%d " +
                            "and creator=user.username " +
                            "and published=1",projectID);
            
            ResultSet resultset = statement.executeQuery(sql);
            while (resultset.next())
            {
                Comment comment = new Comment();
                comment.setContent(resultset.getString("content"));
                
                User creator = new User(resultset.getString("username"));
                creator.setForename(resultset.getString("forename"));
                creator.setSurname(resultset.getString("surname"));
                
                comment.setCreator(creator);
                comment.setDateCreated(resultset.getLong("dateCreated"));
                comment.setDateUpdated(resultset.getLong("dateUpdated"));
                comment.setId(resultset.getInt("commentID"));
                comment.setTitle(resultset.getString("title"));
                
                comments.add(comment);
            }
            resultset.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        }
        finally
        {
            try
            {
                statement.close();
                connection.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
                throw new OakCoreException("Fehler beim Schließen der Datenbankverbindung");
            }
        }


        return comments;
        
    }

    @Override
    public Comment getCommentByID(int commentID) throws OakCoreException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
