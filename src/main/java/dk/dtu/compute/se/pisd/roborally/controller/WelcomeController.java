package dk.dtu.compute.se.pisd.roborally.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private Button hostButton;

    @FXML
    private void handleHostGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/dtu/compute/se/pisd/roborally/host.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Apply CSS file
            scene.getStylesheets().add(getClass().getResource("/dk/dtu/compute/se/pisd/roborally/styles.css").toExternalForm());

            // Assuming you have a reference to the current stage
            Stage stage = (Stage) hostButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleJoinGame() {
        // Logic for Join Game button
    }
}
