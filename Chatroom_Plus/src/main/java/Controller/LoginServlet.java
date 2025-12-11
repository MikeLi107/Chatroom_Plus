package Controller;

import Model.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // **【新增】处理 GET 请求，用于显示登录页面**
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 转发到 WEB-INF 下的 login.jsp，这是因为直接访问 WEB-INF 目录被保护。
        // 注意：这里的路径是相对于 Web 应用程序根目录的绝对路径。
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    // 处理 POST 请求（表单提交）
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");

        Manager chatRoom = Manager.getInstance();

        if (chatRoom.userLogin(username)) {

            HttpSession session = request.getSession();

            session.setAttribute("login", "ok");
            session.setMaxInactiveInterval(20);

            session.setAttribute("username", username);
            response.sendRedirect("chat");

        } else {

            request.setAttribute("error", "用户名已被使用");
            // **【修改】转发到 WEB-INF 下的 login.jsp**
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }
}