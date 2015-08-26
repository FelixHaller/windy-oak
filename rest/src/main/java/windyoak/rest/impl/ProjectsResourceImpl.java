/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import windyoak.core.StoreService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import windyoak.core.Project;
import windyoak.core.Projects;
import windyoak.core.User;

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
    public Response createProject(UriInfo uriInfo, String name, int userID, String description, String dateCreated, String status) {
        Project project = new Project(name);
        if (name == null || name.isEmpty() || description == null
                || description.isEmpty() || dateCreated == null || dateCreated.isEmpty() || status == null || status.isEmpty()) {
            return Response.status(Status.NOT_ACCEPTABLE).tag("Empty Parameter").build();
        }

        User user = storeService.getUser(userID);
        if (user == null) {
            return Response.status(Status.UNAUTHORIZED).build();
        }
        project.setCreator(user);

        project.setDescription(description);

        try {
            project.setDateCreated(format.parse(dateCreated));
        } catch (ParseException ex) {
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }

        this.storeService.createProject(project);
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response getProjects() {
        Projects projects = new Projects(storeService.fetchAllProjects());

        return Response.status(Status.OK).entity(projects).build();
    }

    @Override
    public Response getProject(int projectId) {
        Project project = storeService.getProjectByID(projectId);
        if (project == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response deleteProject(int projectId) {
        Project project = storeService.getProjectByID(projectId);
        if (project == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        storeService.deleteProject(projectId);
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response updateProject(int projectId, UriInfo uriInfo, String name, int userID, String description, String dateUpdated, String status) {
        Project project = storeService.getProjectByID(projectId);
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
        if (dateUpdated.isEmpty()) {
            return Response.status(Status.NOT_ACCEPTABLE).build();
        } else {
            try {
                project.setDateUpdated(format.parse(dateUpdated));
            } catch (ParseException ex) {
                return Response.status(Status.NOT_ACCEPTABLE).build();
            }
        }
        User user = storeService.getUser(userID);
        if (project.getCreator() != user) {
            return Response.status(Status.UNAUTHORIZED).build();
        }
        
        storeService.updateProject(projectId, project);
        return Response.status(Status.OK).entity(project).build();
    }

}
