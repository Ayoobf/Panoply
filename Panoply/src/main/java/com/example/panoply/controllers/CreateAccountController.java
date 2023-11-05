package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateAccountController implements Initializable {

    @FXML
    private HBox Contact;

    @FXML
    private Label Title;

    @FXML
    private HBox TitleBar;

    private final String[] choiceBoxOptions = {"Yes", "No"};
    @FXML
    private ChoiceBox<String> cbAdmin;

    @FXML
    private CheckBox cbTOS;

    @FXML
    private Pane imageArea;
    @FXML
    private BorderPane createPane;

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
    private double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbAdmin.getItems().addAll(choiceBoxOptions);

        createPane.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) createPane.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);

        });

        createPane.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

    }


    @FXML
    void btExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void btSignup(ActionEvent event) {
        String firstName, lastName, email, phoneNumber, password, confirmPassword, team, sAdmin;
        boolean admin;

        firstName = tfFirstName.getText();
        lastName = tfLastName.getText();
        email = tfEmail.getText();
        phoneNumber = tfPhoneNumber.getText();
        password = pfPassword.getText();
        confirmPassword = pfConfirmPassword.getText();
        team = teamName.getText();
        sAdmin = cbAdmin.getValue();
        if (sAdmin.equals("Yes")) {
            admin = true;
        } else {
            admin = false;
        }


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
