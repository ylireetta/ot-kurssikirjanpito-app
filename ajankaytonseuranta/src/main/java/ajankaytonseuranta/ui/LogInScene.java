/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajankaytonseuranta.ui;

import ajankaytonseuranta.domain.TimeManagementService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LogInScene {
    AjankaytonseurantaUi main;
    TimeManagementService time;
    
    /**
     * LogInScene-luokan konstruktori.
     * 
     * @param ui Ohjelman ui-luokkien pääluokka
     * @param tms Läpi ohjelman käytettävä TimeManagementService-olio
     */
    public LogInScene(AjankaytonseurantaUi ui, TimeManagementService tms) {
        this.main = ui;
        this.time = tms;
    }
    
    /**
     * Piirtää sisäänkirjautumisnäkymän.
     * 
     * @return Sisäänkirjautumisnäkymä
     */
    public BorderPane drawMainScene() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10, 10, 10, 10));
        
        VBox content = new VBox();
        content.setSpacing(15);
        
        // Buttons
        TilePane buttonPane = new TilePane();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setHgap(10);
        
        Label info = new Label("Tervetuloa!");
        info.getStyleClass().add("info-label");
        
        TextField username = new TextField();
        username.setPromptText("Käyttäjätunnus");
        username.setFocusTraversable(false);
        username.setMaxWidth(200);
        
        Button loginBtn = new Button("Kirjaudu sisään");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        Button createNewUserBtn = new Button("Luo uusi käyttäjä");
        createNewUserBtn.setMaxWidth(Double.MAX_VALUE);
        Button finishBtn = new Button("Sulje");
        finishBtn.setMaxWidth(Double.MAX_VALUE);
        
        loginBtn.setOnAction((event) -> {
            String name = username.getText();
            
            if (time.login(name)) {
                main.setLoggedInUser(time.getLoggedInUser());
                main.drawCourseListScene(); // Set courselistscene in main private properties
                main.getCourseScene().redrawCourseList();
                main.setScene(main.drawCourseListScene());
                main.getMainStage().sizeToScene(); // Resize to fit all components
            } else {
                info.setText("Käyttäjätunnusta ei löydy kannasta. Luo uusi käyttäjä.");
                info.setTextFill(Color.RED);
            }
        });
        
        createNewUserBtn.setOnAction((event) -> {
            String name = username.getText();
            
            if (name.length() < 3) {
                info.setText("Käyttäjätunnuksen on oltava vähintään kolme merkkiä pitkä.");
                info.setTextFill(Color.RED);
            } else if (time.createUser(name)) {
                info.setText("Uusi käyttäjä luotu.");
                info.setTextFill(Color.GREEN);
            } else {
                info.setText("Käyttäjätunnus on jo varattu.");
                info.setTextFill(Color.RED);
            }
            
        });
        
        finishBtn.setOnAction((event) -> {
            main.getMainStage().close();
        });
        
        buttonPane.getChildren().addAll(loginBtn, createNewUserBtn, finishBtn);
        
        content.getChildren().addAll(info, username, buttonPane);
        content.setAlignment(Pos.CENTER);
        mainLayout.setTop(content);
        
        return mainLayout;
    }
    
}
