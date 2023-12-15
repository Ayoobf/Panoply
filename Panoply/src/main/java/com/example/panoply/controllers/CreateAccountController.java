package com.example.panoply.controllers;

import com.example.panoply.handlers.GoogleCloudHandler;
import com.example.panoply.handlers.MongoDBHandler;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class CreateAccountController extends DefaultController implements Initializable {

    private final String[] choiceBoxOptions = {"Yes", "No"};

    @FXML
    private ChoiceBox<String> cbAdmin;

    @FXML
    private CheckBox cbTOS;

    @FXML
    private BorderPane createPane;

    @FXML
    private PasswordField pfConfirmPassword;

    @FXML
    private PasswordField pfPassword;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbAdmin.getItems().addAll(choiceBoxOptions);
        makeDraggable(createPane);
        btSignup.setDisable(true);

    }


    @FXML
    private void btExit() {
        Platform.exit();
    }

    @FXML
    private void btSignup() {
        String firstName, lastName, email, phoneNumber, password, confirmPassword, teamNameLocal, teamId;
        boolean isAdmin;

        firstName = tfFirstName.getText().trim();
        lastName = tfLastName.getText().trim();
        email = tfEmail.getText().trim();
        phoneNumber = tfPhoneNumber.getText().trim();
        password = pfPassword.getText().trim();
        confirmPassword = pfConfirmPassword.getText().trim();
        teamNameLocal = teamName.getText().trim();
        isAdmin = cbAdmin.getValue().equals("Yes");

        // Check if inputted password is correct
        if (!password.equals(confirmPassword)) {
            // If not, alert user
            showAlert("passwords do not match");

        } else {
            MongoDBHandler mongoDBHandler = new MongoDBHandler();
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
                            true,
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
                    new GoogleCloudHandler().createTeamFolder(teamNameLocal);
                    clearForm();

                }
            } else {
                // team was found and user is admin
                if (isAdmin) {
                    showAlert("Cannot Make team with already establish admin present");

                } else {

                    // team was found and user is not admin
                    mongoDBHandler.signUpUser(
                            firstName,
                            lastName,
                            email,
                            password,
                            false,
                            teamId,
                            phoneNumber
                    );
                    clearForm();
                }

            }
        }
    }

    @FXML
    private void cbTOS() {
        tryToEnableSubmitButton();

    }


    @FXML
    void pfConfirmPassword() {
        tryToEnableSubmitButton();
    }

    @FXML
    void pfPassword() {
        tryToEnableSubmitButton();
    }

    @FXML
    void teamName() {
        tryToEnableSubmitButton();
    }

    @FXML
    void tfEmail() {
        tryToEnableSubmitButton();
    }

    @FXML
    void tfFirstName() {
        tryToEnableSubmitButton();
    }

    @FXML
    void tfLastName() {
        tryToEnableSubmitButton();
    }

    @FXML
    void tfPhoneNumber() {
        tryToEnableSubmitButton();
    }

    @FXML
    private void lLogIn() {
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

    private void tryToEnableSubmitButton() {
        try {
            enableButton();
        } catch (NullPointerException ignore) {
        }
    }

}
