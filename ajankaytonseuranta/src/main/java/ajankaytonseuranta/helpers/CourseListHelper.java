/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.helpers;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.TimeManagementService;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CourseListHelper {
    private TimeManagementService time;
    
    /**
     * CourseListHelper-luokan konstruktori.
     * 
     * @param tms Läpi ohjelman käytettävä TimeManagementService-luokan olio
     */
    public CourseListHelper(TimeManagementService tms) {
        this.time = tms;
    }
    
    /**
     * Muodostaa kurssien alasvetovalikon uudelleen.
     * 
     * @param courseList Alasvetovalikko, jonka sisältö halutaan päivittää
     */
    public void redrawCourseList(ComboBox courseList) {
        courseList.getItems().clear();
        
        if (time.getLoggedInUser() != null) {
            courseList.setItems(FXCollections.observableArrayList(time.getCoursesForLoggedInUser()));
        }
    }
    
    /**
     * Muodostaa alasvetovalikon sisällön uutta näkymää luotaessa.
     * 
     * @param courseList Alasvetovalikko, jonka sisältö halutaan muodostaa
     */
    public void populateCourseList(ComboBox courseList) {
        Callback<ListView<Course>, ListCell<Course>> factory = lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getCourseName());
            }
        };
        
        courseList.setButtonCell(factory.call(null)); // Prevents combobox from displaying object address when selection changes
        courseList.setCellFactory(factory);
        courseList.setValue(null); // Set value to null when entering scene
    }
    
}
