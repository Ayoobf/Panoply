package com.example.panoply;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Create {

    private final BorderPane createPane;

    public Create() {

        createPane = new BorderPane();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("create.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Pane getCreatePane() {
        return createPane;
    }


}
