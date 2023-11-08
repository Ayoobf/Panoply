package com.example.panoply.controllers;

import com.example.panoply.Main;
import com.example.panoply.mongoDB.MongoDBHandlerExtra;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController extends DefaultController implements Initializable {

    @FXML
    private Button btExit;

    @FXML
    private Button btLogin;

    @FXML
    private Button btBack;
    @FXML
    private Hyperlink lCreateAccount;

    @FXML
    private Hyperlink lReset;

    @FXML
    private VBox loginArea;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUsername;

    @FXML
    private BorderPane loginScreen;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    private String username;
    private String password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeDraggable(loginScreen);
        btLogin.setDefaultButton(true);
    }

    @FXML
    void btLogin(ActionEvent event) {

        if (tfUsername.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            showAlert("one or more fields are empty");
        } else {
            String username = tfUsername.getText().trim();
            String password = tfPassword.getText().trim();

            int authentication = new MongoDBHandlerExtra().authenticateUser(username, password);
            if (authentication == 1) {
                switchScene("homePage.fxml");

            }

            if (authentication == 0) {
                // alert user of wrong input
                showAlert("Incorrect username/password");
            }

        }

    }

    @FXML
    void btExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void lCreateAccount(ActionEvent event) {
        switchScene("createAccount.fxml");

    }

    @FXML
    void lReset(ActionEvent event) {

        switchScene("resetPassword.fxml");
    }

    @FXML
    void tfPassword(ActionEvent event) {
        tfPassword.getText();

    }

    @FXML
    void tfUsername(ActionEvent event) {
        tfUsername.getText();

    }


}