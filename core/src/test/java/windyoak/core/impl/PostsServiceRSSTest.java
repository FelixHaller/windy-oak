/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.core.impl;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import windyoak.core.OakCoreException;
import windyoak.core.RSSPosts;

/**
 *
 * @author felix
 */
public class PostsServiceRSSTest
{
    URL url;
    public PostsServiceRSSTest() throws MalformedURLException
    {
        url = new URL("https://github.com/FelixHaller/windy-oak/commits/master.atom");
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
     * Testet den Service für das Abholen der RSS Posts zu einer URL.
     * 
     * Die URL zum Feed (sofern vorhanden) wird im Betrieb zum jeweiligen 
     * Projekt aus der Datenbank geholt.
     * @throws windyoak.core.OakCoreException
     */
    @Test
    public void testGetAllPostsFromURL() throws OakCoreException
    {
        PostsServiceRSS rssService = new PostsServiceRSS();
        RSSPosts result = rssService.getAllPosts(url);
        RSSPosts expResult = new RSSPosts();
        
        assertEquals(expResult.getClass(), result.getClass());
        
    }

    /**
     * Test of getRecentPosts method, of class PostsServiceRSS.
     * @throws windyoak.core.OakCoreException
     */
    @Test
    public void testGetRecentPostsFromURL() throws OakCoreException
    {
        int n = 10;
        PostsServiceRSS rssService = new PostsServiceRSS();
        RSSPosts expResult = new RSSPosts();
        RSSPosts result = rssService.getRecentPosts(url, n);
        assertEquals(expResult.getClass(), result.getClass());
        assertTrue(result.getRSSPosts().size() <= n);
       
    }
    
}
