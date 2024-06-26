package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.model.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GameBoardView extends Application {

    @Autowired
    private ApiService apiService;

    private long playerId = 1; // Ensure this is properly set
    private long gameId = 1; // Ensure this is properly set

    private List<String> selectedCards = new ArrayList<>();
    private List<Player> players;
    private Map<Long, ImageView> playerImageViews = new HashMap<>(); // Map to store player ImageViews
    private GridPane boardGrid;

    @Override
    public void start(Stage primaryStage) {
        Image logo = loadImage("/logo.png");
        if (logo != null) {
            primaryStage.getIcons().add(logo);
        }
        primaryStage.setTitle("RoboRally Game Board");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 10;");

        // Fetch and render the board asynchronously
        new Thread(() -> {
            BoardDTO board = apiService.getBoardById(1); // Fetch board with ID 1
            players = fetchPlayers(); // Fetch players
            Platform.runLater(() -> renderBoard(root, board, players));
            startPolling();
        }).start();

        Scene scene = new Scene(root, 1200, 800); // Adjusted scene size for better visibility
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Image loadImage(String path) {
        try {
            return new Image(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

        boardGrid = new GridPane();
        boardGrid.setGridLinesVisible(true);
        boardGrid.setAlignment(Pos.CENTER); // Center-align the board
        boardGrid.setStyle("-fx-padding: 20; -fx-background-color: #1E1E1E;"); // Adding padding and background color

        // Create a chequered board
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                Region cell = new Region();
                cell.setMinSize(60, 60);  // Adjusted cell size for a larger board
                cell.setStyle((i + j) % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: black;"); // Chequered pattern
                boardGrid.add(cell, j, i);
            }
        }

        // Place players' avatars on the board
        for (Player player : players) {
            addPlayerToBoard(board, player);
        }

        root.setCenter(boardGrid);

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

        // Add players' avatars and names to the side
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
        root.setBottom(controls);
    }

    private void addPlayerToBoard(BoardDTO board, Player player) {
        Image playerImage = loadImage("/avatars/" + player.getAvatar() + ".png");
        if (playerImage == null) {
            playerImage = loadImage("/avatars/default.png"); // Fallback to a default image
        }

        ImageView playerImageView = new ImageView(playerImage);
        playerImageView.setFitWidth(60);  // Adjust size to fit the cell
        playerImageView.setFitHeight(60); // Adjust size to fit the cell
        playerImageViews.put(player.getId(), playerImageView); // Store the ImageView in the map

        StackPane playerCell = new StackPane(playerImageView);
        int transformedY = board.getHeight() - player.getY() - 1;
        if (player.getX() >= 0 && player.getX() < board.getWidth() && transformedY >= 0 && transformedY < board.getHeight()) {
            boardGrid.add(playerCell, player.getX(), transformedY);
        }
    }

    private VBox createPlayerBox(Player player) {
        Image playerImage = loadImage("/avatars/" + player.getAvatar() + ".png");
        if (playerImage == null) {
            playerImage = loadImage("/avatars/default.png"); // Fallback to a default image
        }

        ImageView imageView = new ImageView(playerImage);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        Label nameLabel = new Label(player.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        VBox playerBox = new VBox(imageView, nameLabel);
        playerBox.setAlignment(Pos.CENTER);
        return playerBox;
    }

    private VBox createCard(String name, String imagePath) {
        ImageView imageView = new ImageView(loadImage(imagePath));
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
        Player currentPlayer = players.stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
        if (currentPlayer == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Current player not found.");
            return;
        }

        // Start from the current player's position
        int x = currentPlayer.getX();
        int y = currentPlayer.getY();

        // Adjust coordinates based on selected cards
        for (String card : selectedCards) {
            switch (card) {
                case "Fwd":
                    y += 1; // Move up
                    break;
                case "FwdX2":
                    y += 2; // Move up twice
                    break;
                case "FwdX3":
                    y += 3; // Move up thrice
                    break;
                case "BackUp":
                    y -= 1; // Move down
                    break;
                case "TurnLeft":
                    x -= 1; // Move left
                    break;
                case "TurnRight":
                    x += 1; // Move right
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

        // Ensure coordinates are within valid range
        x = Math.max(0, x);
        y = Math.max(0, y);

        try {
            apiService.movePlayer(playerId, x, y);
            currentPlayer.setX(x);
            currentPlayer.setY(y);
            updatePlayerPosition(currentPlayer);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Moves submitted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit moves.");
        }
    }

    private void updatePlayerPosition(Player player) {
        // Update the player's position on the game board UI
        ImageView playerImageView = playerImageViews.get(player.getId());
        if (playerImageView != null) {
            // Remove the player ImageView from the current cell
            GridPane parent = (GridPane) playerImageView.getParent().getParent();
            parent.getChildren().remove(playerImageView.getParent());

            // Calculate the new cell position
            int transformedY = parent.getRowCount() - player.getY() - 1;

            // Add the player ImageView to the new cell
            StackPane newCell = new StackPane(playerImageView);
            if (player.getX() >= 0 && player.getX() < parent.getColumnCount() && transformedY >= 0 && transformedY < parent.getRowCount()) {
                parent.add(newCell, player.getX(), transformedY);
            }
        }

        System.out.println("Updated player position to (" + player.getX() + ", " + player.getY() + ")");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void startPolling() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> updateGameState()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateGameState() {
        Platform.runLater(() -> {
            GameSessionDTO gameSession = apiService.getGameSessionState(gameId);
            if (gameSession != null) {
                List<Player> updatedPlayers = new ArrayList<>();
                for (PlayerDTO playerDTO : gameSession.getPlayers()) {
                    updatedPlayers.add(convertPlayerDTOToPlayer(playerDTO));
                }
                players = updatedPlayers;
                for (Player player : players) {
                    updatePlayerPosition(player);
                }
            }
        });
    }

    private Player convertPlayerDTOToPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setId(playerDTO.getId());
        player.setName(playerDTO.getName());
        player.setAvatar(playerDTO.getAvatar());
        player.setX(playerDTO.getX());
        player.setY(playerDTO.getY());
        return player;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
