package windyoak.core;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felix Haller
 */
@XmlRootElement(name = "tag")
public class Tag
{
    private String name;
    private String description;
    
    public Tag()
    {
        
    }
    
    public Tag(String name)
    {
        this.name = name;
    }
    
    public Tag(String name, String description)
    {
        this.name = name;
        this.description = description;
    }
    

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
