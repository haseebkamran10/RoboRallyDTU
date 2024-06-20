package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.view.CreatePlayerView;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RoboRally {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(RoboRally.class);
        ConfigurableApplicationContext context = builder.run(args);

        Application.launch(CreatePlayerView.class, args);
    }
}
