package windyoak.rest;

import windyoak.core.StoreService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import windyoak.core.PostsService;
import windyoak.core.impl.PostsServiceRSS;
import windyoak.core.impl.StoreServiceInSQLite;
import windyoak.rest.impl.ProjectsResourceImpl;
import windyoak.rest.impl.RecentResourceImpl;
import windyoak.rest.impl.TagsResourceImpl;
import windyoak.rest.impl.UsersResourceImpl;


/**
 *
 * @author fhaller1
 */
public class WindyOakApplication extends org.glassfish.jersey.server.ResourceConfig
{
    public WindyOakApplication()
    {
        register(ProjectsResourceImpl.class);
        register(RecentResourceImpl.class);
        register(UsersResourceImpl.class);
        register(TagsResourceImpl.class);
        register(new AbstractBinder()
        {
            @Override
            protected void configure()
            {
                bind(new StoreServiceInSQLite()).to(StoreService.class);
                bind(new PostsServiceRSS()).to(PostsService.class);
            }
        });
    }
}
