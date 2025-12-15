package com.example.talking_room;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import listener.OnlineUserListener;

@WebServlet("/talking_room")
public class TalkingServlet extends HttpServlet {
    private Map<String, User> userMap = new ConcurrentHashMap<>();

    // 存储消息列表
    private List<Message> messages = new ArrayList<>();

    // 消息ID生成器
    private AtomicInteger messageId = new AtomicInteger(0);

    // 日期格式化器
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        // 处理获取新消息的请求
        if ("getNewMessages".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                out.print("{\"success\":false,\"message\":\"请先登录\"}");
                return;
            }
            String currentUsername = (String) session.getAttribute("username");

            int lastId = 0;
            try {
                String lastIdStr = request.getParameter("lastId");
                if (lastIdStr != null && !lastIdStr.isEmpty()) {
                    lastId = Integer.parseInt(lastIdStr);
                }
            } catch (NumberFormatException e) {
            }

            List<Message> newMessages = new ArrayList<>();
            synchronized (messages) {
                for (Message msg : messages) {
                    if (msg.getId() > lastId) {
                        String receiveUsers = msg.getReceiveUsers();
                        boolean isAll = "all".equals(receiveUsers);
                        boolean isSelfSend = msg.getUsername().equals(currentUsername);
                        boolean isSendToMe = !isAll && Arrays.asList(receiveUsers.split(",")).contains(currentUsername);
                        if (isAll || isSelfSend || isSendToMe) {
                            newMessages.add(msg);
                        }
                    }
                }
            }

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < newMessages.size(); i++) {
                Message msg = newMessages.get(i);
                String receiveDesc = "所有人";
                if (!"all".equals(msg.getReceiveUsers())) {
                    receiveDesc = msg.getReceiveUsers();
                }

                json.append("{")
                        .append("\"id\":").append(msg.getId()).append(",")
                        .append("\"username\":\"").append(escapeJson(msg.getUsername())).append("\",")
                        .append("\"content\":\"").append(escapeJson(msg.getContent())).append("\",")
                        .append("\"time\":\"").append(escapeJson(msg.getTime())).append("\",")
                        .append("\"receiveDesc\":\"").append(escapeJson(receiveDesc)).append("\"")
                        .append("}");

                if (i < newMessages.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            out.print(json.toString());
        }
        else if ("getOnlineUsers".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                out.print("{\"success\":false,\"message\":\"请先登录\"}");
                return;
            }

            Map<String, String> onlineUsers = OnlineUserListener.getOnlineUsers(); // key=username, value=username
            StringBuilder json = new StringBuilder("{\"success\":true,\"onlineCount\":")
                    .append(OnlineUserListener.getOnlineCount())
                    .append(",\"users\":{");

            int count = 0;
            for (Map.Entry<String, String> entry : onlineUsers.entrySet()) {
                if (count > 0) json.append(",");
                json.append("\"").append(escapeJson(entry.getKey())).append("\":\"").append(escapeJson(entry.getValue())).append("\"");
                count++;
            }
            json.append("}}");
            out.print(json.toString());
        } else {
            response.sendRedirect("no_same.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String type = request.getParameter("type");

        if ("register".equals(type)) {
            handleRegistration(request, response);
        } else if ("login".equals(type)) {
            handleLogin(request, response);
        } else if ("sendMessage".equals(type)) {
            handleSendMessage(request, response);
        } else if ("heartbeat".equals(type)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String username = (String) session.getAttribute("username");
                if (username != null) {
                    OnlineUserListener.updateActive(username);
                }
            }
        }
        else {
            response.sendRedirect("index.jsp");
        }
    }

    // 处理用户注册
    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");

        if (username == null || username.trim().isEmpty() ||
                password == null || !password.equals(password2)) {
            response.sendRedirect("no_same.jsp");
            return;
        }

        if (userMap.containsKey(username)) {
            response.sendRedirect("same.jsp");
            return;
        }
        userMap.put(username, new User(username, password));
        response.sendRedirect("index.jsp");
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute("captchaCode");
        String userInput = request.getParameter("captchaInput");

        if (sessionCode == null || !sessionCode.equalsIgnoreCase(userInput)) {
            session.removeAttribute("captchaCode");
            response.sendRedirect("captcha_error.jsp");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userMap.get(username);

        if (user == null || !user.getPassword().equals(password)) {
            response.sendRedirect("mm_error.jsp");
            return;
        }

        session.setAttribute("username", username);
        OnlineUserListener.addOnlineUser(username);
        Map<String, String> onlineUsers = OnlineUserListener.getOnlineUsers();
        Set<String> allOnline = new HashSet<>(onlineUsers.keySet());
        sendSystemMessageToUsers(username + " 用户已登录", allOnline);
        response.sendRedirect("talking.jsp");
    }

    // 处理发送消息
    private void handleSendMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.getWriter().print("{\"success\":false,\"message\":\"请先登录\"}");
            return;
        }

        String username = (String) session.getAttribute("username");
        String content = request.getParameter("content");
        String receiveUsers = request.getParameter("receiveUsers");

        if (content == null || content.trim().isEmpty()) {
            response.getWriter().print("{\"success\":false,\"message\":\"消息内容不能为空\"}");
            return;
        }

        if (receiveUsers == null || receiveUsers.trim().isEmpty()) {
            receiveUsers = "all";
        }

        Message message = new Message(
                messageId.incrementAndGet(),
                username,
                content.trim(),
                sdf.format(new Date()),
                receiveUsers
        );

        synchronized (messages) {
            messages.add(message);
            if (messages.size() > 1000) {
                messages.remove(0);
            }
        }

        response.getWriter().print("{\"success\":true}");
    }
    private void sendSystemMessageToUsers(String content, Set<String> receivers) {
        if (receivers == null || receivers.isEmpty()) return;

        Message sysMsg = new Message(
                messageId.incrementAndGet(),
                "系统",
                content,
                sdf.format(new Date()),
                String.join(",", receivers)
        );

        synchronized (messages) {
            messages.add(sysMsg);
            if (messages.size() > 1000) {
                messages.remove(0);
            }
        }
    }


    private static TalkingServlet instance;

    @Override
    public void init() throws ServletException {
        instance = this;
        OnlineUserListener.setUserOfflineCallback(this::broadcastUserOffline);
    }

    public static TalkingServlet getInstance() {
        return instance;
    }

    // 提供给 Listener 调用的方法
    public void broadcastUserOffline(String username) {
        Map<String, String> onlineUsers = OnlineUserListener.getOnlineUsers();
        Set<String> receivers = new HashSet<>(onlineUsers.keySet());

        if (!receivers.isEmpty()) {
            sendSystemMessageToUsers(username + " 用户已退出", receivers);
        }
    }
    // JSON 转义工具
    private String escapeJson(String value) {
        if (value == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '/': sb.append("\\/"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }

    // 用户实体类（移除 nickname）
    private static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
    }

    // 消息实体类（移除 nickname）
    private static class Message {
        private int id;
        private String username;
        private String content;
        private String time;
        private String receiveUsers;

        public Message(int id, String username, String content, String time, String receiveUsers) {
            this.id = id;
            this.username = username;
            this.content = content;
            this.time = time;
            this.receiveUsers = receiveUsers;
        }

        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getContent() { return content; }
        public String getTime() { return time; }
        public String getReceiveUsers() { return receiveUsers; }
    }
}