<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 访问首页时，直接跳转到 /login 这个 Servlet
    response.sendRedirect("login");
%>