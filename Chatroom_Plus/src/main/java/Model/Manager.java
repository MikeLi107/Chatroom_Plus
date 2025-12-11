package Model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Manager {
    private static final Manager instance = new Manager();
    private final List<ChatData> messages = new ArrayList<>();
    private final Map<String, Long> onlineUsers = new HashMap<>();

    private Manager() {}

    public static Manager getInstance() {
        return instance;
    }

    /**
     * 获取当前用户能看到的消息
     * 1. 发给所有人的 (receiver == "ALL")
     * 2. 发给自己的 (receiver == currentUser)
     * 3. 自己发的 (username == currentUser)
     */
    public synchronized List<ChatData> getMessages(String currentUser) {
        return messages.stream()
                .filter(msg -> "ALL".equals(msg.getReceiver()) ||
                        msg.getUsername().equals(currentUser) ||
                        msg.getReceiver().equals(currentUser))
                .collect(Collectors.toList());
    }

    public synchronized List<String> getOnlineUsers() {
        cleanupInactiveUsers();
        return new ArrayList<>(onlineUsers.keySet());
    }

    // 发送普通消息
    public synchronized void addMessage(String username, String receiver, String content) {
        if(receiver == null || receiver.trim().isEmpty()){
            receiver = "ALL";
        }
        messages.add(new ChatData(username, receiver, content));
        refreshUser(username);
    }

    // 发送系统消息
    private synchronized void addSystemMessage(String content) {
        messages.add(new ChatData(content));
    }

    public synchronized boolean userLogin(String username) {
        if (!onlineUsers.containsKey(username)) {
            onlineUsers.put(username, System.currentTimeMillis());
            addSystemMessage("欢迎 " + username + " 加入聊天室！");
            return true;
        }
        return false;
    }

    public synchronized void userLogout(String username) {
        if (onlineUsers.containsKey(username)) {
            onlineUsers.remove(username);
            addSystemMessage(username + " 已退出聊天室。");
        }
    }

    public synchronized void refreshUser(String username) {
        if (onlineUsers.containsKey(username)) {
            onlineUsers.put(username, System.currentTimeMillis());
        }
    }

    // 清理超时用户（被心跳检测调用）
    public synchronized void cleanupInactiveUsers() {
        long now = System.currentTimeMillis();
        // 找出超时的用户
        List<String> timeoutUsers = onlineUsers.entrySet().stream()
                .filter(entry -> (now - entry.getValue()) > 20000) // 20秒无心跳
                .map(Map.Entry::getKey)
                .toList();

        for (String user : timeoutUsers) {
            onlineUsers.remove(user);
            addSystemMessage(user + " 连接超时，已离开聊天室。");
        }
    }
}