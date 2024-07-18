package com.ocp.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import javafx.scene.control.TextField;
import java.sql.*;



public class formulaireController {
    @FXML
    private Label welcomeLabel;
    @FXML private TextField Code;
    @FXML private TextField Reference;
    @FXML private TextField Nom;
    @FXML private TextField Description;


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
    public void formulaireAdd() {
        String checkSql = "SELECT nombre FROM pieces WHERE code = ? AND reference = ?";
        String insertSql = "INSERT INTO pieces (code, reference, nom, description, nombre) VALUES (?, ?, ?, ?, 1)";
        String updateSql = "UPDATE pieces SET nombre = nombre + 1 WHERE code = ? AND reference = ?";

        try (Connection conn = Database.connect()) {
            // First, check if the item already exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, Code.getText());
                checkStmt.setString(2, Reference.getText());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // If exists, update the existing record
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, Code.getText());
                        updateStmt.setString(2, Reference.getText());
                        updateStmt.executeUpdate();
                        showAlert(Alert.AlertType.INFORMATION, "Updated", "Quantité incrementée pour l'item existant");
                    }
                } else {
                    // If not exists, insert a new record
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, Code.getText());
                        insertStmt.setString(2, Reference.getText());
                        insertStmt.setString(3, Nom.getText());
                        insertStmt.setString(4, Description.getText());
                        insertStmt.executeUpdate();
                        showAlert(Alert.AlertType.CONFIRMATION, "Added", "Confirmer l'ajout de la pièce");
                    }
                }
            }
            // Clear fields
            Code.setText("");
            Reference.setText("");
            Nom.setText("");
            Description.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Erreur lors de l'ajout/modification de la pièce: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
