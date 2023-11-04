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

    // helps change scene
    private static Stage psg;
    private static Main appicationInstance;
    private double x, y;

    public static void main(String[] args) {
        launch();
    }

    public static Main getApplicationInstance() {
        return appicationInstance;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        appicationInstance = this;

        // preserve primary stage state for changeScene()
        psg = primaryStage;

        // default bs
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        primaryStage.setTitle("Panoply Login");
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);

        // implement my own title bar
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            primaryStage.setX(mouseEvent.getScreenX() - x);
            primaryStage.setY(mouseEvent.getScreenY() - y);

        });


        primaryStage.show();


    }


    public void changeScene(String newScreen) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(newScreen)));
        psg.getScene().setRoot(pane);

    }
}