/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.domain;

import ajankaytonseuranta.dao.FakeCourseDao;
import ajankaytonseuranta.dao.FakeUserDao;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class CourseTest {
    User user;
    Course c;
    
    @Before
    public void setup() throws Exception {
        user = new User("User");
        c = new Course("Course", 5, user.getUserId());
    }
    
    @Test
    public void dummyConstructorWorks() {
        Course dummy = new Course();
        assertNull(dummy.getCourseId());
    }
    
    @Test
    public void courseEqualsCourse() {
        assertTrue(c.equals(c));
    }
    
    @Test
    public void courseDoesNotEqualUser() {
        assertFalse(c.equals(user));
    }
    
    @Test
    public void timeSpentCanBeSet() {
        c.setTimeSpent(2000);
        
        assertTrue(c.getTimeSpent() == 2000);
    }
    
    @Test
    public void courseCreditsAreReturned() {
        assertTrue(c.getCourseCredits() == 5);
    }
    
    
    
}
