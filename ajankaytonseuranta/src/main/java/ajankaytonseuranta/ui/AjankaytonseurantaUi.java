package ajankaytonseuranta.ui;

import ajankaytonseuranta.dao.ConcreteCourseDao;
import ajankaytonseuranta.dao.ConcreteUserDao;
import ajankaytonseuranta.dao.CourseDao;
import ajankaytonseuranta.dao.UserDao;
import ajankaytonseuranta.domain.Course;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import ajankaytonseuranta.domain.TimeManagementService;
import ajankaytonseuranta.domain.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.bson.types.ObjectId;
/**
 *
 * @author ylireett
 */
public class AjankaytonseurantaUi extends Application {
    // Testmode helps with junit tests when creating ConcreteUserDao
    private final boolean testmode = false;
    private Stage mainStage;
    private TimeManagementService tmService;
    
    private User loggedInUser;
    private ComboBox courseList = new ComboBox();
    
    private long timerStartTime;
    private boolean timerRunning = false;
    
    @Override
    public void init() throws Exception {
        UserDao userDao = new ConcreteUserDao(testmode);
        CourseDao courseDao = new ConcreteCourseDao(loggedInUser);
        
        tmService = new TimeManagementService(userDao, courseDao);
    }
    
    @Override
    public void start(Stage window) {
        mainStage = window;
        mainStage.setMaxWidth(500);
        mainStage.setResizable(false);
        
        // Main scene
        BorderPane mainLayout = drawMainScene();
        
        // Setup main scene
        Scene scene = new Scene(mainLayout);
        
        window.setTitle("Kurssien ajankäytön seuranta");
        window.setScene(scene);
        window.show();
    }
    
