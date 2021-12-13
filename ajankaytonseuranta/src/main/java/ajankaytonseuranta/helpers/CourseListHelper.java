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

/**
 *
 * @author ylireett
 */
public class CourseListHelper {
    private TimeManagementService time;
    
    public CourseListHelper(TimeManagementService tms) {
        this.time = tms;
    }
    
    public void redrawCourseList(ComboBox courseList) {
        courseList.getItems().clear();
        
        if (time.getLoggedInUser() != null) {
            courseList.setItems(FXCollections.observableArrayList(time.getCoursesForLoggedInUser()));
        }
    }
    
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
