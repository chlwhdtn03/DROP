package com.david0926.drop.model;

public class UserModel {

    private String name, email, profile;

    public UserModel() { }

    public UserModel(String name, String email, String profile) {
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
