<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Customer" %>
<%
    // Check if user is logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    Customer customer = (Customer) request.getAttribute("customer");
    String accountNumber = (String) request.getAttribute("accountNumber");
    String errorMessage = (String) request.getAttribute("error");
    
    boolean isEditMode = customer != null;
    String pageTitle = isEditMode ? "Edit Customer" : "Add New Customer";
    String formAction = isEditMode ? "update" : "create";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %> - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/customer-form.css">
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
        <a href="<%= ctx %>/dashboard">Dashboard</a> > 
        <a href="<%= ctx %>/customers">Customer Management</a> > 
        <%= pageTitle %>
    </div>
    
    <div class="form-container">
        <h2><%= pageTitle %></h2>
        
        <% if (errorMessage != null) { %>
            <div class="alert alert-danger">
                <%= errorMessage %>
            </div>
        <% } %>
        
        <form method="post" action="<%= ctx %>/customers">
            <input type="hidden" name="action" value="<%= formAction %>">
            <% if (isEditMode) { %>
                <input type="hidden" name="customerId" value="<%= customer.getCustomerId() %>">
            <% } %>
            
            <div class="form-group">
                <label for="accountNumber">Account Number <span class="required">*</span></label>
                <input type="text" 
                       id="accountNumber" 
                       name="accountNumber" 
                       value="<%= isEditMode ? customer.getAccountNumber() : (accountNumber != null ? accountNumber : "") %>"
                       <% if (isEditMode) { %>readonly class="readonly"<% } %>
                       required>
                <% if (!isEditMode) { %>
                    <small style="color: #6c757d;">Account number is auto-generated</small>
                <% } %>
            </div>
            
            <div class="form-group">
                <label for="name">Customer Name <span class="required">*</span></label>
                <input type="text" 
                       id="name" 
                       name="name" 
                       value="<%= isEditMode ? customer.getName() : "" %>"
                       required
                       maxlength="100">
            </div>
            
            <div class="form-group">
                <label for="address">Address <span class="required">*</span></label>
                <textarea id="address" 
                          name="address" 
                          required><%= isEditMode ? customer.getAddress() : "" %></textarea>
            </div>
            
            <div class="form-group">
                <label for="telephone">Telephone <span class="required">*</span></label>
                <input type="tel" 
                       id="telephone" 
                       name="telephone" 
                       value="<%= isEditMode ? customer.getTelephone() : "" %>"
                       required
                       maxlength="15"
                       pattern="[0-9+\-\s()]*"
                       title="Please enter a valid telephone number">
            </div>
            
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" 
                       id="email" 
                       name="email" 
                       value="<%= isEditMode && customer.getEmail() != null ? customer.getEmail() : "" %>"
                       maxlength="100">
                <small style="color: #6c757d;">Optional field</small>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn">
                    <%= isEditMode ? "Update Customer" : "Create Customer" %>
                </button>
                <a href="<%= ctx %>/customers" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/customer-form.js"></script>
</body>
</html>
