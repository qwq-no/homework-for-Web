package com.example.ques_new.service;

import com.example.ques_new.entity.User;

public interface UserService {
    boolean register(User user);
    User login(String username, String password);
    boolean existsUsername(String username);
}