package com.example.panoply.controllers;

import com.example.panoply.User;
import com.example.panoply.UserHolder;
import com.example.panoply.mongoDB.MongoDBHandlerExtra;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HomePageController extends DefaultController implements Initializable {

    //    public Pane imageArea;
    public HBox TitleBar;
    public VBox homePage;
    public SplitPane divPane;
    public VBox sideButtons;
    public Button btCollapseSideBar;
    public Label lblFirstName;
    public Button btHome;
    public Button btUsers;
    public Button btSettings;
    public Button btLogout;
    public Pane containerPane;
    public VBox defaultHomePage;
    public VBox users;
    public VBox settings;
    public TableColumn nameCol;
    public TableColumn emailCol;
    public TableColumn noteCol;
    public TableColumn adminCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeWindowSize(1366, 782);
        makeDraggable(homePage);

        // make buttons more dynamic looking
        btHoverEffect(
                Arrays.asList(btHome, btLogout, btUsers, btSettings),
                "-fx-background-color:#EEEEEE",
                "-fx-background-color:D0D0D0"
        );

        // requires separate method because its special
        btHoverEffect(
                Collections.singletonList(btCollapseSideBar),
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

        // set User lbl
        UserHolder holder = UserHolder.getINSTANCE();
        User user = holder.getUser();
        lblFirstName.setText(user.getFirstName());

        show(defaultHomePage);

        User currentUser = UserHolder.getINSTANCE().getUser();
        System.out.println(currentUser.getLastName());


    }


    @FXML
    void btHome() {
        show(defaultHomePage);

    }

    @FXML
    void btSettings() {
        show(settings);
    }

    @FXML
    void btUsers() {
        show(users);

        User currentUser = UserHolder.getINSTANCE().getUser();

        // Get the list of members
        ArrayList<User> listOfTeamMembers = new MongoDBHandlerExtra().listTeamMembers(currentUser.getTeamId());
        ObservableList<User> teamMembers = FXCollections.observableArrayList(listOfTeamMembers);

        // Create table
        TableView<User> usersTable = new TableView<>();
        usersTable.setItems(teamMembers);

        //Create Columns
        TableColumn<User, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>(listOfTeamMembers.get(0).firstNameProperty().getName()));
        TableColumn<User, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>(listOfTeamMembers.get(0).lastNameProperty().getName()));
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>(listOfTeamMembers.get(0).emailProperty().getName()));
        TableColumn<User, String> isAdminCol = new TableColumn<>("Admin Status");
        isAdminCol.setCellValueFactory(new PropertyValueFactory<>(listOfTeamMembers.get(0).adminProperty().getName()));

        format(firstNameCol);
        format(lastNameCol);
        format(emailCol);
        format(isAdminCol);

        // Add Table
        usersTable.getColumns().setAll(firstNameCol, lastNameCol, emailCol, isAdminCol);
        users.getChildren().add(usersTable);


    }

    private void format(TableColumn<User, String> col) {
        col.setPrefWidth(200);
        col.setMinWidth(75);
        col.setMaxWidth(500);
        col.setResizable(true);
        col.setReorderable(false);
    }

    @FXML
    void btCollapseSideBar() {
        sideButtons.setMinWidth(0);
        divPane.setDividerPosition(0, 0);
    }

    @FXML
    void btLogout() {
        switchScene("login.fxml");
        changeWindowSize(1024, 600);
    }

    @FXML
    void btExit() {
        Platform.exit();
    }

    private void show(VBox activePage) {
        switch (activePage.getId()) {
            case "defaultHomePage" -> {
                defaultHomePage.setVisible(true);
                defaultHomePage.setDisable(false);
                users.setVisible(false);
                users.setDisable(true);
                settings.setVisible(false);
                settings.setDisable(true);

            }
            case "users" -> {
                users.setVisible(true);
                users.setDisable(false);
                defaultHomePage.setVisible(false);
                defaultHomePage.setDisable(true);
                settings.setVisible(false);
                settings.setDisable(true);
            }
            case "settings" -> {
                settings.setVisible(true);
                settings.setDisable(false);
                users.setVisible(false);
                users.setDisable(true);
                defaultHomePage.setVisible(false);
                defaultHomePage.setDisable(true);
            }
        }
    }
}
