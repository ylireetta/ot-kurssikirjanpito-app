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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class DeleteCoursesScene {
    private AjankaytonseurantaUi main;
    private TimeManagementService time;
    private CourseListScene parent;
    private ComboBox courseList = new ComboBox();
    private CourseListHelper helper;
    
    /**
     * DeleteCoursesScene-luokan konstruktori.
     * 
     * @param ui Ohjelman ui-luokkien pääluokka
     * @param tms Läpi ohjelman käytettävä TimeManagementService-olio
     * @param parent Edellinen näkymä
     */
    public DeleteCoursesScene(AjankaytonseurantaUi ui, TimeManagementService tms, CourseListScene parent) {
        this.main = ui;
        this.time = tms;
        this.parent = parent;
        this.helper = new CourseListHelper(tms);
        
    }
    
    /**
     * Piirtää kurssien poistonäkymän.
     * 
     * @return Kurssien poistonäkymä
     */
    public GridPane drawDeleteCoursesScene() {
        GridPane deleteScene = new GridPane();
        deleteScene.setHgap(10);
        deleteScene.setVgap(10);
        deleteScene.setPadding(new Insets(10, 10, 10, 10));
        Label info = new Label("Kurssien poistaminen");
        info.getStyleClass().add("info-label");
        
        // Populate list of courses for logged in user
        helper.redrawCourseList(courseList);
        helper.populateCourseList(courseList);
        
        Button returnBtn = main.drawReturnButton(parent.drawCourseListScene());
        Button deleteOneBtn = new Button("Poista kurssi");
        deleteOneBtn.getStyleClass().add("delete-one-button");
        deleteOneBtn.setDisable(true);
        Button deleteAllBtn = new Button("Poista kaikki kurssit");
        deleteAllBtn.getStyleClass().add("delete-all-button");
        
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
        deleteScene.add(courseList, 0, 1);
        deleteScene.add(btnBox, 0, 2);
        
        Image img = new Image("/trash.png", 100, 100, false, false);
        ImageView view = new ImageView(img);
        
        deleteScene.add(view, 1, 1);
        deleteScene.setRowSpan(view, deleteScene.REMAINING);
        
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        c2.setHalignment(HPos.CENTER);
        deleteScene.getColumnConstraints().addAll(c1, c2);
        
        return deleteScene;
    }
    
    /**
     * Näyttää käyttäjälle varmistusikkunan kurssien poistosta.
     * 
     * @param deleteAll Totuusarvo, halutaanko poistaa kaikki kurssit
     */
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
    
    /**
     * Päivittää kurssien alasvetovalikon.
     */
    public void redrawCourseList() {
        helper.redrawCourseList(courseList);
    }
    
    
}
