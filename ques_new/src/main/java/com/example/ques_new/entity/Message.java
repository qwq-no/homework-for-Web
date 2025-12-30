package com.example.ques_new.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class Message {
    private Integer id;
    private String title;
    private String content;
    private Date time;
    private String sender;
    private List<Comment> comments;
}