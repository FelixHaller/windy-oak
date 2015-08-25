/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.rest;

import windyoak.core.StoreService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import windyoak.core.impl.StoreServiceInSQLite;
import windyoak.rest.impl.ProjectsResourceImpl;


/**
 *
 * @author fhaller1
 */
public class WindyOakApplication extends org.glassfish.jersey.server.ResourceConfig
{
    public WindyOakApplication()
    {
        register(ProjectsResourceImpl.class);
        register(new AbstractBinder()
        {
            @Override
            protected void configure()
            {
                bind(new StoreServiceInSQLite()).to(StoreService.class);
            }
        });
    }
}
