<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.enums.UserRole" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || !currentUser.isAdmin()) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }

    List<User> users = (List<User>) request.getAttribute("users");
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/users.css">
</head>
<body>
<div class="header">
    <h1>Pahana Edu Billing System</h1>
    <div class="user-info">
        <span class="user-name">Welcome, <%= currentUser.getFullName() %></span>
        <span class="role-badge <%= currentUser.getRoleCssClass() %>"><%= currentUser.getRoleEnum() %></span>
        <a href="<%= ctx %>/logout" class="logout-btn">Logout</a>
    </div>
</div>

<div class="container-full">
    <div class="breadcrumb">
        <a href="<%= ctx %>/dashboard">Dashboard</a> > User Management
    </div>

    <div class="content-header">
        <h2>User Management</h2>
        <a href="<%= ctx %>/users?action=new" class="btn btn-primary">Add New User</a>
    </div>

    <% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>
    
    <% if (error != null) { %>
    <div class="alert alert-error"><%= error %></div>
    <% } %>

    <% if (users != null && !users.isEmpty()) { %>
    <table class="table">
        <thead>
        <tr>
            <th>User ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Created Date</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (User user : users) { %>
        <tr>
            <td class="user-id"><%= user.getUserId() %></td>
            <td class="user-name"><%= user.getFullName() %></td>
            <td class="user-email"><%= user.getEmail() %></td>
            <td>
                <span class="role-badge <%= user.getRoleCssClass() %>">
                    <%= user.getRoleEnum() %>
                </span>
            </td>
            <td class="date-info">
                <% if (user.getCreatedAt() != null) { %>
                <%= user.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) %>
                <% } else { %>
                N/A
                <% } %>
            </td>
            <td class="actions">
                <a href="<%= ctx %>/users?action=edit&id=<%= user.getUserId() %>" 
                   class="btn btn-info btn-sm">Edit</a>
                <% if (user.getUserId() != currentUser.getUserId()) { %>
                <a href="<%= ctx %>/users?action=delete&id=<%= user.getUserId() %>" 
                   class="btn btn-danger btn-sm">Delete</a>
                <% } %>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <div class="empty-state">
        <h3>No Users Found</h3>
        <p>No users are currently registered in the system.</p>
        <a href="<%= ctx %>/users?action=new" class="btn btn-primary">Add First User</a>
    </div>
    <% } %>

    <div class="navigation-actions">
        <a href="<%= ctx %>/dashboard" class="btn btn-outline">Back to Dashboard</a>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/users.js"></script>
</body>
</html>
