package com.example.topquiz.Model;

public class User {
    private String mFirstName;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public User(String firstName) {
        mFirstName = firstName;
    }

    public User() {
    }
}
