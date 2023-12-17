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
import java.nio.file.DirectoryStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Ayoob Florival
 */
public class MongoDBHandler {
	/**
	 * uri comes from the app.properties file under "url"
	 */
	private static final String uri = AppProperties.getInstance().getProperty("url");
	/**
	 * creates client from uri
	 */
	private static final MongoClient client = MongoClients.create(uri);
	/**
	 * MongoDB database Object created from client using app.properties' "db_name"
	 */
	private static final MongoDatabase database = client
			.getDatabase(AppProperties.getInstance().getProperty("db_name"));
	/**
	 * user collection created using the "db_userCollectionName" specified in app.properties file
	 */
	private static final MongoCollection<Document> userCollection = database
			.getCollection(AppProperties.getInstance().getProperty("db_userCollectionName"));
	/**
	 * teams collection created using the "db_teamCollectionName" specified in app.properties file
	 */
	private static final MongoCollection<Document> teamsCollection = database
			.getCollection(AppProperties.getInstance().getProperty("db_teamCollectionName"));
	/**
	 * document collection created using the "db_documentCollectionName" specified in app.properties file
	 */
	private static final MongoCollection<Document> documentCollection = database
			.getCollection(AppProperties.getInstance().getProperty("db_documentCollectionName"));
	/**
	 * authenticator Object used for password encryption and decryption
	 */
	private static final BCryptPasswordEncoder authenticator = new BCryptPasswordEncoder();

	/**
	 * Empty Constructor used for instantiation. There is probably a better way to handle this but, it is what it is.
	 */
	public MongoDBHandler() {
	}

	/**
	 * Used to authenticate user
	 * <p>
	 * Authenticator for username and password.
	 * </p>
	 *
	 * @param username username of the user to be checked
	 * @param password password of the user to be checked
	 * @return 1 if user is found. 0 if no user is found
	 */
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

	/**
	 * Checks if user exist within the database
	 * <p>
	 * Checks whether that username exists within the MongoDB.
	 * </p>
	 *
	 * @param username username of the user to be checked
	 * @return true if user exists. false if user does not exist already.
	 */
	public boolean userExists(String username) {
		Document doc = userCollection.find(Filters.eq("username", username)).first();

		return doc != null;
	}

	/**
	 * Signup user
	 * <p>
	 * Signup user with given fields. Used in cases where the user is not an admin.
	 * </p>
	 *
	 * @param firstName   first name of the user
	 * @param lastName    last name of the user
	 * @param email       email of the user
	 * @param password    non-hashed password of the user
	 * @param isAdmin     bool of if user is an admin (usually false in this case)
	 * @param teamId      team id (or team name) of the user
	 * @param phoneNumber phone number of the user
	 */
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

	/**
	 * Signs up user
	 * <p>
	 * Signs up user with given params
	 * </p>
	 *
	 * @param firstName   first name of user
	 * @param lastName    last name of user
	 * @param email       email of user
	 * @param password    password of user
	 * @param isAdmin     admin bool (usually true)
	 * @param phoneNumber phone number of user
	 */
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

	/**
	 * Used to update user's teamId
	 * <p>
	 * Used to update user's teamId. Usually called right after using signUpUser().
	 * </p>
	 *
	 * @param userObjectId object ID of current user
	 * @param teamObjectId Object ID of the team to assign to the specified user
	 */
	public void updateUser(String userObjectId, String teamObjectId) {
		userCollection.updateOne(
				Filters.eq("_id", new ObjectId(userObjectId)),
				Updates.set("team_id", new ObjectId(teamObjectId)
				));
	}

	/**
	 * Makes team
	 * <p>
	 * Makes team for the user. Usually called right before calling updateUser();
	 * </p>
	 *
	 * @param teamName  name of the team to make
	 * @param adminName name of the admin for the team (usually current user at signup)
	 */
	public void makeTeam(String teamName, String adminName) {

		teamsCollection.insertOne(new Document()
				.append("_id", new ObjectId())
				.append("team_name", teamName)
				.append("team_size", 1)
				.append("admin", new ObjectId(adminName))
		);
	}

