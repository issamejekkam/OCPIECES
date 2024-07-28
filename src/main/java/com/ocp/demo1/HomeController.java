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

    @FXML

    private void loadHome(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/home.fxml"));
        loader.setControllerFactory(c -> this); // Ensures the current instance is used
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        stage.show();
    }


    @FXML
    private void loadConsommables(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/consommables.fxml"));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        stage.show();
    }
    @FXML
    private void loadCoupe(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/outilsCoupe.fxml"));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        stage.show();
    }


    @FXML
    private void loadCollectif(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/outillagesCollectifs.fxml"));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        stage.show();
    }
    @FXML
    private void handleCollectif() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        try {
            loadCollectif(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadrechange(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/piecesRechange.fxml"));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        stage.show();
    }
    @FXML
    private void handlerechange() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        try {
            loadrechange(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    private void handleConsommables() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        try {
            loadConsommables(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleCoupe() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        try {
            loadCoupe(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
