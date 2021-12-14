/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.User;
import java.util.List;

public interface UserDao {
    /**
     * Tallettaa annetun User-luokan olion tietokantaan.
     * 
     * @param user Talletettava käyttäjäolio
     * 
     * @return Talletettu käyttäjäolio
     * @throws Exception 
     */
    User createUser(User user) throws Exception;
    
    /**
     * Hakee annettua käyttäjätunnusta vastaavan User-luokan olion tietokannasta.
     * 
     * @param username Haettava käyttäjä
     * 
     * @return Annettua tunnusta vastaava User-olio tai null, mikäli tunnuksella ei löydy käyttäjää
     */
    User findByUsername(String username);
    
    /**
     * Palauttaa tietokantaan talletetut käyttäjät listana.
     * @return Lista tietokantaan talletetuista User-olioista
     */
    List<User> getAll();
    
}
