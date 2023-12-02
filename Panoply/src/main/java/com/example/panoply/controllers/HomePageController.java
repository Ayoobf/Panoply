package com.example.panoply.controllers;

import com.example.panoply.User;
import com.example.panoply.UserHolder;
import com.example.panoply.handlers.GoogleCloudHandler;
import com.example.panoply.handlers.MongoDBHandler;
import com.google.cloud.storage.Blob;

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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

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
    public TableView<User> usersTB;
    public TableColumn<User, String> firstNameCol;
    public TableColumn<User, String> lastNameCol;
    public TableColumn<User, String> emailCol;
    public TableColumn<User, String> isAdminCol;
    public Button btAddFile;
    public ScrollPane sbDocuments;
    public VBox vbDocuments;
    public HBox hbPicturesOfTopFiles;
    public Label lblUsers;


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
        String currentUserTeamName = new MongoDBHandler().findTeamName(user.getTeamId());
        List<Blob> listOfFiles = new GoogleCloudHandler().getFilesInTeamFolder(currentUserTeamName);

        for (Blob file : listOfFiles) {
            Hyperlink hl = new Hyperlink(file.getName().replace(currentUserTeamName + "/", ""));

            vbDocuments.getChildren().addAll(hl);
        }
        show(defaultHomePage);

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
        lblUsers.setText(new MongoDBHandler().findTeamName(currentUser.getTeamId()) + "'s Users");

        // Get the list of members
        ArrayList<User> listOfTeamMembers = new MongoDBHandler().listTeamMembers(currentUser.getTeamId());
        ObservableList<User> teamMembers = FXCollections.observableArrayList(listOfTeamMembers);

        // Create table
        usersTB.setItems(teamMembers);

        //Create Columns
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>(listOfTeamMembers.get(0).firstNameProperty().getName()));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>(listOfTeamMembers.get(0).lastNameProperty().getName()));
        emailCol.setCellValueFactory(new PropertyValueFactory<>(listOfTeamMembers.get(0).emailProperty().getName()));
        isAdminCol.setCellValueFactory(new PropertyValueFactory<>(listOfTeamMembers.get(0).adminProperty().getName()));

        styleCol(firstNameCol, Color.BLACK);
        styleCol(lastNameCol, Color.BLACK);
        styleCol(emailCol, Color.rgb(144, 204, 244));
        styleCol(isAdminCol, Color.BLACK);


        // Add Table
        usersTB.getColumns().setAll(firstNameCol, lastNameCol, emailCol, isAdminCol);

//        if(sidebar is col;lapsed){
//            firstNameCol.setMinWidth(500);
//        }
    }

    private void styleCol(TableColumn<User, String> col, Color color) {
        col.setCellFactory(new Callback<>() {
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setTextFill(color);
                            this.setFont(Font.font("Segoue UI", FontWeight.BOLD, 16));
                            this.setAlignment(Pos.CENTER);


                            if (item.contains("true"))
                                this.setTextFill(Color.rgb(243, 210, 80));
                            setText(item);
                        }
                    }
                };
            }
        });

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
