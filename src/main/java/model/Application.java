package com.bupt.ta.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TA's Application for a specific Job
 */
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String jobId; // Link to Job.id
    private String taUsername; // Link to User.username
    private String status; // pending, accepted, rejected, withdrawn
    private LocalDateTime applyTime;
    private LocalDateTime updateTime;
    private String moFeedback;
    private String cvSnapshotPath; // Path to CV at the time of application

    public Application() {}

    public Application(String id, String jobId, String taUsername, String status, 
                       LocalDateTime applyTime, LocalDateTime updateTime, String moFeedback, 
                       String cvSnapshotPath) {
        this.id = id;
        this.jobId = jobId;
        this.taUsername = taUsername;
        this.status = status;
        this.applyTime = applyTime;
        this.updateTime = updateTime;
        this.moFeedback = moFeedback;
        this.cvSnapshotPath = cvSnapshotPath;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getTaUsername() { return taUsername; }
    public void setTaUsername(String taUsername) { this.taUsername = taUsername; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getApplyTime() { return applyTime; }
    public void setApplyTime(LocalDateTime applyTime) { this.applyTime = applyTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getMoFeedback() { return moFeedback; }
    public void setMoFeedback(String moFeedback) { this.moFeedback = moFeedback; }

    public String getCvSnapshotPath() { return cvSnapshotPath; }
    public void setCvSnapshotPath(String cvSnapshotPath) { this.cvSnapshotPath = cvSnapshotPath; }

    @Override
    public String toString() {
        return "Application{" +
                "id='" + id + '\'' +
                ", jobId='" + jobId + '\'' +
                ", taUsername='" + taUsername + '\'' +
                ", status='" + status + '\'' +
                ", applyTime=" + applyTime +
                '}';
    }
}
