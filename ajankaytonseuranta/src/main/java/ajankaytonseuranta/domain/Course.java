/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("courses")
public class Course {
    @Id
    private ObjectId courseId;
    private String name;
    private int credits;
    private long timeSpent;
    private ObjectId userId;
    
    /**
     * Dummy-konstruktori Morphia-kirjaston mappingia varten. Tätä ei ole tarkoitus käyttää olioiden luomiseen.
     */
    public Course() {
        this.name = null;
        this.credits = 0;
        this.timeSpent = 0;
        this.userId = null;
    }
    
    /**
     * Varsinainen Course-luokan konstruktori.
     * 
     * @param name Kurssin nimi
     * @param credits Opintopisteiden määrä
     * @param userId Kurssin luoneen käyttäjän MongoDB-id
     * 
     * @see ajankaytonseuranta.domain.User
     */
    public Course(String name, int credits, ObjectId userId) {
        this.name = name;
        this.credits = credits;
        this.timeSpent = 0;
        this.userId = userId;
    }
    
    /**
     * Tarkistaa, ovatko kaksi kurssia samat. 
     * Kurssit todetaan samoiksi, jos niiden nimi, MongoDB-id ja kurssin lisännen käyttäjän MongoDB-id ovat samat.
     * 
     * @param object Vertailtava olio
     * 
     * @return Totuusarvo, ovatko kaksi vertailtavaa kurssia samat
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        
        if (!(object instanceof Course)) {
            return false;
        }
        
        Course compareCourse = (Course) object;
        
        if (this.name.equals(compareCourse.name) &&
                this.courseId.equals(compareCourse.courseId) &&
                this.userId.equals(compareCourse.userId)) {
            return true;
        }
        
        return false;
    }
    
    public String getCourseName() {
        return this.name;
    }
    
    public int getCourseCredits() {
        return this.credits;
    }
    
    public long getTimeSpent() {
        return this.timeSpent;
    }
    
    public ObjectId getCourseId() {
        return this.courseId;
    }
    
    /**
     * Kasvattaa kurssiin käytettyä aikaa.
     * 
     * @param time Työskentelyyn käytetty aika millisekunteina
     */
    public void setTimeSpent(long time) {
        this.timeSpent += time;
    }
    
}
