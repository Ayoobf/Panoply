package com.example.panoply.controllers;

import com.example.panoply.User;
import com.example.panoply.UserHolder;
import com.example.panoply.mongoDB.MongoDBHandlerExtra;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class LoginController extends DefaultController implements Initializable {

    @FXML
    private Button btLogin;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUsername;

    @FXML
    private BorderPane loginScreen;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeDraggable(loginScreen);
        btLogin.setDefaultButton(true);
        btHoverEffect(Collections.singletonList(btLogin),
                "-fx-background-color: F78888; -fx-background-radius: 10;",
                "-fx-background-color: FF7777; -fx-background-radius: 10;");

    }

    @FXML
    void btLogin() {

        if (tfUsername.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            showAlert("one or more fields are empty");

        } else {
            String username = tfUsername.getText().trim();
            String password = tfPassword.getText().trim();

            int authentication = new MongoDBHandlerExtra().authenticateUser(username, password);
            if (authentication == 1) {
                // TODO needs a better implementation
                User user = new User(new MongoDBHandlerExtra().findUserFirstName(username), "", "", false, username);
                UserHolder holder = UserHolder.getINSTANCE();
                holder.setUser(user);

                switchScene("homePage.fxml");
            }

            if (authentication == 0) {
                // alert user of wrong input
                showAlert("Incorrect username/password");

            }

        }

    }

    @FXML
    void btExit() {
        Platform.exit();
    }

    @FXML
    void lCreateAccount() {
        switchScene("createAccount.fxml");

    }

    @FXML
    void lReset() {

        switchScene("resetPassword.fxml");
    }

    @FXML
    void tfPassword() {
        tfPassword.getText();

    }

    @FXML
    void tfUsername() {
        tfUsername.getText();

    }


}