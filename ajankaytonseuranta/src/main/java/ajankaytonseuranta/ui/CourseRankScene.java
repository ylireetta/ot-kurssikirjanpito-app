/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.ui;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.TimeManagementService;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class CourseRankScene {
    private AjankaytonseurantaUi main;
    private TimeManagementService time;
    private CourseListScene parent;
    
    /**
     * CourseRankScene-luokan konstruktori.
     * 
     * @param ui Ohjelman ui-luokkien pääluokka
     * @param tms Läpi ohjelman käytettävä TimeManagementService-olio
     * @param parent Edellinen näkymä
     */
    public CourseRankScene(AjankaytonseurantaUi ui, TimeManagementService tms, CourseListScene parent) {
        this.main = ui;
        this.time = tms;
        this.parent = parent;
    }
    
    /**
     * Piirtää aikaavievimpien kurssien top-listanäkymän.
     * 
     * @return Aikaavievimpien kurssien top-listanäkymä
     */
    public GridPane drawCourseRankScene() {
        GridPane courseRankGrid = new GridPane();
        courseRankGrid.setHgap(10);
        courseRankGrid.setVgap(10);
        courseRankGrid.setPadding(new Insets(10, 10, 10, 10));
        
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
        
        Image img = new Image("/images/trophy.png", 100, 100, false, false);
        ImageView view = new ImageView(img);
        
        courseRankGrid.add(view, 1, 1);
        
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        c2.setHalignment(HPos.CENTER);
        courseRankGrid.getColumnConstraints().addAll(c1, c2);
        
        return courseRankGrid;
    }
    
}
