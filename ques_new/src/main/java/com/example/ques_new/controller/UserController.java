package com.example.ques_new.controller;

import com.example.ques_new.entity.User;
import com.example.ques_new.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired // IOC自动注入UserService
    private UserService userService;

    // 跳转到登录页
    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }

    // 跳转到注册页
    @GetMapping("/register")
    public String toRegister() {
        return "register";
    }

    // 处理注册请求
    @PostMapping("/register")
    public String register(User user, Model model) {
        if (userService.register(user)) {
            model.addAttribute("successMessage", "注册成功！请登录");
            return "login";
        } else {
            model.addAttribute("errorMessage", "用户名已存在！");
            return "register";
        }
    }

    // 处理登录请求
    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {
        System.out.println("接收的用户名：" + username + "，密码：" + password); // 调试日志
        User user = userService.login(username, password);
        if (user != null) {
            System.out.println("登录成功，准备跳转到 room"); // 确认进入该分支
            session.setAttribute("user", user);
            return "redirect:/room"; // 重定向到 room
        } else {
            System.out.println("登录失败，用户名或密码错误");
            model.addAttribute("errorMessage", "用户名或密码错误！");
            return "login"; // 登录失败返回登录页
        }
    }

    // 首页跳转
    @GetMapping("/index")
    public String index() {
        return "index";
    }
}