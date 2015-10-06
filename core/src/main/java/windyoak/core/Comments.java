package windyoak.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felix Haller
 */
@XmlRootElement(name="comments")
public class Comments {
    
    private List<Comment> comments = new ArrayList<>();

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
