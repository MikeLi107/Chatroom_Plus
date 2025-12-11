<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>在线聊天室</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; }
        .container { display: flex; gap: 20px; padding: 20px;}
        .chat-area { flex: 3; }
        .user-list { flex: 1; border-left: 1px solid #eee; padding-left: 20px; }

        .messages {
            border: 1px solid #ccc;
            height: 400px;
            overflow-y: auto;
            padding: 10px;
            background-color: #f9f9f9;
            margin-bottom: 15px;
        }

        .message { margin: 8px 0; padding: 5px; border-radius: 4px; }
        .msg-system { color: #888; font-size: 0.9em; text-align: center; background-color: #eee; }
        .msg-private { color: #d63384; background-color: #fff0f6; border: 1px dashed #d63384; }
        .msg-self { text-align: right; }

        .username { font-weight: bold; color: #0066cc; }
        .meta { font-size: 0.8em; color: #aaa; margin-left: 5px; }

        .input-area { display: flex; gap: 10px; }
        select { padding: 8px; border-radius: 4px; border: 1px solid #ccc; }
        input[type="text"] { flex: 1; padding: 8px; border-radius: 4px; border: 1px solid #ccc; }
        input[type="submit"] { padding: 8px 20px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        input[type="submit"]:hover { background-color: #0056b3; }
    </style>
</head>
<body>
<h1>在线聊天室</h1>
<p>欢迎, <b>${currentUser}</b> | <a href="logout">退出登录</a></p>

<div class="container">
    <div class="chat-area">
        <div class="messages" id="msgBox">
            <c:forEach items="${messages}" var="msg">
                <c:choose>
                    <c:when test="${msg.system}">
                        <div class="message msg-system">${msg.content}</div>
                    </c:when>

                    <c:otherwise>
                        <c:set var="isPrivate" value="${msg.receiver != 'ALL'}" />
                        <c:set var="isSelf" value="${msg.username == currentUser}" />

                        <div class="message ${isPrivate ? 'msg-private' : ''} ${isSelf ? 'msg-self' : ''}">
                            <span class="username">${msg.username}</span>

                            <c:if test="${isPrivate}">
                                <span class="meta">[私聊 -> ${isSelf ? msg.receiver : "您"}]</span>
                            </c:if>

                            : <span class="content">${msg.content}</span>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

        <form action="chat" method="post" id="chatForm">
            <div class="input-area">
                <label for="userSelect"></label><select name="toUser" id="userSelect">
                    <option value="ALL">所有人</option>
                    <c:forEach items="${onlineUsers}" var="u">
                        <%-- 不显示自己 --%>
                        <c:if test="${u != currentUser}">
                            <option value="${u}">${u}</option>
                        </c:if>
                    </c:forEach>
                </select>

                <label>
                    <input type="text" name="content" placeholder="输入消息..." autocomplete="off" required>
                </label>
                <input type="submit" value="发送">
            </div>
        </form>
    </div>

    <div class="user-list">
        <%-- fn:length 获取集合大小 --%>
        <h3>在线用户 (<span id="userCount">${onlineUsers != null ? fn:length(onlineUsers) : 0}</span>)</h3>
        <ul id="userListUl">
            <c:forEach items="${onlineUsers}" var="user">
                <li>
                        ${user}
                    <c:if test="${user == currentUser}">(我)</c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<script>
    // JavaScript 保持不变，它负责页面逻辑，不属于 JSP 的 Java 混合代码
    var msgBox = document.getElementById("msgBox");
    msgBox.scrollTop = msgBox.scrollHeight;

    function refreshData() {
        fetch('chat')
            .then(response => response.text())
            .then(html => {
                let parser = new DOMParser();
                let doc = parser.parseFromString(html, 'text/html');

                let newMsgContent = doc.getElementById('msgBox').innerHTML;
                let currentMsgBox = document.getElementById('msgBox');
                let isAtBottom = (currentMsgBox.scrollHeight - currentMsgBox.scrollTop - currentMsgBox.clientHeight) < 100;

                if(currentMsgBox.innerHTML !== newMsgContent) {
                    currentMsgBox.innerHTML = newMsgContent;
                    if(isAtBottom) {
                        currentMsgBox.scrollTop = currentMsgBox.scrollHeight;
                    }
                }

                let newUserList = doc.getElementById('userListUl').innerHTML;
                document.getElementById('userListUl').innerHTML = newUserList;

                let newCount = doc.getElementById('userCount').innerText;
                document.getElementById('userCount').innerText = newCount;

                let selectBox = document.getElementById('userSelect');
                let currentSelection = selectBox.value;
                let newOptions = doc.getElementById('userSelect').innerHTML;

                if (selectBox.innerHTML !== newOptions) {
                    selectBox.innerHTML = newOptions;
                    selectBox.value = currentSelection;
                }
            })
            .catch(err => console.error("刷新失败:", err));
    }

    setInterval(refreshData, 2000);

    setInterval(function() {
        fetch('chat?ping=true');
    }, 10000);
</script>

</body>
</html>