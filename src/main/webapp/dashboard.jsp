<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%
    // Check if user is logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/dashboard.css">
</head>
<body>
<div class="header">
    <h1>Pahana Edu Billing System</h1>
    <div class="user-info">
        <span class="user-name">Welcome, <%= user.getFullName() %></span>
        <span class="role-badge"><%= user.getRole() %></span>
        <a href="<%= request.getContextPath() %>/logout" class="logout-btn">Logout</a>
    </div>
</div>

<div class="container">
    <div class="welcome-card">
        <h2>Welcome to Pahana Edu Billing System</h2>
        <p>You are logged in as <strong><%= user.getRole() %>
        </strong>. Select an option below to get started.</p>
    </div>

    <div class="menu-grid">
        <div class="menu-card">
            <h3>Customer Management</h3>
            <p>Manage customer accounts, add new customers, and edit customer information.</p>
            <a href="<%= ctx %>/customers" class="menu-btn">Manage Customers</a>
        </div>
        
        <div class="menu-card">
            <h3>Item Management</h3>
            <p>Manage inventory items, add new products, and update pricing information.</p>
            <a href="<%= ctx %>/items" class="menu-btn">Manage Items</a>
        </div>
        
        <div class="menu-card">
            <h3>Category Management</h3>
            <p>Manage item categories, organize products, and maintain category structure.</p>
            <a href="<%= ctx %>/categories" class="menu-btn">Manage Categories</a>
        </div>
    </div>
</div>

</body>
</html>
