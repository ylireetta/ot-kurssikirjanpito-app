/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.User;
import com.mongodb.BasicDBObject;
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
public class FakeUserDao implements UserDao {
    private List<User> userlist;
    private Datastore store;
    
    public FakeUserDao() throws Exception {
        this.store = createConnection();
        this.userlist = getUsersFromDb();
    }
    
    private Datastore createConnection() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        
        String testDbAddress = properties.getProperty("testDbAddress");
        String testDatastoreName = properties.getProperty("testDatastoreName");
        String testDatastoreMapper = properties.getProperty("testDatastoreMapper");
        
        MongoClient mc = MongoClients.create(testDbAddress);
        Datastore store = Morphia.createDatastore(mc, testDatastoreName);
        store.getMapper().mapPackage(testDatastoreMapper);
        
        return store;
    }
    
    @Override
    public User createUser(User user) {
        store.save(user);
        userlist = getUsersFromDb();
        return user;
    }
    
    @Override
    public User findByUsername(String username) {
        return userlist.stream()
            .filter(oneUser -> oneUser.getUsername()
            .equals(username))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public List<User> getAll() {
        return userlist;
    }
    
    private List<User> getUsersFromDb() {
        List<User> users = new ArrayList<>();
        
        Query<User> query = store.find(User.class);
        
        for (User user : query) {
            users.add(user);
        }
        
        return users;
    }
    
    public void deleteTestData() {
        store.getCollection(User.class).deleteMany(new BasicDBObject());
    }
}
