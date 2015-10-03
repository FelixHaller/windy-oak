/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
@Path("tags")
public interface TagsResource {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    public Response createTag(
            @Context UriInfo uriInfo,
            @FormParam("tagName") String tagName,
            @FormParam("description") String description
    );

    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    public Response getTags();

    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{tagName}")
    public Response getTag(@PathParam("tagName") String tagName);

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{tagName}")
    public Response updateTag(
            @PathParam("tagName") String tagName,
            @Context UriInfo uriInfo,
            @FormParam("description") String description
    );

    @DELETE
    @Produces({
        MediaType.APPLICATION_XML,
        MediaType.APPLICATION_JSON
    })
    @Path("{tagName}")
    public Response deleteTag(@PathParam("tagName") String tagName);

}
