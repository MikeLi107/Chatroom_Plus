package Controller;

import Model.*;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 过滤器已经处理了编码和登录验证
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        String content = request.getParameter("content");
        String receiver = request.getParameter("toUser"); // 获取私聊对象

        if (username != null && content != null && !content.trim().isEmpty()) {
            Manager.getInstance().addMessage(username, receiver, content);
        }

        response.sendRedirect("chat");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // 1. 心跳请求
        if ("true".equals(request.getParameter("ping"))) {
            Manager.getInstance().refreshUser(username);
            // 顺便触发一下清理超时用户，这样系统消息能及时生成
            Manager.getInstance().cleanupInactiveUsers();
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 2. 普通页面访问
        try {
            Manager.getInstance().refreshUser(username);

            // **修改：只获取该用户能看到的消息**
            List<ChatData> messages = Manager.getInstance().getMessages(username);
            List<String> onlineUsers = Manager.getInstance().getOnlineUsers();

            request.setAttribute("messages", messages);
            request.setAttribute("onlineUsers", onlineUsers);
            // 将当前用户传回去，方便JSP判断
            request.setAttribute("currentUser", username);

            request.getRequestDispatcher("/WEB-INF/chat.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("logout"); // 出错直接登出
        }
    }
}
