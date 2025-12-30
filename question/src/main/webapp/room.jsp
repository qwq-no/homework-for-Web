<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
   <title>room</title>
</head>
<body>
   <h2>欢迎回来“${sessionScope.user.username}”</h2>
   <div class="container-context">
       <c:forEach items="${messageList}" var="mes">
           <div>
               <pre>标题：${mes.title}</pre>
               <p>发布时间：${mes.time}</p>
               <p>发送者：${mes.sender}</p>
               <a href="${pageContext.request.contextPath}/message/view?id=${mes.id}" class="view-btn">查看详情</a>
           </div>
       </c:forEach>
   </div>
   <div class="container-submit">
       <form action="${pageContext.request.contextPath}/room" method="get">
           <button type="submit" id="refreshBtn">刷新消息列表</button>
       </form>
       <form action="${pageContext.request.contextPath}/room" method="post">
           <input type="hidden" name="username" value="${sessionScope.user.username}">
           <p>标题</p>
           <input type="text" name="title">
           <p>内容</p>
           <input type="text" name="content">
           <input type="submit" value="submit">
       </form>
   </div>
</body>
</html>

