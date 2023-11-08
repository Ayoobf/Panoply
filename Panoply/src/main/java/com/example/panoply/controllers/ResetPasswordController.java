package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ResetPasswordController extends DefaultController implements Initializable {
    @FXML
    Button btBack;

    @FXML
    void btBack(ActionEvent event) {

        Main mainScene = new Main();
        try {
            mainScene.changeScene("login.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
