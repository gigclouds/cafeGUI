/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafegui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author anisp
 */
public class LoginScene {

    private Stage primaryStage;
    private Scene mainScene;

    public LoginScene(Stage primaryStage, Scene mainScene) {
        this.primaryStage = primaryStage;
        this.mainScene = mainScene;
    }

    public Scene createScene() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(50));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Restaurant Login");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 8;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8;"));

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Simple hardcoded login for demonstration
            if (username.equals("admin") && password.equals("admin123")) {
                primaryStage.setScene(mainScene);
                primaryStage.setTitle("Restaurant Ordering System");
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        });

        layout.getChildren().addAll(title, usernameField, passwordField, loginButton);

        return new Scene(layout, 1000, 700);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}