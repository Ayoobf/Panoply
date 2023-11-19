package com.example.panoply.controllers;

import com.example.panoply.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
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
    public VBox defaultHomePage;
    public VBox users;
    public VBox settings;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeWindowSize(1366, 782);
        makeDraggable(homePage);

        btHoverEffect(
                Arrays.asList(btHome, btLogout, btUsers, btSettings),
                "-fx-background-color:#EEEEEE",
                "-fx-background-color:D0D0D0"
        );

        // requires seperate method because its special
        btHoverEffect(
                Collections.singletonList(btColapseSideBar),
                "-fx-background-color:#eeeeee;" +
                        "-fx-background-radius:100;" +
                        "-fx-border-color: #000000;" +
                        "-fx-border-radius: 100;" +
                        "-fx-rotate: 90"
                ,
                "-fx-background-color:#D0D0D0;" +
                        "-fx-background-radius:100;" +
                        "-fx-border-color: #000000;" +
                        "-fx-border-radius: 100;" +
                        "-fx-rotate: 90"

        );


    }


    @FXML
    void btHome(ActionEvent event) {

        show(defaultHomePage);
    }


    @FXML
    void btSettings(ActionEvent event) {
        show(settings);
    }

    @FXML
    void btUsers(ActionEvent event) {
        show(users);
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

    // TODO make show method
    private void show(VBox defaultHomePage) {
    }
}
