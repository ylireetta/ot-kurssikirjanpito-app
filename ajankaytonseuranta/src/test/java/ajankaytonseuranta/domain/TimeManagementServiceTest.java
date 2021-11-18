/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.domain;

import ajankaytonseuranta.dao.FakeCourseDao;
import ajankaytonseuranta.dao.FakeUserDao;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ylireett
 */
public class TimeManagementServiceTest {
    User loggedInUser;
    
    FakeUserDao userDao;
    FakeCourseDao courseDao;
    TimeManagementService tmService;
    
    @Before
    public void setup() throws Exception {
        this.userDao = new FakeUserDao();
        this.courseDao = new FakeCourseDao(loggedInUser);
        this.tmService = new TimeManagementService(userDao, courseDao);
    }
    
    @Test
    public void existingUserserIsFound() {
        tmService.createUser("TestUser");
        assertTrue(tmService.userExists("TestUser"));
    }
    
    @Test
    public void notExistingUserIsNotFound() {
        assertFalse(tmService.userExists("Nonexistent"));
    }
    
    @Test
    public void userCanBeAdded() {
        assertTrue(tmService.createUser("TestUser"));
    }
    
    @Test
    public void duplicateUsersCannotBeAdded() {
        tmService.createUser("TestUser");
        assertFalse(tmService.createUser("TestUser"));
    }
    
    @Test
    public void existingUserCanLogIn() {
        tmService.createUser("TestUser");
        assertTrue(tmService.login("TestUser"));
    }
    
    @Test
    public void notExistingUserCannotLogin() {
        assertFalse(tmService.login("Nonexistent"));
    }
    
    @Test
    public void loggedInUserIsAssignedProperly() {
        tmService.createUser("TestUser");
        tmService.login("TestUser");
        User loggedInUser = tmService.getLoggedInUser();
        
        assertEquals(userDao.findByUsername("TestUser"), loggedInUser);
    }
    
    @Test
    public void logoutSetsLoggedInUserToNull() {
        tmService.createUser("TestUser");
        tmService.login("TestUser");
        tmService.logout();
        
        assertNull(tmService.getLoggedInUser());
    }
    
    @Test
    public void returnEmptyListIfUserHasNoCourses() {
        tmService.createUser("TestUser");
        tmService.login("TestUser");
        
        assertTrue(tmService.getCoursesForLoggedInUser().isEmpty());
    }
    
    @Test
    public void returnCourseListIfUserHasCourses() {
        tmService.createUser("TestUser");
        tmService.login("TestUser");
        User loggedInUser = tmService.getLoggedInUser();
        tmService.createCourse(new Course("TestCourse1", 5, loggedInUser.getUserId()), loggedInUser);
        tmService.createCourse(new Course("TestCourse2", 5, loggedInUser.getUserId()), loggedInUser);
        
        assertEquals(2, tmService.getCoursesForLoggedInUser().size());
    }
    
    @After
    public void teardown() {
        userDao.deleteTestData();
    }
    
}
