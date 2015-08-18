package windyoak.core;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author klorenz1
 */
@XmlRootElement(name = "users")
public class Users {
    
    private List<User> users;

    // Diesen Konstruktor ist nötig, da das XML Framework das voraussetzt.
    public Users()
    {
        
    }

    public Users(List<User> users)
    {
        this.users = users;
    }

    @XmlElement(name = "user")
    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

}
