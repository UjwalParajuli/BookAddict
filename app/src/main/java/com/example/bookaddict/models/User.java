package com.example.bookaddict.models;

public class User {
    private int user_id;
    private String full_name,email, phone, password;

    public User(int user_id, String full_name, String email, String phone, String password) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
