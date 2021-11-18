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
import java.io.FileInputStream;
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
    
    public ConcreteUserDao() throws Exception {
        this.store = createConnection();
        this.userlist = getUsersFromDb();
    }
    
    private Datastore createConnection() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        
        String dbAddress = properties.getProperty("dbAddress");
        String datastoreName = properties.getProperty("datastoreName");
        String datastoreMapper = properties.getProperty("datastoreMapper");
        
        MongoClient mc = MongoClients.create(dbAddress);
        Datastore store = Morphia.createDatastore(mc, datastoreName);
        store.getMapper().mapPackage(datastoreMapper);
        
        
        return store;
    }
    
    private List<User> getUsersFromDb() {
        List<User> users = new ArrayList<>();
        
        Query<User> query = store.find(User.class);
        
        for (User user : query) {
            users.add(user);
        }
        
        return users;
    }
    
    @Override
    public User createUser(User user) throws Exception {
        store.save(user);
        userlist = getUsersFromDb();
        return user;
    }
    
    @Override
    public List<User> getAll() {
        return userlist;
    }
    
    @Override
    public User findByUsername(String username) {
        return userlist.stream()
            .filter(oneUser -> oneUser.getUsername()
            .equals(username))
            .findFirst()
            .orElse(null);
    }
}
