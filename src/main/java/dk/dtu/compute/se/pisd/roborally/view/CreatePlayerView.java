package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreatePlayerView extends Application {

    @Autowired
    private ApiService apiService;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Player");

        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 20;");

        Label nameLabel = new Label("Player Name:");
        nameLabel.setStyle("-fx-text-fill: white;");
        TextField nameField = new TextField();

        Label avatarLabel = new Label("Avatar:");
        avatarLabel.setStyle("-fx-text-fill: white;");
        ComboBox<String> avatarComboBox = new ComboBox<>();
        avatarComboBox.getItems().addAll("Avatar1", "Avatar2", "Avatar3");

        Button submitButton = new Button("Create Player");
        submitButton.setOnAction(e -> handleSubmit(nameField, avatarComboBox));

        root.getChildren().addAll(nameLabel, nameField, avatarLabel, avatarComboBox, submitButton);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSubmit(TextField nameField, ComboBox<String> avatarComboBox) {
        String playerName = nameField.getText();
        String avatar = avatarComboBox.getValue();

        if (playerName == null || playerName.isEmpty() || avatar == null || avatar.isEmpty()) {
            // Display error message
            System.out.println("Player name and avatar are required.");
            return;
        }

        Player player = new Player();
        player.setName(playerName);
        player.setAvatar(avatar);

        try {
            apiService.createPlayer(player);
            System.out.println("Player created successfully!");
            // Redirect to the next view or perform further actions
        } catch (Exception e) {
            e.printStackTrace();
            // Display error message
            System.out.println("Failed to create player.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
