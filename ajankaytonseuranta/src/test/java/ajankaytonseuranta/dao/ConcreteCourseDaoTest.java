/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.User;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
    
    @Test
    public void courseRankIsCorrect() throws Exception {
        long time = 8000;
        
        for (int i = 1; i <= 5; i++) {
            String courseName = "C" + i;
            Course course = new Course(courseName, 5, testUser.getUserId());
            dao.createCourse(course, testUser);
            
            dao.setTimeSpentForCourse(course.getCourseId(), time);
            time = time + 1000;
        }
        
        // Another user creates a course with the same name, so add spent time together when getting course rank from db
        User anotherUser = fakeUserDao.createUser(new User("Another User"));
        Course sameName = new Course("C5", 5, anotherUser.getUserId());
        dao.createCourse(sameName, anotherUser);
        dao.setTimeSpentForCourse(sameName.getCourseId(), 4000);
        
        
        List<Course> toplist = dao.getCourseRankFromDb();
        assertTrue(toplist.size() == 5);
        assertTrue(toplist.get(0).getCourseName().equals("C5"));
        assertTrue(toplist.get(0).getTimeSpent() == 16000);
        assertTrue(toplist.get(1).getCourseName().equals("C4"));
        assertTrue(toplist.get(2).getCourseName().equals("C3"));
        assertTrue(toplist.get(3).getCourseName().equals("C2"));
        assertTrue(toplist.get(4).getCourseName().equals("C1"));
    }
    
    @Test
    public void courseCanBeDeleted() throws Exception {
        Course deleteThis = new Course("Delete this", 5, testUser.getUserId());
        dao.createCourse(deleteThis, testUser);
        
        assertNotNull(dao.findCourseById(deleteThis.getCourseId()));
        
        dao.deleteCourseFromDb(deleteThis.getCourseId());
        
        assertNull(dao.findCourseById(deleteThis.getCourseId()));
    }
    
    @Test
    public void multipleCoursesCanBeDeleted() throws Exception {
        Course delete1 = new Course("Delete this 1", 5, testUser.getUserId());
        Course delete2 = new Course("Delete this 2", 5, testUser.getUserId());
        
        dao.createCourse(delete1, testUser);
        dao.createCourse(delete2, testUser);
        
        assertFalse(dao.findCoursesForUser(testUser).isEmpty());
        
        dao.deleteAllCoursesForUser(testUser);
        assertTrue(dao.findCoursesForUser(testUser).isEmpty());
        
    }
    
}
