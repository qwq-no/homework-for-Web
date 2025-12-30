<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
   <title>${message.title} - 详情</title>
</head>
<body>
<div class="container">
   <a href="${pageContext.request.contextPath}/room">← 返回列表</a>
   <div class="message-detail">
       <h2>${message.title}</h2>
       <p>发送者:${message.sender}</p>
       <div class="content">
           <pre>${message.content}</pre>
       </div>
   </div>
   <div class="comments-section">
       <h3>评论区</h3>
       <c:forEach items="${message.comments}" var="comment">
           <div class="comment-card">
               <p>账号名称：${comment.username}</p>
               <pre>评论内容：${comment.content}</pre>
           </div>
       </c:forEach>
   </div>
   <div class="comment-form">
       <h4>发表评论</h4>
       <form action="${pageContext.request.contextPath}/comment/submit" method="post">
           <input type="hidden" name="messageId" value="${message.id}">
           <input type="hidden" name="username" value="${sessionScope.user.username}">
           <textarea name="commentContent" rows="3" placeholder="请输入你的评论..." required></textarea>
           <input type="submit" value="提交评论">
       </form>
   </div>
</div>
</body>
</html>