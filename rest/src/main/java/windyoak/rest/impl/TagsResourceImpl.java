/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import windyoak.core.StoreService;
import windyoak.core.Tag;
import windyoak.rest.TagsResource;

/**
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
public class TagsResourceImpl implements TagsResource {

    @Context
    private StoreService storeService;

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public Response createTag(UriInfo uriInfo, String tagName, String description) {
        Tag tag = new Tag();
        if (tagName == null || tagName.isEmpty() || description == null
                || description.isEmpty()) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Empty Parameter").build();
        }
        //DB eintrag erstellen!
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getTags() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getTag(int tagId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response updateTag(int tagId, UriInfo uriInfo, String tagName, String description) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response deleteTag(int tagId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
