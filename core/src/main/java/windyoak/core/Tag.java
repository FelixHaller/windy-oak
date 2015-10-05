package windyoak.core;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ein Tag-Objekt ist wie ein Schlüsselwort, welches das Projekt näher
 * beschreibt. Ein Projekt-Objekt kann mehrere solche Tags besitzen.
 *
 * @author Felix Haller
 */
@XmlRootElement(name = "tag")
public class Tag {

    private String name;
    private String description;

    public Tag() {

    }

    /**
     * Konstruktor.
     *
     * @param name Tag-Objekt Name (Schlüsselwort)
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Konstruktor.
     *
     * @param name Tag-Objekt Name (Schlüsselwort)
     * @param description Beschreibung/Erklärung des Schlüsselworts
     */
    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Name des Tags zurückgeben.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Name des Tags setzten.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Beschreibung wird zurückgegeben.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Beschreibung wird gesetzt.
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
