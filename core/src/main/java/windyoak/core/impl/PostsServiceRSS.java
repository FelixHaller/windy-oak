package windyoak.core.impl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import org.jsoup.Jsoup;
import windyoak.core.OakCoreException;
import windyoak.core.PostsService;
import windyoak.core.RSSPost;
import windyoak.core.RSSPosts;



/**
 *
 * @author Felix Haller
 */
public class PostsServiceRSS implements PostsService
{
    String errormsg;
    
    @Override
    public RSSPosts getPosts(URL url) throws OakCoreException
    {
        RSSPosts rssPosts = new RSSPosts();
        
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed;
        
        try
        {
            feed = input.build(new XmlReader(url));
        }
        catch (IllegalArgumentException | FeedException | IOException ex)
        {
            errormsg = "Fehler beim Abrufen des Feeds";
            Logger.getLogger(PostsServiceRSS.class.getName()).log(Level.SEVERE, errormsg, ex);
            throw new OakCoreException(errormsg);
        }
        
        for (SyndEntry entry : feed.getEntries())
        {
            RSSPost post = new RSSPost();
            post.setId(getSHA1HashFromString(entry.getUri()));
            post.setTitle(entry.getTitle().trim());
            post.setCreator(entry.getAuthor().trim());
            post.setDescription(Jsoup.parse(entry.getContents().get(0).getValue()).body().text());
            try
            {
                post.setUrl(new URL(entry.getLink()));
            }
            catch (MalformedURLException ex)
            {
                errormsg = "URL ungültig";
                Logger.getLogger(PostsServiceRSS.class.getName()).log(Level.WARNING, errormsg, ex);
            }
            
            if (entry.getUpdatedDate() != null)
            {
                post.setPubDate(entry.getPublishedDate());
            }
            else
            {
                post.setPubDate(entry.getUpdatedDate());
            }
            
            try
            {
                post.setCommentsURL(new URL(entry.getComments()));
            }
            catch (MalformedURLException ex)
            {
                errormsg = "Kommentar URL ungültig";
                Logger.getLogger(PostsServiceRSS.class.getName()).log(Level.WARNING, errormsg, ex);
            }
            
            rssPosts.addRSSPost(post);            
        }
        
        return rssPosts;
        
    }

    @Override
    public RSSPost getPostByID(URL url, String id) throws OakCoreException
    {
        RSSPosts rssPosts = this.getPosts(url);
        
        for (RSSPost post : rssPosts.getRSSPosts())
        {
            if (post.getId().equals(id))
            {
                return post;
            }
        }
        return null;
    }
    
    private String getSHA1HashFromString(String message) throws OakCoreException
	{
		/* Berechnung */
		MessageDigest md;
		try
		{
			md = MessageDigest.getInstance("SHA1");
		}
		catch (NoSuchAlgorithmException ex)
		{
			errormsg = "Verschlüsselungsalgorithmus nicht gefunden";
			Logger.getLogger(PostsServiceRSS.class.getName()).log(Level.SEVERE, errormsg, ex);
			throw new OakCoreException((errormsg));
		}
		md.reset();
		md.update(message.getBytes());
		byte[] result = md.digest();

		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < result.length; i++)
		{
			hexString.append(Integer.toHexString(0xFF & result[i]));
		}
		return hexString.toString();
	}
    

}
