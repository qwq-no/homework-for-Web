package com.example.ques_new.service.Impl;

import com.example.ques_new.entity.Comment;
import com.example.ques_new.service.CommentService;
import com.example.ques_new.util.MemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private MemoryStorage memoryStorage;

    @Override
    public List<Comment> findByMessageId(Integer messageId) {
        return memoryStorage.getCommentsByMessageId(messageId);
    }

    @Override
    public void addComment(Comment comment) {
        memoryStorage.addComment(comment);
    }
}