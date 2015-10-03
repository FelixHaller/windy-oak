/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import windyoak.core.OakCoreException;
import windyoak.core.StoreService;
import windyoak.core.Tag;
import windyoak.core.Tags;
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
        try {
            
            if (tagName == null || tagName.isEmpty() || description == null
                    || description.isEmpty()) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Empty Parameter").build();
            }
            Pattern tp = Pattern.compile("^\\w+");
            Matcher tm = tp.matcher(tagName);
            if (!tm.matches()){
              return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Only whole words possible without special characters!").build();
            }
            if (storeService.getTagByName(tagName) == null) {
                tag.setName(tagName);
                tag.setDescription(description);
                storeService.createTag(tag);
            }else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Tag "+tagName+" already exists!").build();
            }

        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(tag).build();
    }

    @Override
    public Response getTags() {
        try {
            List<Tag> tagList = storeService.getTags();
            if (tagList == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("No Tags found!").build();
            }
            return Response.status(Response.Status.OK).entity(new Tags(tagList)).build();
        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response getTag(String tagName) {
        Tag newTag;
        if (tagName == null || tagName.isEmpty()) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Empty Parameter").build();
        }
        try {
            newTag = storeService.getTagByName(tagName);
            if (newTag == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Can't found Tagname!").build();
            }
            return Response.status(Response.Status.OK).entity(newTag).build();

        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response updateTag(String tagName, UriInfo uriInfo, String description) {
        Tag newTag;
        try {
            if (tagName == null || tagName.isEmpty()) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Empty Parameter!").build();
            }
            
            Pattern tp = Pattern.compile("^\\w+");
            Matcher tm = tp.matcher(tagName);
            if (!tm.matches()){
              return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Only whole words possible without special characters!").build();
            }
            
            newTag = storeService.getTagByName(tagName);
            if (newTag == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Can't found Tagname!").build();
            }
            newTag.setDescription(description);
            storeService.updateTagDescription(newTag);
            return Response.status(Response.Status.OK).entity(newTag).build();

        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response deleteTag(String tagName) {
        if (tagName == null || tagName.isEmpty()) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Empty Paramter!").build();
        }
        try {
            Tag newTag = storeService.deleteTag(tagName);
            //Es muss geprüft werden ob Tag von einem project verwendet wird.
            return Response.status(Response.Status.OK).entity(newTag).build();

        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

}
