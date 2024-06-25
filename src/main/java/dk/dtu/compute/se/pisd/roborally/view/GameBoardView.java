package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameBoardView extends Application {

    @Autowired
    private ApiService apiService;

    private long playerId;
    private long gameId;

    private List<String> selectedCards = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RoboRally Game Board");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        GridPane boardGrid = new GridPane();
        boardGrid.setGridLinesVisible(true);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Label cell = new Label();
                cell.setMinSize(50, 50);
                cell.setStyle("-fx-border-color: black;");
                boardGrid.add(cell, i, j);
            }
        }

        HBox cardSelection = new HBox(10);
        cardSelection.setAlignment(Pos.CENTER);

        // Add cards
        cardSelection.getChildren().add(createCard("Again", "/cards/Again.png"));
        cardSelection.getChildren().add(createCard("BackUp", "/cards/Back up.png"));
        cardSelection.getChildren().add(createCard("Fwd", "/cards/Fwd.png"));
        cardSelection.getChildren().add(createCard("FwdX2", "/cards/Fwd x2.png"));
        cardSelection.getChildren().add(createCard("FwdX3", "/cards/Fwd x3.png"));
        cardSelection.getChildren().add(createCard("TurnLeft", "/cards/Turn Left.png"));
        cardSelection.getChildren().add(createCard("TurnRight", "/cards/Turn Right.png"));
        cardSelection.getChildren().add(createCard("UTurn", "/cards/U-Turn.png"));

        Button submitButton = new Button("Submit Moves");
        submitButton.setStyle("-fx-font-size: 16px;");
        submitButton.setOnAction(e -> handleSubmit());

        root.getChildren().addAll(boardGrid, cardSelection, submitButton);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createCard(String name, String imagePath) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setFitWidth(80);
        imageView.setFitHeight(120);
        Label label = new Label(name);
        VBox cardBox = new VBox(imageView, label);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setOnMouseClicked(event -> {
            if (selectedCards.contains(name)) {
                selectedCards.remove(name);
                cardBox.setStyle("");
            } else {
                selectedCards.add(name);
                cardBox.setStyle("-fx-border-color: yellow;");
            }
        });
        return cardBox;
    }

    private void handleSubmit() {
        if (selectedCards.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No cards selected. Please select cards.");
            return;
        }

        // Calculate new coordinates based on selected cards
        int x = 0, y = 0;  // Starting coordinates
        for (String card : selectedCards) {
            switch (card) {
                case "Fwd":
                    y -= 1;
                    break;
                case "FwdX2":
                    y -= 2;
                    break;
                case "FwdX3":
                    y -= 3;
                    break;
                case "BackUp":
                    y += 1;
                    break;
                case "TurnLeft":
                    x -= 1;
                    break;
                case "TurnRight":
                    x += 1;
                    break;
                case "UTurn":
                    x = -x;
                    y = -y;
                    break;
                case "Again":
                    // Logic for 'Again' card
                    break;
            }
        }

        try {
            apiService.movePlayer(playerId, x, y);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Moves submitted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit moves.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
