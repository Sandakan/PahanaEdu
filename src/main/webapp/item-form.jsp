<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Item" %>
<%@ page import="com.pahanaedu.model.Category" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    Item item = (Item) request.getAttribute("item");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    String errorMessage = (String) request.getAttribute("error");

    boolean isEdit = item != null;
    String pageTitle = isEdit ? "Edit Item" : "Add New Item";
    String formAction = isEdit ? "update" : "create";
    String submitText = isEdit ? "Update Item" : "Create Item";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %> - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/item-form.css">
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
        <a href="<%= ctx %>/items">Item Management</a> &gt;
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

        <form method="post" action="<%= ctx %>/items">
            <input type="hidden" name="action" value="<%= formAction %>">
            <% if (isEdit) { %>
            <input type="hidden" name="itemId" value="<%= item.getItemId() %>">
            <% } %>

            <div class="form-group">
                <label for="name">Item Name <span class="required">*</span></label>
                <input type="text"
                       id="name"
                       name="name"
                       value="<%= isEdit ? item.getName() : "" %>"
                       required
                       maxlength="100">
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description"
                          name="description"><%= isEdit && item.getDescription() != null ? item.getDescription() : "" %></textarea>
                <small style="color: #6c757d;">Optional field</small>
            </div>

            <div class="form-group">
                <label for="categoryId">Category</label>
                <select id="categoryId" name="categoryId" required>
                    <option value="">-- Select Category --</option>
                    <% if (categories != null) {
                        for (Category category : categories) {
                            boolean selected = isEdit && item.getCategoryId() == category.getCategoryId();
                    %>
                    <option value="<%= category.getCategoryId() %>" <%= selected ? "selected" : "" %>>
                        <%= category.getName() %>
                    </option>
                    <% }
                    } %>
                </select>
                <small style="color: #6c757d;">Optional field</small>
            </div>

            <div class="form-group">
                <label for="unitPrice">Unit Price (LKR) <span class="required">*</span></label>
                <input type="number"
                       id="unitPrice"
                       name="unitPrice"
                       value="<%= isEdit && item.getUnitPrice() != null ? item.getUnitPrice() : "" %>"
                       step="0.01"
                       min="0.01"
                       required>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">
                    <%= submitText %>
                </button>
                <a href="<%= ctx %>/items" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/item-form.js"></script>
</body>
</html>
