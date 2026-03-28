package com.bupt.ta.model;

import java.io.Serializable;

/**
 * Module Managed by a Module Organizer
 */
public class MoModule implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String moduleCode;
    private String moduleName;
    private String moUsername; // Link to User.username
    private String department;
    private String description;

    public MoModule() {}

    public MoModule(String id, String moduleCode, String moduleName, String moUsername, String department, String description) {
        this.id = id;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.moUsername = moUsername;
        this.department = department;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }

    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }

    public String getMoUsername() { return moUsername; }
    public void setMoUsername(String moUsername) { this.moUsername = moUsername; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "MoModule{" +
                "id='" + id + '\'' +
                ", moduleCode='" + moduleCode + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", moUsername='" + moUsername + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
