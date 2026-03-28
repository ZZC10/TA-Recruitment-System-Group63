package com.bupt.ta.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * System Notifications for Users
 */
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String receiverUsername; // Link to User.username
    private String title;
    private String content;
    private Boolean isRead;
    private LocalDateTime createTime;

    public Notification() {}

    public Notification(String id, String receiverUsername, String title, String content, 
                        Boolean isRead, LocalDateTime createTime) {
        this.id = id;
        this.receiverUsername = receiverUsername;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
        this.createTime = createTime;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReceiverUsername() { return receiverUsername; }
    public void setReceiverUsername(String receiverUsername) { this.receiverUsername = receiverUsername; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", receiverUsername='" + receiverUsername + '\'' +
                ", title='" + title + '\'' +
                ", isRead=" + isRead +
                ", createTime=" + createTime +
                '}';
    }
}
