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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

/**
 *
 * @author ylireett
 */
public class ConcreteCourseDao implements CourseDao {
    private Datastore store;
    private List<Course> courseList;
    
    /**
     * ConcreteCourseDao-luokan konstruktori.
     * 
     * @param user 
     * @param testmode  Totuusarvo, jonka avulla voidaan eriyttää testi- ja tuotantotietokannat toisistaan
     * @throws Exception 
     */
    public ConcreteCourseDao(User user, boolean testmode) throws Exception {
        this.store = createConnection(testmode);
        this.courseList = findCoursesForUser(user);
    }
    
    /**
     * Muodostaa yhteyden config.properties-tiedostossa määriteltyyn tietokantaan.
     * 
     * @param testmode Totuusarvo, jonka avulla erotetaan testauksessa ja tuotannossa käytettävät dokumenttikokoelmat toisistaan
     * 
     * @return Ohjelman ajon aikana käytettävä tietokantayhteys
     * @throws Exception 
     */
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
    
    /**
     * Hakee annetun käyttäjän lisäämät Course-luokan oliot tietokannasta.
     * 
     * @param user Käyttäjä, jonka kurssit halutaan noutaa
     * 
     * @return Käyttäjän kantaan lisäämät kurssit
     */
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
    
    /**
     * Tallettaa annettuun käyttäjään viittaavan uuden Course-luokan olion tietokantaan.
     * 
     * @param course Luotava kurssi
     * @param user Kurssin luonut käyttäjä
     * 
     * @return Luotu kurssi
     */
    @Override
    public Course createCourse(Course course, User user) {
        store.save(course);
        
        courseList = findCoursesForUser(user);
        return course;
    }
    
    /**
     * Hakee annettua MongoDB-id:tä vastaavan Course-luokan olion tietokannasta.
     * 
     * @param courseId Haettavan kurssin MongoDB-id
     * 
     * @return Annettua MongoDB-id:ä vastaava Course-olio
     */
    @Override
    public Course findCourseById(ObjectId courseId) {
        Course course = store.find(Course.class)
                .filter(Filters.eq("_id", courseId))
                .first();
        
        return course;
    }
    
    /**
     * Päivittää kurssiin käytetyn ajan tietokantaan.
     * 
     * @param courseId Päivitettävän Course-olion MongoDB-id
     * @param timeSpent Kurssiin yhteensä käytetty aika
     */
    @Override
    public void setTimeSpentForCourse(ObjectId courseId, long timeSpent) {
        store.find(Course.class)
                .filter(Filters.eq("_id", courseId))
                .update(UpdateOperators.set("timeSpent", timeSpent))
                .execute();
    }
    
    /**
     * Hakee top 5 eniten aikaa vienyttä kurssia tietokannasta.
     * Haku tehdään koko tietokannasta, ts. lisääjä ei vaikuta ranking-listaan.
     * Jos useampi käyttäjä on lisännyt samannimisen kurssin kantaan, lasketaan näiden yksittäisten kurssien viemät ajat yhteen ja käsitellään ne yhtenä kurssina.
     * 
     * @return Viisi eniten aikaa vienyttä Course-luokan oliota tietokannasta
     */
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
    
    /**
     * Poistaa annettua id:tä vastaavan kurssin tietokannasta.
     * 
     * @param courseId Poistettavan kurssin id
     */
    @Override
    public void deleteCourseFromDb(ObjectId courseId) {
        store.find(Course.class)
                .filter(Filters.eq("_id", courseId))
                .delete();
    }
    
}
