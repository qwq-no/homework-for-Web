package com.example.ques_new.util;

import com.example.ques_new.entity.Comment;
import com.example.ques_new.entity.Message;
import com.example.ques_new.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component // 注入IOC容器，全局复用
public class MemoryStorage {
    // ===== 用户数据 =====
    private final Map<String, User> userMap = new ConcurrentHashMap<>(); // 用户名 -> 用户（唯一索引）
    private final AtomicInteger userIdGenerator = new AtomicInteger(1);   // 用户ID自增

    // ===== 消息数据 =====
    private final Map<Integer, Message> messageMap = new ConcurrentHashMap<>(); // 消息ID -> 消息
    private final AtomicInteger messageIdGenerator = new AtomicInteger(1);     // 消息ID自增

    // ===== 评论数据 =====
    private final Map<Integer, List<Comment>> commentMap = new ConcurrentHashMap<>(); // 消息ID -> 评论列表
    private final AtomicInteger commentIdGenerator = new AtomicInteger(1);           // 评论ID自增

    // ------------------------------ 用户操作 ------------------------------
    // 新增用户
    public boolean addUser(User user) {
        if (userMap.containsKey(user.getUsername())) {
            return false; // 用户名已存在
        }
        user.setId(userIdGenerator.getAndIncrement());
        userMap.put(user.getUsername(), user);
        return true;
    }

    // 根据用户名查询用户
    public User getUserByUsername(String username) {
        return userMap.get(username);
    }

    // 登录验证（用户名+密码）
    public User login(String username, String password) {
        User user = userMap.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // ------------------------------ 消息操作 ------------------------------
    // 新增消息
    public void addMessage(Message message) {
        message.setId(messageIdGenerator.getAndIncrement());
        messageMap.put(message.getId(), message);
        // 初始化该消息的评论列表
        commentMap.put(message.getId(), new ArrayList<>());
    }

    // 查询所有消息
    public List<Message> getAllMessages() {
        return new ArrayList<>(messageMap.values());
    }

    // 根据ID查询消息
    public Message getMessageById(Integer id) {
        Message message = messageMap.get(id);
        if (message != null) {
            // 关联评论
            message.setComments(commentMap.get(id));
        }
        return message;
    }

    // ------------------------------ 评论操作 ------------------------------
    // 新增评论
    public void addComment(Comment comment) {
        comment.setId(commentIdGenerator.getAndIncrement());
        List<Comment> comments = commentMap.get(comment.getMessageId());
        if (comments != null) {
            comments.add(comment);
        }
    }

    // 根据消息ID查询评论
    public List<Comment> getCommentsByMessageId(Integer messageId) {
        return commentMap.getOrDefault(messageId, new ArrayList<>());
    }
}