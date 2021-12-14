/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.User;
import com.mongodb.BasicDBObject;
import dev.morphia.Datastore;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ylireett
 */
public class ConcreteUserDaoTest {
    UserDao dao;
    boolean testmode = true;
    
    @Before
    public void setup() throws Exception {
        dao = new ConcreteUserDao(testmode);
    }
    
    @Test
    public void userCanBeCreated() throws Exception {
        User user = new User("Test User");
        User createdUser = dao.createUser(user);
        
        assertEquals(user, createdUser);
        assertTrue(dao.getAll().contains(createdUser));
    }
    
    @Test
    public void existingUserIsFound() throws Exception {
        User user = new User("Test User");
        dao.createUser(user);
        
        assertEquals(dao.findByUsername("Test User"), user);
    }
    
    @Test
    public void notExistingUserIsNotFound() {
        assertNull(dao.findByUsername("Nonexistent"));
    }
    
    @After
    public void teardown() {
        // TODO: clear test db
    }
}
