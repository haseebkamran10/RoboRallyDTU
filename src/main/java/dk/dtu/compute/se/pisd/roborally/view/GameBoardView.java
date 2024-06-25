package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.SpaceDTO;
import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 10;");

        // Fetch and render the board asynchronously
        new Thread(() -> {
            BoardDTO board = apiService.getBoardById(1); // Fetch board with ID 1
            List<Player> players = fetchPlayers(); // Fetch players
            Platform.runLater(() -> renderBoard(root, board, players));
        }).start();

        Scene scene = new Scene(root, 1000, 800);  // Adjusted size
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<Player> fetchPlayers() {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            try {
                Player player = apiService.getPlayerById(i);
                if (player != null) {
                    players.add(player);
                }
            } catch (Exception e) {
                // Handle exceptions, such as player not found
            }
        }
        return players;
    }

    private void renderBoard(BorderPane root, BoardDTO board, List<Player> players) {
        if (board == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the board.");
            return;
        }

        // Load the board image
        Image boardImage = new Image(getClass().getResourceAsStream("/boards/png/Board1.png"));
        ImageView boardImageView = new ImageView(boardImage);
        boardImageView.setFitWidth(600);  // Adjusted size
        boardImageView.setFitHeight(600);  // Adjusted size

        StackPane boardPane = new StackPane();
        boardPane.getChildren().add(boardImageView);

        GridPane boardGrid = new GridPane();
        boardGrid.setGridLinesVisible(true);

        for (List<SpaceDTO> row : board.getSpaces()) {
            for (SpaceDTO space : row) {
                Region cell = new Region();
                cell.setMinSize(40, 40);  // Adjusted cell size
                cell.setStyle("-fx-border-color: transparent;"); // Remove border color
                boardGrid.add(cell, space.getX(), space.getY());
            }
        }

        boardPane.getChildren().add(boardGrid);

        HBox cardSelection = new HBox(5);  // Adjusted spacing
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

        VBox controls = new VBox(10);  // Adjusted spacing
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(cardSelection, submitButton);

        // Add players' avatars and names
        VBox leftPlayers = new VBox(10);
        leftPlayers.setAlignment(Pos.CENTER);
        VBox rightPlayers = new VBox(10);
        rightPlayers.setAlignment(Pos.CENTER);

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            VBox playerBox = createPlayerBox(player);
            if (i % 2 == 0) {
                leftPlayers.getChildren().add(playerBox);
            } else {
                rightPlayers.getChildren().add(playerBox);
            }
        }

        root.setLeft(leftPlayers);
        root.setRight(rightPlayers);
        root.setCenter(boardPane);
        root.setBottom(controls);
    }

    private VBox createPlayerBox(Player player) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/avatars/" + player.getAvatar() + ".png")));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        Label nameLabel = new Label(player.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        VBox playerBox = new VBox(imageView, nameLabel);
        playerBox.setAlignment(Pos.CENTER);
        return playerBox;
    }

    private VBox createCard(String name, String imagePath) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setFitWidth(50);  // Adjusted card size
        imageView.setFitHeight(70);  // Adjusted card size
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
