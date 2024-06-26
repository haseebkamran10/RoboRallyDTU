package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.view.LoadingScreen;
import dk.dtu.compute.se.pisd.roborally.view.StartPageView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
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
        Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
        primaryStage.getIcons().add(logo);

        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.start(primaryStage);

        new Thread(() -> {
            springContext = SpringApplication.run(RoboRally.class, savedArgs);
            Platform.runLater(() -> {
                StartPageView view = springContext.getBean(StartPageView.class);
                primaryStage.close();
                Stage newStage = new Stage();
                view.start(newStage);
            });
        }).start();
    }

    @Override
    public void stop() {
        if (springContext != null) {
            springContext.close();
        }
        Platform.exit();
    }
}
