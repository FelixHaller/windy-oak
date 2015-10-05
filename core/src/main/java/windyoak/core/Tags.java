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
 * Das Tags-Objekt bildet eine Liste von Tags für die Projekt-Objekte, da diese
 * mehrere Tags besitzen können.
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
@XmlRootElement(name = "tags")
public class Tags {

    private List<Tag> tags;

    // Diesen Konstruktor ist nötig, da das XML Framework das voraussetzt.
    public Tags() {

    }

    /**
     * Konstruktor.
     *
     * @param tags als Tag-Liste
     */
    public Tags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Gibt eine Liste mit Tag-Objekten zurück.
     *
     * @return
     */
    @XmlElement(name = "tag")
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Es wird eine List mit Tags als aktuelle Tag-Liste gesetzt.
     *
     * @param tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
