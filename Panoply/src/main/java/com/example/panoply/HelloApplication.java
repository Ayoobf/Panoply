package com.example.panoply;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));

//        // Implement my own buttons
//        stage.initStyle(StageStyle.UNDECORATED);

        stage.setTitle("Panoply");
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}