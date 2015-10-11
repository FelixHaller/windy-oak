package windyoak.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Dieses Interface dient nur als kleiner Ersatz für eine echte Userverwaltung 
 * mit Authenfizierungsmöglichkeit. Deshalb existiert auch nur lesender Zugriff.
 * 
 * 
 */
@Path("users")
public interface UsersResource {
    
    /**
     * Ruft die Liste aller Benutzer ab.
     * 
     * @return Liste aller Benutzer
     */
    @GET
    @Produces({
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
    })
    public Response getUsers();
    
    /**
     * Liefert die Details zu einem bestimmten username.
     * 
     * @param username Benutzername des Benutzers.
     * @return alle Details, die zum username vorhanden sind.
     */
    @GET
    @Produces({
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
    })
    @Path("{username}")
    public Response showUser(@PathParam("username") String username);
    
}
