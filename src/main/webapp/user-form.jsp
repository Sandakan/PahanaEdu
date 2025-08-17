<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.enums.UserRole" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || !currentUser.isAdmin()) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }

    User user = (User) request.getAttribute("user");
    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    String ctx = request.getContextPath();
    
    boolean isEdit = user != null;
    String pageTitle = isEdit ? "Edit User" : "Add New User";
    String formAction = isEdit ? "update" : "create";
    
    String email = request.getParameter("email");
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String role = request.getParameter("role");
    
    if (isEdit && email == null) {
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        role = user.getRole();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %> - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/user-form.css">
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
        <a href="<%= ctx %>/dashboard">Dashboard</a> > 
        <a href="<%= ctx %>/users">User Management</a> > 
        <%= pageTitle %>
    </div>

    <div class="content-header">
        <h2><%= pageTitle %></h2>
    </div>

    <% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
    <% } %>
    
    <% if (error != null) { %>
    <div class="alert alert-error"><%= error %></div>
    <% } %>

    <form class="form-container" method="post" action="<%= ctx %>/users">
        <input type="hidden" name="action" value="<%= formAction %>">
        <% if (isEdit) { %>
        <input type="hidden" name="userId" value="<%= user.getUserId() %>">
        <% } %>

        <div class="form-section">
            <h3>User Information</h3>
            
            <div class="form-group">
                <label for="firstName">First Name <span class="required">*</span></label>
                <input type="text" id="firstName" name="firstName" 
                       value="<%= firstName != null ? firstName : "" %>" required>
            </div>

            <div class="form-group">
                <label for="lastName">Last Name <span class="required">*</span></label>
                <input type="text" id="lastName" name="lastName" 
                       value="<%= lastName != null ? lastName : "" %>" required>
            </div>

            <div class="form-group">
                <label for="email">Email Address <span class="required">*</span></label>
                <input type="email" id="email" name="email" 
                       value="<%= email != null ? email : "" %>" required>
            </div>

            <div class="form-group">
                <label for="role">Role <span class="required">*</span></label>
                <select id="role" name="role" required>
                    <option value="">Select Role</option>
                    <% for (UserRole userRole : UserRole.values()) { %>
                    <option value="<%= userRole %>" <%= userRole.toString().equals(role) ? "selected" : "" %>>
                        <%= userRole %>
                    </option>
                    <% } %>
                </select>
            </div>

            <div class="form-group">
                <label for="password">Password <% if (!isEdit) { %><span class="required">*</span><% } %></label>
                <input type="password" id="password" name="password" 
                       minlength="6" <% if (!isEdit) { %>required<% } %>>
                <small class="form-help">
                    <% if (isEdit) { %>
                        Leave blank to keep current password - Minimum 6 characters
                    <% } else { %>
                        Minimum 6 characters
                    <% } %>
                </small>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Confirm Password <% if (!isEdit) { %><span class="required">*</span><% } %></label>
                <input type="password" id="confirmPassword" name="confirmPassword" 
                       minlength="6" <% if (!isEdit) { %>required<% } %>>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary" id="submitBtn">
                <%= isEdit ? "Update User" : "Create User" %>
            </button>
            <a href="<%= ctx %>/users" class="btn btn-outline">Cancel</a>
        </div>
    </form>

    <div class="navigation-actions">
        <a href="<%= ctx %>/users" class="btn btn-outline">Back to User List</a>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/user-form.js"></script>
</body>
</html>
