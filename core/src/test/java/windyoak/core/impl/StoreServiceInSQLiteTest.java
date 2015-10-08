/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.core.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import windyoak.core.OakCoreException;
import windyoak.core.Project;
import windyoak.core.Projects;
import windyoak.core.User;
import windyoak.core.Users;

/**
 *
 * @author felix
 */
public class StoreServiceInSQLiteTest
{
    StoreServiceInSQLite storeService;
    
    public StoreServiceInSQLiteTest()
    {
        storeService = new StoreServiceInSQLite();
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
     * Testet ob der Abruf eines einzelnen Projektes (Projekt 1, welches 
     * immer zum Testen verwendet wird) klappt und ruft anschließend alle 
     * Projekte ab und schaut ob Projekt1 auch enthalten ist.
     * 
     * @throws windyoak.core.OakCoreException
     */
    @Test
    public void testFetchAllProjectsAndCheckIfItContainsProject1() throws OakCoreException
    {
        
        Project project1 = storeService.getProjectByID(1);
        assertSame(project1.getClass(), Project.class);
        assertNotNull(project1.getComments());
        assertNotNull(project1.getCreator());
        assertNotNull(project1.getDateCreated());
        assertNotNull(project1.getDateUpdated());
        assertNotNull(project1.getDescription());
        assertNotNull(project1.getId());
        assertNotNull(project1.getMembers());
        
        assertTrue(project1.getMembers().getMembers().get(0).getUser().getUsername().equals("GFaulus"));
        
        assertNotNull(project1.getPosts());
        assertNotNull(project1.getPostsURL());
        assertNotNull(project1.getStatus());
        assertNotNull(project1.getTags());
        assertNotNull(project1.getTitle());
        assertTrue(project1.getTitle().equals("SinnvollesProjekt"));
        
        
        Projects allProjects = storeService.fetchAllProjects();
        assertEquals(allProjects.getClass(), Projects.class);
        assertNotNull(allProjects.getProject(1));
        
    }
    
     /**
     * Testet das Abrufen aller Nutzer.
     */
    @Test
    public void testFetchAllUsers() throws OakCoreException
    {
        StoreServiceInSQLite storeServiceInSQLite = new StoreServiceInSQLite();
        Users allUsers = storeServiceInSQLite.fetchAllUsers();
        assertEquals(allUsers.getClass(), Users.class);
        //Schaut ob User ausgeliefert wurden
        assertTrue(allUsers.getUsers().size() > 0);
        
    }
    
    /**
     * Testet den Demo User "Tutnix"
     * @throws java.lang.Exception
     */
    @Test
    public void testGetUser() throws Exception
    {
        String username = "Tutnix";
        
        User user = storeService.getUser(username);
        assertEquals(user.getUsername(), "Tutnix");
        assertEquals(user.getForename(), "Mussnix");
        assertEquals(user.getSurname(), "Tutnix");
        
        
    }
    
    /**
     * Ruft ein bestehendes Projekt aus der Datenbank ab, 
     * verändert den Namen und den Creator und trägt es als neues Projekt ein. 
     * 
     * @throws windyoak.core.OakCoreException
     */
    @Test
    public void testProjectGuttenberging() throws OakCoreException
    {
        Project project = storeService.getProjectByID(1);
        
        assertEquals(project.getCreator().getUsername(), "Tutnix");
        
        project.setCreator(new User("GFaulus"));
        project.setTitle("Doktorarbeit");
        
        Project newProject = storeService.createProject(project);
        
        assertEquals(project.getDescription(), newProject.getDescription());
        assertNotEquals(project.getId(), newProject.getId());
        
        Project againProject = storeService.getProjectByID(newProject.getId());
        
        assertEquals(againProject.getDescription(), newProject.getDescription());
        assertEquals(againProject.getId(), newProject.getId());
    
    }
    
    /**
     * Ein Projekt abrufen, es clonen, den Clon löschen, schauen ob das, was 
     * zurück gegeben wurde auch wirklich vorher im Projekt stand und danach 
     * schauen ob das Projekt wirklich gelöscht wurde (erneut abrufen).
     * 
     * @throws windyoak.core.OakCoreException
     */
    @Test
    public void testDeleteProject() throws OakCoreException
    {
        int projectID = 7;
        Project oldProject = storeService.getProjectByID(projectID);
        
        Project project = storeService.createProject(oldProject);
        
        Project deletedProject = storeService.deleteProject(project.getId());
        
        assertEquals(project.getId(), deletedProject.getId());
        assertEquals(project.getCreator().getUsername(), deletedProject.getCreator().getUsername());
        assertEquals(project.getDateCreated(), deletedProject.getDateCreated());
        assertEquals(project.getDescription(), deletedProject.getDescription());
        assertEquals(deletedProject.getStatus(), "deleted");
        assertEquals(project.getTitle(), deletedProject.getTitle());
        
        Project againProject = storeService.getProjectByID(project.getId());
        assertNull(againProject);
    }
    
    
    

//    /**
//     * Test of fetchRecentProjects method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testFetchRecentProjects() throws Exception
//    {
//        System.out.println("fetchRecentProjects");
//        int n = 0;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Projects expResult = null;
//        Projects result = instance.fetchRecentProjects(n);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getProjectByID method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testGetProjectByID() throws Exception
//    {
//        System.out.println("getProjectByID");
//        int projectID = 0;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Project expResult = null;
//        Project result = instance.getProjectByID(projectID);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//
// 
//
//    
//
//    /**
//     * Test of createProject method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testCreateProject() throws Exception
//    {
//        System.out.println("createProject");
//        Project project = null;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Project expResult = null;
//        Project result = instance.createProject(project);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of updateProject method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testUpdateProject() throws Exception
//    {
//        System.out.println("updateProject");
//        Project project = null;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Project expResult = null;
//        Project result = instance.updateProject(project);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of fetchAllComments method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testFetchAllComments() throws Exception
//    {
//        System.out.println("fetchAllComments");
//        int projectID = 0;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Comments expResult = null;
//        Comments result = instance.fetchAllComments(projectID);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCommentByID method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testGetCommentByID() throws Exception
//    {
//        System.out.println("getCommentByID");
//        int commentID = 0;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Comment expResult = null;
//        Comment result = instance.getCommentByID(commentID);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteComment method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testDeleteComment() throws Exception
//    {
//        System.out.println("deleteComment");
//        int commentID = 0;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Comment expResult = null;
//        Comment result = instance.deleteComment(commentID);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of updateComment method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testUpdateComment() throws Exception
//    {
//        System.out.println("updateComment");
//        Comment comment = null;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Comment expResult = null;
//        Comment result = instance.updateComment(comment);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of createComment method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testCreateComment() throws Exception
//    {
//        System.out.println("createComment");
//        Comment comment = null;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Comment expResult = null;
//        Comment result = instance.createComment(comment);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getTagByName method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testGetTagByName() throws Exception
//    {
//        System.out.println("getTagByName");
//        String tagName = "";
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Tag expResult = null;
//        Tag result = instance.getTagByName(tagName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getTags method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testGetTags() throws Exception
//    {
//        System.out.println("getTags");
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Tags expResult = null;
//        Tags result = instance.getTags();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of updateTagDescription method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testUpdateTagDescription() throws Exception
//    {
//        System.out.println("updateTagDescription");
//        Tag newtag = null;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Tag expResult = null;
//        Tag result = instance.updateTagDescription(newtag);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteTag method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testDeleteTag() throws Exception
//    {
//        System.out.println("deleteTag");
//        String tagName = "";
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Tag expResult = null;
//        Tag result = instance.deleteTag(tagName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of createTag method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testCreateTag() throws Exception
//    {
//        System.out.println("createTag");
//        Tag tag = null;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Tag expResult = null;
//        Tag result = instance.createTag(tag);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchProject method, of class StoreServiceInSQLite.
//     */
//    @Test
//    public void testSearchProject() throws Exception
//    {
//        System.out.println("searchProject");
//        String title = "";
//        String tagName = "";
//        String creator = "";
//        boolean recent = false;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Projects expResult = null;
//        Projects result = instance.searchProject(title, tagName, creator, recent);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
