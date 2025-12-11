<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>聊天室登录</title>
</head>
<body>
<h1 style="color: #666666;font-size: large ">在线聊天室</h1>
<form action="login" method="post">
    <label for="username">用户名：</label>
    <input type="text" id="username" name="username" placeholder="请输入用户名" required>
    <input type="submit" value="登录">
</form>
<% if (request.getAttribute("error") != null) { %>
<p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>