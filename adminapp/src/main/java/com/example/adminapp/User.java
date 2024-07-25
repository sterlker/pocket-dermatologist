package com.example.adminapp;

public class User {
    private String username;
    private String expand_user;

    public User(String username, String expand_user) {
        this.username = username;
        this.expand_user = expand_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExpand_user() {
        return expand_user;
    }

    public void setExpand_user(String expand_user) {
        this.expand_user = expand_user;
    }
}
