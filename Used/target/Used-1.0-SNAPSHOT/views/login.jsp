<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Login</title></head>
<body>
<c:if test="${param.error == '1'}">
    <p style="color:red">用户名或密码错误！</p>
</c:if>
<form action="${pageContext.request.contextPath}/login" method="post">
    用户名: <input type="text" name="username" required><br>
    密码: <input type="password" name="password" required><br>
    <button type="submit">登录</button>
</form>
<a href="${pageContext.request.contextPath}/views/register.jsp">注册</a>
</body>
</html>