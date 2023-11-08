package com.example.panoply.controllers;

import com.example.panoply.mongoDB.MongoDBHandlerExtra;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CreateAccountController extends DefaultController implements Initializable {

    private final String[] choiceBoxOptions = {"Yes", "No"};
    @FXML
    private HBox Contact;
    @FXML
    private Label Title;
    @FXML
    private HBox TitleBar;
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

    @FXML
    private Button btSignup;

//    private double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbAdmin.getItems().addAll(choiceBoxOptions);
        makeDraggable(createPane);
        btSignup.setDisable(true);

    }


    @FXML
    private void btExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void btSignup(ActionEvent event) {
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
                    mongoDBHandler.signUpUser(
                            firstName,
                            lastName,
                            email,
                            password,
                            isAdmin,
                            phoneNumber
                    );
                    mongoDBHandler.makeTeam(
                            teamNameLocal,
                            mongoDBHandler.findUser(email)
                    );
                    mongoDBHandler.updateUser(
                            mongoDBHandler.findUser(email),
                            mongoDBHandler.findTeam(teamNameLocal)
                    );
                    clearForm();

                }

            } else {
                // team was found and user is not admin
                mongoDBHandler.signUpUser(
                        firstName,
                        lastName,
                        email,
                        password,
                        isAdmin,
                        teamId,
                        phoneNumber
                );
                clearForm();

            }
        }
    }

    @FXML
    private void cbTOS(ActionEvent event) {
        tryToEnableButton();

    }


    @FXML
    void pfConfirmPassword(ActionEvent event) {
        tryToEnableButton();
    }

    @FXML
    void pfPassword(ActionEvent event) {
        tryToEnableButton();
    }

    @FXML
    void teamName(ActionEvent event) {
        tryToEnableButton();
    }

    @FXML
    void tfEmail(ActionEvent event) {
        tryToEnableButton();
    }

    @FXML
    void tfFirstName(ActionEvent event) {
        tryToEnableButton();
    }

    @FXML
    void tfLastName(ActionEvent event) {
        tryToEnableButton();
    }

    @FXML
    void tfPhoneNumber(ActionEvent event) {
        tryToEnableButton();
    }

    @FXML
    private void lLogIn(ActionEvent event) {
        switchScene("login.fxml");

    }

    // Helper Methods
    /*-------------------------------------------------------------------------------------------------------------------------*/

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

    private boolean textFieldsAreFull() {
        return !(tfFirstName.getText().isBlank() ||
                tfLastName.getText().isBlank() ||
                tfEmail.getText().isBlank() ||
                tfPhoneNumber.getText().isBlank() ||
                pfPassword.getText().isBlank() ||
                pfConfirmPassword.getText().isBlank() ||
                teamName.getText().isBlank() ||
                cbAdmin.getValue().isBlank() ||
                !cbTOS.isSelected());
    }

    private void enableButton() {
        if (textFieldsAreFull()) {
            btSignup.setDisable(false);
        }
    }

    private void tryToEnableButton() {
        try {
            enableButton();
        } catch (NullPointerException ignore) {
        }
    }

}
