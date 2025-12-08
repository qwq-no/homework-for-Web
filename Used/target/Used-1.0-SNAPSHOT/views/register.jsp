<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<body>
<c:if test="${param.error == '0'}">
    <p style="color:red">输入不能为空</p>
</c:if>
<c:if test="${param.error == '1'}">
    <p style="color:red">该用户已被注册</p>
</c:if>
<c:if test="${param.error == '2'}">
    <p style="color:green">成功注册</p>
</c:if>
<form action="${pageContext.request.contextPath}/register" method="post">
    用户名: <input type="text" name="username" required><br>
    密码: <input type="password" name="password" required><br>
    <button type="submit">注册</button>
</form>
<a href="${pageContext.request.contextPath}/views/login.jsp">登录</a>
</body>
</html>