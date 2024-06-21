package dk.dtu.compute.se.pisd.roborally.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Override
    public void start(Stage primaryStage) {
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

    public static void main(String[] args) {
        launch(args);
    }
}
