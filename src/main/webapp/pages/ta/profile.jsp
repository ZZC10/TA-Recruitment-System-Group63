<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bupt.ta.model.TaProfile" %>
<%@ page import="com.bupt.ta.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Profile | TA Recruitment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .profile-form { border: 2px solid #333; padding: 20px; margin-top: 20px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; font-weight: bold; margin-bottom: 5px; }
        .form-group input[type="text"], .form-group input[type="number"], .form-group textarea { 
            width: 100%; padding: 8px; border: 1px solid #333; box-sizing: border-box; 
        }
        .form-group textarea { height: 100px; resize: vertical; }
        .btn { padding: 10px 20px; background: #333; color: #fff; border: none; cursor: pointer; text-decoration: none; display: inline-block; font-size: 14px; }
        .btn:hover { opacity: 0.8; }
        .btn-secondary { background: #666; }
        .message { padding: 10px; border: 1px solid #333; margin-bottom: 15px; font-weight: bold; }
    </style>
</head>
<body>
    <div class="container" style="width: 800px; margin: 50px auto;">
        <div class="nav-bar" style="display: flex; justify-content: space-between; border-bottom: 2px solid #333; padding-bottom: 10px;">
            <span style="font-weight: bold;">TA RECRUITMENT SYSTEM</span>
            <a href="${pageContext.request.contextPath}/dashboard" style="border-bottom: 2px solid #000; font-weight: bold; text-decoration: none;">BACK TO DASHBOARD</a>
        </div>

        <h2>EDIT TA PROFILE</h2>

        <% 
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
            <div class="message"><%= message %></div>
        <% } %>

        <% 
            TaProfile profile = (TaProfile) request.getAttribute("profile");
            if (profile != null) {
        %>
            <form action="${pageContext.request.contextPath}/profile" method="POST" class="profile-form">
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                    <div class="form-group">
                        <label for="realName">FULL NAME</label>
                        <input type="text" id="realName" name="realName" value="<%= profile.getRealName() != null ? profile.getRealName() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label for="studentId">STUDENT ID</label>
                        <input type="text" id="studentId" name="studentId" value="<%= profile.getStudentId() != null ? profile.getStudentId() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label for="major">MAJOR</label>
                        <input type="text" id="major" name="major" value="<%= profile.getMajor() != null ? profile.getMajor() : "" %>">
                    </div>
                    <div class="form-group">
                        <label for="yearOfStudy">YEAR OF STUDY</label>
                        <input type="text" id="yearOfStudy" name="yearOfStudy" value="<%= profile.getYearOfStudy() != null ? profile.getYearOfStudy() : "" %>">
                    </div>
                    <div class="form-group">
                        <label for="gpa">GPA</label>
                        <input type="number" step="0.01" id="gpa" name="gpa" value="<%= profile.getGpa() != null ? profile.getGpa() : "" %>">
                    </div>
                </div>

                <div class="form-group">
                    <label for="skills">SKILLS (e.g. Java, Python, Teaching Experience)</label>
                    <textarea id="skills" name="skills"><%= profile.getSkills() != null ? profile.getSkills() : "" %></textarea>
                </div>

                <div class="form-group">
                    <label for="experience">EXPERIENCE / BACKGROUND</label>
                    <textarea id="experience" name="experience"><%= profile.getExperience() != null ? profile.getExperience() : "" %></textarea>
                </div>

                <!-- Paths for now, file upload would update these -->
                <input type="hidden" name="photoPath" value="<%= profile.getPhotoPath() != null ? profile.getPhotoPath() : "" %>">
                <input type="hidden" name="cvFilePath" value="<%= profile.getCvFilePath() != null ? profile.getCvFilePath() : "" %>">

                <div style="margin-top: 20px;">
                    <button type="submit" class="btn">SAVE CHANGES</button>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">CANCEL</a>
                </div>
            </form>
        <% } %>
    </div>
</body>
</html>
