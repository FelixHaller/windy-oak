/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest.impl;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import windyoak.core.OakCoreException;
import windyoak.core.StoreService;
import windyoak.core.User;
import windyoak.core.Users;
import windyoak.rest.UsersResource;

/**
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
public class UsersResourceImpl implements UsersResource {

    @Context
    private StoreService storeService;

    @Override
    public Response getUsers() {
        Users users;
        try
        {
            users = new Users(storeService.fetchAllUsers());
        }
        catch (OakCoreException ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(users).build();
    }

    @Override
    public Response showUser(String username) {
       User user;
        try
        {
            user = storeService.getUser(username);
        }
        catch (OakCoreException ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(user).build();
    }

}
