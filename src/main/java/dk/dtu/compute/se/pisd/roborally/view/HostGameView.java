package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.model.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import dk.dtu.compute.se.pisd.roborally.service.PollingService;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class HostGameView extends Application {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private WaitingScreen waitingScreen;

    @Autowired
    private PollingService pollingService;

    @Autowired
    private GameBoardView gameBoardView;  // Add this line

    private Stage startPageStage;

    @Override
    public void start(Stage primaryStage) {
        this.startPageStage = primaryStage;

        Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
        primaryStage.getIcons().add(logo);

        primaryStage.setTitle("Host Game");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        Label titleLabel = new Label("Host a New Game");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        titleLabel.setAlignment(Pos.CENTER);

        Label boardIdLabel = new Label("Board ID:");
        boardIdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField boardIdField = new TextField();
        boardIdField.setMaxWidth(250);
        boardIdField.setAlignment(Pos.CENTER);

        Label maxPlayersLabel = new Label("Max Players (2-6):");
        maxPlayersLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField maxPlayersField = new TextField();
        maxPlayersField.setMaxWidth(250);
        maxPlayersField.setAlignment(Pos.CENTER);

        Label playerIdLabel = new Label("Player ID:");
        playerIdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField playerIdField = new TextField();
        playerIdField.setMaxWidth(250);
        playerIdField.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Host Game");
        submitButton.setStyle("-fx-font-size: 16px;");
        submitButton.setOnAction(e -> handleSubmit(primaryStage, boardIdField, maxPlayersField, playerIdField));

        root.getChildren().addAll(titleLabel, boardIdLabel, boardIdField, maxPlayersLabel, maxPlayersField, playerIdLabel, playerIdField, submitButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSubmit(Stage primaryStage, TextField boardIdField, TextField maxPlayersField, TextField playerIdField) {
        String boardId = boardIdField.getText();
        String maxPlayers = maxPlayersField.getText();
        String playerId = playerIdField.getText();

        if (boardId == null || boardId.isEmpty() || maxPlayers == null || maxPlayers.isEmpty() || playerId == null || playerId.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Board ID, Max Players, and Player ID are required.");
            return;
        }

        try {
            int maxPlayersInt = Integer.parseInt(maxPlayers);
            if (maxPlayersInt < 2 || maxPlayersInt > 6) {
                showAlert(AlertType.ERROR, "Error", "Max Players must be between 2 and 6.");
                return;
            }

            GameSessionDTO gameSessionDTO = new GameSessionDTO();
            gameSessionDTO.setBoardId(Long.parseLong(boardId));
            gameSessionDTO.setMaxPlayers(maxPlayersInt);

            PlayerDTO playerDTO = new PlayerDTO();
            playerDTO.setId(Long.parseLong(playerId));
            gameSessionDTO.setPlayers(Collections.singletonList(playerDTO));

            GameSessionDTO createdGameSession = apiService.createGameSession(gameSessionDTO);
            showAlert(AlertType.INFORMATION, "Game Created",
                    "Game created successfully! Join Code: " + createdGameSession.getJoinCode(), primaryStage);

            // Redirect to the waiting screen and start polling
            waitingScreen.start(primaryStage, createdGameSession.getId());
            pollingService.startPolling(createdGameSession.getId(), () -> {
                try {
                    gameBoardView.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Invalid input for Board ID, Max Players, or Player ID.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to create game session.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(AlertType alertType, String title, String message, Stage primaryStage) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(response -> {
            primaryStage.close();
            startPageStage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
