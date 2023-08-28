package com.cluster.pojo;

/**
 * Cluster内聊天的消息实体类
 */
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String avatarUrl;

    public enum MessageType{
        CHAT,
        JOIN,
        LEAVE
    }

    public MessageType getType()
    {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
