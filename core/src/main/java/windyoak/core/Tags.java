/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.core;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
@XmlRootElement(name = "tags")
public class Tags {
    
    private List<Tag> tags;

    // Diesen Konstruktor ist nötig, da das XML Framework das voraussetzt.
    public Tags()
    {
        
    }

    public Tags(List<Tag> tags)
    {
        this.tags = tags;
    }

    @XmlElement(name = "tag")
    public List<Tag> getTags()
    {
        return tags;
    }

    public void setTags(List<Tag> users)
    {
        this.tags = users;
    }

}
