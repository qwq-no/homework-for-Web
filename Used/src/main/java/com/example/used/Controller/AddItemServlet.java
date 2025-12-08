package com.example.used.Controller;

import com.example.used.DAO.ItemDao;
import com.example.used.DAO.ItemDaoImpl;
import com.example.used.entity.Item;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.used.entity.User;
import java.io.IOException;
@WebServlet("/addItem")
public class AddItemServlet extends HttpServlet {
    private ItemDao itemdao = new ItemDaoImpl();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) { resp.sendRedirect(req.getContextPath() +"/views/login.jsp"); return; }
        String name = req.getParameter("name");
        String desc = req.getParameter("description");
        Item item = new Item();
        item.setName(name);
        item.setDescription(desc);
        item.setUserId(user.getId());
        itemdao.save(item);
        resp.sendRedirect("home");
    }
}