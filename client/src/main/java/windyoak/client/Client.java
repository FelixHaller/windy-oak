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
import windyoak.core.Tag;

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
            if (responseCode != 200)
            {
                return projects;
            }
            response = getMethod.getResponseBodyAsString();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Fehler bei Ausführung", ex);
            throw new RuntimeException(ex.getMessage());
        }
        
        if (VERBOSE)
        {
            System.out.println(prettyPrintXml(response));
        }
        
        projects = this.projectsFromResponse(response);
        
        return projects;
        
        
        
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
     * - Anzeigen der letzten Posts/angelegten Projekte
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
        
        int id2 = 42;
        
        System.out.println(String.format("Rufe Projekt mit der ID %d ab.", id2));
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

