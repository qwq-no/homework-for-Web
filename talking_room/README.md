# 聊天室系统（Talking Room）

一个基于 Java Servlet + JSP 的简易实时聊天室系统，支持多用户在线聊天、私信、登录状态管理及系统通知。

---

## ✨ 功能特性

- **用户注册与登录**：带验证码校验的登录/注册功能。
- **实时消息收发**：支持公聊和指定用户私聊。
- **在线用户列表**：自动维护当前在线用户状态。
- **心跳保活机制**：通过前端定时心跳防止用户意外掉线。
- **系统通知**：
  - 用户登录时，所有在线用户（包括自己）收到“XXX 已登录”提示。
  - 用户离线（超时或关闭浏览器）后，其他用户收到“XXX 已退出”提示。
- **消息持久化**：服务端内存中缓存最近 1000 条消息，新用户可查看历史记录。
- **响应式前端**：简洁的聊天界面，支持自动滚动、消息高亮。

---

## 🛠 技术栈

- 后端：Java Servlet 3.1 + JSP
- 前端：HTML5 + CSS3 + JavaScript（原生 Fetch API）
- 容器：Tomcat 9+
- 依赖：无第三方框架（纯原生实现）

---

## 📁 项目结构

```
talking-room/
├── src/
│   └── com/
│       └── example/
│           ├── servlet/
│           │   ├── TalkingServlet.java
│           │   └── CaptchaServlet.java
│           └── filter/
│           │   └── IllegalAccessFilter.java
│           └── listener/
│               └── OnlineUserListener.java
├── web/
│   ├── index.jsp
│   ├── talking.jsp
│   └── WEB-INF/
│       └── web.xml
└── README.md
```
