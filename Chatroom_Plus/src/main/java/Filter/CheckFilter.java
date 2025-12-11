package Filter;

import jakarta.servlet.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// 拦截聊天主页、发送消息、退出登录
public class CheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);

        boolean isLoggedIn = (session != null && "ok".equals(session.getAttribute("login")));

        if (isLoggedIn) {
            chain.doFilter(req, resp);
        } else {
            // 未登录，跳转到登录页
            response.sendRedirect("login");
        }
    }
}
