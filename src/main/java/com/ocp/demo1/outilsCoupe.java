package com.ocp.demo1;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class outilsCoupe implements Initializable {

    @FXML
    private TableView<PiecesSearch> piecesTableView;
    @FXML
    private TableColumn<PiecesSearch, String> codeTableColumn;
    @FXML
    private TableColumn<PiecesSearch, String> referenceTableColumn;
    @FXML
    private TableColumn<PiecesSearch, String> nameTableColumn;
    @FXML
    private TableColumn<PiecesSearch, String> typeTableColumn;
    @FXML
    private TableColumn<PiecesSearch, String> unityTableColumn;
    @FXML
    private TableColumn<PiecesSearch, String> emplacementTableColumn;
    @FXML
    private TableColumn<PiecesSearch, Integer> nombreTableColumn;
    @FXML
    private TableColumn<PiecesSearch, String> descriptionTableColumn;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField SearchField;
    @FXML
    private MenuButton menu;

    private AtomicReference<String> selectedType = new AtomicReference<>("");

    ObservableList<PiecesSearch> PiecesSearchObservableList = FXCollections.observableArrayList();
    Database connectNow = new Database();
    Connection connectDB = connectNow.connect();

    public void setUser(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String pieceView = "select code,reference,nom,type,nombre,description,emplacement,unité from pieces where type='outil de coupe'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(pieceView);
            while (resultSet.next()) {
                String QueryCode = resultSet.getString("code");
                String QueryReference = resultSet.getString("reference");
                String QueryName = resultSet.getString("nom");
                String QueryType = resultSet.getString("type");
                String QueryEmplacement = resultSet.getString("emplacement");
                String QueryUnity = resultSet.getString("unité");
                Integer QueryNombre = resultSet.getInt("nombre");
                String QueryDescription = resultSet.getString("description");

                PiecesSearchObservableList.add(new PiecesSearch(QueryCode, QueryReference, QueryName, QueryNombre, QueryDescription, QueryType, QueryEmplacement, QueryUnity));
            }

            MenuItem item1 = new MenuItem("code");
            MenuItem item2 = new MenuItem("reference");
            MenuItem item3 = new MenuItem("nom");
            MenuItem item4 = new MenuItem("type");
            MenuItem item5 = new MenuItem("quantité");
            MenuItem item6 = new MenuItem("description");
            MenuItem item7 = new MenuItem("emplacement");
            MenuItem item8 = new MenuItem("unité");

            menu.getItems().addAll(item1, item2, item3, item4, item5, item6, item7, item8);

            // Adding event handlers to update the selectedType when an item is selected
            for (MenuItem item : menu.getItems()) {
                item.setOnAction(event -> {
                    selectedType.set(item.getText());
                    menu.setText(selectedType.get()); // Update the MenuButton text to show the selected item
                });
            }

            codeTableColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
            referenceTableColumn.setCellValueFactory(new PropertyValueFactory<>("reference"));
            nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            unityTableColumn.setCellValueFactory(new PropertyValueFactory<>("unité"));
            emplacementTableColumn.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
            nombreTableColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            piecesTableView.setItems(PiecesSearchObservableList);
            FilteredList<PiecesSearch> filteredData = new FilteredList<>(PiecesSearchObservableList, b -> true);

            SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(piece -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // If no filter is set, show all items.
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    // Check for null in getType and other fields as necessary.
                    if ("code".equals(selectedType.get()) && piece.getCode() != null) {
                        return piece.getCode().toLowerCase().contains(lowerCaseFilter);
                    } else if ("reference".equals(selectedType.get()) && piece.getReference() != null) {
                        return piece.getReference().toLowerCase().contains(lowerCaseFilter);
                    } else if ("nom".equals(selectedType.get()) && piece.getNom() != null) {
                        return piece.getNom().toLowerCase().contains(lowerCaseFilter);
                    } else if ("type".equals(selectedType.get()) && piece.getType() != null) {
                        return piece.getType().toLowerCase().contains(lowerCaseFilter);
                    } else if ("nombre".equals(selectedType.get()) && piece.getNombre() != null) {
                        return piece.getNombre().toString().contains(lowerCaseFilter);
                    } else if ("description".equals(selectedType.get()) && piece.getDescription() != null) {
                        return piece.getDescription().toLowerCase().contains(lowerCaseFilter);
                    } else if ("unité".equals(selectedType.get()) && piece.getUnité() != null) {
                        return piece.getUnité().toLowerCase().contains(lowerCaseFilter);
                    } else if ("emplacement".equals(selectedType.get()) && piece.getEmplacement() != null) {
                        return piece.getEmplacement().toLowerCase().contains(lowerCaseFilter);
                    }
                    return false; // Default case if none of the fields match the filter or are null.
                });
            });

            // Wrap the FilteredList in a SortedList.
            SortedList<PiecesSearch> sortedData = new SortedList<>(filteredData);

            // Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(piecesTableView.comparatorProperty());

            // Add sorted and filtered data to the table.
            piecesTableView.setItems(sortedData);
            setupContextMenu();

        } catch (SQLException e) {
            Logger.getLogger(PiecesSearchController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHome() {
        Stage stage = (Stage) piecesTableView.getScene().getWindow(); // Use the TableView component
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
        Stage stage = (Stage) piecesTableView.getScene().getWindow(); // Use the TableView component
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

    @FXML
    private void loadConsommables(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/consommables.fxml"));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        stage.show();
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
    private void handleAdd() {
        Stage stage = (Stage) piecesTableView.getScene().getWindow(); // Use the TableView component
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/formulaire.fxml"));
            // Do not set the controller here since it is already set in the FXML
            Parent root = loader.load();
            stage.getScene().setRoot(root);
            stage.show();
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

    private void setupContextMenu() {
        piecesTableView.setRowFactory(tv -> {
            TableRow<PiecesSearch> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addMenuItem = new MenuItem("Add");

            MenuItem optionsMenuItem = new MenuItem("Options");
            MenuItem emplacementMenuItem = new MenuItem("Change Emplacement ");

            // Ajouter un dialogue pour choisir la quantité
            addMenuItem.setOnAction(event -> {
                TextInputDialog dialog = new TextInputDialog("1");
                dialog.setTitle("Choose Quantity");
                dialog.setHeaderText("Enter the quantity to add:");
                dialog.setContentText("Quantity:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(quantity -> {
                    try {
                        int qty = Integer.parseInt(quantity);
                        if (qty > 0) {
                            updateQuantity(row.getItem(), qty);
                        } else {
                            showAlert("Invalid Quantity", "Please enter a positive number.");
                        }
                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter a valid number.");
                    }
                });
            });

            optionsMenuItem.setOnAction(event -> showItemDetails(row.getItem()));
            emplacementMenuItem.setOnAction(event -> changeEmplacement(row.getItem()));
            contextMenu.getItems().addAll(addMenuItem, optionsMenuItem, emplacementMenuItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu));
            return row;
        });
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void changeEmplacement(PiecesSearch item) {
        if (item == null) return;
        TextInputDialog dialog = new TextInputDialog(item.getEmplacement());
        dialog.setTitle("Change Emplacement");
        dialog.setHeaderText("Update Emplacement for " + item.getNom());
        dialog.setContentText("Enter new emplacement:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newEmplacement -> updateEmplacementInDatabase(item, newEmplacement));
    }

    private void updateEmplacementInDatabase(PiecesSearch item, String newEmplacement) {
        if (!newEmplacement.trim().isEmpty() && !newEmplacement.equals(item.getEmplacement())) {
            String updateQuery = "UPDATE pieces SET emplacement = ? WHERE code = ?";
            try (PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, newEmplacement.trim());
                preparedStatement.setString(2, item.getCode());
                preparedStatement.executeUpdate();
                Platform.runLater(() -> {
                    item.setEmplacement(newEmplacement.trim());
                    piecesTableView.refresh();
                });
            } catch (SQLException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating emplacement", e);
            }
        }
    }
    private void updateQuantity(PiecesSearch item, int quantityChange) {
        if (item == null || (item.getNombre() + quantityChange) < 0) {
            return; // Exit if the item is null or the resulting quantity would be negative.
        }

        String updateQuery = "UPDATE pieces SET nombre = nombre + ? WHERE code = ?";
        String insertPieceQuery = "INSERT INTO piece (code, numero, dae) VALUES (?, ?, ?)";
        String checkNumeroQuery = "SELECT MAX(numero) FROM piece"; // To find the highest 'numero' used

        while (quantityChange != 0) {
            final int change = (quantityChange > 0 ? 1 : -1); // Define a final variable for the change

            try {
                connectDB.setAutoCommit(false); // Start transaction

                // Update pieces table
                try (PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery)) {
                    preparedStatement.setInt(1, change);
                    preparedStatement.setString(2, item.getCode());
                    preparedStatement.executeUpdate();
                }

                if (quantityChange > 0) {
                    // Determine the new 'numero' value
                    int newNumero = 1; // Default to 1 if table is empty
                    try (Statement stmt = connectDB.createStatement();
                         ResultSet rs = stmt.executeQuery(checkNumeroQuery)) {
                        if (rs.next()) {
                            newNumero = rs.getInt(1) + 1; // Increment the highest 'numero' found
                        }
                    }

                    // Insert new record into 'piece'
                    try (PreparedStatement preparedStatement1 = connectDB.prepareStatement(insertPieceQuery)) {
                        preparedStatement1.setString(1, item.getCode());
                        preparedStatement1.setInt(2, newNumero);
                        preparedStatement1.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                        preparedStatement1.executeUpdate();
                    }
                }

                connectDB.commit(); // Commit transaction

                // Update the item in the TableView on the JavaFX Application Thread
                Platform.runLater(() -> {
                    item.setNombre(item.getNombre() + change);
                    piecesTableView.refresh();
                });

                quantityChange -= change; // Adjust quantityChange by 1 in the appropriate direction

            } catch (SQLException e) {
                try {
                    connectDB.rollback(); // Roll back the transaction on error
                } catch (SQLException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to roll back the transaction", ex);
                }
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error in updating item quantity or inserting new record", e);
                break; // Exit the loop on error
            } finally {
                try {
                    connectDB.setAutoCommit(true); // Reset default transaction behavior
                } catch (SQLException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to reset auto-commit", ex);
                }
            }
        }
    }

    private void showItemDetails(PiecesSearch item) {
        if (item == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocp/demo1/item_details.fxml"));
            Parent root = loader.load();
            ItemDetailsController controller = loader.getController();
            controller.setCurrentItem(item);
            Stage stage = new Stage();
            stage.setTitle("Item Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error showing item details", e);
        }
    }
}
