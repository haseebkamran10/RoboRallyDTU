package dk.dtu.compute.se.pisd.roborally.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        joinHostGameButton.setOnAction(e -> hostGameView.start(new Stage()));

        root.getChildren().addAll(titleLabel, createBoardButton, createPlayerButton, joinHostGameButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
