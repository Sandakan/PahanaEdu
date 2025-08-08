<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    String ctx = request.getContextPath();
    String successMessage = (String) request.getAttribute("success");
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category Management - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/dashboard.css">
</head>
<body>
<div class="header">
    <h1>Pahana Edu Billing System</h1>
    <div class="user-info">
        <span class="user-name">Welcome, <%= user.getFullName() %></span>
        <span class="role-badge"><%= user.getRole() %></span>
        <a href="<%= ctx %>/logout" class="logout-btn">Logout</a>
    </div>
</div>

<div class="container-full">
    <div class="breadcrumb">
        <a href="<%= ctx %>/dashboard">Dashboard</a> &gt; Category Management
    </div>

    <div class="content-header">
        <h2>Category Management</h2>
        <a href="<%= ctx %>/categories?action=new" class="btn btn-primary">Add New Category</a>
    </div>

    <% if (successMessage != null) { %>
    <div class="alert alert-success">
        <%= successMessage %>
    </div>
    <% } %>

    <% if (errorMessage != null) { %>
    <div class="alert alert-danger">
        <%= errorMessage %>
    </div>
    <% } %>

    <% if (categories != null && !categories.isEmpty()) { %>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Created Date</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Category category : categories) { %>
        <tr>
            <td><%= category.getCategoryId() %>
            </td>
            <td><%= category.getName() %>
            </td>
            <td><%= category.getDescription() != null ? category.getDescription() : "-" %>
            </td>
            <td><%= category.getCreatedAt() != null ? category.getCreatedAt().toLocalDate() : "-" %>
            </td>
            <td>
                <div class="actions">
                    <a href="<%= ctx %>/categories?action=edit&id=<%= category.getCategoryId() %>"
                       class="btn btn-warning">Edit</a>
                    <a href="<%= ctx %>/categories?action=delete&id=<%= category.getCategoryId() %>"
                       class="btn btn-danger">Delete</a>
                </div>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <div class="empty-state">
        <h3>No Categories Found</h3>
        <p>No categories have been added yet. Click "Add New Category" to get started.</p>
    </div>
    <% } %>

    <div style="margin-top: 30px;">
        <a href="<%= ctx %>/dashboard" class="btn btn-secondary">Back to Dashboard</a>
    </div>

</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/categories.js"></script>
</body>
</html>
