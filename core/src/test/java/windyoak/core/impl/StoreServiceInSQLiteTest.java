package windyoak.core.impl;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import windyoak.core.Comment;
import windyoak.core.Comments;
import windyoak.core.OakCoreException;
import windyoak.core.Project;
import windyoak.core.Projects;
import windyoak.core.User;
import windyoak.core.Users;

/**
 *
 * @author Felix Haller
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
     * Anschließend ruft er das neu erstellte Projekt nochmal frisch aus der 
     * Datenbank ab.
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
    //Comments
    @Test
    public void testFetchallComments() throws OakCoreException
    {
        Comments allComments = storeService.fetchAllComments(1);
        assertEquals(allComments.getClass(), Comments.class);
        assertTrue(allComments.getComments().size()==4);

    }
    
    @Test
    public void testGetCommentByID() throws Exception
    {

        Comment comment = storeService.getCommentByID(1);
        assertEquals(comment.getContent(), "Also dass, was ich bis jetzt von eurer Arbeit gesehen habe ist wirklich super! Weiter so! ;)");
        assertEquals(comment.getCreator().getUsername(), "CMusencus");
        assertEquals(comment.getId(), 1);
        assertEquals(comment.getStatus(),"published");
        assertEquals(comment.getDateCreated().getTime(),1388891495000L);
        assertEquals(comment.getDateUpdated(),null);
            
    }
    @Test
    public void testCreateAndUpdateComment() throws Exception
    {
       
        Comment comment = new Comment();
        comment.setContent("Hello");
        comment.setCreator(storeService.getUser("Tutnix"));
        comment.setProjectID(2);
        comment.setStatus("published");
        comment.setTitle("Test");
        Comment newComment = storeService.createComment(comment);
        assertEquals(comment.getContent(),newComment.getContent());
        assertEquals(comment.getCreator().getUsername(),newComment.getCreator().getUsername());
        assertEquals(comment.getProjectID(),newComment.getProjectID());
        assertEquals(comment.getStatus(),newComment.getStatus());
        assertEquals(comment.getTitle(),newComment.getTitle());
        assertTrue(newComment.getDateCreated().getTime()>0);
        assertTrue(newComment.getDateUpdated()==null);
        assertEquals(newComment.getDateCreated().getClass(),Date.class);
        
        newComment.setContent("Hello2");
        newComment.setStatus("new");
        newComment.setTitle("Test2");
        
        Comment nComment = storeService.updateComment(newComment);
        assertEquals(nComment.getContent(),newComment.getContent());
        assertEquals(nComment.getCreator().getUsername(),newComment.getCreator().getUsername());
        assertEquals(nComment.getProjectID(),newComment.getProjectID());
        assertEquals(nComment.getStatus(),newComment.getStatus());
        assertEquals(nComment.getTitle(),newComment.getTitle());
        assertTrue(newComment.getDateCreated().getTime()>0);
        assertTrue(newComment.getDateUpdated().getTime()>0);
        assertEquals(newComment.getDateCreated().getClass(),Date.class);
    }
    @Test
    public void testdeleteComment() throws Exception
    {
        Comment comment =storeService.getCommentByID(1);
        Comment copy = storeService.createComment(comment);
        Comment newComment = storeService.deleteComment(copy.getId());
        
        assertEquals(copy.getContent(),newComment.getContent());
        assertEquals(copy.getCreator().getUsername(),newComment.getCreator().getUsername());
        assertEquals(copy.getProjectID(),newComment.getProjectID());
        assertEquals("deleted",newComment.getStatus());
        assertEquals(copy.getTitle(),newComment.getTitle());
        assertTrue(newComment.getDateCreated().getTime()>0);
        assertEquals(newComment.getDateCreated().getClass(),Date.class);
        
        assertNull(storeService.getCommentByID(newComment.getId()));
    }
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
//    public void testSearchProject() throws OakCoreException
//    {
//        String title = "Sinn";
//        String tagName = "Java";
//        String wrongTagName = "Perl";
//        String creator = "Tutnix";
//        boolean recent = false;
//        StoreServiceInSQLite instance = new StoreServiceInSQLite();
//        Projects expResult = null;
//        Projects result = instance.searchProject(title, tagName, creator, recent);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
