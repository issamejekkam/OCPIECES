package com.ocp.demo1;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PiecesSearchController implements Initializable {
    @FXML
    private TableView<PiecesSearch> piecesTableView;
    @FXML
    private TableColumn<PiecesSearch,String> codeTableColumn;
    @FXML
    private TableColumn<PiecesSearch,String> referenceTableColumn;
    @FXML
    private TableColumn<PiecesSearch,String> nameTableColumn;
    @FXML
    private TableColumn<PiecesSearch,String> typeTableColumn;
    @FXML
    private TableColumn<PiecesSearch,Integer> nombreTableColumn;
    @FXML
    private TableColumn<PiecesSearch,String> descriptionTableColumn;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField SearchField;


    ObservableList<PiecesSearch> PiecesSearchObservableList = FXCollections.observableArrayList();


    public void setUser(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }
    @Override
    public  void  initialize(URL url, ResourceBundle rb) {
    Database connectNow= new Database();
    Connection connectDB = connectNow.connect();
    String pieceView= "select code,reference,nom,type,nombre,description from pieces";
    try{
        Statement statement = connectDB.createStatement();
        ResultSet resultSet = statement.executeQuery(pieceView);
        while (resultSet.next()){
            String QueryCode = resultSet.getString("code");
            String QueryReference = resultSet.getString("reference");
            String QueryName = resultSet.getString("nom");
            String QueryType = resultSet.getString("type");
            Integer QueryNombre = resultSet.getInt("nombre");
            String QueryDescription = resultSet.getString("description");

            PiecesSearchObservableList.add(new PiecesSearch(QueryCode,QueryReference,QueryName,QueryNombre,QueryDescription,QueryType));

        }

        codeTableColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        referenceTableColumn.setCellValueFactory(new PropertyValueFactory<>("reference"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        nombreTableColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        piecesTableView.setItems(PiecesSearchObservableList);
        FilteredList<PiecesSearch> filteredData = new FilteredList<>(PiecesSearchObservableList, b -> true);

        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(piece -> {
                // If filter text is empty, display all parts.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare lower case names with the filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (piece.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                } else if (piece.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches code.
                }
                return false; // Does not match.
            });
        });

        // Wrap the FilteredList in a SortedList.
        SortedList<PiecesSearch> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(piecesTableView.comparatorProperty());

        // Add sorted and filtered data to the table.
        piecesTableView.setItems(sortedData);


    } catch (SQLException e){
        Logger.getLogger(PiecesSearchController.class.getName()).log(Level.SEVERE,null,e);
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


}
