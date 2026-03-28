<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bupt.ta.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | TA Recruitment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
    <div class="container" style="width: 600px;">
        <div class="nav-bar">
            <span>TA RECRUITMENT SYSTEM</span>
            <a href="${pageContext.request.contextPath}/login" style="border-bottom: 2px solid #000; font-weight: bold; text-decoration: none;">LOGOUT</a>
        </div>
        <% 
            User user = (User) session.getAttribute("user");
            if (user != null) {
        %>
            <h2>ADMIN PANEL: <%= user.getUsername() %></h2>
            <div class="dashboard-card">
                <h3>USER MANAGEMENT</h3>
                <p>TOTAL USERS: UNKNOWN</p>
                <button>MANAGE USERS</button>
            </div>
            <div class="dashboard-card">
                <h3>SYSTEM SETTINGS</h3>
                <p>RECRUITMENT PERIOD: CLOSED</p>
                <button>OPEN RECRUITMENT</button>
            </div>
        <% } %>
    </div>
</body>
</html>
