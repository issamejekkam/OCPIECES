package com.ocp.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class ConsommablesController {

    @FXML
    private TableView<?> consommablesTable;

    @FXML
    private void handleHome() {
        Stage stage = (Stage) consommablesTable.getScene().getWindow(); // Use the TableView component
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/home.fxml"));
            // Do not set the controller here since it is already set in the FXML
            Parent root = loader.load();
            stage.getScene().setRoot(root);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) consommablesTable.getScene().getWindow(); // Use the TableView component
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/login.fxml"));
            // Do not set the controller here since it is already set in the FXML
            Parent root = loader.load();
            stage.getScene().setRoot(root);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
