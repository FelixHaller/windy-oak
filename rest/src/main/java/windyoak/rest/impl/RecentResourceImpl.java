/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import windyoak.core.OakCoreException;
import windyoak.core.Projects;
import windyoak.core.StoreService;
import windyoak.rest.RecentResource;

/**
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
public class RecentResourceImpl implements RecentResource {

    @Context
    private StoreService storeService;

    @Override
    public Response getRecents() {
        
        return Response
            .status(Response.Status.NOT_FOUND)
            .type("text/plain; charset=utf-8")
            .entity("Nicht implementiert, da keine zweite RECENT Quelle verfügbar. Für die Zukunft vorbereitet.")
            .build();
    }

    @Override
    public Response getRecentProjects() {
        Projects projects;
        try {
            projects = storeService.fetchRecentProjects(10);
        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(projects).build();
    }

}
