package com.ocp.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;

public class formulaireController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField Code;
    @FXML
    private TextField Reference;
    @FXML
    private TextField Nom;
    @FXML
    private MenuButton Type;
    @FXML
    private TextField Description;
    @FXML
    private TextField Unity;
    @FXML
    private TextField Emplacement;

    private AtomicReference<String> selectedType = new AtomicReference<>("");

    @FXML
    public void initialize() {
        // Initialize the MenuButton items
        MenuItem item1 = new MenuItem("Article Consommable");
        MenuItem item2 = new MenuItem("pièce de rechange");
        MenuItem item3 = new MenuItem("outillage collectif");
        MenuItem item4 = new MenuItem("outil de coupe");

        Type.getItems().addAll(item1, item2, item3, item4);

        // Adding event handlers to update the selectedType when an item is selected
        for (MenuItem item : Type.getItems()) {
            item.setOnAction(event -> {
                selectedType.set(item.getText());
                Type.setText(selectedType.get()); // Update the MenuButton text to show the selected item
            });
        }
    }

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
    private void loadCoupe(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/outilsCoupe.fxml"));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        stage.show();
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

    @FXML
    public void formulaireAdd() {
        String checkSql = "SELECT nombre FROM pieces WHERE code = ? AND reference = ?";
        String insertSql = "INSERT INTO pieces (code, reference, nom, type, description, nombre,unité,emplacement) VALUES (?, ?, ?, ?, ?, 1,?,?)";
        String insertPieceQuery = "INSERT INTO piece (code, numero, dae) VALUES (?, ?, ?)";
        String updateSql = "UPDATE pieces SET nombre = nombre + 1 WHERE code = ? AND reference = ?";
        String checkNumeroQuery = "SELECT MAX(numero) FROM piece";
        try (Connection conn = Database.connect()) {
            // First, check if the item already exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, Code.getText());

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // If exists, update the existing record
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, Code.getText());
                        updateStmt.executeUpdate();
                        showAlert(Alert.AlertType.INFORMATION, "Updated", "Quantité incrementée pour l'item existant");
                    }
                } else {
                    // If not exists, insert a new record
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, Code.getText());
                        insertStmt.setString(2, Reference.getText());
                        insertStmt.setString(3, Nom.getText());
                        insertStmt.setString(4, selectedType.get());
                        insertStmt.setString(5, Description.getText());
                        insertStmt.setString(6, Unity.getText());
                        insertStmt.setString(7, Emplacement.getText());
                        int newNumero = 1;  // Default to 1 if table is empty
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs1 = stmt.executeQuery(checkNumeroQuery)) {
                            if (rs1.next()) {
                                newNumero = rs1.getInt(1) + 1;  // Increment the highest 'numero' found
                            }
                        }

                        // Insert new record into 'piece'
                        try (PreparedStatement preparedStatement1 = conn.prepareStatement(insertPieceQuery)) {
                            preparedStatement1.setString(1, Code.getText());
                            preparedStatement1.setInt(2, newNumero);
                            preparedStatement1.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                            preparedStatement1.executeUpdate();
                        }
                        insertStmt.executeUpdate();
                        showAlert(Alert.AlertType.CONFIRMATION, "Added", "Confirmer l'ajout de la pièce");
                    }

                }
            }
            // Clear fields
            Code.setText("");
            Reference.setText("");
            Nom.setText("");
            Type.setText("Type"); // Reset MenuButton to default text
            Description.setText("");
            Unity.setText("");
            Emplacement.setText("");
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
