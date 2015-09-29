/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import windyoak.core.OakCoreException;
import windyoak.core.Project;
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
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getRecentProjects() {
        List<Project> projects;
        try {
            projects = storeService.fetchRecentProjects(10);
        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(projects).build();
    }

    @Override
    public Response getRecentRSSPosts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
