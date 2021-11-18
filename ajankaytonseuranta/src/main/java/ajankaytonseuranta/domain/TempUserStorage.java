/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.domain;

import java.util.ArrayList;

/**
 *
 * @author ylireett
 */
public class TempUserStorage {
    private ArrayList<User> users;
    
    public TempUserStorage() {
        this.users = new ArrayList<>();
    }
    
    public void addUser(User user) {
        if (user.getUsername().equals("")) {
            System.out.println("Anna käyttäjänimi");
        } else {
            if (!this.users.contains(user)) {
                this.users.add(user);
                System.out.println("Käyttäjä lisätty");
            } else {
                System.out.println("Käyttäjä on jo olemassa. Käytä Kirjaudu sisään -toimintoa");
            }
        }
    }
    
}
