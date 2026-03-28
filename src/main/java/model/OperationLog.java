package com.bupt.ta.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * System Audit Log for Operations
 */
public class OperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String operatorUsername; // Link to User.username
    private String action; // e.g., login, apply_job, post_job, update_status
    private String targetType; // e.g., user, job, application
    private String targetId;
    private LocalDateTime timestamp;
    private String details;

    public OperationLog() {}

    public OperationLog(String id, String operatorUsername, String action, String targetType, 
                        String targetId, LocalDateTime timestamp, String details) {
        this.id = id;
        this.operatorUsername = operatorUsername;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOperatorUsername() { return operatorUsername; }
    public void setOperatorUsername(String operatorUsername) { this.operatorUsername = operatorUsername; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    @Override
    public String toString() {
        return "OperationLog{" +
                "id='" + id + '\'' +
                ", operatorUsername='" + operatorUsername + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
