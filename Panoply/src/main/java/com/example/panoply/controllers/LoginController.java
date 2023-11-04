package com.example.panoply.controllers;

import com.example.panoply.Main;
import com.example.panoply.mongoDB.MongoDBHandlerExtra;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController implements Initializable {

    @FXML
    private Button btExit;

    @FXML
    private Button btLogin;

    @FXML
    private Button btBack;

    @FXML
    private Pane imageArea;

    @FXML
    private Hyperlink lCreateAccount;

    @FXML
    private Hyperlink lReset;

    @FXML
    private VBox loginArea;

    @FXML
    private TextField tfPassword;

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
            username = tfUsername.getText().trim();
            password = tfPassword.getText().trim();

            // authenticate user 1=good 0=badz
            int auth = new MongoDBHandlerExtra().authenticateUser(username, password);
            if (auth == 1) {
                switchScene("homePage.fxml");
            } else if (auth == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong Username or Password", ButtonType.OK);
                alert.setHeaderText("INCORRECT INPUT");
                alert.show();
            }
        }
    }


    @FXML
    void lCreateAccount(ActionEvent event) {
        System.out.println("create");
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
        Main mainScene = new Main();
        try {
            mainScene.changeScene(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}