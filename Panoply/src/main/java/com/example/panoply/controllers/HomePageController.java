package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class HomePageController {

    @FXML
    Button btBack;

    @FXML
    void btBack(ActionEvent event) {

        Main mainScene = Main.getApplicationInstance();
        try {
            mainScene.changeScene("login.fxml");
            LoginController lg = new FXMLLoader(getClass().getResource("login.fxml")).getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
