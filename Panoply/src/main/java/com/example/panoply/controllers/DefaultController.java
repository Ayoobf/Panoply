package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class DefaultController {
    private double x, y;

    // Function to make windows move because of STAGE.initStyle(StageStyle.UNDECORATED);
    // Call in initialize functions
    public void makeDraggable(Parent window) {
        window.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) window.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);

        });

        window.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
    }

    // abstraction for the alert messages
    public void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING, alertMessage, ButtonType.OK);
        alert.setHeaderText("Warning!");
        alert.show();
    }


    public void switchScene(String scene) {
        Main mainScene = Main.getApplicationInstance();
        try {
            mainScene.changeScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Change window size for batter traversal
    public void changeWindowSize(double width, double height) {
        Main mainScene = Main.getApplicationInstance();
        mainScene.setWindowSize(width, height);

    }

    // Takes in a collection of buttons and makes them have the on dragable behavior
    public void btHoverEffect(Collection<Button> buttons, String oldStyle, String newStyle) {
        ArrayList<Button> listOfButtons = new ArrayList<>(buttons);
        for (Button button : listOfButtons) {
            button.setOnMouseEntered(e -> button.setStyle(newStyle));
            button.setOnMouseExited(e -> button.setStyle(oldStyle));
        }

    }

}
