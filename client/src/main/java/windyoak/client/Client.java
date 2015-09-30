package windyoak.client;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import windyoak.core.Comment;
import windyoak.core.Comments;
import windyoak.core.Project;
import windyoak.core.Projects;
import windyoak.core.RSSPosts;
import windyoak.core.Tag;
import windyoak.core.User;

/**
 * Der Testclient für den WindyOak REST-Service.
 * 
 * @author fhaller1
 */
public class Client
{

    // der verwendete Client
    private HttpClient httpClient;
    // die Basis URI zum Webservice
    private String baseUri;
    // Feld für den Mediatype XML
    private final String xml = "application/xml";
    // Feld für den Mediatype json
    private final String json = "application/json";

    public Client(String baseUri)
    {
        this.baseUri = baseUri;
        this.httpClient = new HttpClient();
    }

    public String getBaseUri()
    {
        return baseUri;
    }

    public void setBaseUri(String baseUri)
    {
        this.baseUri = baseUri;
    }

    private Object unmarshallfromXML(Reader in, JAXBContext jaxbContext) throws JAXBException
    {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return unmarshaller.unmarshal(in);
    }

    public Project addProject(Project project)
    {
        String response = null;
        Project newProject = null;
        PostMethod postMethod = new PostMethod(getBaseUri() + "/projects");
        
        postMethod.addParameter("name", project.getTitle());
        postMethod.addParameter("username", project.getCreator().getUsername());
        postMethod.addParameter("description", project.getDescription());
        postMethod.addParameter("members", "tester");
        postMethod.addParameter("status", project.getStatus());
        postMethod.setRequestHeader("Accept", xml);

        

        try
        {
            int responseCode = httpClient.executeMethod(postMethod);
            response = postMethod.getResponseBodyAsString();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung", ex);
            System.exit(-1);
        }
        System.out.println(postMethod.getStatusCode() + ": " + response);
        
        return newProject;
    }
    
    public Project getProjectByID(int id)
    {
        String response;
        GetMethod getMethod = new GetMethod(getBaseUri() + "/projects/"+id);
        getMethod.setRequestHeader("Accept", xml);
        JAXBContext jaxbContext = null;
        Project newProject = null;
        try
        {
            jaxbContext = JAXBContext.newInstance(Project.class);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "JAXB Error", ex);
            System.exit(-1);
        }
        try
        {
            int responseCode = httpClient.executeMethod(getMethod);
            response = getMethod.getResponseBodyAsString();
            newProject = (Project) unmarshallfromXML(new StringReader(response), jaxbContext);
        }
        catch (UnmarshalException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Unmarshalling", ex);
            System.exit(-1);
        }
        catch (IOException | JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "JAXB Fehler", ex);
            System.exit(-1);
        }
        return newProject;
        
        
    }
    
    public Project modProject(int id, Project project)
    {
        return null;
    }
    
    public Projects searchProject(String query)
    {
        return null;
    }
    public Projects filterByTag(Tag tag)
    {
        return null;
    }
    
    public Comments getCommentsForProject(int id)
    {
        return null;
    }
    
    public Comment addCommentToProject(Comment comment, int projectID)
    {
        return null;
    }
    
    public RSSPosts getPostsForProject(int projectID)
    {
        return null;
    }

