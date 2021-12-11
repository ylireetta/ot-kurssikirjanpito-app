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
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.bson.types.ObjectId;
/**
 *
 * @author ylireett
 */
public class AjankaytonseurantaUi extends Application {
    // Testmode helps with junit tests when creating ConcreteDaos
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
        CourseDao courseDao = new ConcreteCourseDao(loggedInUser, testmode);
        
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
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        Label loggedInUsername = new Label("Käyttäjä: " + loggedInUser.getUsername());
        Button logOutBtn = new Button("Kirjaudu ulos");
        logOutBtn.setMaxWidth(Double.MAX_VALUE);
        Button courseRankBtn = new Button("Kurssien top-lista"); // Create new view for most time consuming courses
        courseRankBtn.setMaxWidth(Double.MAX_VALUE);
        Button addCourseBtn = new Button("Lisää uusi kurssi"); // Create new view for course data input
        Button showDataBtn = new Button("Oma kurssidata");
        
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
        courseList.setValue(null); // Set value to null when entering scene
        
        logOutBtn.setOnAction((event) -> {
            if (timerRunning) {
                updateSpentTimeToDb();
            }
            tmService.logout();
            loggedInUser = tmService.getLoggedInUser();
            mainStage.getScene().setRoot(drawMainScene());
            mainStage.sizeToScene(); // Resize to correct size
        });
        
        courseRankBtn.setOnAction((event) -> {
            mainStage.getScene().setRoot(drawCourseRankScene());
        });
        
        addCourseBtn.setOnAction((event) -> {
            mainStage.getScene().setRoot(drawNewCourseScene());
        });
        
        showDataBtn.setOnAction((event) -> {
            // Draw new scene for pie chart
            mainStage.getScene().setRoot(drawCourseDataScene());
        });
        
        grid.add(showDataBtn, 0, 8);
        
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
        
        HBox btnBox = new HBox();
        btnBox.setSpacing(10);
        btnBox.getChildren().addAll(addCourseBtn, startTimerBtn, stopTimerBtn);
        
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
    
    public GridPane drawNewCourseScene() {
        GridPane newCourseGrid = new GridPane();
        
        Button addCourseBtn = new Button("Lisää kurssi");
        Button returnBtn = drawReturnButton(drawLoggedInScene());
        
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
    
    public GridPane drawCourseRankScene() {
        GridPane courseRankGrid = new GridPane();
        
        Button returnBtn = drawReturnButton(drawLoggedInScene());
        
        TextArea courseRank = new TextArea();
        courseRank.setEditable(false);
        courseRank.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        List<Course> topCourses = tmService.getCourseRankFromDb();
        StringBuilder sb = new StringBuilder();
        int iteration = 1;
        
        for (Course c : topCourses) {
            sb.append(iteration + ": " + c.getCourseName() + "\n");
            sb.append(tmService.convertTimeSpent(c) + "\n\n");
            iteration++;
        }
        
        courseRank.setText(sb.toString());
        
        courseRankGrid.add(returnBtn, 0, 0);
        courseRankGrid.add(courseRank, 0, 1);
        
        return courseRankGrid;
    }
    
    public GridPane drawCourseDataScene() {
        // Draw a pie chart of logged in user's courses
        GridPane dataScene = new GridPane();
        
        Button returnBtn = drawReturnButton(drawLoggedInScene());
        redrawCourseList(); // Redraw in case user has started the timer at some point and now returns to this scene
        ObservableList<Course> courseItems = courseList.getItems();
        
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
                    percentLabel.setText(oneData.getName() + " " + text);
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
    
    public Button drawReturnButton(Parent returnScene) {
        Button returnBtn = new Button("Palaa takaisin");
        returnBtn.setOnAction((event) -> {
            mainStage.getScene().setRoot(returnScene);
        });
        
        return returnBtn;
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
