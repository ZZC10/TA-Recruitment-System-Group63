<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register | TA Recruitment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
    <div class="container">
        <h1>REGISTER</h1>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-box"><%= request.getAttribute("error") %></div>
        <% } %>
        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="username">USERNAME</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">PASSWORD</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="email">EMAIL</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="role">ROLE</label>
                <select id="role" name="role" required>
                    <option value="ta">Teaching Assistant (TA)</option>
                    <option value="mo">Module Organizer (MO)</option>
                    <option value="admin">Administrator</option>
                </select>
            </div>
            <button type="submit">SIGN UP</button>
        </form>
        <div class="footer-links">
            <a href="${pageContext.request.contextPath}/login">ALREADY REGISTERED?</a>
        </div>
    </div>
</body>
</html>
