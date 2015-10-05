package windyoak.core.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import windyoak.core.Comment;
import windyoak.core.OakCoreException;
import windyoak.core.Project;
import windyoak.core.ProjectMember;
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
        List<Project> projects = new ArrayList<>();
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
            Iterator<Project> projectIterator = projects.iterator();
            while (projectIterator.hasNext()) {
                Project nextProject = projectIterator.next();
                //ProjectTags abrufen
                sql = "select tag.* from project, projecttag, tag "
                        + "where project.projectID = projecttag.projectID "
                        + "and projecttag.tagName = tag.tagName "
                        + "and project.projectID= " + nextProject.getId();
                resultset = statement.executeQuery(sql);

                ArrayList<Tag> tags = new ArrayList<>();
                while (resultset.next()) {
                    Tag tag = new Tag(resultset.getString("tagName"),
                            resultset.getString("description"));
                    tags.add(tag);
                }
                nextProject.setTags(tags);
                //ProjectMember abrufen
                sql = "select user.*, projectmember.role from user,project, projectmember "
                        + "where project.projectID = projectmember.projectID "
                        + "and projectmember.username = user.username "
                        + "and project.projectID= " + nextProject.getId();

                resultset = statement.executeQuery(sql);

                ArrayList<ProjectMember> members = new ArrayList<>();
                while (resultset.next()) {
                    ProjectMember member = new ProjectMember();

                    User nuser = new User(resultset.getString("username"));
                    nuser.setForename(resultset.getString("forename"));
                    nuser.setSurname(resultset.getString("surname"));

                    member.setUser(nuser);
                    member.setRole(resultset.getString("role"));
                    members.add(member);
                }
                nextProject.setMembers(members);
            }
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

            try {
                project.setPostsURL(new URL(resultset.getString("postsURL")));
            } catch (MalformedURLException ex) {
                Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.WARNING, "URL für Posts aus Datenbank ungültig.", ex);
            }

            User user = new User(resultset.getString("creator"));
            user.setForename(resultset.getString("forename"));
            user.setSurname(resultset.getString("surname"));
            project.setCreator(user);

            /* Informationen zu Projektmitgliedern holen */
            sql = "select user.*, projectmember.role from user,project, projectmember "
                    + "where project.projectID = projectmember.projectID "
                    + "and projectmember.username = user.username "
                    + "and project.projectID= " + projectID;

            resultset = statement.executeQuery(sql);

            ArrayList<ProjectMember> members = new ArrayList<>();
            while (resultset.next()) {
                ProjectMember member = new ProjectMember();

                User nuser = new User(resultset.getString("username"));
                nuser.setForename(resultset.getString("forename"));
                nuser.setSurname(resultset.getString("surname"));

                member.setUser(nuser);
                member.setRole(resultset.getString("role"));
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
            errorMessage = "Fehler bei Datenbankabfrage.";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
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
        int newProjectID;
        try {
            sql = String.format(
                    "INSERT INTO project "
                    + "(creator, title, description, dateCreated, status, postsURL) "
                    + "VALUES("
                    + "'%s',"
                    + "'%s',"
                    + "'%s',"
                    + " %d ,"
                    + "'%s',"
                    + "'%s')",
                    project.getCreator().getUsername(),
                    project.getTitle(),
                    project.getDescription(),
                    new Date().getTime(),
                    project.getStatus(),
                    project.getPostsURL()
            );
            //while(project.getMembers())
            statement.executeUpdate(sql);
            newProjectID = statement.getGeneratedKeys().getInt(1);

            List<ProjectMember> memberList = project.getMembers();
            Iterator<ProjectMember> itMember = memberList.iterator();
            while (itMember.hasNext()) {
                ProjectMember member = itMember.next();
                sql = String.format(
                        "INSERT INTO projectmember "
                        + "(projectID, username, role) "
                        + "VALUES("
                        + "'%d',"
                        + "'%s',"
                        + "'%s')",
                        newProjectID,
                        member.getUser().getUsername(),
                        member.getRole()
                );
                statement.executeUpdate(sql);
            }

        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } finally {
            this.endConnection();
        }
        return this.getProjectByID(newProjectID);

    }

    @Override
    public Project updateProject(Project project) throws OakCoreException {
        this.establishConnection();
        project.setDateUpdated(new Date().getTime());
        PreparedStatement deleteOldMembers = null;
        PreparedStatement updateProject = null;
        PreparedStatement createNewMembers = null;
        ProjectMember member;
        String newMember;
        String delete;
        try {
            statement.close();//wird nicht benötigt.
            connection.setAutoCommit(false); //Auto Commit aus zum Schutz der Datenintegrität.

            sql = "UPDATE project "
                    + "SET "
                    + "creator= '" + project.getCreator().getUsername() + "', "
                    + "title='" + project.getTitle() + "', "
                    + "description='" + project.getDescription() + "', "
                    + "dateUpdated=" + project.getDateUpdated().getTime() + ", "
                    + "status='" + project.getStatus() + "' "
                    + "WHERE projectID = " + project.getId() + "";

            delete = "DELETE FROM "
                    + "projectmember "
                    + "WHERE projectID=" + project.getId();
            newMember = "INSERT INTO projectmember "
                    + "(projectID, username, role) "
                    + "VALUES(?, ?, ?)";
            List<ProjectMember> memberList = project.getMembers();
            Iterator<ProjectMember> itMember = memberList.iterator();

            deleteOldMembers = connection.prepareStatement(delete);
            deleteOldMembers.executeUpdate();
            createNewMembers = connection.prepareStatement(newMember);

            while (itMember.hasNext()) {
                member = itMember.next();
                createNewMembers.setInt(1, project.getId());
                createNewMembers.setString(2, member.getUser().getUsername());
                createNewMembers.setString(3, member.getRole());
                createNewMembers.executeUpdate();
            }

            updateProject = connection.prepareStatement(sql);
            updateProject.executeUpdate();

            connection.commit();

        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } finally {
            try {
                deleteOldMembers.close();
                updateProject.close();
                createNewMembers.close();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                errorMessage = "Fehler bei Datenbankabfrage";
                Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
                throw new OakCoreException(errorMessage);
            }
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
                    + "and status != 'deleted'", projectID);

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
                comment.setStatus(resultset.getString("status"));

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
            comment.setStatus(resultset.getString("status"));

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
        this.establishConnection();
        try {
            String sql = String.format("update comment set status = 'deleted' "
                    + "where commentID = %d", commentID);
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        } finally {
            this.endConnection();
        }
        return this.getComment(commentID, true);
    }

    private Comment getComment(int commentID, boolean showDeleted) throws OakCoreException {
        Comment comment = new Comment();
        ResultSet resultset;

        this.establishConnection();

        if (showDeleted) {
            sql = "select count(*) count, * from comment, user "
                    + "where comment.creator = user.username "
                    + "and comment.commentID = " + commentID;
        } else {
            sql = "select count(*) count, * from commentID, user "
                    + "where comment.creator = user.username "
                    + "and status != 'deleted' "
                    + "and comment.commentID = " + commentID;
        }

        try {
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
            comment.setStatus(resultset.getString("status"));
        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comment;
    }

    @Override
    public Comment updateComment(Comment comment) throws OakCoreException {
        this.establishConnection();
        int publish;
        comment.setDateUpdated(new Date().getTime());
        try {
            sql = String.format(
                    "UPDATE comment "
                    + "SET "
                    + "creator='%s', "
                    + "title='%s', "
                    + "content='%s', "
                    + "dateUpdated=%d, "
                    + "status='%s', "
                    + "projectID=%d "
                    + "WHERE commentID = %d",
                    comment.getCreator().getUsername(),
                    comment.getTitle(),
                    comment.getContent(),
                    comment.getDateUpdated().getTime(),
                    comment.getStatus(),
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

        try {
            sql = String.format(
                    "INSERT INTO comment "
                    + "(creator, title, content, dateCreated, published, projectID) "
                    + "VALUES("
                    + "'%s',"
                    + "'%s',"
                    + "'%s',"
                    + " %d, "
                    + "'%s', "
                    + " %d)",
                    comment.getCreator().getUsername(),
                    comment.getTitle(),
                    comment.getContent(),
                    new Date().getTime(),
                    comment.getStatus(),
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

    @Override
    public List<Project> searchProjectByName(String SearchEx, boolean recent) throws OakCoreException {
        List<Project> projects = new ArrayList<>();
        this.establishConnection();
        try {
            if (recent) {
                sql = "select * from project "
                        + "where status = 'published' "
                        + "and project.title LIKE '" + SearchEx + "'"
                        + "order by dateCreated desc";
            } else {
                sql = "select * from project "
                        + "where (status = 'published' "
                        + "or status = 'closed') "
                        + "and project.title LIKE '" + SearchEx + "'"
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
            Iterator<Project> projectIterator = projects.iterator();
            while (projectIterator.hasNext()) {
                Project nextProject = projectIterator.next();
                //ProjectTags abrufen
                sql = "select tag.* from project, projecttag, tag "
                        + "where project.projectID = projecttag.projectID "
                        + "and projecttag.tagName = tag.tagName "
                        + "and project.projectID= " + nextProject.getId();
                resultset = statement.executeQuery(sql);

                ArrayList<Tag> tags = new ArrayList<>();
                while (resultset.next()) {
                    Tag tag = new Tag(resultset.getString("tagName"),
                            resultset.getString("description"));
                    tags.add(tag);
                }
                nextProject.setTags(tags);
                //ProjectMember abrufen
                sql = "select user.*, projectmember.role from user,project, projectmember "
                        + "where project.projectID = projectmember.projectID "
                        + "and projectmember.username = user.username "
                        + "and project.projectID= " + nextProject.getId();

                resultset = statement.executeQuery(sql);

                ArrayList<ProjectMember> members = new ArrayList<>();
                while (resultset.next()) {
                    ProjectMember member = new ProjectMember();

                    User nuser = new User(resultset.getString("username"));
                    nuser.setForename(resultset.getString("forename"));
                    nuser.setSurname(resultset.getString("surname"));

                    member.setUser(nuser);
                    member.setRole(resultset.getString("role"));
                    members.add(member);
                }
                nextProject.setMembers(members);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new OakCoreException("Fehler bei der Datenbankabfrage");
        } finally {
            this.endConnection();
        }

        return projects;
    
}

}
