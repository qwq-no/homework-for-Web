package com.example.question.entity;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Message {
    private int id;
    private String title;
    private String content;
    private Date time;
    private String  sender;
    private List<Comment> comments;
    public Message() {}
    public Message(int id, String title, String content, Date time, String sender) {
        this.id = id;this.title = title;this.content = content;this.time = time;
        this.sender = sender;this.comments = new CopyOnWriteArrayList<>();
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public List<Comment> getComments() {return comments;}
    public void setComments(List<Comment> comments) {this.comments = comments;}
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}

