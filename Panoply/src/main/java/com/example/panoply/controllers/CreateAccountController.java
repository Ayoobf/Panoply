package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CreateAccountController {
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
}
