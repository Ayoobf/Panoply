package com.example.panoply;

import com.example.panoply.mongoDB.MongoDBHandlerExtra;
import com.google.cloud.MonitoredResourceDescriptor;

import org.bson.types.ObjectId;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String teamId;
    private boolean isAdmin;
    private String userName;
    private int teamSize;


    //---------------------------------------------------------------------------------------------------------//
    //Properties for the Table View
    private StringProperty firstNameSP;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public String getTeamId() {
        return teamId;
    }
    private StringProperty lastNameSP;
    private StringProperty emailSP;
    private StringProperty adminSP;

    public User(String firstName, String lastName, String phoneNumber, boolean isAdmin, String userName) {

        MongoDBHandlerExtra md = new MongoDBHandlerExtra();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.teamId = md.findUserTeamId(userName);
        this.isAdmin = isAdmin;
        this.userName = userName;

        setFirstNameSP(firstName);
        setLastNameSP(lastName);
        setEmailSP(userName);
        setAdminSP(String.valueOf(isAdmin));

        teamSize = md.findTeamSize(teamId);

    }

    public void setFirstNameSP(String value) {
        firstNameProperty().set(value);
    }

    public StringProperty firstNameProperty() {
        if (firstNameSP == null) firstNameSP = new SimpleStringProperty(this, "firstName");
        return firstNameSP;
    }

    public void setLastNameSP(String value) {
        lastNameProperty().set(value);
    }

    public StringProperty lastNameProperty() {
        if (lastNameSP == null) lastNameSP = new SimpleStringProperty(this, "lastName");
        return lastNameSP;
    }

    public void setEmailSP(String value) {
        emailProperty().set(value);
    }

    public StringProperty emailProperty() {
        if (emailSP == null) emailSP = new SimpleStringProperty(this, "email");
        return emailSP;
    }

    public void setAdminSP(String value) {
        adminProperty().set(value);
    }

    public StringProperty adminProperty() {
        if (adminSP == null) adminSP = new SimpleStringProperty(this, "admin");
        return adminSP;
    }


    @Override
    public String toString() {
        return firstName + " " + lastName + " " + phoneNumber + " " + isAdmin + " " + userName;
    }
}