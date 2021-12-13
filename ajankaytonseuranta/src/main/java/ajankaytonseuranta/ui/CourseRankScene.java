/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.ui;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.TimeManagementService;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

/**
 *
 * @author ylireett
 */
public class CourseRankScene {
    private AjankaytonseurantaUi main;
    private TimeManagementService time;
    private CourseListScene parent;
    
    public CourseRankScene(AjankaytonseurantaUi ui, TimeManagementService tms, CourseListScene parent) {
        this.main = ui;
        this.time = tms;
        this.parent = parent;
    }
    
    public GridPane drawCourseRankScene() {
        GridPane courseRankGrid = new GridPane();
        
        Button returnBtn = main.drawReturnButton(parent.drawCourseListScene());
        
        TextArea courseRank = new TextArea();
        courseRank.setEditable(false);
        courseRank.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        List<Course> topCourses = time.getCourseRankFromDb();
        StringBuilder sb = new StringBuilder();
        int iteration = 1;
        
        for (Course c : topCourses) {
            sb.append(iteration + ": " + c.getCourseName() + "\n");
            sb.append(time.convertTimeSpent(c, -1) + "\n\n");
            iteration++;
        }
        
        courseRank.setText(sb.toString());
        
        courseRankGrid.add(returnBtn, 0, 0);
        courseRankGrid.add(courseRank, 0, 1);
        
        return courseRankGrid;
    }
    
}
