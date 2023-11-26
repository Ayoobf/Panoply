package com.example.panoply.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class UserButtonController extends DefaultController implements Initializable {
    public Pane userBtPane;
    public Button btUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btUser.setText("undefined");

    }

}
