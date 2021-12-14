/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.ui;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.TimeManagementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class CourseDataScene {
    private AjankaytonseurantaUi main;
    private TimeManagementService time;
    private CourseListScene parent;
    
    /**
     * CourseDataScene-luokan konstruktori.
     * 
     * @param ui Ohjelman ui-luokkien pääluokka
     * @param tms Läpi ohjelman käytettävä TimeManagementService-olio
     * @param parent Edellinen näkymä
     */
    public CourseDataScene(AjankaytonseurantaUi ui, TimeManagementService tms, CourseListScene parent) {
        this.main = ui;
        this.time = tms;
        this.parent = parent;
    }
    
    /**
     * Piirtää kirjautuneen käyttäjän kurssien yhteenvetonäkymän.
     * 
     * @return Kurssien yhteenvetonäkymä
     */
    public GridPane drawCourseDataScene() {
        // Draw a pie chart of logged in user's courses
        GridPane dataScene = new GridPane();
        
        Button returnBtn = main.drawReturnButton(parent.drawCourseListScene());
        parent.redrawCourseList(); // Redraw in case user has started the timer at some point and now returns to this scene
        ObservableList<Course> courseItems = parent.getCourseList().getItems();
        
        PieChart.Data data[] = new PieChart.Data[courseItems.size()];
        
        for (int i = 0; i < courseItems.size(); i++) {
            Course courseToDraw = courseItems.get(i);
            data[i] = new PieChart.Data(courseToDraw.getCourseName(), courseToDraw.getTimeSpent());
        }
        
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(data);
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Ajankäytön jakauma");
                
        Label percentLabel = new Label("");
        pieChartData.forEach(oneData ->
                oneData.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> {
                    double total = 0;
                        for (PieChart.Data da : chart.getData()) {
                            total += da.getPieValue();
                        }
                    String text = String.format("%1$s %%", Math.round(100 * oneData.getPieValue() / total));
                    percentLabel.setText(oneData.getName() + " " + text + "\n    Aikaa käytetty " + time.convertTimeSpent(null, oneData.getPieValue()));
                })
        );
        
        pieChartData.forEach(oneData ->
                oneData.getNode().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> {
                    percentLabel.setText("");
                })
        );
        
        dataScene.add(chart, 0, 0);
        dataScene.add(returnBtn, 0, 1);
        dataScene.add(percentLabel, 3, 0);
        return dataScene;
    }
    
}