//    public PhoneUser getUser(int userId) throws JAXBException, IOException
//    {
//        String response;
//        GetMethod getMethod = new GetMethod(getBaseUri() + "/users/"+userId);
//        getMethod.setRequestHeader("Accept", xml);
//        JAXBContext jaxbContext = JAXBContext.newInstance(PhoneUser.class);
//        try
//        {
//            int responseCode = httpClient.executeMethod(getMethod);
//            response = getMethod.getResponseBodyAsString();
//            return (PhoneUser) unmarshallfromXML(new StringReader(response), jaxbContext);
//        }
//        catch (UnmarshalException ex)
//        {
//            return null;
//        }
//    }
//    
//    public PhoneUsers getUsers() throws JAXBException, IOException
//    {
//        String response;
//        GetMethod getMethod = new GetMethod(getBaseUri() + "/users");
//        getMethod.setRequestHeader("Accept", xml);
//        JAXBContext jaxbContext = JAXBContext.newInstance(PhoneUsers.class);
//        try
//        {
//            int responseCode = httpClient.executeMethod(getMethod);
//            response = getMethod.getResponseBodyAsString();
//            return (PhoneUsers) unmarshallfromXML(new StringReader(response), jaxbContext);
//        }
//        catch (UnmarshalException ex)
//        {
//            return null;
//        }
//    }
//
//    public void deleteUser(int userId) throws IOException
//    {
//        DeleteMethod deleteMethod = new DeleteMethod(getBaseUri() + "/users/" + userId);
//        deleteMethod.setRequestHeader("Accept", xml);
//        int responseCode = this.httpClient.executeMethod(deleteMethod);
//
//    }
//
//    public void addNumberToUser(int userId, PhoneNumber phoneNumber) throws IOException
//    {
//        PutMethod putMethod = new PutMethod(getBaseUri()+ "/users/" + userId +"/numbers/"+phoneNumber.getCaption());
//        putMethod.setQueryString("number="+phoneNumber.getNumber());
//        int responseCode = this.httpClient.executeMethod(putMethod);
//
//
//    }
//
//    public void deleteNumber(int userId, String caption) throws IOException
//    {
//        DeleteMethod deleteMethod = new DeleteMethod(getBaseUri() + "/users/" + userId + "/numbers/"+ caption);
//        deleteMethod.setRequestHeader("Accept", xml);
//        int responseCode = this.httpClient.executeMethod(deleteMethod);
//    }

    /**
     * Der Client testet u.a.:
     * 
     * - Erstellen eines Projektes
     * - Modifizieren eines Projektes
     * - Suchen nach Projektnamen
     * - Filtern nach tags
     * - Anzeigen der Kommentare zu einem Projekt
     * - Erstellen eines Kommentars zu einem Projekt
     * - Anzeigen der RSS-Posts
     * - Anzeigen der letzten Posts/angelegten Projekte
     * 
     * @param args
     * @throws JAXBException
     * @throws IOException
     */
    public static void main(String[] args) throws JAXBException, IOException
    {
        Client client = new Client("http://localhost:8080");
        
        Logger.getLogger(Client.class.getName()).log(Level.INFO, "Lege neues Projekt \"FooBar\" an");
        
        Project project = new Project("FooBar");
        Tag tag1 = new Tag("Java", "Eine Sprache");
        Tag tag2 = new Tag("The Foo", "Der Foo eben. Der Bruder vom Bar");
        
        project.setTags(new ArrayList<>(Arrays.asList(tag1,tag2)));
        project.setDescription("Das ist die Beschreibung.");
        project.setCreator(new User("Tutnix"));
        project.setStatus("new");
        
        client.addProject(project);
        
        //System.out.println(client.getProjectByID(2).getTitle());

        //User anlegen
//        Logger.getLogger(Client.class.getName()).log(Level.INFO, "Lege user Dagobert an");
//        PhoneUser dagobert = client.addUser("Dagobert");
//        
//        Logger.getLogger(Client.class.getName()).log(Level.INFO, "füge privatnummer hinzu");
//        client.addNumberToUser(dagobert.getId(), new PhoneNumber("0190666666", "Privatnummer"));
//        
//        Logger.getLogger(Client.class.getName()).log(Level.INFO, "füge Mobilnummer hinzu");
//        client.addNumberToUser(dagobert.getId(),new PhoneNumber("0162424242", "Mobil"));
//        
//        Logger.getLogger(Client.class.getName()).log(Level.INFO, "Lege user Donald an");
//        PhoneUser donald = client.addUser("Donald");
//        
//        Logger.getLogger(Client.class.getName()).log(Level.INFO, "Lege user Goofy an");
//        PhoneUser goofy = client.addUser("Goofy");
//        
//        Logger.getLogger(Client.class.getName()).log(Level.INFO, "lösche Dagobert");
//        client.deleteUser(dagobert.getId());
//        
//        PhoneUser existingUser = client.getUser(42);
        
        
        
        
        
        
        
    }

}

