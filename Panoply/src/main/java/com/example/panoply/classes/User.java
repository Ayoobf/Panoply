package com.example.panoply.classes;

import com.example.panoply.handlers.MongoDBHandler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Ayoob Florival <ayoob.florival@gmail.com>
 */
public class User {
	private final boolean isAdmin;
	private final String userName;
	private final String firstName;
	private final String lastName;
	private final String phoneNumber;
	private final String teamId;
	private final String teamName;
	private final int teamSize;
	/**
	 * StringProperty version of firstName var
	 */
	private StringProperty firstNameSP;
	/**
	 * StringProperty version of lastName var
	 */
	private StringProperty lastNameSP;
	/**
	 * StringProperty version of email var
	 */
	private StringProperty emailSP;
	/**
	 * StringProperty version of isAdmin var
	 */
	private StringProperty adminSP;

	/**
	 * Only constructor for our user. Mainly used for User signup.
	 *
	 * @param firstName   first name of the user
	 * @param lastName    last name of the user
	 * @param phoneNumber phone number of the user
	 * @param isAdmin     is admin bool of user
	 * @param userName    users username/email
	 */
	public User(String firstName, String lastName, String phoneNumber, boolean isAdmin, String userName) {

		MongoDBHandler md = new MongoDBHandler();

		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.teamId = md.findUserTeamId(userName);
		this.teamName = md.findTeamName(teamId);
		this.isAdmin = isAdmin;
		this.userName = userName;
		this.teamSize = md.findTeamSize(teamId);

		setFirstNameSP(firstName);
		setLastNameSP(lastName);
		setEmailSP(userName);
		setAdminSP(String.valueOf(isAdmin));

	}

	/**
	 * get username/email
	 *
	 * @return username as String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * gets team size
	 *
	 * @return team size
	 */
	public int getTeamSize() {
		return teamSize;
	}

	/**
	 * gets admin status
	 *
	 * @return true if user is admin. else false
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * gets user first name
	 *
	 * @return first name of user
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * gets the team name of the user from MongoDB
	 *
	 * @return team name of user
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * gets team id from MongoDB
	 *
	 * @return team id of user as String
	 */
	public String getTeamId() {
		return teamId;
	}

	/**
	 * sets user first name
	 *
	 * @param value new name to set the StringProperty
	 */
	public void setFirstNameSP(String value) {
		firstNameProperty().set(value);
	}

	/**
	 * getter for first name as StringProperty
	 *
	 * @return first name as StringProperty
	 */
	public StringProperty firstNameProperty() {
		if (firstNameSP == null) firstNameSP = new SimpleStringProperty(this, "firstName");
		return firstNameSP;
	}

	/**
	 * sets user last name
	 *
	 * @param value new name to set the StringProperty
	 */
	public void setLastNameSP(String value) {
		lastNameProperty().set(value);
	}

	/**
	 * getter for last name as StringProperty
	 *
	 * @return last name as StringProperty
	 */
	public StringProperty lastNameProperty() {
		if (lastNameSP == null) lastNameSP = new SimpleStringProperty(this, "lastName");
		return lastNameSP;
	}

	/**
	 * sets user email StringProperty
	 *
	 * @param value new email to set the StringProperty
	 */
	public void setEmailSP(String value) {
		emailProperty().set(value);
	}

	/**
	 * getter for email as StringProperty
	 *
	 * @return email as StringProperty
	 */
	public StringProperty emailProperty() {
		if (emailSP == null) emailSP = new SimpleStringProperty(this, "email");
		return emailSP;
	}

	/**
	 * sets user admin bool StringProperty
	 *
	 * @param value admin status to set the StringProperty
	 */
	public void setAdminSP(String value) {
		adminProperty().set(value);
	}

	/**
	 * getter for admin bool as StringProperty
	 *
	 * @return admin bool as StringProperty
	 */
	public StringProperty adminProperty() {
		if (adminSP == null) adminSP = new SimpleStringProperty(this, "admin");
		return adminSP;
	}

	/**
	 * custom toSting() for user Object
	 *
	 * @return first name, last name, phone number, admin bool, and username as a truncated string
	 */
	@Override
	public String toString() {
		return firstName + " " + lastName + " " + phoneNumber + " " + isAdmin + " " + userName;
	}
}