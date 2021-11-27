/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.User;
import dev.morphia.Datastore;
import java.util.List;
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
    public void dummyConstructorWorks() {
        ConcreteUserDao dummyDao = new ConcreteUserDao();
        assertNull(dummyDao.getAll());
    }
    
    @Test
    public void userCanBeCreated() throws Exception {
        User user = new User("Test User");
        User createdUser = dao.createUser(user);
        
        assertEquals(user, createdUser);
        assertTrue(dao.getAll().size() == 1);
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
}
