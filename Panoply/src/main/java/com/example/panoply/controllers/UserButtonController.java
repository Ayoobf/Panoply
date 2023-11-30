package com.example.panoply.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import User.java;


public class UserButtonController extends DefaultController implements Initializable {
    public Pane userBtPane;
    public Button btUser;
    User foo = new User();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btUser.setText("undefined");

    }

}
