package com.example.ques_new.service.Impl;

import com.example.ques_new.entity.Message;
import com.example.ques_new.service.MessageService;
import com.example.ques_new.util.MemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MemoryStorage memoryStorage;

    @Override
    public List<Message> findAll() {
        return memoryStorage.getAllMessages();
    }

    @Override
    public Message findById(Integer id) {
        return memoryStorage.getMessageById(id);
    }

    @Override
    public void addMessage(Message message) {
        memoryStorage.addMessage(message);
    }
}