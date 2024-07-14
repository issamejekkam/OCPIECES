package com.ocp.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @FXML
    private Label welcomeLabel;

    public void setUser(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    public void loadHome(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/home.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        stage.show();
    }


    private void backLogin(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow(); // Get current stage
        backLogin(stage);
    }

    @FXML
    private void handleHome() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow(); // Get current stage
        try {
            loadHome(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
