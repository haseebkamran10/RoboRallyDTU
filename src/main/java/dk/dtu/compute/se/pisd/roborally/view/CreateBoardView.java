package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CreateBoardView extends Application {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApplicationContext context;

    private Stage startPageStage;

    @Override
    public void start(Stage primaryStage) {
        this.startPageStage = primaryStage;

        primaryStage.setTitle("Create Board");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        Label titleLabel = new Label("Create a New Board");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        titleLabel.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Board Name:");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField nameField = new TextField();
        nameField.setMaxWidth(250);
        nameField.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Create Board");
        submitButton.setStyle("-fx-font-size: 16px;");
        submitButton.setOnAction(e -> handleSubmit(primaryStage, nameField));

        root.getChildren().addAll(titleLabel, nameLabel, nameField, submitButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSubmit(Stage primaryStage, TextField nameField) {
        String boardName = nameField.getText();

        if (boardName == null || boardName.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Board name is required.");
            return;
        }

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setName(boardName);

        try {
            BoardDTO createdBoard = apiService.createBoard(boardDTO);
            showAlert(AlertType.INFORMATION, "Board Created",
                    "Board created successfully! Board ID: " + createdBoard.getId(), primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to create board.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(AlertType alertType, String title, String message, Stage primaryStage) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(response -> {
            primaryStage.close();
            startPageStage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
