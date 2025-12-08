package com.example.used.service;

import com.example.used.entity.User;

public interface UserService {
    boolean register(String username, String password);
    User login(String username, String password);
}