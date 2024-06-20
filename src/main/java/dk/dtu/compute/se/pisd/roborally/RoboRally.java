package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.view.CreatePlayerView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RoboRally extends Application {

    private ConfigurableApplicationContext springContext;
    private static String[] savedArgs;

    public static void main(String[] args) {
        RoboRally.savedArgs = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        springContext = SpringApplication.run(RoboRally.class, savedArgs);
        CreatePlayerView view = springContext.getBean(CreatePlayerView.class);
        view.start(primaryStage);
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }
}
