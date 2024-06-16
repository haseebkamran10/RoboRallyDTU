package dk.dtu.compute.se.pisd.roborally.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WelcomeController {

    private static final Logger LOGGER = Logger.getLogger(WelcomeController.class.getName());

    @FXML
    private Button hostButton;

    @FXML
    private Button joinButton;

    @FXML
    private void handleHostGame() {
        try {
            LOGGER.info("Host Game button clicked");
            Stage stage = (Stage) hostButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/dk/dtu/compute/se/pisd/roborally/host.fxml"));
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load host.fxml", e);
        }
    }

    @FXML
    private void handleJoinGame() {
        // Implement the logic for joining a game
    }
}
