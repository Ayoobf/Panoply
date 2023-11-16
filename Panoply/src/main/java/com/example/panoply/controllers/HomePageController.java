package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HomePageController extends DefaultController implements Initializable {

    public Pane imageArea;
    public VBox loginArea;
    public HBox TitleBar;
    public VBox homePage;
    public SplitPane divPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeWindowSize(1366, 782);
        makeDraggable(homePage);


    }

    @FXML
    void btExit(ActionEvent event) {
        Platform.exit();
    }

}
