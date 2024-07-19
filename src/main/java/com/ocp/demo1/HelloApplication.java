package com.ocp.demo1;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/ocp/demo1/login.fxml"));

        primaryStage.setTitle("Login Application");
        primaryStage.setScene(new Scene(root,1000,700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
