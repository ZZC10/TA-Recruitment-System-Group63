<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bupt.ta.model.User" %>
<%@ page import="com.bupt.ta.model.Job" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MO Dashboard | TA Recruitment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .dashboard-card { margin-bottom: 30px; border: 2px solid #333; padding: 20px; }
        .job-list { list-style: none; padding: 0; }
        .job-item { border-bottom: 1px solid #ccc; padding: 10px 0; display: flex; justify-content: space-between; align-items: center; }
        .post-form { display: flex; flex-direction: column; gap: 10px; }
        .post-form input, .post-form textarea { padding: 8px; border: 1px solid #333; }
        .post-form button { padding: 10px; background: #333; color: #fff; border: none; cursor: pointer; }
    </style>
</head>
<body>
    <div class="container" style="width: 800px; margin-top: 50px;">
        <div class="nav-bar" style="display: flex; justify-content: space-between; border-bottom: 2px solid #333; padding-bottom: 10px;">
            <span style="font-weight: bold;">TA RECRUITMENT SYSTEM</span>
            <a href="${pageContext.request.contextPath}/login" style="border-bottom: 2px solid #000; font-weight: bold; text-decoration: none;">LOGOUT</a>
        </div>
        <% 
            User user = (User) session.getAttribute("user");
            if (user != null) {
        %>
            <h2>MO DASHBOARD: <%= user.getUsername() %></h2>
            
            <div class="dashboard-card">
                <h3>POST NEW JOB</h3>
                <form action="${pageContext.request.contextPath}/job" method="POST" class="post-form">
                    <input type="hidden" name="action" value="post">
                    <input type="text" name="title" placeholder="JOB TITLE" required>
                    <textarea name="description" placeholder="JOB DESCRIPTION" rows="3" required></textarea>
                    <textarea name="requirements" placeholder="REQUIREMENTS" rows="2" required></textarea>
                    <div style="display: flex; gap: 10px;">
                        <input type="number" name="numRequired" placeholder="NUM REQUIRED" style="flex: 1;" required>
                        <input type="number" step="0.01" name="salary" placeholder="SALARY (GBP/HR)" style="flex: 1;" required>
                    </div>
                    <button type="submit">PUBLISH JOB</button>
                </form>
            </div>

            <div class="dashboard-card">
                <h3>YOUR POSTED JOBS</h3>
                <ul class="job-list">
                    <% 
                        List<Job> jobs = (List<Job>) request.getAttribute("jobs");
                        if (jobs != null && !jobs.isEmpty()) {
                            for (Job job : jobs) {
                    %>
                        <li class="job-item">
                            <div>
                                <strong><%= job.getTitle() %></strong> 
                                (<%= job.getStatus() %>) - 
                                <%= job.getNumRequired() %> POSITIONS, 
                                £<%= job.getSalary() %>/hr
                            </div>
                            <a href="${pageContext.request.contextPath}/review?jobId=<%= job.getId() %>" style="width: auto; padding: 5px 10px; border: 1px solid #333; text-decoration: none; color: #000; font-size: 12px;">VIEW APPS</a>
                        </li>
                    <% 
                            }
                        } else {
                    %>
                        <li>NO JOBS POSTED YET.</li>
                    <% } %>
                </ul>
            </div>
        <% } %>
    </div>
</body>
</html>
