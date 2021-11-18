package ajankaytonseuranta.domain;

import ajankaytonseuranta.dao.CourseDao;
import ajankaytonseuranta.dao.UserDao;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author ylireett
 */

// Sovelluslogiikka löytyy tästä luokasta

public class TimeManagementService {
    private UserDao userDao;
    private CourseDao courseDao;
    
    private User loggedInUser;
    
    public TimeManagementService(UserDao userDao, CourseDao courseDao) {
        this.userDao = userDao;
        this.courseDao = courseDao;
    }
    
    public boolean createUser(String newUsername) {
        if (userDao.findByUsername(newUsername) != null) {
            // Tällä käyttäjänimellä löytyy jo käyttäjä, ei sallita luontia
            return false;
        }
        
        User newUser = new User(newUsername);
        try {
            userDao.createUser(newUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean userExists(String username) {
        if (userDao.findByUsername(username) != null)
            return true;
        
        return false;
    }
    
    public boolean login(String username) {
        if (userExists(username)) {
            loggedInUser = userDao.findByUsername(username);
            return true;
        } else {
            return false;
        }
    }
    
    public void logout() {
        loggedInUser = null;
    }
    
    public User getLoggedInUser() {
        return this.loggedInUser;
    }
    
    public List<Course> getCoursesForLoggedInUser() {
        return courseDao.findCoursesForUser(loggedInUser);
    }
    
    public boolean createCourse(Course course, User user) {
        try {
            courseDao.createCourse(course, user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Course getCourseInfo(ObjectId courseId) {
        return courseDao.findCourseById(courseId);
    }
    
    public void setTimeSpentForCourse(ObjectId courseId, long startTimeInMillis, long endTimeInMillis) {
        long timeSpentInMillis = endTimeInMillis - startTimeInMillis;
        
        Course course = getCourseInfo(courseId);
        long timeSpentTotal = course.getTimeSpent() + timeSpentInMillis;
        
        courseDao.setTimeSpentForCourse(courseId, timeSpentTotal);
    }
    
    private double convertTimeSpent(long millis) {
        // For display purposes?
        
        return 0;
    }
    
}
