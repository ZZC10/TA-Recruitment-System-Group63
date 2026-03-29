<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bupt.ta.model.User" %>
<%@ page import="com.bupt.ta.model.Application" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
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
                <p>TOTAL USERS: <%= request.getAttribute("totalUsers") %></p>
            </div>
            
            <div class="dashboard-card">
                <h3>TA WORKLOAD CHECK</h3>
                <ul class="list" style="list-style: none; padding: 0;">
                    <% 
                        List<User> tas = (List<User>) request.getAttribute("tas");
                        List<Application> acceptedApps = (List<Application>) request.getAttribute("acceptedApps");
                        if (tas != null && !tas.isEmpty()) {
                            for (User ta : tas) {
                                long workload = acceptedApps.stream()
                                    .filter(a -> a.getTaUsername().equals(ta.getUsername()))
                                    .count();
                    %>
                        <li style="display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px solid #eee;">
                            <span><%= ta.getUsername() %> (<%= ta.getEmail() %>)</span>
                            <span style="font-weight: bold;">Workload: <%= workload %> module(s)</span>
                        </li>
                    <% 
                            }
                        } else {
                    %>
                        <li>No TAs registered.</li>
                    <% } %>
                </ul>
            </div>
        <% } %>
    </div>
</body>
</html>