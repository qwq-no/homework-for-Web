package com.example.ques_new.service;

import com.example.ques_new.entity.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> findByMessageId(Integer messageId);
    void addComment(Comment comment);
}