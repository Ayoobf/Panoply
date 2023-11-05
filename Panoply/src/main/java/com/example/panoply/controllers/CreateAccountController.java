package com.example.panoply.controllers;

import com.example.panoply.Main;
import com.example.panoply.mongoDB.MongoDBHandlerExtra;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
        makeDraggable(createPane);

    }

    private void makeDraggable(Pane window) {
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


    @FXML
    void btExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void btSignup(ActionEvent event) {
        String firstName, lastName, email, phoneNumber, password, confirmPassword, teamNameLocal, sAdmin, teamId;
        boolean isAdmin;

        firstName = tfFirstName.getText();
        lastName = tfLastName.getText();
        email = tfEmail.getText();
        phoneNumber = tfPhoneNumber.getText();
        password = pfPassword.getText();
        confirmPassword = pfConfirmPassword.getText();
        teamNameLocal = teamName.getText();
        sAdmin = cbAdmin.getValue();
        isAdmin = sAdmin.equals("Yes");

        // Check if inputted password is correct
        if (!password.equals(confirmPassword)) {
            // If not, alert user
            showAlert("passwords do not match");

        } else {
            MongoDBHandlerExtra mongoDBHandler = new MongoDBHandlerExtra();
            teamId = mongoDBHandler.findTeam(teamNameLocal);

            if (teamId == null) {
                if (!isAdmin) {
                    // if team was not found and user is not an admin
                    showAlert("Team not found");

                } else {
                    // team was not found and user is an admin
                    mongoDBHandler.signUpUser(firstName, lastName, email, password, isAdmin, phoneNumber);
                    mongoDBHandler.makeTeam(teamNameLocal, mongoDBHandler.findUser(email));
                    mongoDBHandler.updateUser(mongoDBHandler.findUser(email), mongoDBHandler.findTeam(teamNameLocal));
                    clearForm();
                }

            } else {
                // team was found and user is not admin
                mongoDBHandler.signUpUser(firstName, lastName, email, password, isAdmin, teamId, phoneNumber);
                clearForm();
            }

        }


    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING, alertMessage, ButtonType.OK);
        alert.setHeaderText("INCORRECT INPUT");
        alert.show();
    }

    // set everything to null
    private void clearForm() {
        tfFirstName.clear();
        tfLastName.clear();
        tfEmail.clear();
        tfPhoneNumber.clear();
        pfPassword.clear();
        pfConfirmPassword.clear();
        teamName.clear();
        cbAdmin.setValue(null);
        cbTOS.setSelected(false);
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
