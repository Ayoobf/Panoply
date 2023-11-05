package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CreateAccountController {

    @FXML
    private HBox Contact;

    @FXML
    private Label Title;

    @FXML
    private HBox TitleBar;

    @FXML
    private ChoiceBox<?> cbAdbmin;

    @FXML
    private CheckBox cbTOS;

    @FXML
    private Pane imageArea;

    @FXML
    private VBox loginArea;

    @FXML
    private HBox nameEntryBox;

    @FXML
    private HBox passwordBox;

    @FXML
    private PasswordField pfConfirmPassword;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private HBox team;

    @FXML
    private TextField teamName;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfPhoneNumber;


    @FXML
    void btExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void btSignup(ActionEvent event) {

    }

    @FXML
    void cbTOS(ActionEvent event) {

    }

    @FXML
    void lLogIn(ActionEvent event) {
        switchScene("login.fxml");

    }

    // Helper method for switching views
    private void switchScene(String fxml) {
        Main mainScene = Main.getApplicationInstance();
        try {
            mainScene.changeScene(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
