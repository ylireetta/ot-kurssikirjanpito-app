/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.dao;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.User;
import java.util.List;
import org.bson.types.ObjectId;

public interface CourseDao {
    /**
     * Tallettaa annettuun käyttäjään viittaavan uuden Course-luokan olion tietokantaan.
     * 
     * @param course Luotava kurssi
     * @param user Kurssin luonut käyttäjä
     * 
     * @return Luotu kurssi
     * @throws Exception
     */
    Course createCourse(Course course, User user) throws Exception;
    
    /**
     * Hakee annetun käyttäjän lisäämät Course-luokan oliot tietokannasta.
     * 
     * @param user Käyttäjä, jonka kurssit halutaan noutaa
     * 
     * @return Käyttäjän kantaan lisäämät kurssit
     */
    List<Course> findCoursesForUser(User user);
    
    /**
     * Hakee annettua MongoDB-id:tä vastaavan Course-luokan olion tietokannasta.
     * 
     * @param courseId Haettavan kurssin MongoDB-id
     * 
     * @return Annettua MongoDB-id:ä vastaava Course-olio
     */
    Course findCourseById(ObjectId courseId);
    
    /**
     * Päivittää kurssiin käytetyn ajan tietokantaan.
     * 
     * @param courseId Päivitettävän Course-olion MongoDB-id
     * @param timeSpent Kurssiin yhteensä käytetty aika
     */
    void setTimeSpentForCourse(ObjectId courseId, long timeSpent);
    
    /**
     * Hakee top 5 eniten aikaa vienyttä kurssia tietokannasta.
     * Haku tehdään koko tietokannasta, ts. lisääjä ei vaikuta ranking-listaan.
     * Jos useampi käyttäjä on lisännyt samannimisen kurssin kantaan, lasketaan näiden yksittäisten kurssien viemät ajat yhteen ja käsitellään ne yhtenä kurssina.
     * 
     * @return Viisi eniten aikaa vienyttä Course-luokan oliota tietokannasta
     */
    List<Course> getCourseRankFromDb();
    
    /**
     * Poistaa annettua id:tä vastaavan kurssin tietokannasta.
     * 
     * @param courseId Poistettavan kurssin id
     */
    void deleteCourseFromDb(ObjectId courseId);
    
    /**
     * Poistaa annetun käyttäjän kaikki kurssit tietokannasta.
     * 
     * @param user Käyttäjä, jonka kurssit poistetaan
     */
    void deleteAllCoursesForUser(User user);
}
