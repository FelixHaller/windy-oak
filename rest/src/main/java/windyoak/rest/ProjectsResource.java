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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author fhaller1
 */
@Path("projects")
public interface ProjectsResource {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
                
            })
    public Response createProject(
            @Context UriInfo uriInfo,
            @FormParam("name") String name,
            @FormParam("username") String username,
            @FormParam("description") String description,
            @FormParam("members") String members,
            @FormParam("status") String status,
            @FormParam("tagNames") String tagNames,
            @FormParam("postsURL") String postsURL
    );

    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    public Response getProjects(
            @QueryParam("title") String title,
            @QueryParam("tag") String tag,
            @QueryParam("creator") String creator
            
    );

    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}")
    public Response getProject(@PathParam("projectid") int projectId);

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}")
    public Response updateProject(
            @PathParam("projectid") int projectId,
            @Context UriInfo uriInfo,
            @QueryParam("name") String name,
            @QueryParam("username") String username,
            @QueryParam("description") String description,
            @QueryParam("status") String status,
            @QueryParam("members") String members,
            @QueryParam("tagNames") String tagNames,
            @QueryParam("postsURL") String postsURL
    );

    @DELETE
    @Produces({
        MediaType.APPLICATION_XML,
        MediaType.APPLICATION_JSON
    })
    @Path("{projectid}")
    public Response deleteProject(@PathParam("projectid") int projectId);

//comments!!!!
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/comments")
    public Response createComment(
            @PathParam("projectid") int projectid,
            @Context UriInfo uriInfo,
            @FormParam("title") String title,
            @FormParam("creator") String creator,
            @FormParam("content") String content,
            @FormParam("status") String status
            
    );

    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/comments")
    public Response getComments(@PathParam("projectid") int projectid);

    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/comments/{commentid}")
    public Response getComment(@PathParam("commentid") int commentid);

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/comments/{commentid}")
    public Response updateComment(
            @PathParam("commentid") int commentid,
            @Context UriInfo uriInfo,
            @FormParam("title") String title,
            @FormParam("content") String content,
            @FormParam("status") String  status
    );

    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/comments/{commentid}")
    public Response deleteComment(@PathParam("commentid") int commentid);
    
    
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/posts")
    public Response getPosts(@PathParam("projectid") int projectid);
    
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/posts/{postid}")
    public Response getPost(@PathParam("projectid") int projectid, @PathParam("postid") String postid);
    
    
    
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
