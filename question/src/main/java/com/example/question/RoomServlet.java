package com.example.question;

import com.example.question.entity.Comment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.question.entity.Message;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet({"/room","/message/view","/comment/submit"})
public class RoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static List<Message> messageList = new CopyOnWriteArrayList<>();
    private static int nextMessageId = 1;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/room")) {
            request.setAttribute("messageList", messageList);
            request.getRequestDispatcher("/WEB-INF/views/room.jsp").forward(request, response);
        } else if (requestURI.endsWith("/message/view")) {
            String messageIdStr = request.getParameter("id");
            if (messageIdStr == null || messageIdStr.isEmpty()) {
                response.getWriter().write("参数错误：缺少帖子ID。");
                return;
            }
            try {
                int messageId = Integer.parseInt(messageIdStr);
                // 使用 Stream API 查找帖子
                Message targetMessage = messageList.stream()
                        .filter(message -> message.getId() == messageId)
                        .findFirst()
                        .orElse(null);

                if (targetMessage == null) {
                    response.getWriter().write("错误：找不到ID为 " + messageId + " 的帖子。");
                    return;
                }
                request.setAttribute("message", targetMessage);
                request.getRequestDispatcher("/WEB-INF/views/message_detail.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.getWriter().write("参数错误：帖子ID必须是数字。");
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (requestURI.endsWith("/room")) {
            String username = request.getParameter("username");
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            if (username != null && !username.isEmpty() && title != null && !title.isEmpty() && content != null && !content.isEmpty()) {
                Message newMessage = new Message(nextMessageId++, title, content, new Date(), username);
                messageList.add(newMessage);
            }
            response.sendRedirect(request.getContextPath() + "/room");

        } else if (requestURI.endsWith("/comment/submit")) {
            String messageIdStr = request.getParameter("messageId");
            String username = request.getParameter("username");
            String commentContent = request.getParameter("commentContent");
            if (messageIdStr == null || username == null || commentContent == null ||
                    messageIdStr.isEmpty() || username.isEmpty() || commentContent.isEmpty()) {
                response.getWriter().write("参数错误：请填写完整评论信息。");
                return;
            }

            try {
                int messageId = Integer.parseInt(messageIdStr);
                Message targetMessage = messageList.stream()
                        .filter(message -> message.getId() == messageId)
                        .findFirst()
                        .orElse(null);

                if (targetMessage == null) {
                    response.getWriter().write("错误：找不到对应的帖子。");
                    return;
                }

                // 创建并添加评论
                Comment newComment = new Comment(username, commentContent, new Date());
                targetMessage.addComment(newComment);

                // 重定向回该帖子的详情页，使用 PRG 模式防止重复提交
                response.sendRedirect(request.getContextPath() + "/message/view?id=" + messageId);

            } catch (NumberFormatException e) {
                response.getWriter().write("参数错误：帖子ID格式不正确。");
            }
        }
    }
}