	/**
	 * Finds user by email
	 * <p>
	 * Finds user by email and returns the id as a String. Mainly used to feed other methods with values at signup.
	 * </p>
	 *
	 * @param email email of the user
	 * @return ID of the user if found. Null if not.
	 */
	public String findUser(String email) {
		Document doc = userCollection.find(Filters.eq("username", email)).first();

		if (doc != null) {
			return doc.getObjectId("_id").toString();

		}
		return null;
	}

	/**
	 * finds user first name by username/email
	 *
	 * @param userName username/email of target
	 * @return users' first name
	 */
	public String findUserFirstName(String userName) {
		return userCollection.distinct("first_name", Filters.eq("username", userName), String.class).first();

	}

	/**
	 * finds user last name by username/email
	 *
	 * @param userName username/email of target
	 * @return users' last name
	 */
	public String findUserLastName(String userName) {
		return userCollection.distinct("last_name", Filters.eq("username", userName), String.class).first();

	}

	/**
	 * finds user phone number by username/email
	 *
	 * @param userName username/email of target
	 * @return users' phone number
	 */
	public String findUserPhoneNumber(String userName) {
		return userCollection.distinct("phone_number", Filters.eq("username", userName), String.class).first();

	}

	/**
	 * finds user admin status as bool by target username/email
	 *
	 * @param userName username/email of target
	 * @return admin status
	 */
	public boolean findUserAdminStatus(String userName) {
		return Boolean.TRUE.equals(userCollection.distinct("is_admin", Filters.eq("username", userName), Boolean.class).first());

	}

	/**
	 * finds user team id by username
	 *
	 * @param userName username of target
	 * @return users' team's ID as String
	 */
	public String findUserTeamId(String userName) {
		return Objects.requireNonNull(userCollection.distinct("team_id", Filters.eq("username", userName), ObjectId.class).first()).toString();

	}

	/**
	 * finds team id using team's name
	 *
	 * @param teamName team of target
	 * @return id of target team
	 */
	public String findTeam(String teamName) {
		Document doc = teamsCollection.find(Filters.eq("team_name", teamName)).first();

		if (doc != null) {
			return doc.getObjectId("_id").toString();

		}
		return null;
	}

	/**
	 * finds team name via team id
	 *
	 * @param teamId teamId of target
	 * @return team name of target
	 */
	public String findTeamName(String teamId) {
		return Objects.requireNonNull(teamsCollection.distinct("team_name", Filters.eq("_id", new ObjectId(teamId)), String.class).first());

	}

	/**
	 * finds team's size via team id
	 *
	 * @param teamIdStr id of the target team
	 * @return number of team members. 0 if none.
	 */
	public int findTeamSize(String teamIdStr) {
		ObjectId teamId = new ObjectId(teamIdStr);
		Integer result = teamsCollection.distinct("team_size", Filters.eq("_id", teamId), Integer.class).first();
		if (result != null) {
			return result;
		}
		return 0;

	}

	/**
	 * increments the team's size by 1
	 *
	 * @param teamObjectId    object id of target team
	 * @param currentTeamSize size of team before incrementing
	 */
	public void incrementTeamSize(String teamObjectId, int currentTeamSize) {
		teamsCollection.updateOne(
				Filters.eq("_id", new ObjectId(teamObjectId)),
				Updates.set("team_size", currentTeamSize + 1
				));
	}

	/**
	 * decrements the team's size by 1
	 *
	 * @param teamObjectId    object id of target team
	 * @param currentTeamSize size of team before decrementing
	 */
	public void decrementTeamSize(String teamObjectId, int currentTeamSize) {
		teamsCollection.updateOne(
				Filters.eq("_id", new ObjectId(teamObjectId)),
				Updates.set("team_size", currentTeamSize - 1
				));
	}


