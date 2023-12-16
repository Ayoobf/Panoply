package com.example.panoply.handlers;

import com.example.panoply.classes.AppProperties;
import com.example.panoply.classes.User;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.BsonDateTime;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MongoDBHandler {
	private static final String uri = AppProperties.getInstance().getProperty("url");
	private static final MongoClient client = MongoClients.create(uri);
	private static final MongoDatabase database = client.getDatabase(AppProperties.getInstance().getProperty("db_name"));
	private static final MongoCollection<Document> userCollection = database.getCollection(AppProperties.getInstance().getProperty("db_userCollectionName"));
	private static final MongoCollection<Document> teamsCollection = database.getCollection(AppProperties.getInstance().getProperty("db_teamCollectionName"));
	private static final MongoCollection<Document> documentCollection = database.getCollection(AppProperties.getInstance().getProperty("db_documentCollectionName"));
	private static final BCryptPasswordEncoder authenticator = new BCryptPasswordEncoder();

	public MongoDBHandler() {
	}

	public int authenticateUser(String username, String password) {

		Document query = new Document("username", username);
		Document retrievedDoc = userCollection.find(query).first();

		if (retrievedDoc != null) {

			String storedPassword = retrievedDoc.getString("password");

			if (authenticator.matches(password, storedPassword)) {
				return 1;
			}
		}
		return 0;
	}

	public boolean userExists(String username) {
		Document doc = userCollection.find(Filters.eq("username", username)).first();

		return doc != null;
	}

	public void signUpUser(String firstName, String lastName, String email, String password, boolean isAdmin, String teamId, String phoneNumber) {

		String securePassword = authenticator.encode(password);

		userCollection.insertOne(new Document()
				.append("_id", new ObjectId())
				.append("username", email)
				.append("password", securePassword)
				.append("team_id", new ObjectId(teamId))
				.append("is_admin", isAdmin)
				.append("first_name", firstName)
				.append("last_name", lastName)
				.append("phone_number", phoneNumber)
		);

		incrementTeamSize(teamId, findTeamSize(teamId));


	}

	public void signUpUser(String firstName, String lastName, String email, String password, boolean isAdmin, String phoneNumber) {


		String securePassword = authenticator.encode(password);

		userCollection.insertOne(new Document()
				.append("_id", new ObjectId())
				.append("username", email)
				.append("password", securePassword)
				.append("team_id", null)
				.append("is_admin", isAdmin)
				.append("first_name", firstName)
				.append("last_name", lastName)
				.append("phone_number", phoneNumber)
		);


	}

	public void updateUser(String userObjectId, String teamObjectId) {

		userCollection.updateOne(
				Filters.eq("_id", new ObjectId(userObjectId)),
				Updates.set("team_id", new ObjectId(teamObjectId)
				));
	}


	public void makeTeam(String teamName, String adminName) {

		teamsCollection.insertOne(new Document()
				.append("_id", new ObjectId())
				.append("team_name", teamName)
				.append("team_size", 1)
				.append("admin", new ObjectId(adminName))
		);


	}

	// Finds User ObjectID
	public String findUser(String email) {
		Document doc = userCollection.find(Filters.eq("username", email)).first();

		if (doc != null) {
			return doc.getObjectId("_id").toString();

		}
		return null;
	}

	// Finds User FirstName (String)
	public String findUserFirstName(String userName) {
		return userCollection.distinct("first_name", Filters.eq("username", userName), String.class).first();

	}

	// Finds User LastName (String)
	public String findUserLastName(String userName) {
		return userCollection.distinct("last_name", Filters.eq("username", userName), String.class).first();

	}

	// Finds User PhoneNumber (String)
	public String findUserPhoneNumber(String userName) {
		return userCollection.distinct("phone_number", Filters.eq("username", userName), String.class).first();

	}

	// Finds User adminStatus (bool)
	public boolean findUserAdminStatus(String userName) {
		return Boolean.TRUE.equals(userCollection.distinct("is_admin", Filters.eq("username", userName), Boolean.class).first());

	}

	// Finds User TeamID via Username
	public String findUserTeamId(String userName) {
		return Objects.requireNonNull(userCollection.distinct("team_id", Filters.eq("username", userName), ObjectId.class).first()).toString();

	}

	// Returns Team Object
	public String findTeam(String teamName) {
		Document doc = teamsCollection.find(Filters.eq("team_name", teamName)).first();

		if (doc != null) {
			return doc.getObjectId("_id").toString();

		}
		return null;
	}

	public String findTeamName(String teamId) {
		return Objects.requireNonNull(teamsCollection.distinct("team_name", Filters.eq("_id", new ObjectId(teamId)), String.class).first());

	}

	// finds the size of a team with teamId
	public int findTeamSize(String teamIdStr) {
		ObjectId teamId = new ObjectId(teamIdStr);
		Integer result = teamsCollection.distinct("team_size", Filters.eq("_id", teamId), Integer.class).first();
		if (result != null) {
			return result;
		}
		return 0;

	}

	public void incrementTeamSize(String teamObjectId, int currentTeamSize) {
		teamsCollection.updateOne(
				Filters.eq("_id", new ObjectId(teamObjectId)),
				Updates.set("team_size", currentTeamSize + 1
				));
	}


	// returns a list of user Objects
	public ArrayList<User> listTeamMembers(String teamId) {
		List<Document> resultList = new ArrayList<>();
		ArrayList<User> userList = new ArrayList<>();
		userCollection.find(Filters.eq("team_id", new ObjectId(teamId))).into(resultList);

		for (Document iUser : resultList) {
			User user = new User(
					iUser.get("first_name").toString(),
					iUser.get("last_name").toString(),
					iUser.get("phone_number").toString(),
					Boolean.parseBoolean(iUser.get("is_admin").toString()),
					iUser.get("username").toString());
			userList.add(user);
		}

		return userList;

	}

	// TODO Make Dates Better
	public void uploadFile(String fileName, String documentPath, String teamName, double documentSize, String userName) {
		documentCollection.insertOne(new Document()
				.append("file_name", fileName)
				.append("team", teamName)
				.append("file_size", documentSize)
				.append("document_path", documentPath)
				.append("upload_date", new BsonDateTime(new Date().getTime()))
				.append("edit_date", new BsonDateTime(new Date().getTime()))
				.append("last_editor", userName)
				.append("is_checked_in", true)

		);

	}

	public void updateFile(String oldFilePlusTeamName, File selectedFile, String teamName, BsonDateTime dateTime) throws MongoException {
		String oldFile = Paths.get(oldFilePlusTeamName).getFileName().toString();
		Document updateFields = new Document();
		updateFields.append("document_path", selectedFile.getAbsolutePath());
		updateFields.append("edit_date", dateTime);
		Document update = new Document("$set", updateFields);

		documentCollection.updateOne(
				Filters.and(
						Filters.eq("team", teamName),
						Filters.eq("file_name", oldFile)
				),
				update
		);
	}

	public String findFileLastEditor(String filePath, String teamName) {
		String file = Paths.get(filePath).getFileName().toString();

		return documentCollection.distinct("last_editor", Filters.and(Filters.eq("file_name", file), Filters.eq("team", teamName)), String.class).first();
	}


	// finds if ile checked in
	public boolean findCheckedStatus(String filePath, String teamName) {
		String fileName = Paths.get(filePath).getFileName().toString();

		return Boolean.TRUE.equals(documentCollection.distinct("is_checked_in", Filters.and(Filters.eq("file_name", fileName), Filters.eq("team", teamName)), Boolean.class).first());

	}

	public void updateFileCheckedStatus(String filePath, String teamName, String currentUser, boolean b) throws MongoException {
		String fileName = Paths.get(filePath).getFileName().toString();

		documentCollection.updateOne(
				Filters.and(Filters.eq("file_name", fileName), Filters.eq("team", teamName)),
				Updates.set("is_checked_in", b)

		);
		documentCollection.updateOne(
				Filters.and(Filters.eq("file_name", fileName), Filters.eq("team", teamName)),
				Updates.set("last_editor", currentUser)

		);

	}

	public void removeUser(User user) {
		userCollection.deleteOne(Filters.eq("username", user.getUserName()));
	}


}
