package windyoak.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felix Haller
 */
@XmlRootElement(name = "rssposts")
public class RSSPosts
{

    private List<RSSPost> rssPosts = new ArrayList<>();

    // Dieser Konstruktor ist nötig, da das XML Framework ihn voraussetzt.
    public RSSPosts()
    {
        
    }

    public RSSPosts(List<RSSPost> rssPosts)
    {
        this.rssPosts = rssPosts;
    }

    @XmlElement(name = "RSSPost")
    public List<RSSPost> getRSSPosts()
    {
        return rssPosts;
    }

    public void setRSSPosts(List<RSSPost> rssPosts)
    {
        this.rssPosts = rssPosts;
    }
    
    public void addRSSPost(RSSPost rssPost)
    {
        this.rssPosts.add(rssPost);
    }

}
