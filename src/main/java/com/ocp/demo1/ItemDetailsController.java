package com.ocp.demo1;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemDetailsController implements Initializable {
    @FXML private Label codeLabel;
    @FXML private Label nameLabel;
    @FXML private Label referenceLabel;
    @FXML private Label typeLabel;
    @FXML private Label emplacementLabel;
    @FXML private Label quantityLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label unityLabel;






    private PiecesSearch currentItem;

    public void setCurrentItem(PiecesSearch item) {
        this.currentItem = item;
        updateLabels();
    }

    private void updateLabels() {
        if (currentItem != null) {
            codeLabel.setText(currentItem.getCode());
            nameLabel.setText(currentItem.getNom());
            referenceLabel.setText(currentItem.getReference());
            typeLabel.setText(currentItem.getType());
            emplacementLabel.setText(currentItem.getEmplacement());
            quantityLabel.setText(String.valueOf(currentItem.getNombre()));
            descriptionLabel.setText(currentItem.getDescription());
            unityLabel.setText(currentItem.getUnité());

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Database connectNow= new Database();
        Connection connectDB = connectNow.connect();
    }
}
