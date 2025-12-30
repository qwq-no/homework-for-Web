package com.example.ques_new.service.Impl;

import com.example.ques_new.entity.User;
import com.example.ques_new.service.UserService;
import com.example.ques_new.util.MemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 注入IOC容器
public class UserServiceImpl implements UserService {

    @Autowired // IOC自动注入内存存储工具
    private MemoryStorage memoryStorage;

    @Override
    public boolean register(User user) {
        return memoryStorage.addUser(user);
    }

    @Override
    public User login(String username, String password) {
        return memoryStorage.login(username, password);
    }

    @Override
    public boolean existsUsername(String username) {
        return memoryStorage.getUserByUsername(username) != null;
    }
}