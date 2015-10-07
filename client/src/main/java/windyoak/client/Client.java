package windyoak.client;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import windyoak.core.Comment;
import windyoak.core.Comments;
import windyoak.core.Project;
import windyoak.core.Projects;
import windyoak.core.RSSPosts;

/**
 * Der Testclient für den WindyOak REST-Service.
 * 
 * @author fhaller1
 */
public class Client
{

    // der verwendete Client
    private final HttpClient httpClient;
    // die Basis URI zum Webservice
    private String baseUri;
    // Feld für den Mediatype XML
    private final String xml = "application/xml";
    // Feld für den Mediatype json
    private final String json = "application/json";
    
    private static final boolean VERBOSE = true;

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

    public Project addProject(  String title, 
                            String creator, 
                            String description, 
                            String members,
                            String status,
                            String tagNames,
                            String postsURL
                            )
    {
        String response;
        Project project;
        PostMethod postMethod = new PostMethod(getBaseUri() + "/projects");
        
        //Request auf Charset UTF-8 (das Charset der Datenbank) setzen
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        
        postMethod.addParameter("name", title);
        postMethod.addParameter("username", creator);
        postMethod.addParameter("description", description);
        postMethod.addParameter("members", members);
        postMethod.addParameter("status", status);
        postMethod.addParameter("postsURL", postsURL);
        postMethod.addParameter("tagNames", tagNames);
        postMethod.setRequestHeader("Accept", xml);

        try
        {
            int responseCode = httpClient.executeMethod(postMethod);
            System.out.println("Der Response-Code war: " + responseCode);
            response = new String(postMethod.getResponseBody(),"UTF-8");
            

        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        project = projectFromResponse(response);

        return project;
    }
    
    public Project getProjectByID(int id)
    {
        String response;
        Project project;
        GetMethod getMethod = new GetMethod(getBaseUri() + "/projects/"+id);
        getMethod.setRequestHeader("Accept", xml);
        try
        {
            int responseCode = httpClient.executeMethod(getMethod);
            System.out.println("Der ResponseCode war: " + responseCode);
            if (responseCode != 200)
            {
                return null;
            }
            response = getMethod.getResponseBodyAsString();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        project = projectFromResponse(response);
        
        return project;
        
    }
    
    
    public Project updateProjectAttribute(
                            int id,
                            String key,
                            String value
                        )
    {
        String response;
        Project project;
        
        PutMethod putMethod = new PutMethod(getBaseUri() + "/projects/"+id);
        
        //Request auf Charset UTF-8 (das Charset der Datenbank) setzen
        putMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        putMethod.setRequestHeader("Accept", xml);
        
        putMethod.setQueryString(new NameValuePair[] {
            new NameValuePair(key, value)
        });
        
        
        try
        {
            int responseCode = httpClient.executeMethod(putMethod);
            System.out.println("Der Response-Code war: " + responseCode);
            response = new String(putMethod.getResponseBody(),"UTF-8");
            if (responseCode >= 300)
            {
                throw new RuntimeException(response);
            }
            

        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        project = projectFromResponse(response);

        return project;
    }
    
    public Projects searchProject(String key, String value)
    {
        Projects projects = new Projects();
        String response;
        GetMethod getMethod = new GetMethod(getBaseUri() + "/projects");      
        getMethod.setRequestHeader("Accept", xml);
        getMethod.setQueryString(new NameValuePair[] {
            new NameValuePair(key, value)
        });
        try
        {
            int responseCode = this.httpClient.executeMethod(getMethod);
            System.out.println("ResponseCode war: " + responseCode);
            response = getMethod.getResponseBodyAsString();
            if (responseCode != 200)
            {
                System.out.println(response);
                return projects;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        projects = this.projectsFromResponse(response);
        
        return projects;
        
        
        
    }
    
    public Comments getCommentsForProject(int id)
    {
        Comments comments = new Comments();
        String response;
        GetMethod getMethod = new GetMethod(getBaseUri() + "/projects/"+id+"/comments");      
        getMethod.setRequestHeader("Accept", xml);
        try
        {
            int responseCode = this.httpClient.executeMethod(getMethod);
            System.out.println("ResponseCode war: " + responseCode);
            response = getMethod.getResponseBodyAsString();
            if (responseCode != 200)
            {
                System.out.println(response);
                return comments;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        comments = this.commentsFromResponse(response);
        
        return comments;
    }
    
    public Comment addCommentToProject(int projectID, String title, String creator, String content, String status)
    {
        String response;
        Comment comment = new Comment();
        PostMethod postMethod = new PostMethod(getBaseUri() + "/projects/"+projectID+"/comments");
        
        //Request auf Charset UTF-8 (das Charset der Datenbank) setzen
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        
        postMethod.addParameter("title", title);
        postMethod.addParameter("creator", creator);
        postMethod.addParameter("content", content);
        postMethod.addParameter("status", status);
        postMethod.setRequestHeader("Accept", xml);

        try
        {
            int responseCode = httpClient.executeMethod(postMethod);
            System.out.println("Der Response-Code war: " + responseCode);
            response = new String(postMethod.getResponseBody(),"UTF-8");
            if (responseCode >=300)
            {
                System.out.println(response);
                return comment;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        comment = commentFromResponse(response);

        return comment;
    }
    
    public RSSPosts getPostsForProject(int projectID)
    {
        RSSPosts rssPosts = new RSSPosts();
        String response;
        GetMethod getMethod = new GetMethod(getBaseUri() + "/projects/"+projectID+"/posts");      
        getMethod.setRequestHeader("Accept", xml);
        try
        {
            int responseCode = this.httpClient.executeMethod(getMethod);
            System.out.println("ResponseCode war: " + responseCode);
            response = getMethod.getResponseBodyAsString();
            if (responseCode != 200)
            {
                System.out.println(response);
                return rssPosts;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        rssPosts = this.rssPostsFromResponse(response);
        
        return rssPosts;
    }
    
    public Projects getRecentProjects()
    {
        Projects projects = new Projects();
        String response;
        GetMethod getMethod = new GetMethod(getBaseUri() + "/recent/projects");      
        getMethod.setRequestHeader("Accept", xml);
        try
        {
            int responseCode = this.httpClient.executeMethod(getMethod);
            System.out.println("ResponseCode war: " + responseCode);
            response = getMethod.getResponseBodyAsString();
            if (responseCode != 200)
            {
                System.out.println(response);
                return projects;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        projects = this.projectsFromResponse(response);
        
        return projects;
    }
    
    public Project deleteProject(int projectID)
    {
        String response;
        Project project;
        DeleteMethod deleteMethod = new DeleteMethod(getBaseUri() + "/projects/"+projectID);
        deleteMethod.setRequestHeader("Accept", xml);
        try
        {
            int responseCode = httpClient.executeMethod(deleteMethod);
            System.out.println("Der ResponseCode war: " + responseCode);
            response = deleteMethod.getResponseBodyAsString();
            if (responseCode != 200)
            {
                throw new RuntimeException(response);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung");
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println("Letzter Stand des gelöschten Projektes: ");
            System.out.println(prettyPrintXml(response));
        }
        
        project = projectFromResponse(response);
        
        return project;
    }
    

    /**
     * Der Client testet:
     * 
     * - Anzeigen eines Demo - Projektes
     * - Erstellen eines Projektes
     * - Suchen nach Projektnamen
     * - Modifizieren eines Projektes
     * - Tags zu Projekt hinzufügen
     * - Filtern nach tags
     * - Anzeigen der Kommentare zu einem Projekt
     * - Erstellen eines Kommentars zu einem Projekt
     * - Anzeigen der RSS-Posts
     * - Anzeigen der letzten angelegten Projekte
     * - Projekt löschen
     * - Projekt erneut versuchen anzuzeigen
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        Client client = new Client("http://localhost:8080");
            
        System.out.println("========================DEMO startet========================");
        int id = 3;
        
        System.out.println(String.format("Rufe Projekt mit der ID %d ab.", id));
        Project project = client.getProjectByID(id);
        System.out.println(String.format("Name von Projekt mit ID %d: ", id) + project.getTitle());
        //pause();
        
        System.out.println("Rufe auch Kommentare zu diesem Projekt ab.");
        Comments comments = client.getCommentsForProject(id);
        if (comments.getComments().size() > 0)
        {
            System.out.println("Inhalt des ersten Comments: " + comments.getComments().get(0).getContent());
        }
        else
        {
            System.out.println("Keine Comments im Projekt.");
        }
        
        
        int id2 = 42;
        
        System.out.println(String.format("Rufe Projekt mit der ID %d ab. (welches nicht vorhanden ist)", id2));
        Project project2 = client.getProjectByID(id2);
        
        System.out.println("Lege neues Projekt an...");
        Project windyoakProject = client.addProject(
            "Windy-Oak", 
            "Tutnix", 
            "Unser Beleg für das Modul Verteilte Sisteme", 
            "Tutnix,Chef;GMine,Chefin;",
            "published",
            "Java",
            "https://github.com/FelixHaller/windy-oak/commits/master.atom");
        
        System.out.println("Das neue Projekt wurde angelegt unter der ID: " + windyoakProject.getId());
        
        String suchString= "sinnvoll";
        
        System.out.println(String.format("Suche nach Projekt mit '%s' im Namen.", suchString));
        Projects foundProjects = client.searchProject("title",suchString);
        System.out.println(String.format("Es wurden %d Projekt(e) gefunden mit '%s' im Namen.", foundProjects.getProjects().size(), suchString));
        

        System.out.println("Entferne Tippfehler in angelegtem windy-oak Projekt...");
        Project updatedProject = client.updateProjectAttribute(
            windyoakProject.getId(),
            "description",
            "Unser Beleg für das Modul Verteilte Systeme");
        System.out.println("...und füge Tag Java mit Hilfe vorher erhaltener ID hinzu.");
        updatedProject = client.updateProjectAttribute(
            windyoakProject.getId(),
            "tagNames",
            "java,rest");
        
        String tag = "Java";
        System.out.println(String.format("Rufe alle Projekte die mit '%s' markiert sind ab.",tag));
        Projects foundProjects2 = client.searchProject("tag", tag);
        System.out.println(String.format("Es wurden %d Projekt(e) gefunden.", foundProjects2.getProjects().size()));
        
        System.out.println("Füge Kommentar zu windy-Oak Projekt hinzu.");
        Comment comment = client.addCommentToProject(
            windyoakProject.getId(), 
            "Weiter so!", 
            "Tutnix", 
            "Mir gefällt, was ihr macht.", 
            "published");
        System.out.println("Der Kommentar bekam die ID: " + comment.getId());
        
        System.out.println("Zeige Posts (Live RSS/Atom Feed von hinterlegter URL) zu Windy-Oak Projekt an.");
        RSSPosts rssPosts = client.getPostsForProject(windyoakProject.getId());
        
        if (rssPosts.getRSSPosts().size() > 0)
        {
            System.out.println("Inhalt des letzten Posts: \n ======================================\n" + rssPosts.getRSSPosts().get(0).getDescription());
        }
        else
        {
            System.out.println("Keine Nachrichten vom Projekt.");
        }
        System.out.println("======================================");
        
        System.out.println("Rufe zuletzt angelegte Projekte ab.");
        Projects recentProjects = client.getRecentProjects();
        System.out.println(String.format("Das neueste Projekt nennt sich '%s' und hat die ID %d", 
            recentProjects.getProjects().get(0).getTitle(),
            recentProjects.getProjects().get(0).getId()));
        
        System.out.println("Lösche Windy-Oak Projekt wieder.");
        Project deletedProject = client.deleteProject(windyoakProject.getId());
        
        System.out.println("Versuche gerade gelöschtes Projekt erneut aufzurufen");
        Project nomoreProject = client.getProjectByID(windyoakProject.getId());
        
        
        
    }
    
    public static void pause()
    {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Enter drücken um fortzufahren...");
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();
    }
    
    private Project projectFromResponse(String response)
    {
        Project project;
        
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            project = (Project) unmarshallfromXML(new StringReader(response), jaxbContext);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Unmarshalling von Project", ex);
            throw new RuntimeException("Fehler beim Unmarshalling von Project");
        }
        return project;
    }
    
    private Projects projectsFromResponse(String response)
    {
        Projects projects;
        
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Projects.class);
            projects = (Projects) unmarshallfromXML(new StringReader(response), jaxbContext);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Unmarshalling von Projects", ex);
            throw new RuntimeException("Fehler beim Unmarshalling von Projects");
        }
        return projects;
    }
    private Comments commentsFromResponse(String response)
    {
        Comments comments;
        
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Comments.class);
            comments = (Comments) unmarshallfromXML(new StringReader(response), jaxbContext);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Unmarshalling von Comments", ex);
            throw new RuntimeException("Fehler beim Unmarshalling von Comments");
        }
        return comments;
    }
    
    private Comment commentFromResponse(String response)
    {
        Comment comment;
        
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Comment.class);
            comment = (Comment) unmarshallfromXML(new StringReader(response), jaxbContext);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Unmarshalling von Comment", ex);
            throw new RuntimeException("Fehler beim Unmarshalling von Comment");
        }
        return comment;
    }
    
    private RSSPosts rssPostsFromResponse(String response)
    {
        RSSPosts rssPosts;
        
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(RSSPosts.class);
            rssPosts = (RSSPosts) unmarshallfromXML(new StringReader(response), jaxbContext);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Unmarshalling von Posts", ex);
            throw new RuntimeException("Fehler beim Unmarshalling von Posts");
        }
        return rssPosts;
    }
    
    public static String prettyPrintXml(final String xml)
    {
        Writer writer;
        try
        {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
        }
        catch (ParserConfigurationException | TransformerException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Pretty Print von XML", ex);
            throw new RuntimeException(ex.getMessage());
        }
        catch (SAXException | IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler beim Pretty Print von XML", ex);
            throw new RuntimeException(ex.getMessage());
        }
        return writer.toString();
    }
    

}

