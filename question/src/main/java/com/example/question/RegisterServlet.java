package com.example.question;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.question.entity.User;
import com.example.question.UserDB;
import java.io.IOException;

@WebServlet({"/toregister","/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String preage = request.getParameter("age");
        if(username.trim().isEmpty() || password.trim().isEmpty() || preage.trim().isEmpty()) {
            request.setAttribute("errorMessage","输入不能为空！");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
        int age = Integer.parseInt(preage);
        if(age>130 || age<0){request.setAttribute("errorMessage","请输入正常的年龄");}
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAge(age);
        user.setID(UserDB.getID());
        if(!UserDB.addUser(user)){
            request.setAttribute("errorMessage","用户名已被注册!");
        }
        else{
            request.setAttribute("successMessage","成功注册！");
        }
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
}
