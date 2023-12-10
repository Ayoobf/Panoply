package com.example.panoply.controllers;

import com.example.panoply.Document;
import com.example.panoply.User;
import com.example.panoply.UserHolder;
import com.example.panoply.handlers.GoogleCloudHandler;
import com.example.panoply.handlers.MongoDBHandler;
import com.google.cloud.storage.Blob;

import org.bson.BsonDateTime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HomePageController extends DefaultController implements Initializable {

	public static List<Blob> listOfFiles = new ArrayList<>();
	public static UserHolder holder = UserHolder.getINSTANCE();
	public static User user;
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
	public HBox hbTopBar;
	public Button btRemoveUser;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		user = holder.getUser();

		changeWindowSize(800, 400);
		makeDraggable(homePage);

		// make buttons more dynamic looking
		btHoverEffect(Arrays.asList(btHome, btLogout, btUsers, btSettings), "-fx-background-color:#EEEEEE", "-fx-background-color:D0D0D0");

		// requires separate method because its special
		btHoverEffect(Collections.singletonList(btCollapseSideBar), "-fx-background-color:#eeeeee;" + "-fx-background-radius:100;" + "-fx-border-color: #000000;" + "-fx-border-radius: 100;" + "-fx-rotate: 90", "-fx-background-color:#D0D0D0;" + "-fx-background-radius:100;" + "-fx-border-color: #000000;" + "-fx-border-radius: 100;" + "-fx-rotate: 90");
		refreshDocumentsSelection();

		// set User lbl
		lblFirstName.setText(user.getFirstName());

//		show(defaultHomePage);
		if (!user.isAdmin()) {
			show(defaultHomePage);
		} else {
			// you'd assume this should be show() method
			// you'd be wrong
			// I coded my defaultHomePage and Show() -->
			// objects in such a way that it breaks if you use show() in this block
			btUsers();
			btRemoveUser.setVisible(true);
		}
	}

	@FXML
	public void btHome() {
		show(defaultHomePage);
		listOfFiles.clear();
		vbDocuments.getChildren().clear();
		vbDocuments.getChildren().add(hbPicturesOfTopFiles);

		refreshDocumentsSelection();
	}

	@FXML
	public void btAddFile() {
		VBox vbFiles = new VBox();
		Button submit = new Button("Submit");
		vbFiles.getChildren().addAll(new Label("Drag your files Here"));
		vbFiles.setPrefHeight(200);
		vbFiles.setPrefWidth(200);

		List<Document> arrDocuments = new ArrayList<>();

		vbFiles.setOnDragOver(dragEvent -> {
			if (dragEvent.getGestureSource() != vbFiles && dragEvent.getDragboard().hasFiles()) {
				dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			dragEvent.consume();
		});

		vbFiles.setOnDragDropped(event -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			// system facing var
			String sysDroppedFile = "";
			if (db.hasFiles()) {
				sysDroppedFile = db.getFiles().toString();
				// user facing var
				Label lblDroppedFile = new Label(sysDroppedFile);
				vbFiles.getChildren().addAll(lblDroppedFile);
				success = true;
			}

			/* let the source know whether the string was successfully
			 * transferred and used */
			event.setDropCompleted(success);
			Document doc = new Document(sysDroppedFile.replaceAll("\\[", "").replaceAll("]", ""));
			doc.setUploadDate(new BsonDateTime(new Date().getTime()));
			arrDocuments.add(doc);

			event.consume();
		});

		Stage stage = new Stage();
		Scene scene = new Scene(vbFiles);
		stage.setScene(scene);
		stage.setTitle("File Handler");
		stage.show();

		submit.setOnAction(actionEvent -> {
			arrDocuments.forEach(document -> {
				try {
					User currentUser = holder.getUser();
					if (currentUser != null) {
						String currentUserTeamName = new MongoDBHandler().findTeamName(currentUser.getTeamId());
						new Document().uploadDocument(document.getDocumentPath(), currentUserTeamName, currentUser.getUserName());
					} else {
						// Handle the case where currentUser is null
						System.out.println("Current user is null");
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
			btHome();
			stage.close();
		});

		vbFiles.getChildren().add(submit);
	}

	@FXML
	public void btSettings() {
		show(settings);
	}

	@FXML
	public void btUsers() {
		show(users);
		lblUsers.setText(new MongoDBHandler().findTeamName(user.getTeamId()) + "'s Users");

		// Get the list of members
		ArrayList<User> listOfTeamMembers = new MongoDBHandler().listTeamMembers(user.getTeamId());
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

	@FXML
	public void btCollapseSideBar() {
		sideButtons.setMinWidth(0);
		divPane.setDividerPosition(0, 0);

	}

	@FXML
	public void btLogout() {
		switchScene("login.fxml");
		holder.setUser(null);
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

	private void refreshDocumentsSelection() {
		String currentUserTeamName = new MongoDBHandler().findTeamName(holder.getUser().getTeamId());
//		listOfFiles.addAll(new GoogleCloudHandler().getFilesInTeamFolder(currentUserTeamName));

		GoogleCloudHandler gc = new GoogleCloudHandler();
		listOfFiles = new ArrayList<>();
		List<Blob> arr = gc.getFilesInTeamFolder(currentUserTeamName);

		// Spits out list of Files not Dirs
		arr.forEach(file -> {
			if (file.getName().contains(".")) {
				listOfFiles.add(file);
			}
		});

		// Make new elements based on copied elements
		listOfFiles.forEach(file -> {
//			Hyperlink hl = new Hyperlink(file.getName().replace(currentUserTeamName + "/", ""));
//
//			hl.setOnAction(event -> {
//				try {
//					// method downloads file and returns its path
//					String destPathOfDownloadedFile = new GoogleCloudHandler().downloadFile(currentUserTeamName, file.getName());
//					openFile(destPathOfDownloadedFile);
//
//				} catch (NullPointerException e) {
//					showAlert("Download Failed, Try Again");
//				} catch (IOException e) {
//					throw new RuntimeException(e);
//				}
//			});
//			vbDocuments.getChildren().addAll(hl);
//
//			// TODO
			HBox hbDocumentAsset = new HBox();

			// TODO when Button is clicked, it retrieves doc stats
			Button btDoc = new Button();
			btDoc.setText(file.getName().replace(currentUserTeamName + "/", ""));


			// TODO file handers
			MenuButton mbButtonDoc = new MenuButton(":");
			MenuItem checkOutFile = new MenuItem("Check Out File");
			MenuItem checkInFile = new MenuItem("Check In File");
			mbButtonDoc.getItems().addAll(checkOutFile, checkInFile);

			// TODO Styling
			hbDocumentAsset.setMinWidth(vbDocuments.getMinWidth());
			hbDocumentAsset.setStyle("-fx-border-color: #000000");
			hbDocumentAsset.getChildren().addAll(btDoc, mbButtonDoc);
			vbDocuments.getChildren().add(hbDocumentAsset);

		});
	}

	private void openFile(String destPathOfDownloadedFile) throws IOException {
		File file = new File(destPathOfDownloadedFile);
		if (!file.exists()) {
			showAlert("File does not exist");
		} else {
			// open with os
			Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", file.getAbsolutePath()});
		}
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

							if (item.contains("true")) this.setTextFill(Color.rgb(243, 210, 80));
							setText(item);
						}
					}
				};
			}
		});

	}


	public void btRemoveUser() {
		VBox usersToRemove = new VBox();

		usersToRemove.setPrefHeight(200);
		usersToRemove.setPrefWidth(200);
		usersToRemove.setStyle("-fx-background-color: #EEEEEE");

		// Add button
		Button rm = new Button("Remove Selected");
		rm.setStyle("-fx-background-color: #f78888; -fx-text-fill: #FFFFFF");
		usersToRemove.getChildren().add(rm);
		VBox.setMargin(rm, new Insets(15));

		// output team members' username
		ArrayList<User> listOfTeamMembers = new MongoDBHandler().listTeamMembers(user.getTeamId());
		List<User> selectedUser = new ArrayList<>();

		listOfTeamMembers.forEach(user -> {
			CheckBox cbUser = new CheckBox(user.getUserName());

			cbUser.setTextOverrun(OverrunStyle.CLIP);
			cbUser.setStyle("");
			cbUser.setMinWidth(usersToRemove.getWidth());
			cbUser.setOnAction(event -> {
				// if selected, add to arr
				if (cbUser.isSelected()) {
					selectedUser.add(user);
					// not selected but is in
				} else if (!cbUser.isSelected()) {
					selectedUser.remove(user);
				}

			});
			// show to user
			usersToRemove.getChildren().add(cbUser);
		});

		Scene scene = new Scene(usersToRemove);
		usersToRemove.setPadding(new Insets(15));
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Remove User");
		stage.show();

		// remove users button action
		rm.setOnAction(event -> {
			// for each person in the arr, delete them
			selectedUser.forEach(user -> {
				new MongoDBHandler().removeUser(user);
			});
			btUsers();
			stage.close();
		});


	}
}
