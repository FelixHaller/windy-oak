package windyoak.client;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;

/**
 *
 * @author Felix Haller
 */
public class Test {
    
    public static void main(String[] args) throws MalformedURLException, IllegalArgumentException, FeedException, IOException
    {   
        SyndFeedInput input = new SyndFeedInput();
//        SyndFeed feed = input.build(new XmlReader(new URL("https://github.com/FelixHaller/windy-oak/commits/master.atom")));
        SyndFeed feed = input.build(new XmlReader(new URL("http://ein-freier-mensch.de/feed/")));
        
        for (SyndEntry entry : feed.getEntries())
        {
            System.out.println("===============");
            System.out.println("Titel: " + entry.getTitle().trim());
            System.out.println("Datum: " + entry.getUpdatedDate());
            System.out.println("Datum_pub: " + entry.getPublishedDate());
            System.out.println("Author: "+ entry.getAuthor());
            System.out.println("Desc: " +  Jsoup.parse(entry.getContents().get(0).getValue()).body().text());
            System.out.println(entry.getComments());
        }
        
    }

}
