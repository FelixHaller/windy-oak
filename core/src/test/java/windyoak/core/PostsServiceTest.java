/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.core;

import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author felix
 */
public class PostsServiceTest
{
    
    public PostsServiceTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getAllPosts method, of class PostsService.
     */
    @Test
    public void testGetAllPosts() throws Exception
    {
        System.out.println("getAllPosts");
        URL url = null;
        PostsService instance = new PostsServiceImpl();
        RSSPosts expResult = null;
        RSSPosts result = instance.getAllPosts(url);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRecentPosts method, of class PostsService.
     */
    @Test
    public void testGetRecentPosts() throws Exception
    {
        System.out.println("getRecentPosts");
        URL url = null;
        int n = 0;
        PostsService instance = new PostsServiceImpl();
        RSSPosts expResult = null;
        RSSPosts result = instance.getRecentPosts(url, n);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPosts method, of class PostsService.
     */
    @Test
    public void testGetPosts() throws Exception
    {
        System.out.println("getPosts");
        URL url = null;
        boolean recent = false;
        int count = 0;
        PostsService instance = new PostsServiceImpl();
        RSSPosts expResult = null;
        RSSPosts result = instance.getPosts(url, recent, count);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPostByID method, of class PostsService.
     */
    @Test
    public void testGetPostByID() throws Exception
    {
        System.out.println("getPostByID");
        URL url = null;
        String id = "";
        PostsService instance = new PostsServiceImpl();
        RSSPost expResult = null;
        RSSPost result = instance.getPostByID(url, id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class PostsServiceImpl implements PostsService
    {

        public RSSPosts getAllPosts(URL url) throws OakCoreException
        {
            return null;
        }

        public RSSPosts getRecentPosts(URL url, int n) throws OakCoreException
        {
            return null;
        }

        public RSSPosts getPosts(URL url, boolean recent, int count) throws OakCoreException
        {
            return null;
        }

        public RSSPost getPostByID(URL url, String id) throws OakCoreException
        {
            return null;
        }
    }
    
}
