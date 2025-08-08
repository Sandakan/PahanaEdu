<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Item" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    List<Item> items = (List<Item>) request.getAttribute("items");
    String successMessage = (String) request.getAttribute("success");
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Management - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= ctx %>/css/items.css">
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
        <a href="<%= ctx %>/dashboard">Dashboard</a> &gt; Item Management
    </div>

    <div class="content-header">
        <h2>Item Management</h2>
        <a href="<%= ctx %>/items?action=new" class="btn btn-primary">Add New Item</a>
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

    <% if (items != null && !items.isEmpty()) { %>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Category</th>
            <th>Unit Price</th>
            <th>Created Date</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Item item : items) { %>
        <tr>
            <td><%= item.getItemId() %>
            </td>
            <td><%= item.getName() %>
            </td>
            <td><%= item.getDescription() != null ? item.getDescription() : "-" %>
            </td>
            <td><%= item.getCategoryName() != null ? item.getCategoryName() : "N/A" %>
            </td>
            <td><%= "LKR " + item.getUnitPrice().toString() %>
            </td>
            <td><%= item.getCreatedAt() != null ? item.getCreatedAt().toLocalDate() : "-" %>
            </td>
            <td>
                <div class="actions">
                    <a href="<%= ctx %>/items?action=edit&id=<%= item.getItemId() %>"
                       class="btn btn-warning">Edit</a>
                    <a href="<%= ctx %>/items?action=delete&id=<%= item.getItemId() %>"
                       class="btn btn-danger">Delete</a>
                </div>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <div class="empty-state">
        <h3>No Items Found</h3>
        <p>No items have been added yet. Click "Add New Item" to get started.</p>
    </div>
    <% } %>

    <div class="navigation-actions">
        <a href="<%= ctx %>/dashboard" class="btn btn-secondary">Back to Dashboard</a>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/items.js"></script>
</body>
</html>
