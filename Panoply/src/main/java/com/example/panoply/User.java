package com.example.panoply;

import com.example.panoply.mongoDB.MongoDBHandlerExtra;

import org.bson.types.ObjectId;

public class User {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String teamId;
    private boolean isAdmin;
    private String userName;
    private int teamSize;


    public User(String firstName, String lastName, String phoneNumber, boolean isAdmin, String userName) {

        MongoDBHandlerExtra md = new MongoDBHandlerExtra();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.teamId = md.findUserTeamId(userName);
        this.isAdmin = isAdmin;
        this.userName = userName;

        teamSize = md.findTeamSize(teamId);

    }

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

}