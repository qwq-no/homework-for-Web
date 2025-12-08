<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <style>
        .container { display: flex; flex-direction: column; height: 100vh; }
        .header { padding: 10px; background: #f0f0f0; }
        .main { flex: 1; overflow-y: auto; padding: 10px; }
        .footer { padding: 10px; background: #eee; }
        .item { border-bottom: 1px solid #ccc; padding: 8px 0; }
    </style>
</head>
<body>
<div class="container">
    <!-- 上方搜索栏 -->
    <div class="header">
        <form action="${pageContext.request.contextPath}/home" method="get">
            <input type="text" name="q" value="${keyword}" placeholder="搜索二手物品...">
            <button type="submit">搜索</button>
            <a href="home">清空</a>
        </form>
    </div>

    <!-- 中间物品列表 -->
    <div class="main">
        <c:choose>
            <c:when test="${empty items}">
                <p>未找到相关内容。</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${items}" var="item">
                    <div class="item">
                        <h3>${item.name}</h3>
                        <p>${item.description}</p>
                        <small>发布者: ${item.username}</small>
                        <c:if test="${item.userId == sessionScope.user.id}">
                            <form action="${pageContext.request.contextPath}/deleteItem" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${item.id}">
                                <button type="submit">删除</button>
                            </form>
                        </c:if>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- 底部发布表单 -->
    <div class="footer">
        <form action="${pageContext.request.contextPath}/addItem" method="post">
            名称: <input type="text" name="name" required>
            描述: <input type="text" name="description" required>
            <button type="submit">发布</button>
        </form>
    </div>
</div>
</body>
</html>