package com.example.used.Controller;

import com.example.used.DAO.ItemDao;
import com.example.used.DAO.ItemDaoImpl;
import com.example.used.entity.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.used.entity.User;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class ItemListServlet extends HttpServlet {
    private ItemDao itemdao = new ItemDaoImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() +"/views/login.jsp");
            return;
        }
        String keyword = req.getParameter("q");
        List<Item> items;
        if (keyword != null && !keyword.trim().isEmpty()) {
            items = itemdao.searchItems(keyword.trim());
            req.setAttribute("keyword", keyword);
        } else {
            items = itemdao.findAll();
        }

        req.setAttribute("items", items);
        req.getRequestDispatcher("/views/home.jsp").forward(req, resp);
    }
}