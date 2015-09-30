package windyoak.core;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Felix Haller
 */
public class RSSPosts
{

    private List<RSSPost> rssPosts;

    // Diesen Konstruktor ist nötig, da das XML Framework ihn voraussetzt.
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
