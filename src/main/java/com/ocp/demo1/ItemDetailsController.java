package com.ocp.demo1;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemDetailsController implements Initializable {
    @FXML private Label codeLabel, nameLabel, referenceLabel, typeLabel, emplacementLabel, quantityLabel, descriptionLabel, unityLabel, disponibleLabel;
    @FXML private TableView<PieceOptions> pieceTableView;
    @FXML private TableColumn<PieceOptions, String> codeTableColumn, dcpTableColumn, chargéTableColumn, décisionTableColumn,ChargeDaeProvProvTableColumn, ChargeDasProvProvTableColumn;
    @FXML private TableColumn<PieceOptions, Integer> numeroTableColumn;
    @FXML private TableColumn<PieceOptions, LocalDate> daeTableColumn, dasTableColumn, dfcTableColumn,daeProvTableColumn,dasProvTableColumn;
    @FXML private TableColumn<PieceOptions, Void> editColumn;
    @FXML private TextField SearchField;
    @FXML private MenuButton menu;
    private AtomicReference<String> selectedType = new AtomicReference<>("");

    private ObservableList<PieceOptions> pieceOptionsObservableList = FXCollections.observableArrayList();
    private Database connectNow = new Database();
    private Connection connectDB = connectNow.connect();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupContextMenu();
        setupEditColumn();
    }

    private void setupTableColumns() {
        MenuItem item1 = new MenuItem("Date d'entrée");
        MenuItem item2 = new MenuItem("Date de sortie");
        MenuItem item3 = new MenuItem("date de controle périodique");
        MenuItem item4 = new MenuItem("date de futur controle");
        MenuItem item7 = new MenuItem("Décision du réforme");
        MenuItem item8 = new MenuItem("chargé du controle");


            MenuItem item5 = new MenuItem("Date de dernière entrée");
            MenuItem item6 = new MenuItem("Date de sortie provisoire");
            MenuItem item9 = new MenuItem("Chargé de la sortie provisoire");
            MenuItem item10 = new MenuItem("Chargé de la dernière entrée");
            menu.getItems().addAll(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10);


        for (MenuItem item : menu.getItems()) {
            item.setOnAction(event -> {
                selectedType.set(item.getText());
                menu.setText(selectedType.get());
            });
        }

        codeTableColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        numeroTableColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        daeTableColumn.setCellValueFactory(new PropertyValueFactory<>("dae"));
        dasTableColumn.setCellValueFactory(new PropertyValueFactory<>("das"));
        dcpTableColumn.setCellValueFactory(new PropertyValueFactory<>("dateControlePeriodique"));
        dfcTableColumn.setCellValueFactory(new PropertyValueFactory<>("dateFuturControle"));
        chargéTableColumn.setCellValueFactory(new PropertyValueFactory<>("chargeControle"));
        décisionTableColumn.setCellValueFactory(new PropertyValueFactory<>("decisionReforme"));
    }

    private boolean isConditionalType() {
        if("outil de coupe".equals(typeLabel.getText()) || "outillage collectif".equals(typeLabel.getText()))
        {System.out.println("yes");
        return true;}

        else{ return false;}
    }

    private void addConditionalColumns() {
        if (isConditionalType() && !pieceTableView.getColumns().contains(daeProvTableColumn)) {
            TableColumn<PieceOptions, LocalDate> daeProvColumn = new TableColumn<>("Date de dernière entrée");
            daeProvColumn.setCellValueFactory(new PropertyValueFactory<>("daeProvisoire"));
            pieceTableView.getColumns().add(daeProvColumn);

            TableColumn<PieceOptions, String> chargeDaeProvColumn = new TableColumn<>("Chargé de la sortie provisoire");
            chargeDaeProvColumn.setCellValueFactory(new PropertyValueFactory<>("chargeDaeProvisoire"));
            pieceTableView.getColumns().add(chargeDaeProvColumn);

            TableColumn<PieceOptions, LocalDate> dasProvColumn = new TableColumn<>("Date de sortie provisoire");
            dasProvColumn.setCellValueFactory(new PropertyValueFactory<>("dasProvisoire"));
            pieceTableView.getColumns().add(dasProvColumn);

            TableColumn<PieceOptions, String> chargeDasProvColumn = new TableColumn<>("Chargé de la dernière entrée");
            chargeDasProvColumn.setCellValueFactory(new PropertyValueFactory<>("chargeDasProvisoire"));
            pieceTableView.getColumns().add(chargeDasProvColumn);
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
            disponibleLabel.setText(String.valueOf(countItemsWithoutDAS(item.getCode())));
            addConditionalColumns();
            loadData(item.getCode());
        }
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
            MenuItem removeMenuItem = new MenuItem("sortir définitivement");

            if (isConditionalType()) {
                MenuItem removeProvMenuItem = new MenuItem("sortir provisoirement");
                MenuItem retourMenuItem = new MenuItem("Retour");

                removeProvMenuItem.setOnAction(event -> showChargeDialog(row.getItem(), true));
                retourMenuItem.setOnAction(event -> showChargeDialog(row.getItem(), false));

                contextMenu.getItems().addAll(removeProvMenuItem, retourMenuItem);
            }

            removeMenuItem.setOnAction(event -> {
                PieceOptions selectedItem = row.getItem();
                if (selectedItem != null) {
                    updateDateOfExit(selectedItem);
                    refreshPage(selectedItem.getCode());
                }
            });

            contextMenu.getItems().add(removeMenuItem);
            row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
            return row;
        });
    }

    public void updateDateOfExit(PieceOptions piece) {
        piece.setDas(LocalDate.now());
        LocalDate das = piece.getDas();

        if (das != null) {

            try (PreparedStatement stmt = connectDB.prepareStatement("UPDATE piece SET das = ? WHERE numero = ?")) {
                stmt.setDate(1, Date.valueOf(piece.getDas()));
                stmt.setString(2, piece.getNumero());
                stmt.executeUpdate();
            } catch (SQLException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating date of provisional exit in database", e);
            }
        } else {
            // Handle the case where dasProvisoire is null
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Attempted to update exit date with null value");
        }
    }

    // Update the date of provisional exit for an item
    private void updateDateOfExitProv(PieceOptions piece) {
        piece.setDasProvisoire(LocalDate.now());
        LocalDate dasProvisoire = piece.getDasProvisoire();
        if (dasProvisoire != null) {
            if(piece.getDas()==null){

                try (PreparedStatement stmt = connectDB.prepareStatement("UPDATE piece SET dasProvisoire  = ?,daeProvisoire=NULL WHERE numero = ?")) {
                    stmt.setDate(1, Date.valueOf(piece.getDasProvisoire()));
                    stmt.setString(2, piece.getNumero());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating date of provisional exit in database", e);
                }
            }} else {
            // Handle the case where dasProvisoire is null
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Attempted to update provisional exit date with null value");
        }
    }


    // Update the date of return for an item
    private void updateDateOfRetourProv(PieceOptions piece) {
        piece.setDaeProvisoire(LocalDate.now());
        LocalDate daeProvisoire = piece.getDaeProvisoire();
        if (daeProvisoire != null) {
            if(piece.getDas()==null){
                try (PreparedStatement stmt = connectDB.prepareStatement("UPDATE piece SET daeProvisoire  = ?,dasProvisoire=NULL WHERE numero = ?")) {
                    stmt.setDate(1, Date.valueOf(piece.getDasProvisoire()));
                    stmt.setString(2, piece.getNumero());
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating date of provisional exit in database", e);
                }
            }} else {
            // Handle the case where dasProvisoire is null
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Attempted to update provisional entry date with null value");
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
        refreshPage(data.getCode());
    }

    private void setupDialogFields(GridPane grid, PieceOptions data) {
        TextField chargeControleField = new TextField(data.getChargeControle());
        TextField dateControlePeriodiqueField = new TextField(data.getDateControlePeriodique());

        // Utilisation de ComboBox pour la décision de réforme
        ComboBox<String> decisionReformeComboBox = new ComboBox<>();
        decisionReformeComboBox.getItems().addAll("positive", "negative");
        decisionReformeComboBox.setValue(data.getDecisionReforme());  // Set initial value based on data

        DatePicker dateFuturControlePicker = new DatePicker(data.getDateFuturControle());

        grid.addRow(2, new Label("Charge de controle:"), chargeControleField);
        grid.addRow(3, new Label("Date Controle periodique:"), dateControlePeriodiqueField);
        grid.addRow(4, new Label("Decision de reforme: "), decisionReformeComboBox);
        grid.addRow(5, new Label("Date de futur controle:"), dateFuturControlePicker);
    }




    private PieceOptions updatePieceOptions(PieceOptions pieceOptions, GridPane grid) {
        pieceOptions.setChargeControle(((TextField) grid.getChildren().get(1)).getText());
        pieceOptions.setDateControlePeriodique(((TextField) grid.getChildren().get(3)).getText());

        // Utilisation de ComboBox pour la décision de réforme
        pieceOptions.setDecisionReforme(((ComboBox<String>) grid.getChildren().get(5)).getValue());

        pieceOptions.setDateFuturControle(((DatePicker) grid.getChildren().get(7)).getValue());

        try (PreparedStatement stmt = connectDB.prepareStatement(
                "UPDATE piece SET charge_du_controle = ?, decision_du_reforme = ?, date_du_controle_periodique = ?, date_de_futur_controle = ? WHERE numero = ?")) {
            stmt.setString(1, pieceOptions.getChargeControle());
            stmt.setString(2, pieceOptions.getDecisionReforme());
            stmt.setString(3, pieceOptions.getDateControlePeriodique());
            stmt.setDate(4, pieceOptions.getDateFuturControle() != null ? Date.valueOf(pieceOptions.getDateFuturControle()) : null);
            stmt.setString(5, pieceOptions.getNumero());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating piece details in database", e);
        }
        return pieceOptions;
    }


    private void loadData(String itemCode) {
        if (itemCode == null) return;

        String pieceView = "SELECT code, numero, dae, das, date_du_controle_periodique, date_de_futur_controle, charge_du_controle, decision_du_reforme, daeProvisoire, dasProvisoire, chargedaeProvisoire, chargeDasProvisoire FROM piece WHERE code = ?";
        try (PreparedStatement preparedStatement = connectDB.prepareStatement(pieceView)) {
            preparedStatement.setString(1, itemCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            pieceOptionsObservableList.clear();
            while (resultSet.next()) {
                LocalDate dae = resultSet.getDate("dae") != null ? resultSet.getDate("dae").toLocalDate() : null;
                LocalDate das = resultSet.getDate("das") != null ? resultSet.getDate("das").toLocalDate() : null;
                LocalDate daeProvisoire = resultSet.getDate("daeProvisoire") != null ? resultSet.getDate("daeProvisoire").toLocalDate() : null;
                LocalDate dasProvisoire = resultSet.getDate("dasProvisoire") != null ? resultSet.getDate("dasProvisoire").toLocalDate() : null;
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
                        resultSet.getString("decision_du_reforme"),
                        daeProvisoire,
                        resultSet.getString("chargedaeProvisoire"),
                        dasProvisoire,
                        resultSet.getString("chargeDasProvisoire")
                ));
            }
            pieceTableView.setItems(pieceOptionsObservableList);
            setupSearchFilter();
            disponibleLabel.setText(String.valueOf(countItemsWithoutDAS(itemCode)));
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading data from database", e);
        }
    }

    private void setupSearchFilter() {
        FilteredList<PieceOptions> filteredData = new FilteredList<>(pieceOptionsObservableList, b -> true);

        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(piece -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                switch (selectedType.get()) {
                    case "Date d'entrée":
                        return piece.getDae() != null && piece.getDae().toString().toLowerCase().contains(lowerCaseFilter);
                    case "Date de sortie":
                        return piece.getDas() != null && piece.getDas().toString().toLowerCase().contains(lowerCaseFilter);
                    case "date de controle périodique":
                        return piece.getDateControlePeriodique() != null && piece.getDateControlePeriodique().toLowerCase().contains(lowerCaseFilter);
                    case "date de futur controle":
                        return piece.getDateFuturControle() != null && piece.getDateFuturControle().toString().toLowerCase().contains(lowerCaseFilter);
                    case "chargé du controle":
                        return piece.getChargeControle() != null && piece.getChargeControle().toLowerCase().contains(lowerCaseFilter);
                    case "Décision du réforme":
                        return piece.getDecisionReforme() != null && piece.getDecisionReforme().toLowerCase().contains(lowerCaseFilter);
                    case "Date de dernière entrée":
                        return piece.getDaeProvisoire() != null && piece.getDaeProvisoire().toString().toLowerCase().contains(lowerCaseFilter);
                    case "Date de sortie provisoire":
                        return piece.getDasProvisoire() != null && piece.getDasProvisoire().toString().toLowerCase().contains(lowerCaseFilter);
                    case "Chargé de la sortie provisoire":
                        return piece.getChargeDaeProvisoire() != null && piece.getChargeDaeProvisoire().toLowerCase().contains(lowerCaseFilter);
                    case "Chargé de la dernière entrée":
                        return piece.getChargeDasProvisoire() != null && piece.getChargeDasProvisoire().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return false;
                }
            });
        });

        pieceTableView.setItems(filteredData);
    }

    private int countItemsWithoutDAS(String itemCode) {
        String countQuery = "SELECT COUNT(*) FROM piece WHERE code = ? AND das IS NULL";
        try (PreparedStatement preparedStatement = connectDB.prepareStatement(countQuery)) {
            preparedStatement.setString(1, itemCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error counting items without DAS", e);
        }
        return 0;
    }

    private void refreshPage(String itemCode) {
        if (itemCode != null) {
            loadData(itemCode);
            disponibleLabel.setText(String.valueOf(countItemsWithoutDAS(itemCode)));
        }
    }

    private void showChargeDialog(PieceOptions piece, boolean isEntreeProvisoire) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(isEntreeProvisoire ? "Entree Provisoire" : "Retour");
        dialog.setHeaderText(isEntreeProvisoire ? "Choisir la charge pour entree provisoire:" : "Choisir la charge pour retour:");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField chargeField = new TextField();
        grid.add(new Label("Charge:"), 0, 0);
        grid.add(chargeField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(chargeField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return chargeField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(charge -> {
            if (isEntreeProvisoire) {
                updateProvisionalEntry(piece, charge);
            } else {
                updateProvisionalExit(piece, charge);
            }
            refreshPage(piece.getCode());
        });
    }

    private void updateProvisionalEntry(PieceOptions piece, String charge) {
        if (piece.getDas() == null) {
            piece.setChargeDaeProvisoire(charge);
            updateCharge(piece, "chargeDaeProvisoire","chargeDasProvisoire", piece.getChargeDaeProvisoire());
            updateDateOfExitProv(piece);
        }
    }

    private void updateProvisionalExit(PieceOptions piece, String charge) {
        if (piece.getDas() == null) {
            piece.setChargeDasProvisoire(charge);
            updateCharge(piece, "chargeDasProvisoire","chargeDaeProvisoire", piece.getChargeDasProvisoire());
            updateDateOfRetourProv(piece);
        }
    }

    private void updateCharge(PieceOptions piece, String column,String column2, String charge) {
        String query = String.format("UPDATE piece SET %s = ?, %s = NULL WHERE numero = ?", column,column2);
        try (PreparedStatement stmt = connectDB.prepareStatement(query)) {
            stmt.setString(1, charge);
            stmt.setString(2, piece.getNumero());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating charge in database", e);
        }
    }
}

