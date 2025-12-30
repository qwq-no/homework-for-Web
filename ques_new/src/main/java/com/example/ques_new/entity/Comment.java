package com.example.ques_new.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Comment {
    private Integer id;
    private Integer messageId;
    private String username;
    private String content;
    private Date time;
}