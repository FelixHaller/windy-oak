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
import windyoak.core.User;
import windyoak.core.OakCoreException;
import windyoak.core.Projects;
import windyoak.rest.ProjectsResource;

/**
 *
 * @author fhaller1
 */
public class ProjectsResourceImpl implements ProjectsResource
{

    @Context
    private StoreService storeService;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMANY);

    public StoreService getStoreService()
    {
        return storeService;
    }

    public void setStoreService(StoreService storeService)
    {
        this.storeService = storeService;
    }

    @Override
    public Response createProject(UriInfo uriInfo, String name, String username, String description, String dateCreated, String status)
    {
        Project createdProject;
        Project project = new Project(name);
        User user;
        if (name == null || name.isEmpty() || description == null
            || description.isEmpty() || dateCreated == null || dateCreated.isEmpty() || status == null || status.isEmpty())
        {
            return Response.status(Status.NOT_ACCEPTABLE).tag("Empty Parameter").build();
        }
        try
        {
            user = storeService.getUser(username);
        }
        catch (OakCoreException ex)
        {
           return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        if (user == null)
        {
            return Response.status(Status.UNAUTHORIZED).build();
        }
        project.setCreator(user);

        project.setDescription(description);
        project.setStatus(status);

        try
        {
            project.setDateCreated(format.parse(dateCreated));
        }
        catch (ParseException ex)
        {
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }

        try
        {
            createdProject = this.storeService.createProject(project);
        }
        catch (OakCoreException ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(createdProject).build();
    }

    @Override
    public Response getProjects()
    {
        try
        {
            return Response.status(Status.OK).entity(new Projects(storeService.fetchAllProjects())).build();
        }
        catch (OakCoreException ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

        
    }

    @Override
    public Response getProject(int projectId)
    {
        Project project;
        try
        {
            project = storeService.getProjectByID(projectId);
        }
        catch (OakCoreException ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        if (project == null)
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response deleteProject(int projectId)
    {
        Project project;
        try
        {
            if (storeService.getProjectByID(projectId) == null)
            {
                return Response.status(Status.NOT_FOUND).build();
            }
        }
        catch (OakCoreException ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        try
        {
            project = storeService.deleteProject(projectId);
        }
        catch (OakCoreException ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response updateProject(int projectId, UriInfo uriInfo, String name, String username, String description, String status)
    {
        Project project;
        try
        {
            project = storeService.getProjectByID(projectId);
        }
        catch (OakCoreException ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        if (project == null)
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        if (!name.isEmpty())
        {
            project.setTitle(name);
        }
        if (!description.isEmpty())
        {
            project.setDescription(description);
        }
        if (!status.isEmpty())
        {
            project.setStatus(status);
        }

        project.setCreator(new User(username));
        try
        {
            storeService.updateProject(projectId, project);
        }
        catch (OakCoreException ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

}
