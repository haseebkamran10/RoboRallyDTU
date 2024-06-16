package dk.dtu.compute.se.pisd.roborally.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoadingController {

    @FXML
    private ImageView loadingImage;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/dk/dtu/compute/se/pisd/roborally/robot.png"));
        loadingImage.setImage(image);
    }
}
