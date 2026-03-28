<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bupt.ta.model.User" %>
<%@ page import="com.bupt.ta.model.Job" %>
<%@ page import="com.bupt.ta.model.Application" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>TA Dashboard | TA Recruitment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .dashboard-card { margin-bottom: 20px; border: 2px solid #333; padding: 15px; }
        .list { list-style: none; padding: 0; }
        .item { border-bottom: 1px solid #ccc; padding: 10px 0; display: flex; justify-content: space-between; align-items: center; }
        .btn { padding: 5px 15px; background: #333; color: #fff; border: none; cursor: pointer; text-decoration: none; display: inline-block; font-size: 12px; }
        .status-badge { padding: 2px 8px; border: 1px solid #333; font-size: 10px; font-weight: bold; }
    </style>
</head>
<body>
    <div class="container" style="width: 900px; margin-top: 50px;">
        <div class="nav-bar" style="display: flex; justify-content: space-between; border-bottom: 2px solid #333; padding-bottom: 10px;">
            <span style="font-weight: bold;">TA RECRUITMENT SYSTEM</span>
            <a href="${pageContext.request.contextPath}/login" style="border-bottom: 2px solid #000; font-weight: bold; text-decoration: none;">LOGOUT</a>
        </div>
        <% 
            User user = (User) session.getAttribute("user");
            if (user != null) {
        %>
            <h2>TA DASHBOARD: <%= user.getUsername() %></h2>
            
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <div class="dashboard-card">
                    <h3>AVAILABLE JOBS</h3>
                    <ul class="list">
                        <% 
                            List<Job> jobs = (List<Job>) request.getAttribute("jobs");
                            if (jobs != null && !jobs.isEmpty()) {
                                for (Job job : jobs) {
                        %>
                            <li class="item">
                                <div>
                                    <strong><%= job.getTitle() %></strong><br>
                                    <small>£<%= job.getSalary() %>/hr</small>
                                </div>
                                <form action="${pageContext.request.contextPath}/apply" method="POST" style="margin: 0;">
                                    <input type="hidden" name="jobId" value="<%= job.getId() %>">
                                    <button type="submit" class="btn">APPLY</button>
                                </form>
                            </li>
                        <% 
                                }
                            } else {
                        %>
                            <li>NO OPEN JOBS.</li>
                        <% } %>
                    </ul>
                </div>

                <div class="dashboard-card">
                    <h3>MY APPLICATIONS</h3>
                    <ul class="list">
                        <% 
                            List<Application> apps = (List<Application>) request.getAttribute("applications");
                            if (apps != null && !apps.isEmpty()) {
                                for (Application app : apps) {
                        %>
                            <li class="item">
                                <div>
                                    <strong>JOB ID: <%= app.getJobId().substring(0, 8) %>...</strong><br>
                                    <small>APPLIED: <%= app.getApplyTime().toLocalDate() %></small>
                                </div>
                                <span class="status-badge"><%= app.getStatus().toUpperCase() %></span>
                            </li>
                        <% 
                                }
                            } else {
                        %>
                            <li>NO APPLICATIONS YET.</li>
                        <% } %>
                    </ul>
                </div>
            </div>

            <div class="dashboard-card">
                <h3>MY PROFILE</h3>
                <div style="display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <p>EMAIL: <%= user.getEmail() %></p>
                        <p>STATUS: <%= user.getStatus() %></p>
                    </div>
                    <button class="btn" style="padding: 10px 20px;">UPDATE RESUME / PROFILE</button>
                </div>
            </div>
        <% } %>
    </div>
</body>
</html>
