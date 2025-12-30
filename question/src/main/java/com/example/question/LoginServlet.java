package com.example.question;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.question.entity.User;
import com.example.question.UserDB;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serial;

@WebServlet({"/tologin","/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = new User();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userInputCaptcha = request.getParameter("captcha");
        if (username.trim().isEmpty() || password.trim().isEmpty()||userInputCaptcha.trim().isEmpty()) {
            request.setAttribute("errorMessage","请输入文本");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
        HttpSession session = request.getSession();
        String sessionCaptcha = (String) session.getAttribute("captcha");
        session.removeAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(userInputCaptcha)) {
            request.setAttribute("errorMessage", "验证码错误！");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
        user.setUsername(username);
        user.setPassword(password);
        if(UserDB.findUser(user)){
            request.getSession().setAttribute("user",user);
            request.getRequestDispatcher("/WEB-INF/views/room.jsp").forward(request, response);
        }
        else{
            request.setAttribute("errorMessage","用户或密码错误");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}