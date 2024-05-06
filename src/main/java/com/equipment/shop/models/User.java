package com.equipment.shop.models;


import java.io.Serializable;

public class User implements Serializable {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String fullName;

    public User() {
    }

    public User(int user_id, String username, String password, String email, String fullName) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
