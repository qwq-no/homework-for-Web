package com.example.used.DAO;
import com.example.used.entity.User;

public interface UserDao {
    User findByUsername(String username);
    void save(User user);
}