	/**
	 * lists all the team members
	 * <p>
	 * populates an arraylist with User Objects that belong to the specified team
	 * </p>
	 *
	 * @param teamId target teams' ID
	 * @return A populated arraylist of user object that belong to that specified team
	 */
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

	/**
	 * uploads file to mongo
	 * <p>
	 * uploads file to mongodb with the given params. Usually used in conjunction with the Google counterpart
	 * </p>
	 *
	 * @param fileName     file name to be uploaded
	 * @param documentPath path of file to be uploaded
	 * @param teamName     team name of the current user
	 * @param documentSize size of the document
	 * @param userName     username of current user
	 */
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

	/**
	 * updates a files team name and file name
	 * <p>
	 * updates file for use in the check in and out system
	 * </p>
	 *
	 * @param oldFilePlusTeamName path of the old file which contains the team name and file name
	 * @param selectedFile        the File object of target to update
	 * @param teamName            current user team name
	 * @param dateTime            new date time
	 * @throws MongoException yes :)
	 */
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

	/**
	 * finds the last editor of a file
	 *
	 * @param filePath path of file to find last editor
	 * @param teamName current user team name
	 * @return last user to edit/checkin/checkout file
	 */
	public String findFileLastEditor(String filePath, String teamName) {
		String file = Paths.get(filePath).getFileName().toString();

		return documentCollection.distinct("last_editor", Filters.and(Filters.eq("file_name", file), Filters.eq("team", teamName)), String.class).first();
	}


	/**
	 * finds out if file is checked in or out
	 *
	 * @param filePath path to file its checking
	 * @param teamName current user team name
	 * @return true if file is checked in. false if file is checked out
	 */
	public boolean findCheckedStatus(String filePath, String teamName) {
		String fileName = Paths.get(filePath).getFileName().toString();

		return Boolean.TRUE.equals(documentCollection.distinct("is_checked_in", Filters.and(Filters.eq("file_name", fileName), Filters.eq("team", teamName)), Boolean.class).first());

	}

	/**
	 * updates a filed checked status
	 * <p>
	 * updates the checked status as well as the last editor with the new editor name
	 * </p>
	 *
	 * @param filePath    path to target file
	 * @param teamName    current user team name
	 * @param currentUser current user username
	 * @param b           new bool status
	 * @throws MongoException deal with it
	 */
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

	/**
	 * removes a user from MongoDB
	 *
	 * @param user the user to remove
	 */
	public void removeUser(User user) {
		decrementTeamSize(user.getTeamId(), user.getTeamSize());
		userCollection.deleteOne(Filters.eq("username", user.getUserName()));

	}

	public String findFileStats(String fileWithTeamName, String teamName) {
		String file = Paths.get(fileWithTeamName).getFileName().toString();
		Document doc = Objects
				.requireNonNull(
						documentCollection.find(
								Filters.and(
										Filters.eq("file_name", file),
										Filters.eq("team", teamName))
						).first());

		String fileName = doc.getString("file_name");
		double fileSize = doc.getDouble("file_size");
		Date uploadDate = doc.getDate("upload_date");
		Date lastEditDate = doc.getDate("edit_date");
		String lastPersonToUpdate = doc.getString("last_editor");
		boolean checked = doc.getBoolean("is_checked_in");

		return "File Name: " + fileName +
				"\nFile Size: " + fileSize +
				"\nFile Upload Date: " + uploadDate +
				"\nFile Last Edit Date: " + lastEditDate +
				"\nFile Last Editor: " + lastPersonToUpdate +
				"\nFile is Check In? " + checked;
	}

	public boolean fileExists(String fileName, String currentUserTeamName) {
		Document doc = documentCollection.find(Filters.and(Filters.eq("file_name", fileName), Filters.eq("team", currentUserTeamName))).first();

		return doc != null;
	}

	public void updateFileSize(String fileName, String currentUserTeamName, double newSize) {
		documentCollection.updateOne(
				Filters.and(Filters.eq("file_name", fileName), Filters.eq("team", currentUserTeamName)),
				Updates.set("file_size", newSize
				));


	}
}
