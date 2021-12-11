package ajankaytonseuranta.domain;

import ajankaytonseuranta.dao.CourseDao;
import ajankaytonseuranta.dao.UserDao;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.types.ObjectId;

/**
 *
 * @author ylireett
 */

public class TimeManagementService {
    private UserDao userDao;
    private CourseDao courseDao;
    
    private User loggedInUser;
    
    /**
     * TimeManagementService-luokan konstruktori.
     * 
     * @param userDao Ohjelman käyttämä UserDao-rajapinnan toteuttava olio, joka huolehtii käyttäjätietokannasta
     * @param courseDao  Ohjelman käyttämä CourseDao-rajapinnan toteuttava olio, joka huolehtii kurssitietokannasta 
     */
    public TimeManagementService(UserDao userDao, CourseDao courseDao) {
        this.userDao = userDao;
        this.courseDao = courseDao;
    }
    
    /**
     * Luo annettua käyttäjätunnusta vastaavan User-luokan olion ja tallettaa sen kantaan, jos tunnus ei ole varattu.
     * 
     * @param newUsername Uuden käyttäjän käyttäjätunnus
     * 
     * @return Totuusarvo, onnistuiko uuden käyttäjän luominen
     */
    public boolean createUser(String newUsername) {
        if (userDao.findByUsername(newUsername) != null) {
            // Username is already taken, do not allow creating new user
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
    
    /**
     * Tarkistaa, onko annettu käyttäjätunnus jo varattu.
     * 
     * @param username Tarkistettava käyttäjätunnus
     * 
     * @return Totuusarvo, löytyykö annetulla tunnuksella jo ennestään käyttäjä tietokannasta
     */
    public boolean userExists(String username) {
        if (userDao.findByUsername(username) != null) {
            return true;
        }
            
        return false;
    }
    
    /**
     * Kirjaa annetun käyttäjätunnuksen sisään ohjelmaan, jos tunnus on olemassa.
     * 
     * @param username Sisään kirjattava käyttäjätunnus
     * 
     * @return Totuusarvo, onko käyttäjä olemassa ja onnistuiko sisäänkirjautuminen
     */
    public boolean login(String username) {
        if (userExists(username)) {
            loggedInUser = userDao.findByUsername(username);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Kirjaa sisään kirjautuneen käyttäjän ulos asettamalla TimeManagementService-luokan yksityisen oliomuuttujan nulliksi.
     */
    public void logout() {
        loggedInUser = null;
    }
    
    /**
     * 
     * @return Sisäänkirjautunutta käyttäjää vastaava User-olio
     */
    public User getLoggedInUser() {
        return this.loggedInUser;
    }
    
    /**
     * Hakee sisäänkirjautuneen käyttäjän lisäämät kurssit tietokannasta.
     * 
     * @return Lista Course-luokan olioita, jotka käyttäjä on tallettanut tietokantaan
     * 
     * @see ajankaytonseuranta.dao.ConcreteCourseDao#findCoursesForUser(ajankaytonseuranta.domain.User) 
     */
    public List<Course> getCoursesForLoggedInUser() {
        return courseDao.findCoursesForUser(loggedInUser);
    }
    
    /**
     * Luo uuden kurssin tietokantaan.
     * 
     * @param course Lisättävä Course-luokan olio
     * @param user Kurssin luonut käyttäjä
     * 
     * @return Totuusarvo, onnistuiko kurssin tallettaminen
     * 
     * @see ajankaytonseuranta.dao.ConcreteCourseDao#createCourse(ajankaytonseuranta.domain.Course, ajankaytonseuranta.domain.User) 
     */
    public boolean createCourse(Course course, User user) {
        try {
            courseDao.createCourse(course, user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Hakee annettua MongoDB-id:tä vastaavan kurssin tiedot tietokannasta.
     * 
     * @param courseId Haettavan kurssin MongoDB-id
     * 
     * @return Annettua id:tä vastaava Course-luokan olio
     * 
     * @see ajankaytonseuranta.dao.ConcreteCourseDao#findCourseById(org.bson.types.ObjectId) 
     */
    public Course getCourseInfo(ObjectId courseId) {
        return courseDao.findCourseById(courseId);
    }
    
    /**
     * Kasvattaa kurssiin käytettyä aikaa ja tallettaa sen tietokantaan.
     * 
     * @param courseId Päivitettävän kurssin MongoDB-id
     * @param startTimeInMillis Työskentelyn aloittamisaika millisekunteina
     * @param endTimeInMillis Työskentelyn lopettamisaika millisekunteina
     * 
     * @see ajankaytonseuranta.dao.ConcreteCourseDao#setTimeSpentForCourse(org.bson.types.ObjectId, long) 
     */
    public void setTimeSpentForCourse(ObjectId courseId, long startTimeInMillis, long endTimeInMillis) {
        long timeSpentInMillis = endTimeInMillis - startTimeInMillis;
        
        Course course = getCourseInfo(courseId);
        long timeSpentTotal = course.getTimeSpent() + timeSpentInMillis;
        
        courseDao.setTimeSpentForCourse(courseId, timeSpentTotal);
    }
    
    /**
     * Muuntaa annettuun kurssin käytetyn ajan millisekunneista minuuteiksi ja sekunneiksi.
     * Metodi on tarkoitettu käyttöliittymän kurssinäkymässä käytettäväksi.
     * 
     * @param course Käsiteltävä Course-luokan olio
     * 
     * @return Kurssiin käytetty aika minuuteiksi ja sekunneiksi muunnettuna ja merkkijonoksi muotoiltuna
     */
    public String convertTimeSpent(Course course) {
        // For display purposes
        long timeSpentInMillis = course.getTimeSpent();
        
        String ret = String.format("%02d minuuttia, %02d sekuntia", 
                TimeUnit.MILLISECONDS.toMinutes(timeSpentInMillis), 
                TimeUnit.MILLISECONDS.toSeconds(timeSpentInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeSpentInMillis)));
        
        return ret;
    }
    
    /**
     * Hakee viisi eniten aikaa vienyttä kurssia tietokannasta.
     * 
     * @return Lista, joka sisältää viisi eniten aikaa vienyttä kurssia kaikkien käyttäjien keskuudesta
     * 
     * @see ajankaytonseuranta.dao.ConcreteCourseDao#getCourseRankFromDb() 
     */
    public List<Course> getCourseRankFromDb() {
        return courseDao.getCourseRankFromDb();
    }
    
}
