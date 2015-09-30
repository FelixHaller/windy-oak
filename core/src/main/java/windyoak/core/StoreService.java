package windyoak.core;

import java.util.List;

/**
 *
 * @author fhaller1
 */
public interface StoreService {
    //Project

    /**
     * Gibt eine Liste aller Projekte zurück.
     *
     * Sollte es keine Projekte geben, gibt die Funktion eine leere Liste
     * zurück.
     *
     * @return Eine Liste mit allen Projekten
     */
    List<Project> fetchAllProjects() throws OakCoreException;

    /**
     * Gibt die letzten n Projekte absteigend geordnet nach dem
     * Erstellungsdatum.
     *
     * Die Liste beinhaltet nur Projekte, die sich im Status "published"
     * befinden.
     *
     * Sollte es keine Projekte geben, gibt die Funktion eine leere Liste
     * zurück.
     *
     * @param n maxmimale Anzahl an Projekten, die zurückgeliefert werden soll
     * @return Eine Liste mit Projekten
     */
    List<Project> fetchRecentProjects(int n) throws OakCoreException;

    /**
     * Gibt alle verfügbaren Informationen zu einem bestimmten Projekt zurück.
     *
     * Sollte die eingegebene ID keinem Projekt zugeordnet sein, gibt die
     * Methode <b>null</b> zurück.
     *
     * @param projectID Die id des Projektes
     * @return Ein Projekt oder null, wenn id nicht vorhanden.
     */
    Project getProjectByID(int projectID) throws OakCoreException;

    /**
     * Erstellt ein neues Projekt und liefert das erstellte Projekt samt
     * zugewiesener id zurück.
     *
     * @param project Das Projekt-Objekt, das in die Datenbank eingetragen
     * werden soll
     * @return Das angelegte Projekt.
     */
    Project createProject(Project project) throws OakCoreException;

    /**
     * Aktualisiert ein bestehendes Projekt.
     *
     * @param project Das überarbeitete Projekt-Objekt
     */
    Project updateProject(Project project) throws OakCoreException;

    /**
     * Löscht ein bestehendes Projekt.
     *
     * Dabei wird das Projekt in der Datenbank nicht gelöscht, sondern lediglich
     * als gelöscht markiert.
     *
     * @param projectID ID des zu löschenden Projektes.
     * @return Den letzten Stand des gelöschten Projektes.
     */
    Project deleteProject(int projectID) throws OakCoreException;

    //User
    /**
     * Gibt eine Liste mit allen bekannten Benutzern zurück.
     *
     * Angezeigt werden lediglich der username, Vorname und Nachname. Sollte es
     * keine Benutzer geben, gibt die Funktion eine leere Liste zurück.
     *
     * @return Liste alle Benutzer.
     */
    List<User> fetchAllUsers() throws OakCoreException;

    /**
     * Gibt alle bekannten Daten für einen bestimmten Benutzer aus.
     *
     * Sollte der übergebene username nicht existieren, gibt die Methode
     * <b>null</b> zurück.
     *
     * @param username
     * @return User Objekt.
     */
    User getUser(String username) throws OakCoreException;

    //Project/Comments
    /**
     * Gibt eine Liste aller Kommentare zu einem bestimmten Projekt zurück.
     *
     * @param ProjectsID Die ID des Projektes
     * @return Liste mit Kommentar-Objekten.
     */
    List<Comment> fetchAllComments(int projectID) throws OakCoreException;

    /**
     * Ein einzelnes Kommentar mit allen Details abrufen.
     *
     * @param commentID
     * @return Ein Kommentar-Objekt.
     */
    Comment getCommentByID(int commentID) throws OakCoreException;

    Comment deleteComment(int commentID) throws OakCoreException;

    Comment updateComment(Comment comment) throws OakCoreException;

    Comment createComment(Comment comment) throws OakCoreException;

}
