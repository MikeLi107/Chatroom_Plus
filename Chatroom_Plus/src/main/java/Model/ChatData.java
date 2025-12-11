package Model;

public class ChatData {
    private final String username; // 发送者
    private final String receiver; // 接收者 ("ALL" 表示所有人)
    private final String content;
    private final boolean isSystem; // 是否为系统消息

    // 普通消息构造
    public ChatData(String username, String receiver, String content) {
        this.username = username;
        this.receiver = receiver;
        this.content = content;
        this.isSystem = false;
    }

    // 系统消息构造
    public ChatData(String content) {
        this.username = "系统消息";
        this.receiver = "ALL";
        this.content = content;
        this.isSystem = true;
    }

    public String getUsername() {
        return username;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getContent() {
        return content;
    }
    public boolean isSystem() {
        return isSystem;
    }
}