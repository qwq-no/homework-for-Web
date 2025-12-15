<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>error</title>
</head>
<body>
<%
    String msg = request.getParameter("msg");
    if (msg == null) msg = "输入的账号或密码错误!";
%>
<p><%= msg %></p>
<a href="index.jsp">返回登录页面</a>
</body>
</html>