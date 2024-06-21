package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JoinGameView extends Application {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApplicationContext context;

    private Stage startPageStage;

    @Override
    public void start(Stage primaryStage) {
        this.startPageStage = primaryStage;

        primaryStage.setTitle("Join Game");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        Label titleLabel = new Label("Join a Game");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        titleLabel.setAlignment(Pos.CENTER);

        Label joinCodeLabel = new Label("Join Code:");
        joinCodeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField joinCodeField = new TextField();
        joinCodeField.setMaxWidth(250);
        joinCodeField.setAlignment(Pos.CENTER);

        Label playerIdLabel = new Label("Player ID:");
        playerIdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField playerIdField = new TextField();
        playerIdField.setMaxWidth(250);
        playerIdField.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Join Game");
        submitButton.setStyle("-fx-font-size: 16px;");
        submitButton.setOnAction(e -> handleSubmit(primaryStage, joinCodeField, playerIdField));

        root.getChildren().addAll(titleLabel, joinCodeLabel, joinCodeField, playerIdLabel, playerIdField, submitButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSubmit(Stage primaryStage, TextField joinCodeField, TextField playerIdField) {
        String joinCode = joinCodeField.getText();
        String playerId = playerIdField.getText();

        if (joinCode == null || joinCode.isEmpty() || playerId == null || playerId.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Join Code and Player ID are required.");
            return;
        }

        try {
            PlayerDTO playerDTO = new PlayerDTO();
            playerDTO.setId(Long.parseLong(playerId));

            apiService.joinGameSessionByCode(joinCode, playerDTO);
            showAlert(AlertType.INFORMATION, "Joined Game",
                    "Successfully joined the game!");
            primaryStage.close();
            startPageStage.show();
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid input for Player ID.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to join game session.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
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
