package dk.dtu.compute.se.pisd.roborally.view;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

@Component
public class LoadingScreen {

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Loading...");


        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        // Load the logo image
        Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(200);
        logoView.setFitHeight(200);

        // Create a loading animation for the logo
        TranslateTransition logoTranslateTransition = new TranslateTransition(Duration.seconds(1), logoView);
        logoTranslateTransition.setByY(20);
        logoTranslateTransition.setAutoReverse(true);
        logoTranslateTransition.setCycleCount(TranslateTransition.INDEFINITE);

        // Create a loading animation for the loading text
        VBox loadingBox = new VBox();
        loadingBox.setAlignment(Pos.CENTER);

        Label loadingLabel = new Label("Loading...");
        loadingLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        TranslateTransition textTranslateTransition = new TranslateTransition(Duration.seconds(1), loadingLabel);
        textTranslateTransition.setByY(20);
        textTranslateTransition.setAutoReverse(true);
        textTranslateTransition.setCycleCount(TranslateTransition.INDEFINITE);

        FadeTransition textFadeTransition = new FadeTransition(Duration.seconds(1), loadingLabel);
        textFadeTransition.setFromValue(1.0);
        textFadeTransition.setToValue(0.5);
        textFadeTransition.setAutoReverse(true);
        textFadeTransition.setCycleCount(FadeTransition.INDEFINITE);

        logoTranslateTransition.play();
        textTranslateTransition.play();
        textFadeTransition.play();

        loadingBox.getChildren().add(loadingLabel);

        root.getChildren().addAll(logoView, loadingBox);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
