/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author klorenz1
 */
@Path("users")
public interface UsersResource {
    
    @GET
    @Produces({
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
    })
    public Response getUsers();
    
    @GET
    @Produces({
                MediaType.APPLICATION_XML,
                MediaType.APPLICATION_JSON
    })
    @Path("{userid}")
    public Response showUser(@PathParam("userid") int userID);
    
}
