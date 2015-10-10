/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import windyoak.core.OakCoreException;
import windyoak.core.Projects;
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
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Empty Parameter").build();
            }
            Pattern tp = Pattern.compile("^\\w+");
            Matcher tm = tp.matcher(tagName);
            if (!tm.matches()) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Only whole words possible without special characters!").build();
            }
            if (storeService.getTagByName(tagName) == null) {
                tag.setName(tagName);
                tag.setDescription(description);
                storeService.createTag(tag);
            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Tag " + tagName + " already exists!").build();
            }

        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(tag).build();
    }

    @Override
    public Response getTags() {
        try {
            Tags tagList = storeService.getTags();
            if (tagList == null) {
                return Response.status(Response.Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("No Tags found!").build();
            }
            return Response.status(Response.Status.OK).entity(tagList).build();
        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response getTag(String tagName) {
        Tag newTag;
        if (tagName == null || tagName.isEmpty()) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Empty Parameter").build();
        }
        try {
            newTag = storeService.getTagByName(tagName);
            if (newTag == null) {
                return Response.status(Response.Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("Can't found Tagname!").build();
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
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Empty Parameter!").build();
            }

            Pattern tp = Pattern.compile("^\\w+");
            Matcher tm = tp.matcher(tagName);
            if (!tm.matches()) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Only whole words possible without special characters!").build();
            }

            newTag = storeService.getTagByName(tagName);
            if (newTag == null) {
                return Response.status(Response.Status.NOT_FOUND).type("text/plain; charset=utf-8").entity("Can't found Tagname!").build();
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
            return Response.status(Response.Status.NOT_ACCEPTABLE).type("text/plain; charset=utf-8").entity("Empty Paramter!").build();
        }
        try {

            Projects projects = storeService.searchProject("",tagName,"", false);
            
            if (projects.getProjects().isEmpty()) {
                Tag newTag = storeService.deleteTag(tagName);
                return Response.status(Response.Status.OK).entity(newTag).build();
            } else {
                return Response.status(Response.Status.CONFLICT).type("text/plain; charset=utf-8").entity("Tag is used in a Project. Delete the Project first!").build();
            }

        } catch (OakCoreException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

}
