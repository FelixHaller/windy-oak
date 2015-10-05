package windyoak.client;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.json.JSONObject;
import windyoak.core.Comment;
import windyoak.core.Comments;
import windyoak.core.Project;
import windyoak.core.Projects;
import windyoak.core.RSSPosts;
import windyoak.core.Tag;

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

    public int addProject(  String title, 
                            String creator, 
                            String description, 
                            String members,
                            String status,
                            String postsURL)
    {
        String response;
        JSONObject jsonObject;
        int projectID;
        PostMethod postMethod = new PostMethod(getBaseUri() + "/projects");
        
        //Request auf Charset UTF-8 (das Charset der Datenbank) setzen
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        
        postMethod.addParameter("name", title);
        postMethod.addParameter("username", creator);
        postMethod.addParameter("description", description);
        postMethod.addParameter("members", members);
        postMethod.addParameter("status", status);
        postMethod.addParameter("postsURL", postsURL);
        postMethod.setRequestHeader("Accept", json);

        try
        {
            int responseCode = httpClient.executeMethod(postMethod);
            response = new String(postMethod.getResponseBody(),"UTF-8");

        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex);
        }
        try
        {
            jsonObject = new JSONObject(response);
            System.out.println(jsonObject.toString(4));
            projectID = jsonObject.getInt("id");
        }
        catch (JSONException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Rückgabe fehlerhaft");
            throw new RuntimeException(ex);
        }

        return projectID;
    }
    
    public Project getProjectByID(int id)
    {
        String response;
        GetMethod getMethod = new GetMethod(getBaseUri() + "/projects/"+id);
        getMethod.setRequestHeader("Accept", xml);
        JAXBContext jaxbContext;
        Project newProject;
        try
        {
            jaxbContext = JAXBContext.newInstance(Project.class);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "JAXB Error");
            throw new RuntimeException(ex);
        }
        try
        {
            int responseCode = httpClient.executeMethod(getMethod);
            response = getMethod.getResponseBodyAsString();
            newProject = (Project) unmarshallfromXML(new StringReader(response), jaxbContext);
        }
        catch (UnmarshalException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Unmarshalling");
            throw new RuntimeException(ex);
        }
        catch (IOException | JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "JAXB Fehler");
            throw new RuntimeException(ex);
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
     * - Projekt löschen
     * - Projekt erneut versuchen anzuzeigen
     * 
     * @param args
     * @throws JAXBException
     * @throws IOException
     */
    public static void main(String[] args)
    {
        Client client = new Client("http://localhost:8080");
        
        System.out.println("========================DEMO startet========================");
        System.out.println("Lege neues Projekt an...");
        System.out.println("Ausgabe: ");
        int projectID = client.addProject("Windy-Oak", "Tutnix", "Unser Beleg für das Modul Verteilte Systeme", "Tutnix,Chef;GMine,Chefin;","new","https://github.com/FelixHaller/windy-oak/commits/master.atom");
        
        System.out.println("Die ID des angelegten Projektes ist: " + projectID);
        
        
        
    }

}

