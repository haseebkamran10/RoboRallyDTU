// HostGameView.java
package dk.dtu.compute.se.pisd.roborally.view;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class HostGameView extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Host Game");
        // Implement the UI for hosting a game
        primaryStage.show();
    }
}
