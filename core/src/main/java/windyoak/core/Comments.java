package windyoak.core;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Felix Haller
 */
public class Comments {
    
    private List<Comment> comments;

    // Diesen Konstruktor ist nötig, da das XML Framework das voraussetzt.
    public Comments()
    {
        
    }

    public Comments(List<Comment> comments)
    {
        this.comments = comments;
    }

    @XmlElement(name = "comment")
    public List<Comment> getComments()
    {
        return comments;
    }

    public void setComments(List<Comment> comments)
    {
        this.comments = comments;
    }

}
