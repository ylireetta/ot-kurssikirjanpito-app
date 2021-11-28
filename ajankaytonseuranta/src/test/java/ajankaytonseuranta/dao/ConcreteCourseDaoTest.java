/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.User;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ylireett
 */
public class ConcreteCourseDaoTest {
    CourseDao dao;
    FakeUserDao fakeUserDao;
    User testUser;
    boolean testmode = true;
    
    @Before
    public void setup() throws Exception {
        fakeUserDao = new FakeUserDao();
        testUser = new User("TestUser");
        fakeUserDao.createUser(testUser);
        dao = new ConcreteCourseDao(testUser, testmode);
    }
    
    @Test
    public void courseCanBeAdded() throws Exception {
        Course newCourse = new Course("TestCourse", 5, testUser.getUserId());
        dao.createCourse(newCourse, testUser);
        
        assertEquals(dao.findCoursesForUser(testUser).size(), 1);
    }
    
    @Test
    public void emptyListReturnedIfNoCourses() {
        assertEquals(dao.findCoursesForUser(testUser).size(), 0);
    }
    
    @Test
    public void courseIsFoundById() throws Exception {
        Course newCourse = new Course("TestCourse", 5, testUser.getUserId());
        dao.createCourse(newCourse, testUser);
        
        assertEquals(newCourse, dao.findCourseById(newCourse.getCourseId()));
    }
    
    @Test
    public void spentTimeCanBeSet() throws Exception {
        Course newCourse = new Course("TestCourse", 5, testUser.getUserId());
        dao.createCourse(newCourse, testUser);
        
        dao.setTimeSpentForCourse(newCourse.getCourseId(), 2000);
        Course courseFromDb = dao.findCourseById(newCourse.getCourseId());
        assertEquals(courseFromDb.getTimeSpent(), 2000);
    }
    
    
    
}
