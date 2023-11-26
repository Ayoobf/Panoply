package com.example.panoply;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User2 {
    private StringProperty firstName;
    private StringProperty lastName;

    public User2(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getFirstName() {
        return firstNameProperty().get();
    }

    public void setFirstName(String value) {
        firstNameProperty().set(value);
    }

    public StringProperty firstNameProperty() {
        if (firstName == null) firstName = new SimpleStringProperty(this, "firstName");
        return firstName;
    }

    public String getLastName() {
        return lastNameProperty().get();
    }

    public void setLastName(String value) {
        lastNameProperty().set(value);
    }

    public StringProperty lastNameProperty() {
        if (lastName == null) lastName = new SimpleStringProperty(this, "lastName");
        return lastName;
    }

}
