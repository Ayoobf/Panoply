package com.example.panoply.classes;

public final class UserHolder {

    private final static UserHolder INSTANCE = new UserHolder();
    private User user;

    private UserHolder() {
    }

    public static UserHolder getINSTANCE() {
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
