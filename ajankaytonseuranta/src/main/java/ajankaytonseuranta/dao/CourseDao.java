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

/**
 *
 * @author ylireett
 */
public interface CourseDao {
    Course createCourse(Course course, User user) throws Exception;
    List<Course> findCoursesForUser(User user);
    Course findCourseById(ObjectId courseId);
    void setTimeSpentForCourse(ObjectId courseId, long timeSpent);
}
