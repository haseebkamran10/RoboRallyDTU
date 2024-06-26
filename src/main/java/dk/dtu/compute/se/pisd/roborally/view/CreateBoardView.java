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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

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

        Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
        primaryStage.getIcons().add(logo);


        primaryStage.setTitle("Create Board");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        Label titleLabel = new Label("Create a New Board");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        titleLabel.setAlignment(Pos.CENTER);

        Label boardSelectionLabel = new Label("Select a Board:");
        boardSelectionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        ToggleGroup toggleGroup = new ToggleGroup();

        VBox board1 = createBoardOption("Board1", "src/main/resources/boards/png/Board1.png", toggleGroup);
        VBox board2 = createBoardOption("Board2", "src/main/resources/boards/png/Board2.png", toggleGroup);
        VBox board3 = createBoardOption("Board3", "src/main/resources/boards/png/Board3.png", toggleGroup);

        Button submitButton = new Button("Create Board");
        submitButton.setStyle("-fx-font-size: 16px;");
        submitButton.setOnAction(e -> handleSubmit(primaryStage, toggleGroup));

        root.getChildren().addAll(titleLabel, boardSelectionLabel, board1, board2, board3, submitButton);

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createBoardOption(String boardName, String imagePath, ToggleGroup toggleGroup) {
        RadioButton radioButton = new RadioButton(boardName);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(Files.newInputStream(Paths.get(imagePath))));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox vbox = new VBox(10, radioButton, imageView);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    private void handleSubmit(Stage primaryStage, ToggleGroup toggleGroup) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton == null) {
            showAlert(AlertType.ERROR, "Error", "You must select a board.");
            return;
        }

        String selectedBoard = selectedRadioButton.getText();
        String jsonFilePath = "src/main/resources/boards/json/" + selectedBoard + ".json";

        // Create BoardDTO and set the properties
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setName(selectedBoard);

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
