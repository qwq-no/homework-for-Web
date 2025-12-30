package com.example.ques_new.controller;

import com.example.ques_new.entity.Comment;
import com.example.ques_new.entity.Message;
import com.example.ques_new.entity.User;
import com.example.ques_new.service.CommentService;
import com.example.ques_new.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class RoomController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommentService commentService;

    // 消息房间首页
    @GetMapping("/room")
    public String room(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login"; // 未登录跳转到登录页
        }
        model.addAttribute("messageList", messageService.findAll());
        return "room";
    }

    // 发布新消息
    @PostMapping("/room")
    public String addMessage(Message message, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        message.setSender(user.getUsername());
        message.setTime(new Date());
        messageService.addMessage(message);
        return "redirect:/room"; // 发布后刷新列表
    }

    // 查看消息详情
    @GetMapping("/message/view")
    public String viewMessage(@RequestParam Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        Message message = messageService.findById(id);
        model.addAttribute("message", message);
        model.addAttribute("comments", commentService.findByMessageId(id));
        return "message_detail";
    }

    // 提交评论
    @PostMapping("/comment/submit")
    public String addComment(Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        comment.setUsername(user.getUsername());
        comment.setTime(new Date());
        commentService.addComment(comment);
        return "redirect:/message/view?id=" + comment.getMessageId(); // 评论后返回详情页
    }
}