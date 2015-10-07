package windyoak.core.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import windyoak.core.Comment;
import windyoak.core.Comments;
import windyoak.core.OakCoreException;
import windyoak.core.Project;
import windyoak.core.Member;
import windyoak.core.Members;
import windyoak.core.Projects;
import windyoak.core.StoreService;
import windyoak.core.Tag;
import windyoak.core.Tags;
import windyoak.core.User;
import windyoak.core.Users;

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

    private Projects fetchProjects(boolean recent, int count) throws OakCoreException {
        Projects projects = new Projects();
        this.establishConnection();
        try {
            if (recent) {
                sql = String.format("select * from project "
                        + "where status = 'published' "
                        + "COLLATE NOCASE "
                        + "order by dateCreated desc limit %d", count);
            } else {
                sql = "select * from project "
                        + "where status = 'published' "
                        + "COLLATE NOCASE "
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
                project.setDescription(resultset.getString("description"));
                project.setCreator(null);

                projects.getProjects().add(project);
            }
            resultset.close();
            for (Project nextProject : projects.getProjects())
            {
                //ProjectTags abrufen
                sql = "select tag.* from project, projecttag, tag "
                        + "where project.projectID = projecttag.projectID "
                        + "and projecttag.tagName = tag.tagName "
                        + "and project.projectID= " + nextProject.getId();
                resultset = statement.executeQuery(sql);

                Tags tags = new Tags();
                while (resultset.next()) {
                    Tag tag = new Tag(resultset.getString("tagName"),
                            resultset.getString("description"));
                    tags.getTags().add(tag);
                }
                nextProject.setTags(tags);
                //ProjectMember abrufen
                sql = "select user.*, projectmember.role from user,project, projectmember "
                        + "where project.projectID = projectmember.projectID "
                        + "and projectmember.username = user.username "
                        + "and project.projectID= " + nextProject.getId();

                resultset = statement.executeQuery(sql);

                Members members = new Members();
                while (resultset.next()) {
                    Member member = new Member();

                    User nuser = new User(resultset.getString("username"));
                    nuser.setForename(resultset.getString("forename"));
                    nuser.setSurname(resultset.getString("surname"));

                    member.setUser(nuser);
                    member.setRole(resultset.getString("role"));
                    members.getMembers().add(member);
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
    public Projects fetchAllProjects() throws OakCoreException {
        return this.fetchProjects(false, 0);
    }

    @Override
    public Projects fetchRecentProjects(int n) throws OakCoreException {
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
                this.endConnection();
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

            String postsURL = resultset.getString("postsURL");
            
            // Nur wenn auch eine URL hinterlegt ist
            if (postsURL != null && ! postsURL.matches("\\s*"))
            {
                try {
                    project.setPostsURL(new URL(postsURL));
                } 
                catch (MalformedURLException ex) {
                    Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.WARNING, "URL für Posts aus Datenbank ungültig.");
                }
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

            Members members = new Members();
            while (resultset.next()) {
                Member member = new Member();

                User nuser = new User(resultset.getString("username"));
                nuser.setForename(resultset.getString("forename"));
                nuser.setSurname(resultset.getString("surname"));

                member.setUser(nuser);
                member.setRole(resultset.getString("role"));
                members.getMembers().add(member);
            }
            project.setMembers(members);

            /*Informationen zu ProjectTags holen*/
            sql = "select tag.* from project, projecttag, tag "
                    + "where project.projectID = projecttag.projectID "
                    + "and projecttag.tagName = tag.tagName "
                    + "and project.projectID= " + projectID;
            resultset = statement.executeQuery(sql);

            Tags tags = new Tags();
            while (resultset.next()) {
                Tag tag = new Tag(resultset.getString("tagName"),
                        resultset.getString("description"));
                tags.getTags().add(tag);
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
    public Users fetchAllUsers() throws OakCoreException {
        Users users = new Users();
        this.establishConnection();
        try {
            sql = "select * from user";
            ResultSet resultset = statement.executeQuery(sql);
            while (resultset.next()) {
                User user = new User(resultset.getString("username"));
                user.setForename(resultset.getString("forename"));
                user.setSurname(resultset.getString("surname"));

                users.getUsers().add(user);
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
                this.endConnection();
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
            connection.setAutoCommit(false);
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

            Members memberList = project.getMembers();
            Iterator<Member> itMember = memberList.getMembers().iterator();
            
            while (itMember.hasNext()) {
                Member member = itMember.next();
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
            if (project.getTags() != null) 
            {
                Tags tags = project.getTags();
                for (Tag newTag : tags.getTags())
                {
                    sql = String.format("INSERT INTO projecttag "
                            + "(projectID,tagName)"
                            + "VALUES("
                            + "%d,"
                            + "'%s')",
                            newProjectID,
                            newTag.getName()
                    );
                    statement.executeUpdate(sql);

                }

            connection.commit();
            connection.setAutoCommit(true);
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
        PreparedStatement deleteTags = null;
        PreparedStatement createTags = null;

        Member member;

        String newMember;
        String delete;
        String deleteTagsSt;
        String createTagsSt;

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

            deleteTagsSt = "DELETE FROM "
                    + "projecttag "
                    + "WHERE projectID="
                    + project.getId();

            createTagsSt = "INSERT "
                    + "INTO projecttag "
                    + "(projectID, tagName) "
                    + "VALUES(?,?)";

            Members memberList = project.getMembers();
            Iterator<Member> itMember = memberList.getMembers().iterator();

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

            
           
            if (project.getTags() != null)
            {
                deleteTags = connection.prepareStatement(deleteTagsSt);
                deleteTags.executeUpdate();
                
                Tags tags = project.getTags();
                createTags = connection.prepareStatement(createTagsSt);
                for (Tag newTag : tags.getTags())
                {
                    System.out.println(newTag.getName());
                    createTags.setInt(1, project.getId());
                    createTags.setString(2, newTag.getName());
                    createTags.executeUpdate();
                }
            }

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
    public Comments fetchAllComments(int projectID) throws OakCoreException {
        Comments comments = new Comments();
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

                comments.getComments().add(comment);
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
                this.endConnection();
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
                this.endConnection();
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
        int newCommentID = 0;

        try {
            sql = String.format(
                    "INSERT INTO comment "
                    + "(creator, title, content, dateCreated, status, projectID) "
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
            newCommentID = statement.getGeneratedKeys().getInt(1);

        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        } finally {
            this.endConnection();
        }
        return this.getCommentByID(newCommentID);
    }

    @Override
    public Projects searchProjectByName(String SearchEx, boolean recent) throws OakCoreException {
        Projects projects = new Projects();
        this.establishConnection();
        try {
            if (recent) {
                sql = "select * from project "
                        + "where status = 'published' "
                        + "and project.title LIKE '%" + SearchEx + "%' "
                        + "COLLATE NOCASE "
                        + "order by dateCreated desc " ;
            } else {
                sql = "select * from project "
                        + "where (status = 'published' "
                        + "or status = 'closed') "
                        + "and project.title LIKE '%" + SearchEx + "%' "
                        + "COLLATE NOCASE "
                        + "order by projectID";
            }
            ResultSet resultset = statement.executeQuery(sql);
            while (resultset.next()) {
                Project project = new Project(resultset.getString("title"));
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

                projects.getProjects().add(project);
            }
            resultset.close();
            
           
            
            Iterator<Project> projectIterator = projects.getProjects().iterator();
            while (projectIterator.hasNext()) {
                Project nextProject = projectIterator.next();
                //ProjectTags abrufen
                sql = "select tag.* from project, projecttag, tag "
                        + "where project.projectID = projecttag.projectID "
                        + "and projecttag.tagName = tag.tagName "
                        + "and project.projectID= " + nextProject.getId();
                resultset = statement.executeQuery(sql);

                Tags tags = new Tags();
                while (resultset.next()) {
                    Tag tag = new Tag(resultset.getString("tagName"),
                            resultset.getString("description"));
                    tags.getTags().add(tag);
                }
                nextProject.setTags(tags);
                //ProjectMember abrufen
                sql = "select user.*, projectmember.role from user,project, projectmember "
                        + "where project.projectID = projectmember.projectID "
                        + "and projectmember.username = user.username "
                        + "and project.projectID= " + nextProject.getId();

                resultset = statement.executeQuery(sql);

                Members members = new Members();
                while (resultset.next()) {
                    Member member = new Member();

                    User nuser = new User(resultset.getString("username"));
                    nuser.setForename(resultset.getString("forename"));
                    nuser.setSurname(resultset.getString("surname"));

                    member.setUser(nuser);
                    member.setRole(resultset.getString("role"));
                    members.getMembers().add(member);
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
    public Tag getTagByName(String tagName) throws OakCoreException {
        Tag newTag = new Tag();
        
        this.establishConnection();
        try {
            sql = String.format("SELECT count(*) count, * "
                    + "FROM tag "
                    + "WHERE tagName='%s' "
                    + "COLLATE NOCASE ", tagName);

            ResultSet resultset = statement.executeQuery(sql);
            if (resultset.getInt("count") == 0) {
                this.endConnection();
                return null;
            }
            newTag.setName(resultset.getString("tagName"));
            newTag.setDescription(resultset.getString("description"));
            resultset.close();
        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        }
        this.endConnection();
        return newTag;
    }

    @Override
    public Tags getTags() throws OakCoreException {
        Tags tags = new Tags();
        this.establishConnection();
        try {
            sql = "SELECT * "
                    + "FROM tag ";

            ResultSet resultset = statement.executeQuery(sql);

            while (resultset.next()) {
                Tag newTag = new Tag();
                newTag.setName(resultset.getString("tagName"));
                newTag.setDescription(resultset.getString("description"));
                tags.getTags().add(newTag);
            }

        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        }
        this.endConnection();
        return tags;
    }

    @Override
    public Tag updateTagDescription(Tag newtag) throws OakCoreException {
        this.establishConnection();
        try {
            sql = String.format("UPDATE tag "
                    + "SET "
                    + "description='%s'"
                    + "WHERE tagName='%s'",
                    newtag.getDescription(),
                    newtag.getName()
            );

            statement.executeUpdate(sql);

        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        }
        this.endConnection();
        return newtag;
    }

    @Override
    public Tag deleteTag(String tagName) throws OakCoreException {
        Tag tag;
        tag = getTagByName(tagName);
        this.establishConnection();

        try {
            sql = String.format("DELETE "
                    + "FROM tag "
                    + "WHERE tagName='%s'",
                    tag.getName()
            );

            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        }
        this.endConnection();
        return tag;
    }

    @Override
    public Tag createTag(Tag tag) throws OakCoreException {
        this.establishConnection();
        try {
            sql = String.format("INSERT INTO "
                    + "tag (tagName, description) "
                    + "VALUES("
                    + "'%s', "
                    + "'%s')",
                    tag.getName(),
                    tag.getDescription()
            );

            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            errorMessage = "Fehler bei Datenbankabfrage";
            Logger.getLogger(StoreServiceInSQLite.class.getName()).log(Level.SEVERE, errorMessage, ex);
            throw new OakCoreException(errorMessage);
        }
        this.endConnection();
        return tag;
    }

    @Override
    public Projects searchProjectByTag(String SearchEx, boolean recent) throws OakCoreException {
        Projects projects = new Projects();
        this.establishConnection();

        try {

            if (recent) {
                sql = "SELECT project.*, user.* "
                        + "FROM  project, projecttag, user "
                        + "WHERE project.projectID = projecttag.projectID "
                        + "and user.username=project.creator "
                        + "and project.status='published' "
                        + "and tagName='"+SearchEx+"' "
                        + "COLLATE NOCASE "
                        + "order by dateCreated desc";
            } else {
                sql = "SELECT project.*, user.* "
                        + "FROM  project, projecttag, user "
                        + "WHERE project.projectID = projecttag.projectID "
                        + "and user.username=project.creator "
                        + "and (project.status='published' or project.status='closed') "
                        + "and tagName='"+SearchEx+"' "
                        + "COLLATE NOCASE "
                        + "order by projectID;";
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
                project.setDescription(resultset.getString("description"));
                User user = new User(resultset.getString("creator"));
                user.setForename(resultset.getString("forename"));
                user.setSurname(resultset.getString("surname"));
                project.setCreator(user);

                projects.getProjects().add(project);
            }
            resultset.close();

            Iterator<Project> projectIterator = projects.getProjects().iterator();

            while (projectIterator.hasNext()) {

                Project nextProject = projectIterator.next();
                //creater abrufen
                //ProjectTags abrufen
                sql = "select tag.* from project, projecttag, tag "
                        + "where project.projectID = projecttag.projectID "
                        + "and projecttag.tagName = tag.tagName "
                        + "and project.projectID= " + nextProject.getId();
                resultset = statement.executeQuery(sql);

                Tags tags = new Tags();
                while (resultset.next()) {
                    Tag tag = new Tag(resultset.getString("tagName"),
                            resultset.getString("description"));
                    tags.getTags().add(tag);
                }
                nextProject.setTags(tags);
                //ProjectMember abrufen
                sql = "select user.*, projectmember.role from user,project, projectmember "
                        + "where project.projectID = projectmember.projectID "
                        + "and projectmember.username = user.username "
                        + "and project.projectID= " + nextProject.getId();

                resultset = statement.executeQuery(sql);

                Members members = new Members();
                while (resultset.next()) {
                    Member member = new Member();

                    User nuser = new User(resultset.getString("username"));
                    nuser.setForename(resultset.getString("forename"));
                    nuser.setSurname(resultset.getString("surname"));

                    member.setUser(nuser);
                    member.setRole(resultset.getString("role"));
                    members.getMembers().add(member);
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
