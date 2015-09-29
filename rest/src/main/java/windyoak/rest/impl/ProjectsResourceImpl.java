/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import windyoak.core.StoreService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import windyoak.core.Comment;
import windyoak.core.Comments;
import windyoak.core.Project;
import windyoak.core.User;
import windyoak.core.OakCoreException;
import windyoak.core.Projects;
import windyoak.rest.ProjectsResource;

/**
 *
 * @author fhaller1
 */
public class ProjectsResourceImpl implements ProjectsResource {

    @Context
    private StoreService storeService;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMANY);

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public Response createProject(UriInfo uriInfo, String name, String username, String status, String description, String members) {
        Project createdProject;
        Project project = new Project(name);
        User user;
        if (name == null || name.isEmpty() || description == null
                || description.isEmpty() || status == null || status.isEmpty()) {
            return Response.status(Status.NOT_ACCEPTABLE).entity("Empty Parameter").build();
        }
        try {
            user = storeService.getUser(username);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        if (user == null) {
            return Response.status(Status.UNAUTHORIZED).build();
        }
        project.setCreator(user);
        project.setDescription(description);
        
        if ("new".equals(status) || "published".equals(status) || "closed".equals(status)) {
            project.setStatus(status);
        } else {
            return Response.status(Status.NOT_ACCEPTABLE).entity("Unknown Status").build();
        }

        List<User> memberList = new ArrayList<>(); //= Arrays.asList(ts)
        String[] arrayMembers = members.split(";");
        for (int i = 0; arrayMembers.length > i; i++) {
            try {
                String testuser = arrayMembers[i];
                User newMember = storeService.getUser(testuser);

                if (newMember == null) {
                    return Response.status(Status.NOT_ACCEPTABLE).entity("User " + testuser + " not in Database!").build();
                }
                memberList.add(newMember);
            } catch (OakCoreException ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        project.setMembers(memberList);
        try {
            createdProject = this.storeService.createProject(project);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

        return Response.status(Status.OK).entity(createdProject).build();
    }

    @Override
    public Response getProjects() {
        try {
            return Response.status(Status.OK).entity(new Projects(storeService.fetchAllProjects())).build();
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
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        try {
            project = storeService.deleteProject(projectId);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response updateProject(int projectId, UriInfo uriInfo, String name, String username, String description, String status) {
        Project project;
        try {
            project = storeService.getProjectByID(projectId);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        if (project == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        if (!name.isEmpty()) {
            project.setTitle(name);
        }
        if (!description.isEmpty()) {
            project.setDescription(description);
        }
        if (!status.isEmpty()) {
            project.setStatus(status);
        }

        project.setCreator(new User(username));
        try {
            storeService.updateProject(projectId, project);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response updateComment(int commentid, UriInfo uriInfo, String title, String content, String dateUpdated, Boolean published) {
        Comment comment;
        try {
            comment = storeService.getCommentByID(commentid);
        } catch (OakCoreException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        if (comment == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        if (!title.isEmpty()) {
            comment.setTitle(title);
        }
        if (!content.isEmpty()) {
            comment.setContent(content);
        }
        comment.setPublished(published);
        if (dateUpdated.isEmpty()) {
            return Response.status(Status.NOT_ACCEPTABLE).entity("No Update-Date was given.").build();
        } else {
            try {
                comment.setDateUpdated(format.parse(dateUpdated));
            } catch (ParseException ex) {
                return Response.status(Status.NOT_ACCEPTABLE).entity(ex.getMessage()).build();
            }
        }
        //comment=storeService.updateComment(comment);
        //
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response deleteComment(int commentid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response createComment(UriInfo uriInfo, String title, String creator, String content, String dateCreated, Boolean published, int projectid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getComments(int projectid) {
//        try {
//
//            return Response.status(Status.OK).entity(new Comments(storeService.fetchAllComments(projectid))).build();
//        } catch (OakCoreException ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getComment(int commentid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
