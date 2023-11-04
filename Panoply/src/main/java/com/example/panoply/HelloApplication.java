package com.example.panoply;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    // Helper method to help me move the window
    private static void mouseDraggedToMoveWindow(Stage stage, Scene scene, Delta dragDelta) {
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() + dragDelta.x);
                stage.setY(mouseEvent.getScreenY() + dragDelta.y);

            }
        });
    }

    // Helper method to help me move the window
    private static void mousePressedForDrag(Stage stage, Scene scene, Delta dragDelta) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragDelta.x = stage.getX() - mouseEvent.getScreenX();
                dragDelta.y = stage.getY() - mouseEvent.getScreenY();

            }
        });
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Implement my own buttons
        stage.initStyle(StageStyle.UNDECORATED);

        // Implement my own Drag behavior
        final Delta dragDelta = new Delta();
        mousePressedForDrag(stage, scene, dragDelta);
        mouseDraggedToMoveWindow(stage, scene, dragDelta);

        // default garbage
        stage.setTitle("Panoply");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}