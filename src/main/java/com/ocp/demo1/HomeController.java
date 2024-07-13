package com.ocp.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {
    @FXML
    private Label welcomeLabel;

    public void setUser(String username) {
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    @FXML
    private void handleLogout() {
        // Code to handle logout
    }
}
