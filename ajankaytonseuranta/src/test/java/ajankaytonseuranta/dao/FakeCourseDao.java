/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.User;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.bson.types.ObjectId;

/**
 *
 * @author ylireett
 */
public class FakeCourseDao implements CourseDao {
    private Datastore store;
    private List<Course> courseList;
    
    public FakeCourseDao(User user) throws Exception {
        this.store = createConnection();
        this.courseList = findCoursesForUser(user);
    }
    
    private Datastore createConnection() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/config.properties"));
        
        String testDbAddress = properties.getProperty("testDbAddress");
        String testDatastoreName = properties.getProperty("testDatastoreName");
        String testDatastoreMapper = properties.getProperty("testDatastoreMapper");
        
        MongoClient mc = MongoClients.create(testDbAddress);
        Datastore store = Morphia.createDatastore(mc, testDatastoreName);
        store.getMapper().mapPackage(testDatastoreMapper);
        
        
        return store;
    }
    
    @Override
    public Course createCourse(Course course, User user) {
        store.save(course);
        
        courseList = findCoursesForUser(user);
        return course;
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
        List<Course> topFiveCourses = store.find(Course.class)
                .iterator(new FindOptions()
                        .sort(Sort.descending("timeSpent"))
                        .limit(5))
                .toList();
        
        return topFiveCourses;
    }
    
    @Override
    public void deleteCourseFromDb(ObjectId courseId) {
        store.find(Course.class)
                .filter(Filters.eq("_id", courseId))
                .delete();
    }
    
    public void deleteTestData() {
        store.getCollection(Course.class).deleteMany(new BasicDBObject());
    }
    
}
