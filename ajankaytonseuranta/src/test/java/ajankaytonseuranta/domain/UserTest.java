/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author ylireett
 */
public class UserTest {
    @Test
    public void equalsTrueWhenUsernameIsTheSame() {
        User u1 = new User("User");
        User u2 = new User("User");
        
        assertTrue(u1.equals(u2));
    }
    
    @Test
    public void dummyConstructorWorks() {
        User user = new User();
        assertNull(user.getUsername());
    }
    
}
