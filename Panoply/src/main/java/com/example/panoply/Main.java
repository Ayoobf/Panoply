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
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        primaryStage.setTitle("Panoply Login");
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);

        // implement my own title bar and drag behavior
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();


        // TODO: THIS IS REFRENCE CODE FOR LATER
        // LOAD HOMESCREEN ELEMENT --> LOAD ASSET PANE/BUTTON/SCENE --> ADD CHILDREN TO HOMSCREEN
//        VBox homeScreenPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("test1.fxml")));
//
//
//        for (int i = 0; i <50; i++) {
//            Button element1Pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("test2.fxml")));
//
//            element1Pane.setText(String.valueOf(i));
//            homeScreenPane.getChildren().add(element1Pane);
//        }


    }


    public void changeScene(String newScreen) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(newScreen)));
        psg.getScene().setRoot(pane);

    }
}