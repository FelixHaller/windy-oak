/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author fhaller1
 */
@Path("projects")
public interface ProjectsResource
{

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
        {
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON
        })
    public Response createProject(@Context UriInfo uriInfo, @FormParam("name") String name);

    @GET
    @Produces(
        {
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON
        })
    public Response getProjects();
    
    @GET
    @Produces(
        {
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON
        })
    @Path("{projectid}")
    public Response getProject(@PathParam("projectid") int projectId);

//    @GET
//    @Produces(
//        {
//            MediaType.APPLICATION_XML,
//            MediaType.APPLICATION_JSON
//        })
//    @Path("{userid}")
//    public Response getUser(@PathParam("userid") int userId);
//
//    @PUT
//    @Path("{userid}" + "/numbers/" + "{caption}")
//    public Response addNumber(@Context UriInfo uriInfo,
//        @PathParam("userid") int userId,
//        @PathParam("caption") String caption,
//        @QueryParam("number") @DefaultValue("") String number);
//
//    @DELETE
//    @Path("{userid}" + "/numbers/" + "{caption}")
//    public Response deleteNumber(@PathParam("userid") int userId,
//        @PathParam("caption") String caption);

//    @DELETE
//    @Path("/projects/" + "{projectid}")
//    public Response deleteUser(@PathParam("projectid") int projectId);
    
    
    

}
