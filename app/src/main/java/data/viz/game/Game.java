package data.viz.game;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class Game extends Application {

    private static final int CIRCLE_DIAMETER = 15;
    private static final int DISPLAY_DURATION_MS = 1000;

    private final int numberOfPoints;
    private final int extraPoints;

    private int currentNumberOfPoints;
    private int highScore;
    private Label highScoreLabel;

    private RandomPointsRectangle randomPointsRectangle1;
    private RandomPointsRectangle randomPointsRectangle2;

    private Button yesButton;
    private Button noButton;
    private PauseTransition hideTransition;

    public Game() {
        this(5, 5);
    }

    public Game(final int numberOfPoints, final int extraPoints) {
        this.numberOfPoints = numberOfPoints;
        this.currentNumberOfPoints = numberOfPoints;
        this.extraPoints = extraPoints;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialize(primaryStage);
    }

    private void reset() {
        this.currentNumberOfPoints = this.numberOfPoints;
        draw();
    }

    private void draw() {
        this.randomPointsRectangle1.clearPoints();
        this.randomPointsRectangle2.clearPoints();  
        this.randomPointsRectangle1.addRandomPoints(currentNumberOfPoints);
        double[][] points1 = randomPointsRectangle1.getPoints();
        this.randomPointsRectangle2.addPoints(points1);
        // 50% chance of adding extra points to randomPointsRectangle2
        if (Math.random() < 0.5) {
            this.randomPointsRectangle2.addRandomPoints(extraPoints);
        }
        startDisplayTimer();
    }

    private void startDisplayTimer() {
        yesButton.setDisable(true);
        noButton.setDisable(true);
        if (hideTransition != null) {
            hideTransition.stop();
        }
        hideTransition = new PauseTransition(Duration.millis(DISPLAY_DURATION_MS));
        hideTransition.setOnFinished(event -> {
            randomPointsRectangle1.hidePoints();
            randomPointsRectangle2.hidePoints();
            yesButton.setDisable(false);
            noButton.setDisable(false);
        });
        hideTransition.play();
    }

    private void compareAnswer(boolean answer) {
        boolean isEqual = randomPointsRectangle1.equals(randomPointsRectangle2);
        if (!(answer ^ isEqual)) { // correct answer
            highScore = Math.max(highScore, currentNumberOfPoints);
            highScoreLabel.setText("Highscore: " + highScore);
            this.currentNumberOfPoints += extraPoints;
            draw();
        } else { // incorrect answer
            reset();
        }
    }

    private void initialize(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Label label = new Label("Are the two pictures the same?");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-size: 24px;");

        highScoreLabel = new Label("Highscore: 0");
        highScoreLabel.setPadding(new Insets(10));
        highScoreLabel.setStyle("-fx-font-size: 24px;");

        StackPane topBar = new StackPane();
        topBar.getChildren().addAll(label, highScoreLabel);
        StackPane.setAlignment(label, Pos.CENTER);
        StackPane.setAlignment(highScoreLabel, Pos.CENTER_RIGHT);

        HBox hbox = new HBox(200);
        hbox.setPadding(new Insets(20));
        this.randomPointsRectangle1 = new RandomPointsRectangle(400, 400, CIRCLE_DIAMETER);
        this.randomPointsRectangle2 = new RandomPointsRectangle(400, 400, CIRCLE_DIAMETER);
        hbox.getChildren().add(this.randomPointsRectangle1);
        hbox.getChildren().add(this.randomPointsRectangle2);

        yesButton = new Button("Yes");
        yesButton.setPrefSize(120, 50);
        yesButton.setStyle("-fx-font-size: 18px;");
        yesButton.setOnAction(event -> {
            compareAnswer(true);
        });

        noButton = new Button("No");
        noButton.setPrefSize(120, 50);
        noButton.setStyle("-fx-font-size: 18px;");
        noButton.setOnAction(event -> {
            compareAnswer(false);
        });

        HBox hbox2 = new HBox(20);
        hbox2.setPadding(new Insets(20));
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().add(yesButton);
        hbox2.getChildren().add(noButton);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(hbox2);

        root.setTop(topBar);
        root.setCenter(vbox);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        
        primaryStage.setTitle("Data Visualization Game");
        
        // if window is closed, exit the application
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        primaryStage.show();

        draw();
    }

    public static void main(String[] args) {
        launch(args);
    }   

}
