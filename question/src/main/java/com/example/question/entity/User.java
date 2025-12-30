package com.example.question.entity;

public class User {
    private String username;
    private String password;
    private Integer ID;
    private Integer age;
    public User() {
    }
    public User(String username,String password ,Integer ID, int age) {
        this.username = username;
        this.password = password;
        this.ID = ID;
        this.age = age;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getID() {
        return ID;
    }
    public void setID(Integer ID) {
        this.ID = ID;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}