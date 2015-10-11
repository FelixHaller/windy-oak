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

    /**
     * Legt ein neues Projekt in der Datenbank an.
     * 
     * @param uriInfo
     * @param name
     * @param username
     * @param description
     * @param members
     * @param status
     * @param tagNames
     * @param postsURL
     * @return Das neu angelegte Projekt.
     */
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

    /**
     * Ruft eine Liste aller Projekte ab. Die Ausgabe kann mittels Angabe von 
     * Projekttitel (title), einer Markierung (tag) oder des Erstellers 
     * gefiltert werden.
     * 
     * @param title Bei Angabe dieses Parameters werden Projekte mit Inhalt von
     * title im Projektnamen gesucht.
     * @param tag Bei Angabe dieses Parameters wird nach Markierung gefiltert.
     * @param creator Bei Angabe dieses Parameters wird nach Ersteller gefiltert.
     * @return Alle gefundenen Projekte.
     */
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

    /**
     * Ruft ein einzelnes Projekt ab.
     *
     * @param projectId Die ID des Projektes.
     * @return Das Projekt, sollte die ID gülig sein. Sonst 404.
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}")
    public Response getProject(@PathParam("projectid") int projectId);

    /**
     * Aktualisiert ein bestimmtes Projekt. Es werden nur die Teile 
     * aktualisiert, die auch als Parameter übergeben werden. Sollte ein Teil 
     * entfernt werden, muss ein leerer String ("") übergeben werden 
     * 
     * @param projectId Die ID des zu aktualisierenden Projektes.
     * @param uriInfo
     * @param name
     * @param username
     * @param description
     * @param status
     * @param members
     * @param tagNames
     * @param postsURL
     * @return
     */
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

    /**
     * Löscht ein Projekt.
     * 
     * @param projectId Die ID des zu löschenden Projektes.
     * @return Der letzte Stand des gelöschten Projektes.
     */
    @DELETE
    @Produces({
        MediaType.APPLICATION_XML,
        MediaType.APPLICATION_JSON
    })
    @Path("{projectid}")
    public Response deleteProject(@PathParam("projectid") int projectId);

    //comments!!!!

    /**
     * Legt ein neuen Kommentar zu einem Projekt an.
     * 
     * @param projectid ID des Projektes, das Kommentiert werden soll.
     * @param uriInfo
     * @param title
     * @param creator
     * @param content
     * @param status
     * @return Das neu angelegte Projekt mit vergebener ID.
     */
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

    /**
     * Alle Kommentare zu einem Projekt aus der Datenbank abrufen.
     * @param projectid Die ID des Projektes.
     * @return Alle gefundenen Kommentare.
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/comments")
    public Response getComments(@PathParam("projectid") int projectid);

    /**
     * Ruft einen bestimmten Kommentar ab.
     * 
     * @param commentid ID des Kommentars.
     * @return Den gewünschten Kommentar, sonst 404.
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/comments/{commentid}")
    public Response getComment(@PathParam("commentid") int commentid);

    /**
     * Aktualisiert einen bestimmten Kommentar.
     * 
     * @param commentid ID des zu aktualisierenden Kommentars.
     * @param uriInfo
     * @param title
     * @param content
     * @param status
     * @return Den aktualisierten Kommentar.
     */
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

    /**
     * Löscht einen Kommenatar.
     * @param commentid Die ID des zu löschenden Kommentars.
     * @return Den letzten Stand des Kommentars.
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/comments/{commentid}")
    public Response deleteComment(@PathParam("commentid") int commentid);
    
    /**
     * Die Posts (RSS Feed News) zu einem Projekt abrufen (sofern vorhanden).
     * @param projectid Die ID des Projektes, zu dem Feeds abgerufen werden 
     * sollen.
     * @return Alle Posts zum Projekt (Auszug aus Inhalt des RSS Feeds)
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/posts")
    public Response getPosts(@PathParam("projectid") int projectid);
    
    /**
     * Einen bestimmten Post anhand seiner ID abrufen.
     * 
     * @param projectid
     * @param postid
     * @return Der gewünschte Post.
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{projectid}/posts/{postid}")
    public Response getPost(@PathParam("projectid") int projectid, @PathParam("postid") String postid);
}
