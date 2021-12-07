/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

/**
 *
 * @author ylireett
 */
public class ConcreteCourseDao implements CourseDao {
    private Datastore store;
    private List<Course> courseList;
    
    public ConcreteCourseDao(User user, boolean testmode) throws Exception {
        this.store = createConnection(testmode);
        this.courseList = findCoursesForUser(user);
    }
    
    private Datastore createConnection(boolean testmode) throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/config.properties"));
        
        String dbAddress = "";
        String datastoreName = "";
        String datastoreMapper = "";
        
        if (testmode) {
            dbAddress = properties.getProperty("testDbAddress");
            datastoreName = properties.getProperty("testDatastoreName");
            datastoreMapper = properties.getProperty("testDatastoreMapper");
        } else {
            dbAddress = properties.getProperty("dbAddress");
            datastoreName = properties.getProperty("datastoreName");
            datastoreMapper = properties.getProperty("datastoreMapper");
        }
        
        MongoClient mc = MongoClients.create(dbAddress);
        Datastore store = Morphia.createDatastore(mc, datastoreName);
        store.getMapper().mapPackage(datastoreMapper);
        
        return store;
    }
    
    @Override
    public List<Course> findCoursesForUser(User user) {
        List<Course> courses = new ArrayList<>();
        
        if (user == null) {
            return courses;
        }
        
        Query<Course> query = store.find(Course.class)
                .filter(Filters.eq("userId", user.getUserId()));
        
        for (Course course : query) {
            courses.add(course);
        }
        
        return courses;
    }
    
    @Override
    public Course createCourse(Course course, User user) {
        store.save(course);
        
        courseList = findCoursesForUser(user);
        return course;
    }
    
    @Override
    public Course findCourseById(ObjectId courseId) {
        Course course = store.find(Course.class)
                .filter(Filters.eq("_id", courseId))
                .first();
        
        return course;
    }
    
    @Override
    public void setTimeSpentForCourse(ObjectId courseId, long timeSpent) {
        store.find(Course.class)
                .filter(Filters.eq("_id", courseId))
                .update(UpdateOperators.set("timeSpent", timeSpent))
                .execute();
    }
    
    @Override
    public List<Course> getCourseRankFromDb() {
        // if multiple users have added the same course (name) to db, add spent time together and display that
        List<Course> allCourses = store.find(Course.class)
                .iterator(new FindOptions()
                    .sort(Sort.descending("timeSpent")))
                .toList();
        
        HashMap<String, Course> courseMap = new HashMap<>();
        
        for (Course c : allCourses) {
            if (courseMap.containsKey(c.getCourseName())) {
                // course is already in map, increase time spent
                Course origCourse = courseMap.get(c.getCourseName());
                origCourse.setTimeSpent(c.getTimeSpent());
                courseMap.put(c.getCourseName(), origCourse);
            } else {
                courseMap.put(c.getCourseName(), c);
            }
        }
        
        // Sort descending and get top five
        List<Course> topFiveCourses = courseMap.values()
                .stream()
                .sorted((o1, o2) -> Long.compare(o2.getTimeSpent(), o1.getTimeSpent())).limit(5).collect(Collectors.toList());
        
        return topFiveCourses;
    }
    
}
