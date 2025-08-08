<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Category" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    Category category = (Category) request.getAttribute("category");
    String errorMessage = (String) request.getAttribute("error");

    boolean isEdit = category != null;
    String pageTitle = isEdit ? "Edit Category" : "Add New Category";
    String formAction = isEdit ? "update" : "create";
    String submitText = isEdit ? "Update Category" : "Create Category";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %> - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
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

<div class="container">
    <div class="breadcrumb">
        <a href="<%= ctx %>/dashboard">Dashboard</a> &gt;
        <a href="<%= ctx %>/categories">Category Management</a> &gt;
        <%= pageTitle %>
    </div>

    <div class="form-container">
        <h2><%= pageTitle %>
        </h2>

        <% if (errorMessage != null) { %>
        <div class="alert alert-danger">
            <%= errorMessage %>
        </div>
        <% } %>

        <form method="post" action="<%= ctx %>/categories">
            <input type="hidden" name="action" value="<%= formAction %>">
            <% if (isEdit) { %>
            <input type="hidden" name="categoryId" value="<%= category.getCategoryId() %>">
            <% } %>

            <div class="form-group">
                <label for="name">Category Name <span class="required">*</span></label>
                <input type="text"
                       id="name"
                       name="name"
                       value="<%= isEdit ? category.getName() : "" %>"
                       required
                       maxlength="100">
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description"
                          name="description"><%= isEdit && category.getDescription() != null ? category.getDescription() : "" %></textarea>
                <small style="color: #6c757d;">Optional field</small>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">
                    <%= submitText %>
                </button>
                <a href="<%= ctx %>/categories" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/category-form.js"></script>
</body>
</html>
