package windyoak.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felix Haller
 */
@XmlRootElement(name="members")
public class Members {
    
    private List<Member> members = new ArrayList<>();

    // Diesen Konstruktor ist nötig, da das XML Framework das voraussetzt.
    public Members()
    {
        
    }

    public Members(List<Member> members)
    {
        this.members = members;
    }

    @XmlElement(name = "member")
    public List<Member> getMembers()
    {
        return members;
    }

    public void setMembers(List<Member> members)
    {
        this.members = members;
    }

}
