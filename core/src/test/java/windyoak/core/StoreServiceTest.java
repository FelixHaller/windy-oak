/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.core;

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
public class StoreServiceTest
{
    
    public StoreServiceTest()
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
     * Test of fetchAllProjects method, of class StoreService.
     */
    @Test
    public void testFetchAllProjects() throws Exception
    {
        System.out.println("fetchAllProjects");
        StoreService instance = new StoreServiceImpl();
        Projects expResult = null;
        Projects result = instance.fetchAllProjects();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fetchRecentProjects method, of class StoreService.
     */
    @Test
    public void testFetchRecentProjects() throws Exception
    {
        System.out.println("fetchRecentProjects");
        int n = 0;
        StoreService instance = new StoreServiceImpl();
        Projects expResult = null;
        Projects result = instance.fetchRecentProjects(n);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProjectByID method, of class StoreService.
     */
    @Test
    public void testGetProjectByID() throws Exception
    {
        System.out.println("getProjectByID");
        int projectID = 0;
        StoreService instance = new StoreServiceImpl();
        Project expResult = null;
        Project result = instance.getProjectByID(projectID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createProject method, of class StoreService.
     */
    @Test
    public void testCreateProject() throws Exception
    {
        System.out.println("createProject");
        Project project = null;
        StoreService instance = new StoreServiceImpl();
        Project expResult = null;
        Project result = instance.createProject(project);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProject method, of class StoreService.
     */
    @Test
    public void testUpdateProject() throws Exception
    {
        System.out.println("updateProject");
        Project project = null;
        StoreService instance = new StoreServiceImpl();
        Project expResult = null;
        Project result = instance.updateProject(project);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteProject method, of class StoreService.
     */
    @Test
    public void testDeleteProject() throws Exception
    {
        System.out.println("deleteProject");
        int projectID = 0;
        StoreService instance = new StoreServiceImpl();
        Project expResult = null;
        Project result = instance.deleteProject(projectID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fetchAllUsers method, of class StoreService.
     */
    @Test
    public void testFetchAllUsers() throws Exception
    {
        System.out.println("fetchAllUsers");
        StoreService instance = new StoreServiceImpl();
        Users expResult = null;
        Users result = instance.fetchAllUsers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUser method, of class StoreService.
     */
    @Test
    public void testGetUser() throws Exception
    {
        System.out.println("getUser");
        String username = "";
        StoreService instance = new StoreServiceImpl();
        User expResult = null;
        User result = instance.getUser(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fetchAllComments method, of class StoreService.
     */
    @Test
    public void testFetchAllComments() throws Exception
    {
        System.out.println("fetchAllComments");
        int projectID = 0;
        StoreService instance = new StoreServiceImpl();
        Comments expResult = null;
        Comments result = instance.fetchAllComments(projectID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCommentByID method, of class StoreService.
     */
    @Test
    public void testGetCommentByID() throws Exception
    {
        System.out.println("getCommentByID");
        int commentID = 0;
        StoreService instance = new StoreServiceImpl();
        Comment expResult = null;
        Comment result = instance.getCommentByID(commentID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteComment method, of class StoreService.
     */
    @Test
    public void testDeleteComment() throws Exception
    {
        System.out.println("deleteComment");
        int commentID = 0;
        StoreService instance = new StoreServiceImpl();
        Comment expResult = null;
        Comment result = instance.deleteComment(commentID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateComment method, of class StoreService.
     */
    @Test
    public void testUpdateComment() throws Exception
    {
        System.out.println("updateComment");
        Comment comment = null;
        StoreService instance = new StoreServiceImpl();
        Comment expResult = null;
        Comment result = instance.updateComment(comment);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createComment method, of class StoreService.
     */
    @Test
    public void testCreateComment() throws Exception
    {
        System.out.println("createComment");
        Comment comment = null;
        StoreService instance = new StoreServiceImpl();
        Comment expResult = null;
        Comment result = instance.createComment(comment);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTagByName method, of class StoreService.
     */
    @Test
    public void testGetTagByName() throws Exception
    {
        System.out.println("getTagByName");
        String tagName = "";
        StoreService instance = new StoreServiceImpl();
        Tag expResult = null;
        Tag result = instance.getTagByName(tagName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTag method, of class StoreService.
     */
    @Test
    public void testCreateTag() throws Exception
    {
        System.out.println("createTag");
        Tag tag = null;
        StoreService instance = new StoreServiceImpl();
        Tag expResult = null;
        Tag result = instance.createTag(tag);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTags method, of class StoreService.
     */
    @Test
    public void testGetTags() throws Exception
    {
        System.out.println("getTags");
        StoreService instance = new StoreServiceImpl();
        Tags expResult = null;
        Tags result = instance.getTags();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateTagDescription method, of class StoreService.
     */
    @Test
    public void testUpdateTagDescription() throws Exception
    {
        System.out.println("updateTagDescription");
        Tag newtag = null;
        StoreService instance = new StoreServiceImpl();
        Tag expResult = null;
        Tag result = instance.updateTagDescription(newtag);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteTag method, of class StoreService.
     */
    @Test
    public void testDeleteTag() throws Exception
    {
        System.out.println("deleteTag");
        String tagName = "";
        StoreService instance = new StoreServiceImpl();
        Tag expResult = null;
        Tag result = instance.deleteTag(tagName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchProject method, of class StoreService.
     */
    @Test
    public void testSearchProject() throws Exception
    {
        System.out.println("searchProject");
        String title = "";
        String tagName = "";
        String creator = "";
        boolean recent = false;
        StoreService instance = new StoreServiceImpl();
        Projects expResult = null;
        Projects result = instance.searchProject(title, tagName, creator, recent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class StoreServiceImpl implements StoreService
    {

        public Projects fetchAllProjects() throws OakCoreException
        {
            return null;
        }

        public Projects fetchRecentProjects(int n) throws OakCoreException
        {
            return null;
        }

        public Project getProjectByID(int projectID) throws OakCoreException
        {
            return null;
        }

        public Project createProject(Project project) throws OakCoreException
        {
            return null;
        }

        public Project updateProject(Project project) throws OakCoreException
        {
            return null;
        }

        public Project deleteProject(int projectID) throws OakCoreException
        {
            return null;
        }

        public Users fetchAllUsers() throws OakCoreException
        {
            return null;
        }

        public User getUser(String username) throws OakCoreException
        {
            return null;
        }

        public Comments fetchAllComments(int projectID) throws OakCoreException
        {
            return null;
        }

        public Comment getCommentByID(int commentID) throws OakCoreException
        {
            return null;
        }

        public Comment deleteComment(int commentID) throws OakCoreException
        {
            return null;
        }

        public Comment updateComment(Comment comment) throws OakCoreException
        {
            return null;
        }

        public Comment createComment(Comment comment) throws OakCoreException
        {
            return null;
        }

        public Tag getTagByName(String tagName) throws OakCoreException
        {
            return null;
        }

        public Tag createTag(Tag tag) throws OakCoreException
        {
            return null;
        }

        public Tags getTags() throws OakCoreException
        {
            return null;
        }

        public Tag updateTagDescription(Tag newtag) throws OakCoreException
        {
            return null;
        }

        public Tag deleteTag(String tagName) throws OakCoreException
        {
            return null;
        }

        public Projects searchProject(String title, String tagName, String creator, boolean recent) throws OakCoreException
        {
            return null;
        }
    }
    
}
