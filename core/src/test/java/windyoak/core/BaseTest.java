package windyoak.core;

import windyoak.core.impl.StoreServiceInSQLite;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class BaseTest
{

    StoreService storeService;
    Project project;

    @Before
    public void buildService()
    {
        storeService = new StoreServiceInSQLite();
    }
    
    @Test
    public Project createRandomProject(StoreService storeService) throws OakCoreException
    {
        return createProject(storeService, RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
    }
    
    @Test
    public Project createProject(StoreService storeService, String projectName) throws OakCoreException
    {
        project = new Project(projectName);
        throw new AssertionError();

        //return storeService.createProject(project);
    }
    
    @After
    public void cleanUp(StoreService storeService) throws OakCoreException
    {
        System.out.println("Lösche");
        //storeService.deleteProject(project.getId());
    }
    
    
    
//    public PhoneUser createRandomUser(StoreService storeService)
//    {
//        return createUser(storeService, RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
//    }
//
//    public PhoneUser createUser(PhonebookService storeService, String userName)
//    {
//        return storeService.createUser(userName);
//    }
}