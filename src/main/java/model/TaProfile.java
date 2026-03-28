package com.bupt.ta.model;

import java.io.Serializable;

/**
 * TA Personal Profile / Resume Information
 */
public class TaProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username; // Link to User.username
    private String realName;
    private String studentId;
    private String major;
    private String yearOfStudy;
    private Double gpa;
    private String skills;
    private String experience;
    private String cvFilePath;
    private String photoPath;

    public TaProfile() {}

    public TaProfile(String id, String username, String realName, String studentId, String major, 
                     String yearOfStudy, Double gpa, String skills, String experience, 
                     String cvFilePath, String photoPath) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.studentId = studentId;
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.gpa = gpa;
        this.skills = skills;
        this.experience = experience;
        this.cvFilePath = cvFilePath;
        this.photoPath = photoPath;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(String yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getCvFilePath() { return cvFilePath; }
    public void setCvFilePath(String cvFilePath) { this.cvFilePath = cvFilePath; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    @Override
    public String toString() {
        return "TaProfile{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", major='" + major + '\'' +
                ", yearOfStudy='" + yearOfStudy + '\'' +
                ", gpa=" + gpa +
                ", cvFilePath='" + cvFilePath + '\'' +
                '}';
    }
}
