<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
   <title>首页 - 我的问答网站</title>
   <style>
       body { font-family: sans-serif; text-align: center; margin-top: 100px; }
       .option { display: inline-block; margin: 20px; padding: 15px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }
       .option:hover { background-color: #0056b3; }
   </style>
</head>
<body>
<h1>欢迎来到我的问答网站</h1>
<a href="${pageContext.request.contextPath}/tologin" class="option">登录</a>
<a href="${pageContext.request.contextPath}/toregister" class="option">注册</a>
</body>
</html>