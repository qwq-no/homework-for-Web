package com.example.used.Controller;

import com.example.used.DAO.ItemDao;
import com.example.used.DAO.ItemDaoImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.used.entity.User;
import java.io.IOException;

@WebServlet("/deleteItem")
public class DeleteItemServlet extends HttpServlet {
    private ItemDao itemdao = new ItemDaoImpl();
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        int itemId = Integer.parseInt(req.getParameter("id"));

        if (itemdao.findByIdAndUserId(itemId,user.getId()) != null) {
            itemdao.deleteById(itemId);
        } else {
            throw new SecurityException("商品不存在或无权操作该商品");
        }
        resp.sendRedirect(req.getContextPath() +"/home");
    }
}