package ajankaytonseuranta.ui;

import ajankaytonseuranta.dao.ConcreteCourseDao;
import ajankaytonseuranta.dao.ConcreteUserDao;
import ajankaytonseuranta.dao.CourseDao;
import ajankaytonseuranta.dao.UserDao;
import ajankaytonseuranta.domain.Course;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import ajankaytonseuranta.domain.TimeManagementService;
import ajankaytonseuranta.domain.User;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import org.bson.types.ObjectId;

public class AjankaytonseurantaUi extends Application {
    // Testmode helps with junit tests when creating ConcreteDaos
    private final boolean testmode = false;
    private Stage mainStage;
    private TimeManagementService tmService;
    
    private User loggedInUser;
    private CourseListScene courseScene;
    
    @Override
    public void init() throws Exception {
        UserDao userDao = new ConcreteUserDao(testmode);
        CourseDao courseDao = new ConcreteCourseDao(loggedInUser, testmode);
        
        tmService = new TimeManagementService(userDao, courseDao);
    }
    
    @Override
    public void start(Stage window) {
        mainStage = window;
        mainStage.setMaxWidth(500);
        mainStage.setResizable(false);
        
        // Main scene
        LogInScene login = new LogInScene(this, tmService);
        BorderPane mainLayout = login.drawMainScene();
        
        // Setup main scene
        Scene scene = new Scene(mainLayout);
        scene.getStylesheets().add("/style.css");
        
        window.setTitle("Kurssien ajankäytön seuranta");
        window.setScene(scene);
        window.show();
    }
    
    /**
     * Vaihtaa ikkunan näkymää.
     * 
     * @param sceneToSet Näkymä, johon vaihdetaan
     */
    public void setScene(Parent sceneToSet) {
        mainStage.getScene().setRoot(sceneToSet);
    }
    
    public Stage getMainStage() {
        return mainStage;
    }
    
    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }
    
    public CourseListScene getCourseScene() {
        return courseScene;
    }
    
    /**
     * Muodostaa ohjelman kirjautumisnäkymän.
     * 
     * @return Ohjelman kirjautumisnäkymä
     */
    public BorderPane drawLoginScene() {
        LogInScene login = new LogInScene(this, tmService);
        return login.drawMainScene();
    }
    
    /**
     * Muodostaa kirjautuneen käyttäjän kurssinäkymän
     * 
     * @return Kirjautuneen käyttäjän kurssinäkymä
     */
    public GridPane drawCourseListScene() {
        if (courseScene == null) {
            CourseListScene courseScene = new CourseListScene(this, tmService);
            this.courseScene = courseScene;
        }
        return courseScene.drawCourseListScene();
    }
    
    /**
     * Muodostaa napin, jonka avulla pääsee palaamaan edelliseen näkymään
     * 
     * @param returnScene Näkymä, johon halutaan palata
     * @return Paluunappi
     */
    public Button drawReturnButton(Parent returnScene) {
        Button returnBtn = new Button("Palaa takaisin");
        returnBtn.setOnAction((event) -> {
            mainStage.getScene().setRoot(returnScene);
            mainStage.sizeToScene();
        });
        
        return returnBtn;
    }
    
    /**
     * Päivittää kurssiin käytetyn ajan tietokantaan, jos ohjelma suljetaan epätavallisesti
     * 
     * @param selectedCourseId Päivitettävän kurssin id
     */
    public void updateSpentTimeToDb(ObjectId selectedCourseId) {
        System.out.println(String.format("Päivitetään käytetty aika kurssille id %1$s...", selectedCourseId));
        System.out.println("---------------");
        tmService.setTimeSpentForCourse(selectedCourseId, courseScene.getTimerStartTime(), System.currentTimeMillis());
        System.out.println(String.format("Käytetty aika päivitetty kurssille id %1$s.", selectedCourseId));
    }
    
    /**
     * Jos sisäänkirjautunut käyttäjä sulkee ohjelman pysäyttämättä aikalaskuria ensin, päivitetään käytetty aika tietokantaan.
     */
    @Override
    public void stop() {
        // If timer is running and program is closed while user is logged in, update time to db
        if (loggedInUser != null && courseScene.timerRunning()) {
            ObjectId selected = ((Course) (courseScene.getCourseList().getSelectionModel().getSelectedItem())).getCourseId();
            updateSpentTimeToDb(selected);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
