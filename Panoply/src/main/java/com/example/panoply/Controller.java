package com.example.panoply;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

    final Delta dragDelta = new Delta();
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

    @FXML
    void stageMouseDragged(MouseEvent event) {

        stage.setX(event.getScreenX() + dragDelta.x);
        stage.setY(event.getScreenY() + dragDelta.y);


    }

    @FXML
    void stageMousePressed(MouseEvent event) {

        dragDelta.x = stage.getX() - event.getScreenX();
        dragDelta.y = stage.getY() - event.getScreenY();


    }


}