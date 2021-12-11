/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author ylireett
 */
public class ConcreteUserDao implements UserDao {
    private Datastore store;
    private List<User> userlist;
    
    // Dummy constructor
    public ConcreteUserDao() {
        store = null;
        userlist = null;
    }
    
    /**
     * ConcreteUserDao-luokan konstruktori.
     * 
     * @param testmode  Totuusarvo, jonka avulla voidaan eriyttää testi- ja tuotantotietokannat toisistaan
     * @throws Exception 
     */
    public ConcreteUserDao(boolean testmode) throws Exception {
        this.store = createConnection(testmode);
        this.userlist = getUsersFromDb();
    }
    
    /**
     * Muodostaa yhteyden config.properties-tiedostossa määriteltyyn tietokantaan.
     * 
     * @param testmode Totuusarvo, jonka avulla erotetaan testauksessa ja tuotannossa käytettävät dokumenttikokoelmat toisistaan
     * 
     * @return Ohjelman ajon aikana käytettävä tietokantayhteys
     * @throws Exception 
     */
    private Datastore createConnection(boolean testmode) throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/config.properties"));
        
        String dbAddress = "";
        String datastoreName = "";
        String datastoreMapper = "";
        
        if (testmode) {
            dbAddress = properties.getProperty("testDbAddress");
            datastoreName = properties.getProperty("testDatastoreName");
            datastoreMapper = properties.getProperty("testDatastoreMapper");
        } else {
            dbAddress = properties.getProperty("dbAddress");
            datastoreName = properties.getProperty("datastoreName");
            datastoreMapper = properties.getProperty("datastoreMapper");
        }
        
        MongoClient mc = MongoClients.create(dbAddress);
        Datastore store = Morphia.createDatastore(mc, datastoreName);
        store.getMapper().mapPackage(datastoreMapper);
        
        return store;
    }
    
    /**
     * Hakee tietokantaan talletetut User-luokan oliot ja asettaa saadun listan ConcreteUserDao-luokan yksityiseen oliomuuttujaan.
     * 
     * @return Lista, joka sisältää tietokantaan talletetut käyttäjäoliot
     */
    private List<User> getUsersFromDb() {
        List<User> users = new ArrayList<>();
        
        Query<User> query = store.find(User.class);
        
        for (User user : query) {
            users.add(user);
        }
        
        return users;
    }
    
    /**
     * Tallettaa annetun User-luokan olion tietokantaan.
     * 
     * @param user Talletettava käyttäjäolio
     * 
     * @return Talletettu käyttäjäolio
     * @throws Exception 
     */
    @Override
    public User createUser(User user) throws Exception {
        store.save(user);
        userlist = getUsersFromDb();
        return user;
    }
    
    /**
     * 
     * @return Tietokantaan talletetut käyttäjät
     */
    @Override
    public List<User> getAll() {
        return userlist;
    }
    
    /**
     * Hakee annettua käyttäjätunnusta vastaavan User-luokan olion tietokannasta.
     * 
     * @param username Haettava käyttäjä
     * 
     * @return Annettua tunnusta vastaava User-olio tai null, mikäli tunnuksella ei löydy käyttäjää
     */
    @Override
    public User findByUsername(String username) {
        return userlist.stream()
            .filter(oneUser -> oneUser.getUsername()
            .equals(username))
            .findFirst()
            .orElse(null);
    }
}
