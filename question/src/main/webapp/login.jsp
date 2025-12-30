<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
   <title>登录</title>
</head>
<body>
<div class="login-container">
   <p>请输入登录账号密码</p>
   <form action="${pageContext.request.contextPath}/login" method="post">
       <div>
           <p>账号:</p>
           <input type="text" name="username">
       </div>
       <div>
           <p>密码:</p>
           <input type="password" name="password">
       </div>
       <div class="form-group captcha-container">
           <label for="captcha">验证码:</label>
           <input type="text" id="captcha" name="captcha" placeholder="请输入验证码" required>
           <img id="captchaImg" src="captcha" alt="验证码" onclick　="refreshCaptcha()">
       </div>
       <input type="submit">
       <p>${requestScope.errorMessage}</p>
   </form>
   <a href="${pageContext.request.contextPath}/toregister" class="option">没有账号？点击注册</a>
</div>
</body>
</html>