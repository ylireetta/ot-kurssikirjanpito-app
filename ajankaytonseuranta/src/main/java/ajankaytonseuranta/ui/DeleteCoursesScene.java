/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.ui;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.TimeManagementService;
import ajankaytonseuranta.helpers.CourseListHelper;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.bson.types.ObjectId;

/**
 *
 * @author ylireett
 */
public class DeleteCoursesScene {
    private AjankaytonseurantaUi main;
    private TimeManagementService time;
    private CourseListScene parent;
    private ComboBox courseList = new ComboBox();
    private CourseListHelper helper;
    
    public DeleteCoursesScene(AjankaytonseurantaUi ui, TimeManagementService tms, CourseListScene parent) {
        this.main = ui;
        this.time = tms;
        this.parent = parent;
        this.helper = new CourseListHelper(tms);
        
    }
    
    public GridPane drawDeleteCoursesScene() {
        GridPane deleteScene = new GridPane();
        Label info = new Label("Kurssien poistaminen");
        info.setFont(new Font(32));
        // Populate list of courses for logged in user
        helper.redrawCourseList(courseList);
        helper.populateCourseList(courseList);
        
        Button returnBtn = main.drawReturnButton(parent.drawCourseListScene());
        Button deleteOneBtn = new Button("Poista kurssi");
        deleteOneBtn.setStyle("-fx-text-fill: #ff0000; ");
        deleteOneBtn.setDisable(true);
        Button deleteAllBtn = new Button("Poista kaikki kurssit");
        deleteAllBtn.setStyle("-fx-text-fill: #ff0000; ");
        
        deleteAllBtn.setOnAction((event) -> {
            raiseAlert(true);
            if (courseList.getItems().size() < 1) {
                deleteAllBtn.setDisable(true);
            }
        });
        
        deleteOneBtn.setOnAction((event) -> {
            raiseAlert(false);
            if (courseList.getItems().size() < 1) {
                deleteAllBtn.setDisable(true);
            }
        });
        
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (courseList.getSelectionModel().getSelectedItem() != null) {
                    deleteOneBtn.setDisable(false);
                }
                
                if (courseList.getSelectionModel().getSelectedItem() == null) {
                    deleteOneBtn.setDisable(true);
                }
            }
        };
        
        courseList.setOnAction(event);
        
        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.getChildren().addAll(deleteOneBtn, deleteAllBtn, returnBtn);
        
        deleteScene.add(info, 0, 0);
        deleteScene.add(courseList, 0, 2);
        deleteScene.add(btnBox, 0, 3);
        
        return deleteScene;
    }
    
    public void raiseAlert(boolean deleteAll) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Course selectedCourse = null;
        
        if (deleteAll) {
            alert.setTitle("Poista kaikki kurssit");
            alert.setHeaderText("Poista kaikki kurssit");
            alert.setContentText("Haluatko varmasti poistaa kaikki lisäämäsi kurssit tietokannasta?");
        } else {
            // user wants to delete only one course
            selectedCourse = (Course) courseList.getSelectionModel().getSelectedItem();
            alert.setTitle("Poista kurssi");
            alert.setHeaderText("Poista kurssi");
            alert.setContentText("Haluatko varmasti poistaa kurssin " + selectedCourse.getCourseName() + "?");
        }
        
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Poista");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Peruuta");
        Optional<ButtonType> pressedBtn = alert.showAndWait();
            
        if (!pressedBtn.isPresent() || pressedBtn.get() == ButtonType.CANCEL) {
            System.out.println("CANCEL");
        } else if (pressedBtn.get() == ButtonType.OK) {
            if (deleteAll) {
                time.deleteAllCourses(time.getLoggedInUser());
            } else {
                // delete selected course
                time.deleteCourse(selectedCourse.getCourseId());
            }
            courseList.setValue(null);
            helper.redrawCourseList(parent.getCourseList());
            redrawCourseList();
        }
    }
    
    public void redrawCourseList() {
        helper.redrawCourseList(courseList);
    }
    
    
}
