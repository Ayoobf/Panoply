package com.example.panoply;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button btExit;

    @FXML
    private Button btLogin;

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

    @FXML
    void btExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void btLogin(ActionEvent event) {

        if (!tfUsername.getText().isEmpty() && !tfPassword.getText().isEmpty()) {
            System.out.println(tfUsername.getText().trim() + " " + tfPassword.getText().trim());

        }

    }

    @FXML
    void lCreateAccount(ActionEvent event) {
        System.out.println("create");
        Main mainScene = new Main();
        try {
            mainScene.changeScene("createAccount.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void lReset(ActionEvent event) {
        System.out.println("reset");
    }

    @FXML
    void tfPassword(ActionEvent event) {

        String password = tfPassword.getText();

    }

    @FXML
    void tfUsername(ActionEvent event) {

        String username = tfUsername.getText();

    }




}