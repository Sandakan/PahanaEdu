<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Customer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    // Check if user is logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
    String successMessage = (String) request.getAttribute("success");
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Management - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= ctx %>/css/customers.css">
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
        <a href="<%= ctx %>/dashboard">Dashboard</a> &gt; Customer Management
    </div>

    <div class="content-header">
        <h2>Customer Management</h2>
        <a href="<%= ctx %>/customers?action=new" class="btn btn-primary">Add New Customer</a>
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

    <% if (customers != null && !customers.isEmpty()) { %>
    <table class="table">
        <thead>
        <tr>
            <th>Account Number</th>
            <th>Name</th>
            <th>Address</th>
            <th>Telephone</th>
            <th>Email</th>
            <th>Created At</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Customer customer : customers) { %>
        <tr>
            <td><%= customer.getAccountNumber() %>
            </td>
            <td><%= customer.getName() %>
            </td>
            <td><%= customer.getAddress() %>
            </td>
            <td><%= customer.getTelephone() %>
            </td>
            <td><%= customer.getEmail() != null ? customer.getEmail() : "-" %>
            </td>
            <td><%= customer.getCreatedAt() != null ? customer.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a")) : "-" %>
            </td>
            <td>
                <div class="actions">
                    <a href="<%= ctx %>/customers?action=view&id=<%= customer.getCustomerId() %>"
                       class="btn btn-info">View Details</a>
                    <a href="<%= ctx %>/customers?action=edit&id=<%= customer.getCustomerId() %>"
                       class="btn btn-warning">Edit</a>
                    <a href="<%= ctx %>/customers?action=delete&id=<%= customer.getCustomerId() %>"
                       class="btn btn-danger">Delete</a>
                </div>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <div class="empty-state">
        <h3>No Customers Found</h3>
        <p>No customers have been added yet. Click "Add New Customer" to get started.</p>
    </div>
    <% } %>

    <div class="navigation-actions">
        <a href="<%= ctx %>/dashboard" class="btn btn-secondary">Back to Dashboard</a>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/customers.js"></script>
</body>
</html>
