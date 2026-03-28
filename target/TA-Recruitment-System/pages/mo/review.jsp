<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bupt.ta.model.User" %>
<%@ page import="com.bupt.ta.model.Application" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Review Applications | TA Recruitment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .app-card { margin-bottom: 20px; border: 2px solid #333; padding: 15px; }
        .review-form { display: flex; flex-direction: column; gap: 10px; margin-top: 10px; }
        .btn-group { display: flex; gap: 10px; }
        .btn { padding: 5px 15px; border: 1px solid #333; cursor: pointer; }
        .btn-accept { background: #000; color: #fff; }
        .btn-reject { background: #fff; color: #000; }
    </style>
</head>
<body>
    <div class="container" style="width: 800px; margin-top: 50px;">
        <div class="nav-bar" style="display: flex; justify-content: space-between; border-bottom: 2px solid #333; padding-bottom: 10px;">
            <a href="${pageContext.request.contextPath}/job" style="font-weight: bold; text-decoration: none;">BACK TO DASHBOARD</a>
            <span style="font-weight: bold;">REVIEW APPLICATIONS</span>
        </div>
        
        <h2>APPLICATIONS FOR JOB: <%= request.getAttribute("jobId") %></h2>

        <% 
            List<Application> apps = (List<Application>) request.getAttribute("applications");
            if (apps != null && !apps.isEmpty()) {
                for (Application app : apps) {
        %>
            <div class="app-card">
                <div>
                    <strong>TA: <%= app.getTaUsername() %></strong> | 
                    Status: <span style="font-weight: bold;"><%= app.getStatus().toUpperCase() %></span>
                </div>
                <form action="${pageContext.request.contextPath}/review" method="POST" class="review-form">
                    <input type="hidden" name="appId" value="<%= app.getId() %>">
                    <input type="hidden" name="jobId" value="<%= request.getAttribute("jobId") %>">
                    <textarea name="feedback" placeholder="ADD FEEDBACK..." rows="2"><%= app.getMoFeedback() %></textarea>
                    <div class="btn-group">
                        <button type="submit" name="status" value="accepted" class="btn btn-accept">ACCEPT</button>
                        <button type="submit" name="status" value="rejected" class="btn btn-reject">REJECT</button>
                    </div>
                </form>
            </div>
        <% 
                }
            } else {
        %>
            <p>NO APPLICATIONS RECEIVED YET.</p>
        <% } %>
    </div>
</body>
</html>
