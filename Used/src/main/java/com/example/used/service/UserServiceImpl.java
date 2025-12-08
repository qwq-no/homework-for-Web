package com.example.used.service;

import com.example.used.DAO.UserDao;
import com.example.used.DAO.UserDaoImpl;
import com.example.used.entity.User;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean register(String username, String password) {
        if (userDao.findByUsername(username) != null) return false;
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordUtil.hashPassword(password));
        userDao.save(user);
        return true;
    }

    @Override
    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(PasswordUtil.hashPassword(password))) {
            return user;
        }
        return null;
    }
}