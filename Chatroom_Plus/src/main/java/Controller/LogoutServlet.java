package Controller;

import Model.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String username = (String) session.getAttribute("username");
            if(username != null) {
                Manager.getInstance().userLogout(username); // 这会触发“退出”系统消息
            }
            session.invalidate(); // 销毁 Session
        }

        response.sendRedirect("login");
    }
}