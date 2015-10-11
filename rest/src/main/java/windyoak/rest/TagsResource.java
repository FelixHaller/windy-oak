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

    /**
     * Erstellt einen Tag in der Datenbank, sofern dieser noch nicht vorhanden 
     * ist.
     * 
     * @param uriInfo
     * @param tagName
     * @param description
     * @return Den erstellten Tag.
     */
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

    /**
     * Liefert Liste alle Tags.
     * 
     * @return Liste aller Tags.
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    public Response getTags();

    /**
     * Liefert einen bestimmten Tag.
     * 
     * @param tagName
     * @return
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
            })
    @Path("{tagName}")
    public Response getTag(@PathParam("tagName") String tagName);

    /**
     * Ändert zu einem Tag mit Namen tagName die Beschreibung (description).
     * 
     * 
     * @param tagName Name des zu ändernden Tags 
     * @param uriInfo
     * @param description Neue Beschreibung für den Tag.
     * @return den geänderten Tag.
     */
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

    /**
     * Löscht einen bestehenden Tag.
     * 
     * 
     * @param tagName
     * @return
     */
    @DELETE
    @Produces({
        MediaType.APPLICATION_XML,
        MediaType.APPLICATION_JSON
    })
    @Path("{tagName}")
    public Response deleteTag(@PathParam("tagName") String tagName);

}
