<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/login.css">
</head>
<body>
<div class="login-container">
    <div class="logo">
        <h1>Pahana Edu</h1>
        <p>Billing System</p>
    </div>

    <!-- Display error message -->
    <% if (request.getAttribute("errorMessage") != null) { %>
    <div class="error-message">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>

    <!-- Display logout success message -->
    <% if ("logout".equals(request.getParameter("message"))) { %>
    <div class="success-message">
        You have been successfully logged out.
    </div>
    <% } %>

    <form method="post" action="<%= ctx %>/login" id="loginForm">
        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" required
                   value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
                   placeholder="Enter your email address">
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required
                   placeholder="Enter your password">
        </div>

        <button type="submit" class="btn btn-primary">Login</button>
    </form>

    <div class="forgot-password">
        <a href="#" onclick="alert('Please contact administrator for password reset.')">
            Forgot your password?
        </a>
    </div>
</div>

<script src="<%= request.getContextPath() %>/js/common.js"></script>
<script src="<%= request.getContextPath() %>/js/login.js"></script>
</body>
</html>
