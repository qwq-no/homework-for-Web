package com.example.ques_new.service;

import com.example.ques_new.entity.Message;
import java.util.List;

public interface MessageService {
    List<Message> findAll();
    Message findById(Integer id);
    void addMessage(Message message);
}