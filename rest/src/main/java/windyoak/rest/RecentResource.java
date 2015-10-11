/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
@Path("recent")
public interface RecentResource {

    @GET
    @Produces({
        MediaType.APPLICATION_XML,
        MediaType.APPLICATION_JSON
    })
    public Response getRecents();

    /**
     * Liefert die letzten 10 angelegten Projekte zurück.
     * 
     * @return Die Liste der Projekte in absteigender Reihenfolge geordnet nach 
     * Erstellungsdatum.
     */
    @GET
    @Produces({
        MediaType.APPLICATION_XML,
        MediaType.APPLICATION_JSON
    })
    @Path("projects")
    public Response getRecentProjects();


}
