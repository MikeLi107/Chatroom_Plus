package Listener;

import Model.Manager;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class ChatSessionListener implements HttpSessionListener {
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // 当 Session 真正销毁时（例如30分钟无操作自动过期），确保从列表中移除
        String username = (String) se.getSession().getAttribute("username");
        if (username != null) {
            Manager.getInstance().userLogout(username);
        }
    }
}
