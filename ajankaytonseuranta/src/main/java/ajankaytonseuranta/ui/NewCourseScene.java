/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.ui;

import ajankaytonseuranta.domain.Course;
import ajankaytonseuranta.domain.TimeManagementService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 *
 * @author ylireett
 */
public class NewCourseScene {
    private AjankaytonseurantaUi main;
    private TimeManagementService time;
    private CourseListScene p;
    
    /**
     * NewCourseScene-luokan konstruktori.
     * 
     * @param ui Ohjelman ui-luokkien pääluokka
     * @param tms Läpi ohjelman käytettävä TimeManagementService-olio
     * @param parent Edellinen näkymä
     */
    public NewCourseScene(AjankaytonseurantaUi ui, TimeManagementService tms, CourseListScene parent) {
        this.main = ui;
        this.time = tms;
        this.p = parent;
    }
    
    /**
     * Piirtää kurssin luontinäkymän.
     * 
     * @return Uuden kurssin luontinäkymä
     */
    public GridPane drawNewCourseScene() {
        GridPane newCourseGrid = new GridPane();
        
        Button addCourseBtn = new Button("Lisää kurssi");
        Button returnBtn = main.drawReturnButton(main.drawCourseListScene());
        
        Label courseNameLabel = new Label("Kurssin nimi:");
        TextField courseName = new TextField();
        Label courseCreditLabel = new Label("Opintopisteet:");
        TextField courseCredit = new TextField();
        
        Label courseInfo = new Label("Syötä lisättävän kurssin tiedot.");
        
        Label instructions = new Label("");
        StringBuilder sb = new StringBuilder();
        sb.append("Ohjeet:\n\n");
        sb.append("- Anna kurssin nimi\n");
        sb.append("- Anna kurssin opintopisteet positiivisena kokonaislukuna\n\n");
        sb.append("Esim. Ohjelmistotekniikka 5");
        instructions.setText(sb.toString());
        
        addCourseBtn.setOnAction((event) -> {
            if (!courseName.getText().equals("") && isInteger(courseCredit.getText())) {
                Course newCourse = new Course(courseName.getText(), Integer.parseInt(courseCredit.getText()), time.getLoggedInUser().getUserId());
                time.createCourse(newCourse, time.getLoggedInUser());
                courseInfo.setText("Uusi kurssi lisätty.");
                courseInfo.setTextFill(Color.GREEN);
                p.redrawCourseList();
                courseName.setText("");
                courseCredit.setText("");
            } else {
                courseInfo.setText("Tarkista syöte.");
                courseInfo.setTextFill(Color.RED);
            }
        });
        
        HBox btnBox = new HBox();
        btnBox.getChildren().addAll(addCourseBtn, returnBtn);
        btnBox.setSpacing(10);
        btnBox.setAlignment(Pos.CENTER);
        
        newCourseGrid.setPadding(new Insets(10, 10, 10, 10));
        newCourseGrid.add(courseInfo, 1, 0);
        newCourseGrid.add(courseNameLabel, 0, 1);
        newCourseGrid.add(courseName, 1, 1);
        
        newCourseGrid.add(courseCreditLabel, 0, 2);
        newCourseGrid.add(courseCredit, 1, 2);
        newCourseGrid.add(btnBox, 0, 3);
        newCourseGrid.setColumnSpan(btnBox, newCourseGrid.REMAINING);

        newCourseGrid.add(instructions, 0, 4);
        newCourseGrid.setColumnSpan(instructions, newCourseGrid.REMAINING);
        
        return newCourseGrid;
    }
    
    /**
     * Tarkistaa, voiko annetun merkkijonon muuuntaa positiiviseksi kokonaisluvuksi.
     * 
     * @param text Tarkistettava merkkijono
     * 
     * @return Totuusarvo, voiko merkkijonon muuntaa positiiviseksi kokonaisluvuksi
     */
    private boolean isInteger(String text) {
        if (text == null) {
            return false;
        }
        
        try {
            int converted = Integer.parseInt(text);
            if (converted < 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        
    }
    
}
