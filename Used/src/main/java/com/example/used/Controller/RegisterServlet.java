package com.example.used.Controller;

import com.example.used.service.UserService;
import com.example.used.service.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService user = new UserServiceImpl();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username.trim().isEmpty() || password.trim().isEmpty()) {
            resp.sendRedirect(request.getContextPath() + "/views/register.jsp?error=0");
            return;
        }
        if(!user.register(username,password)){
            resp.sendRedirect(request.getContextPath() + "/views/register.jsp?error=1");
        }
        else{
            resp.sendRedirect(request.getContextPath() + "/views/register.jsp?error=2");
        }
    }
}
