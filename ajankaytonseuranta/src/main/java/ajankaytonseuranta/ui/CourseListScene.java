/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.ui;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.TimeManagementService;
import ajankaytonseuranta.helpers.CourseListHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.types.ObjectId;

public class CourseListScene {
    private AjankaytonseurantaUi main;
    private TimeManagementService time;
    private boolean timerRunning;
    private long timerStartTime;
    
    private ComboBox courseList = new ComboBox();
    private CourseListHelper helper;
    
    /**
     * CourseListScene-luokan konstruktori.
     * 
     * @param ui Ohjelman ui-luokkien pääluokka
     * @param tms Läpi ohjelman käytettävä TimeManagementService-olio
     */
    public CourseListScene(AjankaytonseurantaUi ui, TimeManagementService tms) {
        this.main = ui;
        this.time = tms;
        this.helper = new CourseListHelper(tms);
    }
    
    public ComboBox getCourseList() {
        return courseList;
    }
    
    public long getTimerStartTime() {
        return timerStartTime;
    }
    
    public boolean timerRunning() {
        return timerRunning;
    }
    
    /**
     * Piirtää kirjautuneen käyttäjän kurssinäkymän.
     * 
     * @return Kirjautuneen käyttäjän kurssinäkymä
     */
    public GridPane drawCourseListScene() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        Label loggedInUsername = new Label("Käyttäjä: " + time.getLoggedInUser().getUsername());
        Button logOutBtn = new Button("Kirjaudu ulos");
        logOutBtn.setMaxWidth(Double.MAX_VALUE);
        Button courseRankBtn = new Button("Kurssien top-lista"); // Create new view for most time consuming courses
        courseRankBtn.setMaxWidth(Double.MAX_VALUE);
        Button addCourseBtn = new Button("Lisää uusi kurssi"); // Create new view for course data input
        Button showDataBtn = new Button("Oma kurssidata");
        Button deleteCourseBtn = new Button("Poista...");
        
        TextArea courseInfoFromDb = new TextArea();
        courseInfoFromDb.setEditable(false);
        
        // Display courses for logged in user
        helper.populateCourseList(courseList);
        
        logOutBtn.setOnAction((event) -> {
            if (timerRunning) {
                ObjectId selectedCourseId = ((Course) courseList.getSelectionModel().getSelectedItem()).getCourseId();
                main.updateSpentTimeToDb(selectedCourseId);
            }
            time.logout();
            timerRunning = false;
            main.setLoggedInUser(time.getLoggedInUser());
            main.setScene(main.drawLoginScene());
            main.getMainStage().sizeToScene(); // Resize to correct size
        });
        
        courseRankBtn.setOnAction((event) -> {
            CourseRankScene rankScene = new CourseRankScene(main, time, this);
            main.setScene(rankScene.drawCourseRankScene());
        });
        
        addCourseBtn.setOnAction((event) -> {
            NewCourseScene ncs = new NewCourseScene(main, time, this);
            main.setScene(ncs.drawNewCourseScene());
        });
        
        showDataBtn.setOnAction((event) -> {
            // Draw new scene for pie chart
            CourseDataScene dataScene = new CourseDataScene(main, time, this);
            main.setScene(dataScene.drawCourseDataScene());
        });
        
        deleteCourseBtn.setOnAction((event) -> {
            DeleteCoursesScene delete = new DeleteCoursesScene(main, time, this);
            main.setScene(delete.drawDeleteCoursesScene());
        });
        
        grid.add(showDataBtn, 0, 8);
        
        Button startTimerBtn = new Button("Käynnistä ajanotto");
        startTimerBtn.setVisible(false);
        Button stopTimerBtn = new Button("Lopeta ajanotto");
        stopTimerBtn.setVisible(false);
        
        startTimerBtn.setOnAction((event) -> {
            startTimerBtn.setVisible(false);
            stopTimerBtn.setVisible(true);
            // Start timer
            timerRunning = true;
            timerStartTime = System.currentTimeMillis();
        });
        
        stopTimerBtn.setOnAction((event) -> {
            timerRunning = false;
            startTimerBtn.setVisible(true);
            stopTimerBtn.setVisible(false);
            
            ObjectId selectedCourseId = ((Course) courseList.getSelectionModel().getSelectedItem()).getCourseId();
            time.setTimeSpentForCourse(selectedCourseId, timerStartTime, System.currentTimeMillis());
            courseInfoFromDb.setText(refreshCourseInfo());
            
        });
        
        // Event handler for course combobox selection change
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (time.getLoggedInUser() != null && courseList.getSelectionModel().getSelectedItem() != null) {
                    try {
                        startTimerBtn.setVisible(true);
                        courseInfoFromDb.setText(refreshCourseInfo());
                    } catch (Exception ex) {
                        System.out.println("Jokin meni vikaan.");
                    }
                }
                
                if (courseList.getSelectionModel().getSelectedItem() == null) {
                    startTimerBtn.setVisible(false);
                    courseInfoFromDb.setText(refreshCourseInfo());
                }
            }
        };
        
        courseList.setOnAction(event);
        
        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.getChildren().addAll(addCourseBtn, startTimerBtn, stopTimerBtn, deleteCourseBtn);
        
        VBox topCornerBox = new VBox();
        topCornerBox.setSpacing(5);
        topCornerBox.getChildren().addAll(loggedInUsername, logOutBtn, courseRankBtn);
        
        grid.add(topCornerBox, 3, 0);
        grid.setRowSpan(topCornerBox, grid.REMAINING);
        grid.add(courseList, 1, 1);
        
        grid.add(btnBox, 1, 2);
        grid.add(courseInfoFromDb, 1, 4);
        grid.setColumnSpan(courseInfoFromDb, grid.REMAINING);
       
        return grid;
    }
    
    /**
     * Päivittää kurssinäkymän alasvetovalikon.
     */
    public void redrawCourseList() {
        helper.redrawCourseList(courseList);
    }
    
    /**
     * Muodostaa tekstimuotoisen esityksen valitun kurssin tiedoista.
     * 
     * @return Valitun kurssin tiedot tekstimuodossa
     */
    public String refreshCourseInfo() {
        if (courseList.getSelectionModel().getSelectedItem() != null) {
            Course selectedCourse = time.getCourseInfo(((Course) courseList.getSelectionModel().getSelectedItem()).getCourseId());
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Kurssin nimi: %1$s \n", selectedCourse.getCourseName()));
            sb.append(String.format("Opintopisteet: %1$s \n", selectedCourse.getCourseCredits()));
            sb.append(String.format("Aikaa käytetty: %1$s \n", time.convertTimeSpent(selectedCourse, -1)));

            return sb.toString();
        } else {
            return "";
        }
    }
    
}
