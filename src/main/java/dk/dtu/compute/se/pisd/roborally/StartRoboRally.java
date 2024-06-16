/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020,2021: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package dk.dtu.compute.se.pisd.roborally;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;


/**
 * This is a class for starting up the RoboRally application. This is a
 * workaround for a strange quirk in the Open JavaFX project launcher,
 * which prevents starting a JavaFX application in IntelliJ directly:
 *
 *   https://stackoverflow.com/questions/52569724/javafx-11-create-a-jar-file-with-gradle/52571719#52571719
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */

public class StartRoboRally extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/dk/dtu/compute/se/pisd/roborally/robot.png")));
        showLoadingScreen(primaryStage);
    }

    private void showLoadingScreen(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/dtu/compute/se/pisd/roborally/loading.fxml"));
        Scene loadingScene = new Scene(loader.load());
        loadingScene.setFill(null); // Make sure the scene is transparent
        primaryStage.setScene(loadingScene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        // Simulate loading time with a delay (3 seconds for example)
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Simulate a 3-second loading time
                javafx.application.Platform.runLater(() -> {
                    try {
                        showWelcomeScreen();
                        primaryStage.close(); // Close the loading screen stage
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showWelcomeScreen() throws Exception {
        Stage welcomeStage = new Stage();
        welcomeStage.getIcons().add(new Image(getClass().getResourceAsStream("/dk/dtu/compute/se/pisd/roborally/robot.png")));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/dtu/compute/se/pisd/roborally/welcome.fxml"));
        Scene welcomeScene = new Scene(loader.load());
        welcomeStage.setScene(welcomeScene);
        welcomeStage.initStyle(StageStyle.DECORATED);
        welcomeStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}