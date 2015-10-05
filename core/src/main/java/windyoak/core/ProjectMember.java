/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.core;

/**
 * Dieses Objekt repräsentiert ein Projekt-Mitglied. Es besteht aus einem User
 * und einer Rolle (role).
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
public class ProjectMember {

    private User user;
    private String role;

    public ProjectMember() {
    }

    /**
     * Gibt den User zurück.
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Setzt den User.
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Holt die Rolle, die ein Projekt-Mitglied übernimmt.
     *
     * @return
     */
    public String getRole() {
        return role;
    }

    /**
     * Setzt die Rolle, die ein Projekt-Mitglied übernimmt.
     *
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

}
