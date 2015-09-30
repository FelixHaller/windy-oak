package windyoak.core.impl;

import java.util.List;
import java.sql.*;
import java.util.Date;
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
public class StoreServiceInSQLite implements StoreService {

    Connection connection;
    Statement statement;
    String sql;
    String errorMessage;

    public StoreServiceInSQLite() {

    }

    private void establishConnection() throws OakCoreException {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            statement = connection.createStatement();
        } catch (ClassNotFoundException ex) {
            errorMessage = "SQLite JDBC Treiber konnte nicht gefunden werden.";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } catch (SQLException ex) {
            errorMessage = "Verbindung zu Datenbank konnte nicht hergestellt werden.";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        }

    }

    private void endConnection() throws OakCoreException {
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            errorMessage = "Fehler beim Schließen der Datenbankverbindung";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        }
    }

    private List<Project> fetchProjects(boolean recent, int count) throws OakCoreException {
        ArrayList<Project> projects = new ArrayList<>();
        this.establishConnection();
        try {
            if (recent) {
                sql = String.format("select * from project "
                        + "where status = 'published' "
                        + "order by dateCreated desc limit %d", count);
            } else {
                sql = "select * from project "
                        + "where status = 'published' "
                        + "or status = 'closed' "
                        + "order by projectID";
            }
            ResultSet resultset = statement.executeQuery(sql);
            while (resultset.next()) {
                Project project = new Project(resultset.getString("title"));
                project.setId(resultset.getInt("projectID"));

                project.setDateCreated(resultset.getLong("dateCreated"));
                // Wir nehmen an, dass kein Projekt 1970 aktualisiert wurde
                // Diese Ausnahme ist möglich, da SQLite einen leeren Wert als 0
                // ausgibt und nicht als NULL
                if (resultset.getLong("dateUpdated") > 0) {
                    project.setDateUpdated(resultset.getLong("dateUpdated"));
                }
                project.setStatus(resultset.getString("status"));

                projects.add(project);
            }
            resultset.close();
        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        } finally {
            this.endConnection();
        }

        return projects;
    }

    @Override
    public List<Project> fetchAllProjects() throws OakCoreException {
        return this.fetchProjects(false, 0);
    }

    @Override
    public List<Project> fetchRecentProjects(int n) throws OakCoreException {
        return this.fetchProjects(true, n);
    }

    private Project getProject(int projectID, boolean showDeleted) throws OakCoreException {
        Project project = new Project();
        ResultSet resultset;

        this.establishConnection();

        try {
            if (showDeleted) {
                sql = "select count(*) count, * from project, user "
                        + "where project.creator = user.username "
                        + "and project.projectID = " + projectID;
            } else {
                sql = "select count(*) count, * from project, user "
                        + "where project.creator = user.username "
                        + "and status != 'deleted' "
                        + "and project.projectID = " + projectID;
            }

            resultset = statement.executeQuery(sql);

            if (resultset.getInt("count") == 0) {
                return null;
            }

            project.setTitle(resultset.getString("title"));
            project.setId(resultset.getInt("projectID"));
            project.setDescription(resultset.getString("description"));
            project.setDateCreated(resultset.getLong("dateCreated"));
            // Wir nehmen an, dass kein Projekt 1970 aktualisiert wurde
            // Diese Ausnahme ist möglich, da SQLite einen leeren Wert als 0
            // ausgibt und nicht als NULL
            if (resultset.getLong("dateUpdated") > 0) {
                project.setDateUpdated(resultset.getLong("dateUpdated"));
            }
            project.setStatus(resultset.getString("status"));

            User user = new User(resultset.getString("creator"));
            user.setForename(resultset.getString("forename"));
            user.setSurname(resultset.getString("surname"));
            project.setCreator(user);

            /* Informationen zu Projektmitgliedern holen */
            sql = "select user.* from user,project, projectmember "
                    + "where project.projectID = projectmember.projectID "
                    + "and projectmember.username = user.username "
                    + "and project.projectID= " + projectID;

            resultset = statement.executeQuery(sql);

            ArrayList<User> members = new ArrayList<>();
            while (resultset.next()) {
                User member = new User(resultset.getString("username"));
                member.setForename(resultset.getString("forename"));
                member.setSurname(resultset.getString("surname"));
                members.add(member);
            }
            project.setMembers(members);

            /*Informationen zu ProjectTags holen*/
            sql = "select tag.* from project, projecttag, tag "
                    + "where project.projectID = projecttag.projectID "
                    + "and projecttag.tagName = tag.tagName "
                    + "and project.projectID= " + projectID;
            resultset = statement.executeQuery(sql);

            ArrayList<Tag> tags = new ArrayList<>();
            while (resultset.next()) {
                Tag tag = new Tag(resultset.getString("tagName"),
                        resultset.getString("description"));
                tags.add(tag);
            }
            project.setTags(tags);
            resultset.close();

        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei Datenbankabfrage.");
        } finally {
            this.endConnection();
        }
        return project;
    }

    @Override
    public Project getProjectByID(int projectID) throws OakCoreException {
        return this.getProject(projectID, false);
    }

    @Override
    public List<User> fetchAllUsers() throws OakCoreException {
        ArrayList<User> users = new ArrayList<>();
        this.establishConnection();
        try {
            sql = "select * from user";
            ResultSet resultset = statement.executeQuery(sql);
            while (resultset.next()) {
                User user = new User(resultset.getString("username"));
                user.setForename(resultset.getString("forename"));
                user.setSurname(resultset.getString("surname"));

                users.add(user);
            }
            resultset.close();
        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage.");
        } finally {
            this.endConnection();
        }

        return users;
    }

    @Override
    public User getUser(String username) throws OakCoreException {
        User user = new User();
        this.establishConnection();
        try {
            sql = String.format("select count(*) count, * from user where username='%s'", username);
            ResultSet resultset = statement.executeQuery(sql);
            if (resultset.getInt("count") == 0) {
                return null;
            }
            user.setUsername(resultset.getString("username"));
            user.setForename(resultset.getString("forename"));
            user.setSurname(resultset.getString("surname"));

            resultset.close();
        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        } finally {
            this.endConnection();
        }

        return user;
    }

    @Override
    public Project deleteProject(int projectID) throws OakCoreException {
        this.establishConnection();
        try {
            String sql = String.format("update project set status = 'deleted' "
                    + "where projectID = %d", projectID);
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        } finally {
            this.endConnection();
        }
        return this.getProject(projectID, true);
    }

    @Override
    public Project createProject(Project project) throws OakCoreException {
        this.establishConnection();

        try {
            sql = String.format(
                    "INSERT INTO project "
                    + "(creator, title, description, dateCreated, status) "
                    + "VALUES("
                    + "'%s',"
                    + "'%s',"
                    + "'%s',"
                    + " %d, "
                    + "'%s')",
                    project.getCreator().getUsername(),
                    project.getTitle(),
                    project.getDescription(),
                    new Date().getTime(),
                    project.getStatus()
            );
            //while(project.getMembers())
            statement.executeUpdate(sql);
            int newProjectID = statement.getGeneratedKeys().getInt(1);

            return this.getProjectByID(newProjectID);
        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } finally {
            this.endConnection();
        }
    }

    @Override
    public Project updateProject(Project project) throws OakCoreException {
        this.establishConnection();
        project.setDateUpdated(new Date().getTime());
        try {
            sql = String.format(
                    "UPDATE project "
                    + "SET "
                    + "creator='%s', "
                    + "title='%s', "
                    + "description='%s', "
                    + "dateUpdated=%d, "
                    + "status='%s' "
                    + "WHERE projectID = %d",
                    project.getCreator().getUsername(),
                    project.getTitle(),
                    project.getDescription(),
                    project.getDateUpdated().getTime(),
                    project.getStatus(),
                    project.getId()
            );
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } finally {
            this.endConnection();
        }
        return project;
    }

    @Override
    public List<Comment> fetchAllComments(int projectID) throws OakCoreException {
        ArrayList<Comment> comments = new ArrayList<>();
        this.establishConnection();
        try {
            sql = String.format(
                    "select * from comment, user "
                    + "where projectID=%d "
                    + "and creator=user.username "
                    + "and published=1", projectID);

            ResultSet resultset = statement.executeQuery(sql);
            if (!resultset.next()) {
                this.endConnection();
                return null;
            }
            do {

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
                comment.setProjectID(resultset.getInt("projectID"));

                comments.add(comment);
            } while (resultset.next());

            resultset.close();
        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        } finally {
            this.endConnection();
        }

        return comments;

    }

    @Override
    public Comment getCommentByID(int commentID) throws OakCoreException {
        this.establishConnection();
        ResultSet resultset;
        Comment comment = new Comment();
        try {

            sql = "select count(*) count, * from comment, user "
                    + "where comment.creator = user.username "
                    + "and comment.commentID = " + commentID;

            resultset = statement.executeQuery(sql);
            if (resultset.getInt("count") == 0) {
                return null;
            }
            comment.setContent(resultset.getString("content"));
            User creator = new User(resultset.getString("username"));
            creator.setForename(resultset.getString("forename"));
            creator.setSurname(resultset.getString("surname"));

            comment.setCreator(creator);
            comment.setDateCreated(resultset.getLong("dateCreated"));
            if (resultset.getLong("dateUpdated") > 0) {
                comment.setDateUpdated(resultset.getLong("dateUpdated"));
            }
            comment.setId(resultset.getInt("commentID"));
            comment.setTitle(resultset.getString("title"));
            comment.setProjectID(resultset.getInt("projectID"));
            if (resultset.getInt("published") == 1) {
                comment.setPublished(true);
            } else {
                comment.setPublished(false);
            }

        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } finally {
            this.endConnection();
        }
        return comment;

    }

    @Override
    public Comment deleteComment(int commentID) throws OakCoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comment updateComment(Comment comment) throws OakCoreException {
        this.establishConnection();
        int publish;
        if (comment.isPublished()) {
            publish = 1;
        } else {
            publish = 0;
        }
        comment.setDateUpdated(new Date().getTime());
        try {
            sql = String.format(
                    "UPDATE comment "
                    + "SET "
                    + "creator='%s', "
                    + "title='%s', "
                    + "content='%s', "
                    + "dateUpdated=%d, "
                    + "published=%d, "
                    + "projectID=%d "
                    + "WHERE commentID = %d",
                    comment.getCreator().getUsername(),
                    comment.getTitle(),
                    comment.getContent(),
                    comment.getDateUpdated().getTime(),
                    publish,
                    comment.getProjectID(),
                    comment.getId()
            );
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } finally {
            this.endConnection();
        }
        return comment;
    }

    @Override
    public Comment createComment(Comment comment) throws OakCoreException {
        this.establishConnection();

        int publish;
        if (comment.isPublished()) {
            publish = 1;
        } else {
            publish = 0;
        }

        try {
            sql = String.format(
                    "INSERT INTO comment "
                    + "(creator, title, content, dateCreated, published, projectID) "
                    + "VALUES("
                    + "'%s',"
                    + "'%s',"
                    + "'%s',"
                    + " %d, "
                    + " %d, "
                    + " %d)",
                    comment.getCreator().getUsername(),
                    comment.getTitle(),
                    comment.getContent(),
                    new Date().getTime(),
                    publish,
                    comment.getProjectID()
            );
            //while(project.getMembers())
            statement.executeUpdate(sql);
            int newCommentID = statement.getGeneratedKeys().getInt(1);

            return this.getCommentByID(newCommentID);
        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } finally {
            this.endConnection();
        }
    }

}
