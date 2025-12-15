<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
</head>
<body>
<form action="talking_room" method="post" onsubmit　="return validateCaptcha()">
    <input type="hidden" name="type" value="login">
    <fieldset>
        <legend>用户登录</legend>
        <p>
            <label>用户名: <input type="text" name="username" required/></label>
        </p>
        <p>
            <label>密码：<input type="password" name="password" required/></label>
        </p>

        <!-- 图形验证码区域 -->
        <p>
            <label>验证码：
                <input type="text" name="captchaInput" id="captchaInput" required placeholder="请输入验证码">
            </label>
            <!-- 验证码图片（点击刷新） -->
            <img src="captcha" alt="验证码" id="captchaImg" style="cursor: pointer; margin-left: 10px;">
            <a href="javascript:refreshCaptcha()">刷新</a>
        </p>

        <p>
            <input type="submit" value="登录"/>
            <input type="reset" value="重置"/>
        </p>
    </fieldset>
</form>
<a href="register.jsp">注册</a>
<script>
    // 页面加载时初始化验证码
    window.onload　 = refreshCaptcha;

    // 刷新验证码（添加随机参数防止缓存）
    function refreshCaptcha() {
        const captchaImg = document.getElementById("captchaImg");
        captchaImg.src = "captcha?random=" + Math.random();
    }
    document.getElementById("captchaImg").addEventListener("click", refreshCaptcha);
    function validateCaptcha() {
        const input = document.getElementById("captchaInput").value.trim();
        if (input.length !== 4) {
            alert("验证码长度为4位");
            return false;
        }
        return true;
    }
</script>
</body>
</html>