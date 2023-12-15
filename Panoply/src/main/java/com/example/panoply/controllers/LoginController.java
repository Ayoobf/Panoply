package com.example.panoply.controllers;

import com.example.panoply.classes.User;
import com.example.panoply.classes.UserHolder;
import com.example.panoply.handlers.MongoDBHandler;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class LoginController extends DefaultController implements Initializable {

	@FXML
	private Button btLogin;

	@FXML
	private PasswordField tfPassword;

	@FXML
	private TextField tfUsername;

	@FXML
	private BorderPane loginScreen;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		makeDraggable(loginScreen);
		btLogin.setDefaultButton(true);
		btHoverEffect(Collections.singletonList(btLogin),
				"-fx-background-color: F78888; -fx-background-radius: 10;",
				"-fx-background-color: FF7777; -fx-background-radius: 10;");

	}

	@FXML
	public void btLogin() {

		if (tfUsername.getText().isEmpty() || tfPassword.getText().isEmpty()) {
			showAlert("one or more fields are empty");

		} else {
			String username = tfUsername.getText().trim();
			String password = tfPassword.getText().trim();

			MongoDBHandler finder = new MongoDBHandler();
			int authentication = finder.authenticateUser(username, password);

			if (authentication == 1) {
				User user = new User(
						finder.findUserFirstName(username),
						finder.findUserLastName(username),
						finder.findUserPhoneNumber(username),
						finder.findUserAdminStatus(username),
						username
				);
				UserHolder holder = UserHolder.getINSTANCE();
				holder.setUser(user);

				switchScene("homePage.fxml");
			}

			if (authentication == 0) {
				// alert user of wrong input
				showAlert("Incorrect username/password");

			}

		}

	}

	@FXML
	public void btExit() {
		Platform.exit();
	}

	@FXML
	public void lCreateAccount() {
		switchScene("createAccount.fxml");

	}

	@FXML
	public void tfPassword() {
		tfPassword.getText();

	}

	@FXML
	public void tfUsername() {
		tfUsername.getText();

	}


}