    public BorderPane drawMainScene() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10, 10, 10, 10));
        
        // Buttons
        TilePane buttonPane = new TilePane();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setHgap(10);
        
        Label info = new Label();
        
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
            
            if (tmService.login(name)) {
                loggedInUser = tmService.getLoggedInUser();
                redrawCourseList();
                mainStage.getScene().setRoot(drawLoggedInScene());
                mainStage.sizeToScene(); // Resize to fit all components
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
            } else if (tmService.createUser(name)) {
                info.setText("Uusi käyttäjä luotu.");
                info.setTextFill(Color.GREEN);
            } else {
                info.setText("Käyttäjätunnus on jo varattu.");
                info.setTextFill(Color.RED);
            }
            
        });
        
        finishBtn.setOnAction((event) -> {
            mainStage.close();
        });
        
        buttonPane.getChildren().addAll(loginBtn, createNewUserBtn, finishBtn);
        
        mainLayout.setTop(info);
        mainLayout.setAlignment(info, Pos.TOP_CENTER);
        mainLayout.setCenter(username);
        mainLayout.setBottom(buttonPane);
        
        return mainLayout;
    }
    
    public GridPane drawLoggedInScene() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        
        Label loggedInUsername = new Label("Käyttäjä: " + loggedInUser.getUsername());
        Button logOutBtn = new Button("Kirjaudu ulos");
        Button addCourseBtn = new Button("Lisää uusi kurssi"); // Create new view for course data input
        
        // Display courses for logged in user
        Callback<ListView<Course>, ListCell<Course>> factory = lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getCourseName());
            }
        };
        
        courseList.setButtonCell(factory.call(null)); // Prevents combobox from displaying object address when selection changes
        courseList.setCellFactory(factory);
        
        logOutBtn.setOnAction((event) -> {
            if (timerRunning) {
                updateSpentTimeToDb();
            }
            tmService.logout();
            loggedInUser = tmService.getLoggedInUser();
            mainStage.getScene().setRoot(drawMainScene());
            mainStage.sizeToScene(); // Resize to correct size
        });
        
        addCourseBtn.setOnAction((event) -> {
            mainStage.getScene().setRoot(drawNewCourseScene());
        });
        
        
        TextArea courseInfoFromDb = new TextArea();
        courseInfoFromDb.setEditable(false);
        
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
            tmService.setTimeSpentForCourse(selectedCourseId, timerStartTime, System.currentTimeMillis());
            courseInfoFromDb.setText(refreshCourseInfo());
            
        });
        
        // Event handler for course combobox selection change
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (loggedInUser != null && courseList.getSelectionModel().getSelectedItem() != null) {
                    try {
                        startTimerBtn.setVisible(true);
                        courseInfoFromDb.setText(refreshCourseInfo());
                    } catch (Exception ex) {
                        System.out.println("Jokin meni vikaan.");
                    }    
                }
            }
        };
        
        courseList.setOnAction(event);
        
        grid.add(loggedInUsername, 3, 0);
        grid.add(logOutBtn, 4, 0);
        grid.add(courseList, 1, 1);
        
        grid.add(addCourseBtn, 1, 2);
        grid.add(startTimerBtn, 2, 2);
        grid.add(stopTimerBtn, 3, 2);
        grid.add(courseInfoFromDb, 1, 4);
        
        
        return grid;
    }
    
    public GridPane drawNewCourseScene() {
        GridPane newCourseGrid = new GridPane();
        
        Button addCourseBtn = new Button("Lisää kurssi");
        Button returnBtn = new Button("Palaa takaisin");
        
        returnBtn.setOnAction((event) -> {
            mainStage.getScene().setRoot(drawLoggedInScene());
        });
        
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
                Course newCourse = new Course(courseName.getText(), Integer.parseInt(courseCredit.getText()), loggedInUser.getUserId());
                tmService.createCourse(newCourse, loggedInUser);
                courseInfo.setText("Uusi kurssi lisätty.");
                courseInfo.setTextFill(Color.GREEN);
                redrawCourseList();
                courseName.setText("");
                courseCredit.setText("");
            } else {
                courseInfo.setText("Tarkista syöte.");
                courseInfo.setTextFill(Color.RED);
            }
        });
        
        newCourseGrid.setHgap(10);
        newCourseGrid.setVgap(10);
        newCourseGrid.add(courseInfo, 1, 0);
        newCourseGrid.add(courseNameLabel, 0, 1);
        newCourseGrid.add(courseName, 1, 1);
        
        newCourseGrid.add(courseCreditLabel, 0, 2);
        newCourseGrid.add(courseCredit, 1, 2);
        newCourseGrid.add(addCourseBtn, 0, 4);
        newCourseGrid.add(returnBtn, 1, 4);
        
        newCourseGrid.add(instructions, 2, 0);
        newCourseGrid.setRowSpan(instructions, newCourseGrid.REMAINING);
        
        return newCourseGrid;
    }
    
    public boolean isInteger(String text) {
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
    
    public void redrawCourseList() {
        courseList.getItems().clear();
        
        if (loggedInUser != null) {
            courseList.setItems(FXCollections.observableArrayList(tmService.getCoursesForLoggedInUser()));
        }
    }
    
    public String refreshCourseInfo() {
        Course selectedCourse = tmService.getCourseInfo(((Course) courseList.getSelectionModel().getSelectedItem()).getCourseId());
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Kurssin nimi: %1$s \n", selectedCourse.getCourseName()));
        sb.append(String.format("Opintopisteet: %1$s \n", selectedCourse.getCourseCredits()));
        sb.append(String.format("Aikaa käytetty: %1$s \n", tmService.convertTimeSpent(selectedCourse)));
                            
        return sb.toString();
    }
    
    public void updateSpentTimeToDb() {
        ObjectId selectedCourseId = ((Course) courseList.getSelectionModel().getSelectedItem()).getCourseId();
        System.out.println(String.format("Päivitetään käytetty aika kurssille id %1$s...", selectedCourseId));
        System.out.println("---------------");
        tmService.setTimeSpentForCourse(selectedCourseId, timerStartTime, System.currentTimeMillis());
        System.out.println(String.format("Käytetty aika päivitetty kurssille id %1$s.", selectedCourseId));
    }
    
    @Override
    public void stop() {
        // If timer is running and program is closed while user is logged in, update time to db
        if (loggedInUser != null && timerRunning) {
            updateSpentTimeToDb();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
