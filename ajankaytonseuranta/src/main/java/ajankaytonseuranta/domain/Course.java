/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.domain;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

/**
 *
 * @author ylireett
 */
@Entity("courses")
public class Course {
    @Id
    private ObjectId courseId;
    private String name;
    private int credits;
    private long timeSpent;
    private ObjectId userId;
    
    public Course() {
        this.name = null;
        this.credits = 0;
        this.timeSpent = 0;
        this.userId = null;
    }
    
    public Course(String name, int credits, ObjectId userId) {
        this.name = name;
        this.credits = credits;
        this.timeSpent = 0;
        this.userId = userId;
    }
    
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
    
    public void setTimeSpent(long time) {
        this.timeSpent += time;
    }
    
}
