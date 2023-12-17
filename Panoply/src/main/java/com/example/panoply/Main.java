package com.example.panoply;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private static Stage psg;
    private static Main applicationInstance;
    public static void main(String[] args) {
        launch();
    }
    public static Main getApplicationInstance() {
        return applicationInstance;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        applicationInstance = this;

        // preserve primary stage state for changeScene()
        psg = primaryStage;

        // default bs
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        primaryStage.setTitle("Panoply Login");
        Scene primaryScene = new Scene(root, 1024, 600);
        primaryStage.setScene(primaryScene);

        // implement my own title bar and drag behavior
        psg.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }

    public void changeScene(String newScreen) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(newScreen)));
        psg.getScene().setRoot(pane);

    }

    // This code may seem convoluted and stupid, but it makes my project windows work...
    public void setWindowSize(double width, double height) {
        psg.setMinWidth(width);
        psg.setMinHeight(height);
        psg.setMaxWidth(width);
        psg.setMaxHeight(height);

    }

}