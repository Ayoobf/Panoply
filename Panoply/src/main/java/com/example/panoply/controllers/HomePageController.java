package com.example.panoply.controllers;

import com.example.panoply.classes.Document;
import com.example.panoply.classes.User;
import com.example.panoply.classes.UserHolder;
import com.example.panoply.handlers.GoogleCloudHandler;
import com.example.panoply.handlers.MongoDBHandler;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageException;
import com.mongodb.MongoException;

import org.bson.BsonDateTime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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
	public Label lblUsers;
	public HBox hbTopBar;
	public Button btRemoveUser;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		user = holder.getUser();

		changeWindowSize(1366, 782);
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


		refreshDocumentsSelection();
	}

	@FXML
	public void btAddFile() {
		VBox vbFiles = new VBox();
		Button btSubmit = new Button("Submit");
		vbFiles.getChildren().addAll(new Label("Drag your files Here"));
		vbFiles.setPrefHeight(200);
		vbFiles.setPrefWidth(200);

		List<Document> arrayOfDocuments = new ArrayList<>();

		implementDragDropBehavior(vbFiles, arrayOfDocuments);

		Stage stage = new Stage();
		Scene scene = new Scene(vbFiles);
		stage.setScene(scene);
		stage.setTitle("File Handler");
		stage.show();

		btSubmitOnActionBehavior(btSubmit, arrayOfDocuments, stage);

		vbFiles.getChildren().add(btSubmit);
	}

	private void btSubmitOnActionBehavior(Button submit, List<Document> arrDocuments, Stage stage) {
		submit.setOnAction(actionEvent -> {
			arrDocuments.forEach(document -> {
				try {
					if (user != null) {
						String currentUserTeamName = user.getTeamName();
						new Document().uploadDocument(document.getDocumentPath(), currentUserTeamName, user.getUserName());
						deleteFile(document.getDocumentPath());
					} else {
						// Handle the case where currentUser is null
						System.out.println("Current user is null");
					}
				} catch (IOException e) {
					showAlert("File exists in database. Rename or choose different file");
				}
			});
			btHome();
			stage.close();
		});
	}

	private void deleteFile(String documentPath) {
		File file = new File(documentPath);

		if (file.delete()) {
			Platform.runLater(() -> {
				Alert a = new Alert(Alert.AlertType.INFORMATION, "Success!", ButtonType.OK);
				a.show();
			});
		}
	}

	private void implementDragDropBehavior(VBox vbFiles, List<Document> arrDocuments) {
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
	}


	@FXML
	public void btUsers() {
		show(users);
		lblUsers.setText(user.getTeamName() + "'s Users");

		// Get the list of members
		ArrayList<User> listOfTeamMembers = new MongoDBHandler().listTeamMembers(user.getTeamId());
		ObservableList<User> teamMembers = FXCollections.observableArrayList(listOfTeamMembers);

		createTable(teamMembers, listOfTeamMembers);

		// Add Table
		usersTB.getColumns().setAll(firstNameCol, lastNameCol, emailCol, isAdminCol);

	}

	private void createTable(ObservableList<User> teamMembers, ArrayList<User> listOfTeamMembers) {
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
	}

	@FXML
	public void btSettings() {
		show(settings);

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
	public void btExit() {
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
		MongoDBHandler md = new MongoDBHandler();
		String currentUserTeamName = md.findTeamName(holder.getUser().getTeamId());

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
		listOfFiles.forEach(file ->
				makeDocumentUIElement(file, currentUserTeamName, md)
		);
	}

	private void makeDocumentUIElement(Blob file, String currentUserTeamName, MongoDBHandler md) {
		HBox hbDocumentAsset = new HBox();

		Button btDoc = new Button();
		btDoc.setText(file.getName().replace(currentUserTeamName + "/", ""));

		btDoc.setOnAction(action -> {
			// style pane
			Pane documentStats = new Pane();
			double width = 300;
			double height = 125;
			documentStats.setPrefSize(width, height);

			// get stats
			Label stats = new Label();
			stats.setText(md.findFileStats(file.getName(), currentUserTeamName));

			// regular stuff
			documentStats.getChildren().add(stats);
			Scene sc = new Scene(documentStats);
			Stage st = new Stage();
			st.setTitle(file.getName().replace(currentUserTeamName + "/", ""));
			st.setResizable(false);
			st.setScene(sc);
			st.show();
		});

		Button btOpenDoc = new Button("Open File");
		btOpenDoc.setVisible(!md.findCheckedStatus(file.getName(), currentUserTeamName));
		btOpenDoc.setDisable(md.findCheckedStatus(file.getName(), currentUserTeamName));
		btOpenDoc.setOnAction(action -> {
			String destPathOfDownloadedFile = new GoogleCloudHandler()
					.downloadFile(currentUserTeamName, file.getName());
			try {
				openFile(destPathOfDownloadedFile);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		boolean checkedIn = md.findCheckedStatus(file.getName(), currentUserTeamName);
		Label status = new Label("Checked-In Status: " + checkedIn + " by: " + md.findFileLastEditor(file.getName(), currentUserTeamName));


		MenuButton mbButtonDoc = new MenuButton(":");
		MenuItem checkOutFile = new MenuItem("Check Out File");
		MenuItem checkInFile = new MenuItem("Check In File");


		checkOutBehavior(file, currentUserTeamName, md, checkOutFile, checkedIn, btOpenDoc);
		checkInBehavior(file, currentUserTeamName, md, checkInFile, checkedIn, btOpenDoc);

		mbButtonDoc.getItems().addAll(checkOutFile, checkInFile);

		hbDocumentAsset.setMinWidth(vbDocuments.getMinWidth());
		hbDocumentAsset.setStyle("-fx-border-color: #000000");
		hbDocumentAsset.getChildren().addAll(btDoc, status, mbButtonDoc, btOpenDoc);
		vbDocuments.getChildren().add(hbDocumentAsset);
	}

	private void checkInBehavior(Blob file, String currentUserTeamName, MongoDBHandler md, MenuItem checkInFile, boolean checkedIn, Button btOpenDoc) {
		checkInFile.setOnAction(action -> {

			if (checkedIn) {
				showAlert("Cannot Check In");
			} else if (!user.getUserName().equalsIgnoreCase(md.findFileLastEditor(file.getName(), currentUserTeamName))) {
				showAlert("You are not the one who checked out this document");
			} else {
				String fileNameWithoutPath = Paths.get(file.getName()).getFileName().toString();
				File selectedFile = new File("C:\\Panoply\\" + currentUserTeamName + "\\" + fileNameWithoutPath);


				if (selectedFile.exists() && fileNameWithoutPath.equalsIgnoreCase(selectedFile.getName())) {

					try {
						new GoogleCloudHandler().updateFile(file.getName(), selectedFile);
						md.updateFile(file.getName(), selectedFile, currentUserTeamName, new BsonDateTime(new Date().getTime()));
						md.updateFileCheckedStatus(file.getName(), currentUserTeamName, user.getUserName(), true);
						deleteFile(selectedFile.getPath());
						btOpenDoc.setDisable(true);
						btOpenDoc.setVisible(false);
						btHome();


					} catch (MongoException e) {
						showAlert(e.toString());
					} catch (StorageException e) {
						showAlert("Google Failed");
					} catch (IOException e) {
						showAlert("File Selected Produced IO Exception");
					} catch (InvalidPathException e) {
						showAlert("Path got corrupted somewhere");
					}

				} else {
					showAlert("Selected wrong file");
				}


			}
		});
	}

	private void checkOutBehavior(Blob file, String currentUserTeamName, MongoDBHandler md, MenuItem checkOutFile, boolean checkedIn, Button btOpenDoc) {
		checkOutFile.setOnAction(action -> {
			md.updateFileCheckedStatus(file.getName(), currentUserTeamName, user.getUserName(), false);

			if (!checkedIn) {
				showAlert("Cannot Check Out");

			} else {
				try {
					String destPathOfDownloadedFile = new GoogleCloudHandler()
							.downloadFile(currentUserTeamName, file.getName());
					btOpenDoc.setDisable(false);
					btOpenDoc.setVisible(true);
					openFile(destPathOfDownloadedFile);
				} catch (NullPointerException e) {
					showAlert("Download Failed, Try Again");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				btHome();
			}
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

		listOfTeamMembers.forEach(user -> makeCheckBox(user, usersToRemove, selectedUser));

		Scene scene = new Scene(usersToRemove);
		usersToRemove.setPadding(new Insets(15));
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Remove User");
		stage.show();

		// remove users button action
		rm.setOnAction(event -> {
			// for each person in the arr, delete them
			selectedUser.forEach(user -> new MongoDBHandler().removeUser(user));
			btUsers();
			stage.close();
		});

	}

	private void makeCheckBox(User user, VBox usersToRemove, List<User> selectedUser) {
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
	}

	public void btLeaveTeam() {
		if (user.isAdmin() && user.getTeamSize() > 0) {
			showAlert("You cannot leave team as you are an admin with team members still present");
		} else {
			btLogout();
			new MongoDBHandler().removeUser(user);
		}
	}

	public void btConfig() {
		try {
			openFile("app.properties");
		} catch (IOException e) {
			showAlert("Properties File not Found");
		}
	}
}
