<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
   <title>注册</title>
</head>
<body>
<div class="register-container">
   <p>请输入注册账号密码</p>
   <form action="${pageContext.request.contextPath}/register" method="post">
       <div>
           <p>账号:</p>
           <input type="text" name="username" value="${param.username}">
       </div>
       <div>
           <p>密码:</p>
           <input type="password" name="password">
       </div>
       <div>
           <p>年龄:</p>
           <input type="number" name="age">
       </div>
       <input type="submit">
       <p>${requestScope.errorMessage}${requestScope.successMessage}</p>
   </form>
   <a href="${pageContext.request.contextPath}/tologin" class="option">回到登录页面</a>
</div>
</body>
</html>