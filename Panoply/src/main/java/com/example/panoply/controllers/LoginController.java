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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController implements Initializable {

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
    private Stage stage;
    @FXML
    private Scene scene;
    private String username;
    private String password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void btLogin(ActionEvent event) throws IOException {
        // if fields are not empty
        if (!tfUsername.getText().isEmpty() && !tfPassword.getText().isEmpty()) {
            String username = tfUsername.getText().trim();
            String password = tfPassword.getText().trim();

            // authenticate user 1=good 0=bad
            int auth = new MongoDBHandlerExtra().authenticateUser(username, password);
            if (auth == 1) {
                switchScene("homePage.fxml");

            } else if (auth == 0) {
                // alert user of wrong input
                Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong Username or Password", ButtonType.OK);
                alert.setHeaderText("INCORRECT INPUT");
                alert.show();
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

        String password = tfPassword.getText();

    }

    @FXML
    void tfUsername(ActionEvent event) {

        String username = tfUsername.getText();

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