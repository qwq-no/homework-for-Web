```
Used/
├── src/
│   └── main/
│       ├── java/
│       │   ├── dao/
│       │   │   ├── ItemDao.java
│       │   │   ├── ItemDaoImpl.java
│       │   │   ├── UserDao.java
│       │   │   └── UserDaoImpl.java
│       │   ├── model/ 
│       │   │   ├── User.java
│       │   │   └── Item.java
│       │   ├── servlet/
│       │   │   ├── LoginServlet.java
│       │   │   ├── RegisterServlet.java
│       │   │   ├── PostItemServlet.java
│       │   │   ├── DeleteItemServlet.java
│       │   │   └── MyItemsServlet.java
│       │   └── util/
│       │   │   ├── PasswordUtil.java
│       │   │   ├── UserService.java
│       │   │   ├── UserServiceImpl.java
│       │       └── DBUtil.java
│       └── webapp/
│           ├── views/
│           │   ├── login.jsp
│           │   ├── register.jsp
│           │   ├── home.jsp
│           │   └── error.jsp
│           └── WEB-INF/
│               └── web.xml
└── pom.xml
```

注册登录后可以在下方输入名称和描述然后点击发布。中间部分可以看到发布的物品信息，并且可以删除属于自己的，上方可以搜索关键字，可以检索到包含这些字的物品信息。数据库建表信息在schema.sql里面，连接信息见DBUtil.java

已有账号：

用户名：1  密码：1

用户名：2  密码：2
