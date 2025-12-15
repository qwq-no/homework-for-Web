<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册页面</title>
</head>
<body>
<!-- 注册表单提交到UserServlet，类型为register -->
<form action="talking_room" method="post">
    <input type="hidden" name="type" value="register">  <!-- 标识注册请求 -->
    <fieldset>
        <legend>用户注册</legend>
        <p>
            <label>用户名: <input type="text" name="username" required/></label>
        </p>
        <p>
            <label>密码：<input type="password" name="password" required/></label>
        </p>
        <p>
            <label>再次确认密码：<input type="password" name="password2" required/></label>
        </p>
        <p>
            <label>名称：<input type="text" name="name" required/></label>
        </p>
        <p>
            <label>
                <input type="submit" value="注册"/>
                <input type="reset" value="重置"/>
            </label>
        </p>
    </fieldset>
</form>
<a href="index.jsp">返回登录页面</a>
</body>
</html>