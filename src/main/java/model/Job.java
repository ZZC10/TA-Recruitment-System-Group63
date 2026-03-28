package com.bupt.ta.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TA Job Vacancy Posted by MO
 */
public class Job implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String moduleId; // Link to MoModule.id
    private String moUsername; // Link to User.username
    private String title;
    private String description;
    private String requirements;
    private Integer numRequired;
    private Double salary;
    private LocalDateTime deadline;
    private String status; // open, closed
    private LocalDateTime createTime;

    public Job() {}

    public Job(String id, String moduleId, String moUsername, String title, String description, 
               String requirements, Integer numRequired, Double salary, LocalDateTime deadline, 
               String status, LocalDateTime createTime) {
        this.id = id;
        this.moduleId = moduleId;
        this.moUsername = moUsername;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.numRequired = numRequired;
        this.salary = salary;
        this.deadline = deadline;
        this.status = status;
        this.createTime = createTime;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }

    public String getMoUsername() { return moUsername; }
    public void setMoUsername(String moUsername) { this.moUsername = moUsername; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public Integer getNumRequired() { return numRequired; }
    public void setNumRequired(Integer numRequired) { this.numRequired = numRequired; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", moUsername='" + moUsername + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", deadline=" + deadline +
                '}';
    }
}
