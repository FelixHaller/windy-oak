/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import windyoak.core.StoreService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import windyoak.core.Project;
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

    public StoreService getStoreService()
    {
        return storeService;
    }

    public void setPhoneService(StoreService storeService)
    {
        this.storeService = storeService;
    }

    @Override
    public Response createProject(UriInfo uriInfo, String name)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getProjects()
    {
        Projects projects = new Projects(storeService.fetchAllProjects());
        
        return Response.status(Status.OK).entity(projects).build();
    }

    @Override
    public Response getProject(int projectId)
    {
        Project project = storeService.getProjectByID(projectId);
        if(project == null)
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(project).build();
    }

    @Override
    public Response updateProject(int projectId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response deleteProject(int projectId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    

    
}
