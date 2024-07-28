package com.ocp.demo1;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemDetailsController implements Initializable {
    @FXML private Label codeLabel, nameLabel, referenceLabel, typeLabel, emplacementLabel, quantityLabel, descriptionLabel, unityLabel;
    @FXML private TableView<PieceOptions> pieceTableView;
    @FXML private TableColumn<PieceOptions, String> codeTableColumn, dcpTableColumn, chargéTableColumn, décisionTableColumn;
    @FXML private TableColumn<PieceOptions, Integer> numeroTableColumn;
    @FXML private TableColumn<PieceOptions, LocalDate> daeTableColumn, dasTableColumn, dfcTableColumn;
    @FXML private TableColumn<PieceOptions, Void> editColumn;

    private ObservableList<PieceOptions> pieceOptionsObservableList = FXCollections.observableArrayList();
    private Database connectNow = new Database();
    private Connection connectDB = connectNow.connect();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupEditColumn();
        setupContextMenu();
    }

    private void setupTableColumns() {
        codeTableColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        numeroTableColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        daeTableColumn.setCellValueFactory(new PropertyValueFactory<>("dae"));
        dasTableColumn.setCellValueFactory(new PropertyValueFactory<>("das"));
        dcpTableColumn.setCellValueFactory(new PropertyValueFactory<>("dateControlePeriodique"));
        dfcTableColumn.setCellValueFactory(new PropertyValueFactory<>("dateFuturControle"));
        chargéTableColumn.setCellValueFactory(new PropertyValueFactory<>("chargeControle"));
        décisionTableColumn.setCellValueFactory(new PropertyValueFactory<>("decisionReforme"));
    }

    private void setupEditColumn() {
        editColumn.setCellFactory(param -> new TableCell<PieceOptions, Void>() {
            private final Button btn = new Button("Edit");
            {
                btn.setOnAction(event -> {
                    PieceOptions data = getTableView().getItems().get(getIndex());
                    openEditDialog(data);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void setupContextMenu() {
        pieceTableView.setRowFactory(tv -> {
            TableRow<PieceOptions> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem removeMenuItem = new MenuItem("Remove");
            removeMenuItem.setOnAction(event -> {
                PieceOptions selectedItem = row.getItem();
                if (selectedItem != null) {
                    updateDateOfExit(selectedItem);

                }
            });
            contextMenu.getItems().add(removeMenuItem);
            row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
            return row;
        });
    }

    private void updateDateOfExit(PieceOptions piece) {
        piece.setDas(LocalDate.now());
        try (PreparedStatement stmt = connectDB.prepareStatement("UPDATE piece SET das = ? WHERE numero = ?")) {
            stmt.setDate(1, Date.valueOf(piece.getDas()));
            stmt.setString(2, piece.getNumero());
            stmt.executeUpdate();
            loadData(piece.getCode());
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating date of exit in database", e);
        }
    }

    private void openEditDialog(PieceOptions data) {
        Dialog<PieceOptions> dialog = new Dialog<>();
        dialog.setTitle("Edit Piece");
        dialog.setHeaderText("Edit the details of the piece:");
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Save", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        setupDialogFields(grid, data);
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> grid.getChildren().get(0).requestFocus());

        dialog.setResultConverter(dialogButton -> dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE ? updatePieceOptions(data, grid) : null);
        dialog.showAndWait();
    }

    private void setupDialogFields(GridPane grid, PieceOptions data) {
        TextField codeField = new TextField(data.getCode());
        TextField numeroField = new TextField(data.getNumero());
        TextField chargeControleField = new TextField(data.getChargeControle());
        TextField dateControlePeriodiqueField = new TextField(data.getDateControlePeriodique());
        TextField decisionReformeField = new TextField(data.getDecisionReforme());
        DatePicker dateFuturControlePicker = new DatePicker(data.getDateFuturControle());

        grid.addRow(0, new Label("Code:"), codeField);
        grid.addRow(1, new Label("Numero:"), numeroField);
        grid.addRow(2, new Label("Charge de controle:"), chargeControleField);
        grid.addRow(3, new Label("Date Controle periodique:"), dateControlePeriodiqueField);
        grid.addRow(4, new Label("Decision de reforme:"), decisionReformeField);
        grid.addRow(5, new Label("Date de futur controle:"), dateFuturControlePicker);
    }

    private PieceOptions updatePieceOptions(PieceOptions pieceOptions, GridPane grid) {
        pieceOptions.setCode(((TextField) grid.getChildren().get(1)).getText());
        pieceOptions.setNumero(((TextField) grid.getChildren().get(3)).getText());
        pieceOptions.setChargeControle(((TextField) grid.getChildren().get(5)).getText());
        pieceOptions.setDateControlePeriodique(((TextField) grid.getChildren().get(7)).getText());
        pieceOptions.setDecisionReforme(((TextField) grid.getChildren().get(9)).getText());
        pieceOptions.setDateFuturControle(((DatePicker) grid.getChildren().get(11)).getValue());

        try (PreparedStatement stmt = connectDB.prepareStatement(
                "UPDATE piece SET code = ?, numero = ?, charge_du_controle = ?, decision_du_reforme = ?, date_du_controle_periodique = ?, date_de_futur_controle = ? WHERE numero = ?")) {
            stmt.setString(1, pieceOptions.getCode());
            stmt.setString(2, pieceOptions.getNumero());
            stmt.setString(3, pieceOptions.getChargeControle());
            stmt.setString(4, pieceOptions.getDecisionReforme());
            stmt.setString(5, pieceOptions.getDateControlePeriodique());
            stmt.setDate(6, pieceOptions.getDateFuturControle() != null ? Date.valueOf(pieceOptions.getDateFuturControle()) : null);
            stmt.setString(7, pieceOptions.getNumero());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating piece details in database", e);
        }
        return pieceOptions;
    }
    private void loadData(String itemCode) {
        if (itemCode == null) return; // Guard against null codes

        String pieceView = "SELECT code, numero, dae, das, date_du_controle_periodique, date_de_futur_controle, charge_du_controle, decision_du_reforme FROM piece WHERE code = ?";
        try (PreparedStatement preparedStatement = connectDB.prepareStatement(pieceView)) {
            preparedStatement.setString(1, itemCode); // Set the item code
            ResultSet resultSet = preparedStatement.executeQuery();

            pieceOptionsObservableList.clear();
            while (resultSet.next()) {
                LocalDate dae = resultSet.getDate("dae") != null ? resultSet.getDate("dae").toLocalDate() : null;
                LocalDate das = resultSet.getDate("das") != null ? resultSet.getDate("das").toLocalDate() : null;
                LocalDate dateFuturControle = resultSet.getDate("date_de_futur_controle") != null ? resultSet.getDate("date_de_futur_controle").toLocalDate() : null;
                String dateControlePeriodique = resultSet.getString("date_du_controle_periodique");

                pieceOptionsObservableList.add(new PieceOptions(
                        resultSet.getString("code"),
                        resultSet.getString("numero"),
                        dae,
                        das,
                        dateControlePeriodique,
                        dateFuturControle,
                        resultSet.getString("charge_du_controle"),
                        resultSet.getString("decision_du_reforme")
                ));
            }
            pieceTableView.setItems(pieceOptionsObservableList);
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading data from database", e);
        }
    }

    public void setCurrentItem(PiecesSearch item) {
        if (item != null) {
            codeLabel.setText(item.getCode());
            nameLabel.setText(item.getNom());
            referenceLabel.setText(item.getReference());
            typeLabel.setText(item.getType());
            emplacementLabel.setText(item.getEmplacement());
            quantityLabel.setText(String.valueOf(item.getNombre()));
            descriptionLabel.setText(item.getDescription());
            unityLabel.setText(item.getUnité());
            loadData(item.getCode());
        }
    }
}
