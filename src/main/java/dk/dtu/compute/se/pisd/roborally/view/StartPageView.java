package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class StartPageView extends Application {

    @Autowired
    private CreateBoardView createBoardView;

    @Autowired
    private CreatePlayerView createPlayerView;

    @Autowired
    private HostGameView hostGameView;

    @Autowired
    private JoinGameView joinGameView;

    @Autowired
    private WaitingScreen waitingScreen;

    @Autowired
    private GameBoardView gameBoardView;

    @Autowired
    private ApiService apiService;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
        primaryStage.getIcons().add(logo);
        primaryStage.setTitle("RoboRally Web");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        Label titleLabel = new Label("Welcome to RoboRally Web!");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        titleLabel.setAlignment(Pos.CENTER);

        Button createBoardButton = new Button("Create a Board");
        createBoardButton.setStyle("-fx-font-size: 16px;");
        createBoardButton.setOnAction(e -> createBoardView.start(new Stage()));

        Button createPlayerButton = new Button("Create New Player");
        createPlayerButton.setStyle("-fx-font-size: 16px;");
        createPlayerButton.setOnAction(e -> createPlayerView.start(new Stage()));

        Button joinHostGameButton = new Button("Join/Host Game");
        joinHostGameButton.setStyle("-fx-font-size: 16px;");
        joinHostGameButton.setOnAction(e -> showJoinHostOptions(primaryStage));

        root.getChildren().addAll(titleLabel, createBoardButton, createPlayerButton, joinHostGameButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showJoinHostOptions(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        Label titleLabel = new Label("Join or Host a Game");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        titleLabel.setAlignment(Pos.CENTER);

        Button hostGameButton = new Button("Host Game");
        hostGameButton.setStyle("-fx-font-size: 16px;");
        hostGameButton.setOnAction(e -> hostGameView.start(new Stage()));

        Button joinGameButton = new Button("Join Game");
        joinGameButton.setStyle("-fx-font-size: 16px;");
        joinGameButton.setOnAction(e -> joinGameView.start(new Stage()));

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e -> start(primaryStage));

        root.getChildren().addAll(titleLabel, hostGameButton, joinGameButton, backButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showWaitingScreen(Stage stage, Long gameId) {
        waitingScreen.start(stage, gameId);
        pollGameState(stage, gameId);
    }

    private void pollGameState(Stage stage, Long gameId) {
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (apiService.isGameStarted(gameId)) {
                        timer.cancel();
                        // Transition to GameBoardView
                        gameBoardView.start(stage);
                    }
                });
            }
        };
        timer.schedule(task, 0, 2000); // Poll every 2 seconds
    }

    public static void main(String[] args) {
        launch(args);
    }
}
