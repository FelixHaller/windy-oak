/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windyoak.core;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Konstantin Lorenz (klorenz1@hs-mittweida.de)
 */
public class Member {

    private User user;
    private String role;


    public Member() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
