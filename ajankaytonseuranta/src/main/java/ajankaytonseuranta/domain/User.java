/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("users")
public class User {
    private String username;
    @Id
    private ObjectId userId;
    
    /**
     * Dummy-konstruktori Morphia-kirjaston mappingia varten. Tätä ei ole tarkoitus käyttää olioiden luomiseen.
     */
    public User() {
        this.username = null;
    }
    
    /**
     * Varsinainen User-luokan konstruktori.
     * 
     * @param username Käyttäjän valitsema käyttäjätunnus
     */
    public User(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public ObjectId getUserId() {
        return this.userId;
    }
    
    /**
     * Tarkistaa, ovatko kaksi käyttäjää samat. 
     * Käyttäjät todetaan samoiksi, jos niiden käyttäjätunnus on sama.
     * 
     * @param object Vertailtava olio
     * 
     * @return Totuusarvo, ovatko kaksi vertailtavaa käyttäjää samat
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        
        if (!(object instanceof User)) {
            return false;
        }
        
        User compareUser = (User) object;
        
        if (this.username.equals(compareUser.username)) {
            return true;
        }
        
        return false;
    }
    
}
