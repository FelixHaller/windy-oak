/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import windyoak.core.StoreService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import windyoak.core.Comment;
import windyoak.core.Comments;
import windyoak.core.Project;
import windyoak.core.User;
import windyoak.core.OakCoreException;
import windyoak.core.Member;
import windyoak.core.Members;
import windyoak.core.PostsService;
import windyoak.core.Projects;
import windyoak.core.RSSPost;
import windyoak.core.RSSPosts;
import windyoak.core.Tag;
import windyoak.core.Tags;
import windyoak.rest.ProjectsResource;

/**
 *
 * @author fhaller1
 */
public class ProjectsResourceImpl implements ProjectsResource {

    @Context
    private StoreService storeService;
    @Context
    private PostsService postsService;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMANY);
    String errormsg;
    
    
    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public PostsService getPostsService() {
        return postsService;
    }

    public void setPostsService(PostsService postsService) {
        this.postsService = postsService;
    }

    @Override

    public Response createProject(  UriInfo uriInfo, 
                                    String name, 
                                    String username, 
                                    String description, 
                                    String members, 
                                    String status,
                                    String tagNames,
                                    String postsURL) {
        Project createdProject;
        Project project = new Project(name);
        User user;
        try {
            if (name == null || name.isEmpty() || description == null
                    || description.isEmpty() || status == null || status.isEmpty()) {
                return Response.status(Status.NOT_ACCEPTABLE).entity("Empty Parameter").build();
            }
            user = storeService.getUser(username);
            if (user == null) {
                return Response.status(Status.NOT_FOUND).entity("User not found in Database!").build();
            }
            project.setCreator(user);
            project.setDescription(description);

            if ("new".equals(status) || "published".equals(status) || "closed".equals(status)) {
                project.setStatus(status);
            } else {
                return Response.status(Status.NOT_ACCEPTABLE).entity("Unknown Status").build();
            }

            try
            {
                project.setPostsURL(new URL(postsURL));
            }
            catch (MalformedURLException ex)
            {
                errormsg = "keine gültige Feed-URL angegeben";
                Logger.getLogger(ProjectsResourceImpl.class.getName()).log(Level.SEVERE, errormsg, ex);
                return Response.status(Status.BAD_REQUEST).entity(errormsg).build();
            }
            Pattern p = Pattern.compile("^([\\w\\s]+,[\\w\\s]*;)+$");
            Matcher m = p.matcher(members);
            if (m.matches()) {
                Members memberList = new Members(); //= Arrays.asList(ts)
                String[] MembersAndRoles = members.split(";");
                for (String MemberAndRole : MembersAndRoles)
                {
                    Member member = new Member();
                    String[] paar = MemberAndRole.split(",");
                    member.setUser(storeService.getUser(paar[0]));
                    if (paar.length == 1) {
                        member.setRole("");
                    } else {
                        member.setRole(paar[1]);
                    }
                    if (member.getUser() == null) {
                        return Response.status(Status.NOT_ACCEPTABLE).entity("Member " + paar[0] + " not in Database!").build();
                    }
                    memberList.getMembers().add(member);
                }
                project.setMembers(memberList);
            } else {
                return Response.status(Status.NOT_ACCEPTABLE).entity("No valid members Specification!").build();
            }
            //TAG
            if (tagNames == null || tagNames.isEmpty()){
            project.setTags(null);
            }else{
            Pattern pt = Pattern.compile("^[\\w+,]*\\w$");
            Matcher mt = pt.matcher(tagNames);
            Tags tags = new Tags();
            Tag newTag;
            if (mt.matches()) {
                String[] tagArray = tagNames.split(",");
                for (int i = 0; tagArray.length > i; i++) {
                    newTag = storeService.getTagByName(tagArray[i]);
                    if (newTag == null) {
                        return Response.status(Status.NOT_ACCEPTABLE).entity("Tag " + tagArray[i] + " not in Database!").build();
                    } else {
                        tags.getTags().add(newTag);
                    }
                }
            } else {
                return Response.status(Status.NOT_ACCEPTABLE).entity("No valid tagName Specification!").build();
            }
            project.setTags(tags);
            }
            createdProject = this.storeService.createProject(project);

        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

        return Response.status(Status.CREATED).entity(createdProject).build();
    }

    @Override
    public Response getProjects(String title, String tag, String creator) {
        try {

            Pattern p = Pattern.compile("\'+");

            if (title == null ) {
                title="";
            }
            if (tag == null ) {
                tag="";
            }
            if (creator == null) {
                creator="";
            }
                Matcher m1 = p.matcher(title);
                Matcher m2 = p.matcher(tag);
                Matcher m3 = p.matcher(creator);
                if (!m1.matches()&!m2.matches()&!m3.matches()) {
                    Projects projects = storeService.searchProject(title,tag,creator,false);
                    if (projects.getProjects().isEmpty()) {
                        
                        return Response.status(Status.NOT_FOUND).type("text/plain; charset=utf-8").entity(String.format("Keine Projekte mit '%s' im Titel gefunden!", title)).build();
                    }
                    return Response.status(Status.OK).entity(projects).build();
                } else {
                    return Response.status(Status.NOT_ACCEPTABLE).entity("\' not allowed!").build();
                }

        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response getProject(int projectId) {
        Project project;
        try {
            project = storeService.getProjectByID(projectId);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        if (project == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response deleteProject(int projectId) {
        Project project;
        try {
            if (storeService.getProjectByID(projectId) == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            project = storeService.deleteProject(projectId);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response updateProject(  int projectId, 
                                    UriInfo uriInfo, 
                                    String name, 
                                    String username, 
                                    String description, 
                                    String status, 
                                    String members,
                                    String tagNames,
                                    String postsURL) {
        Project project;
        User newUser;
        try {
            project = storeService.getProjectByID(projectId);

            if (project == null) {
                return Response.status(Status.NOT_FOUND).build();
            }

            if (!(name == null || name.isEmpty())) {
                project.setTitle(name);//nur Ausführen, wenn der name nicht null und auch nicht empty ist. Schreibweise muss so sein um die NullPointerException vorzugreifen!
            }
            if (!(description == null || description.isEmpty())) {
                project.setDescription(description);
            }
            if (!(status == null || status.isEmpty())) {
                if ("new".equals(status) || "published".equals(status) || "closed".equals(status)) {
                    project.setStatus(status);
                } else {
                    return Response.status(Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Unknown Status").build();
                }
            }
            if (!(username == null || username.isEmpty())) {

                newUser = storeService.getUser(username);

                if (newUser == null) {
                    return Response.status(Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("User not found in Database!").build();
                }
                project.setCreator(newUser);
            }
            
            if (postsURL != null)
            {
                try
                {
                    project.setPostsURL(new URL(postsURL));
                }
                catch (MalformedURLException ex)
                {
                    errormsg = "keine gültige Feed-URL angegeben";
                    Logger.getLogger(ProjectsResourceImpl.class.getName()).log(Level.SEVERE, errormsg, ex);
                    return Response.status(Status.BAD_REQUEST).type("text/plain; charset=utf-8").entity(errormsg).build();
                }
            }
            if (!(members == null || members.isEmpty())) 
            {
                Pattern p = Pattern.compile("^([\\w\\s]+,[\\w\\s]*;)+$");
                Matcher m = p.matcher(members);
                if (m.matches()) {
                    Members memberList = new Members(); //= Arrays.asList(ts)
                    String[] MembersAndRole = members.split(";");
                    for (int i = 0; i < MembersAndRole.length;i++) {
                        Member member = new Member();
                        String[] paar = MembersAndRole[i].split(",");
                        member.setUser(storeService.getUser(paar[0]));
                        if (paar.length == 1) {
                            member.setRole("");
                        } 
                        else {
                            member.setRole(paar[1]);
                        }

                        if (member.getUser() == null) {
                            return Response.status(Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Member " + paar[0] + " not in Database!").build();
                        }
                        memberList.getMembers().add(member);
                    }
                    project.setMembers(memberList);
                } else {
                    return Response.status(Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("No Match in RegEx Member").build();
                }
            }
            //TAG
            if (tagNames == null || tagNames.isEmpty()) 
            {
                project.setTags(null);
            }
            else
            {
                Pattern pt = Pattern.compile("^[\\w+,]*\\w$");
                Matcher mt = pt.matcher(tagNames);
                Tags tags = new Tags();
                Tag newTag;
                if (mt.matches()) {
                    for (String tag : tagNames.split(",")) 
                    {
                        newTag = storeService.getTagByName(tag);
                        if (newTag == null) {
                            return Response.status(Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Tag " + tag + " not in Database!").build();
                        } 
                        else 
                        {
                            tags.getTags().add(newTag);
                        }
                    }
                } else {
                    return Response.status(Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("No valid tagName Specification!").build();
                }
                project.setTags(tags);
            }
            storeService.updateProject(project);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain; charset=utf-8").entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response updateComment(int commentid, UriInfo uriInfo, String title, String content, String status) {
        Comment comment;
        try {
            comment = storeService.getCommentByID(commentid);

            if (comment == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            if (!(title == null || title.isEmpty())) {
                comment.setTitle(title);
            }
            if (!(content == null || content.isEmpty())) {
                comment.setContent(content);
            }
            if (!(status == null || status.isEmpty())) {
                if ("new".equals(status) || "published".equals(status) || "closed".equals(status)) {
                    comment.setStatus(status);
                } else {
                    return Response.status(Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Unknown Status").build();
                }
            }

            storeService.updateComment(comment);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain; charset=utf-8").entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(comment).build();

    }

    @Override
    public Response deleteComment(int commentid) {
        Comment comment;
        try {
            if (storeService.getCommentByID(commentid) == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            comment = storeService.deleteComment(commentid);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("text/plain; charset=utf-8").entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(comment).build();
    }

    @Override
    public Response createComment(int projectid, UriInfo uriInfo, String title, String creator, String content, String status) {
        Comment createdComment;
        Comment comment = new Comment();

        User user;
        try {
            if (storeService.getProjectByID(projectid) == null) {
                return Response.status(Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("Project not in Database!").build();
            }
            if (title == null || title.isEmpty() || content == null
                    || content.isEmpty() || status == null || status.isEmpty()) {
                return Response.status(Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Empty Parameter").build();
            }

            user = storeService.getUser(creator);

            if (user == null) {
                return Response.status(Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("Creator not found in Database!").build();
            }
            comment.setTitle(title);
            comment.setCreator(user);
            comment.setContent(content);
            comment.setProjectID(projectid);
            if ("new".equals(status) || "published".equals(status) || "closed".equals(status)) {
                comment.setStatus(status);
            } else {
                return Response.status(Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Unknown Status").build();
            }

            createdComment = this.storeService.createComment(comment);

        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

        return Response.status(Status.CREATED)
                .entity(createdComment).build();
    }

    @Override
    public Response getComments(int projectid) {
        try {
            Comments comments = storeService.fetchAllComments(projectid);
            if (comments == null) {
                return Response.status(Status.NO_CONTENT).type("text/plain; charset=utf-8").entity("Project has no comments in Database!").build();
            }

            return Response.status(Status.OK).entity(comments).build();
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @Override
    public Response getComment(int commentid) {
        Comment comment;
        try {
            comment = storeService.getCommentByID(commentid);
            if (comment == null) {
                return Response.status(Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("Comment not found in Database!").build();
            }
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(comment).build();
    }

    @Override
    public Response getPosts(int projectid) {
        Project project;
        RSSPosts rssPosts;

        try {
            project = storeService.getProjectByID(projectid);
            if (project == null) {
                return Response.status(Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("Project not found in Database!").build();
            }
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

        URL postsURL = project.getPostsURL();

        if (postsURL == null) {
            return Response.status(Status.NO_CONTENT).type("text/plain; charset=utf-8").entity("No Posts (RSS, ATOM, ...) URL defined for Project.").build();
        }

        try {
            rssPosts = postsService.getAllPosts(project.getPostsURL());
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(rssPosts).build();
    }

    @Override
    public Response getPost(int projectid, String postid) {
        Project project;
        RSSPost rssPost;

        try {
            project = storeService.getProjectByID(projectid);
            if (project == null) {
                return Response.status(Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("Project not found in Database!").build();
            }
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

        URL postsURL = project.getPostsURL();

        if (postsURL == null) {
            return Response.status(Status.NO_CONTENT).type("text/plain; charset=utf-8").entity("No Posts (RSS, ATOM, ...) URL defined for Project.").build();
        }

        try {
            rssPost = postsService.getPostByID(project.getPostsURL(), postid);
            if (rssPost == null) {
                return Response.status(Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("Post ID nicht gefunden.").build();
            }
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(rssPost).build();
    }
}
