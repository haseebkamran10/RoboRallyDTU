package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.service.ApiService;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CreatePlayerView extends Application {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApplicationContext context;

    private Stage startPageStage;

    @Override
    public void start(Stage primaryStage) {
        this.startPageStage = primaryStage;

        primaryStage.setTitle("Create Player");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 30;");

        Label titleLabel = new Label("Create a New Player");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        titleLabel.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Player Name:");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        TextField nameField = new TextField();
        nameField.setMaxWidth(250);
        nameField.setAlignment(Pos.CENTER);

        Label avatarLabel = new Label("Avatar:");
        avatarLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        ComboBox<HBox> avatarComboBox = new ComboBox<>();
        avatarComboBox.setMaxWidth(250);
        avatarComboBox.getItems().addAll(
                createAvatarOption("BlackAvatar", "/avatars/BlackAvatar.png"),
                createAvatarOption("BlueAvatar", "/avatars/BlueAvatar.png"),
                createAvatarOption("GreenAvatar", "/avatars/GreenAvatar.png"),
                createAvatarOption("PurpleAvatar", "/avatars/PurpleAvatar.png"),
                createAvatarOption("RedAvatar", "/avatars/RedAvatar.png"),
                createAvatarOption("YellowAvatar", "/avatars/YellowAvatar.png")
        );

        avatarComboBox.setButtonCell(createListCell());
        avatarComboBox.setCellFactory(listView -> createListCell());

        Button submitButton = new Button("Create Player");
        submitButton.setStyle("-fx-font-size: 16px;");
        submitButton.setOnAction(e -> handleSubmit(primaryStage, nameField, avatarComboBox));

        root.getChildren().addAll(titleLabel, nameLabel, nameField, avatarLabel, avatarComboBox, submitButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createAvatarOption(String name, String imagePath) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        Label label = new Label(name);
        label.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
        HBox hBox = new HBox(10, imageView, label);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return hBox;
    }

    private javafx.scene.control.ListCell<HBox> createListCell() {
        return new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                }
            }
        };
    }

    private void handleSubmit(Stage primaryStage, TextField nameField, ComboBox<HBox> avatarComboBox) {
        String playerName = nameField.getText();
        HBox selectedAvatarBox = avatarComboBox.getValue();

        if (playerName == null || playerName.isEmpty() || selectedAvatarBox == null) {
            showAlert(AlertType.ERROR, "Error", "Player name and avatar are required.");
            return;
        }

        String avatar = ((Label) selectedAvatarBox.getChildren().get(1)).getText();

        Player player = new Player();
        player.setName(playerName);
        player.setAvatar(avatar);

        try {
            apiService.createPlayer(player);
            showAlert(AlertType.INFORMATION, "Player Created",
                    "Player created successfully!", primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to create player.");
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
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
