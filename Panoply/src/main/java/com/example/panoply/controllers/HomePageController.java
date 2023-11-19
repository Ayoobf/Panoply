package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    public VBox sideButtons;
    public Button btColapseSideBar;
    public Label lblFirstName;
    public Button btHome;
    public Button btUsers;
    public Button btSettings;
    public Button btLogout;
    public Pane containerPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeWindowSize(1366, 782);
        makeDraggable(homePage);
    }

    @FXML
    void btHome(ActionEvent event) {

    }

    @FXML
    void btSettings(ActionEvent event) {

    }

    @FXML
    void btUsers(ActionEvent event) {

    }

    @FXML
    void btColapseSideBar(ActionEvent event) {
        sideButtons.setMinWidth(0);
        divPane.setDividerPosition(0, 0);
    }

    @FXML
    void btLogout(ActionEvent event) {
        switchScene("login.fxml");
        changeWindowSize(1024, 600);

    }

    @FXML
    void btExit(ActionEvent event) {
        Platform.exit();
    }

}
