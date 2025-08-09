<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Bill" %>
<%@ page import="com.pahanaedu.enums.PaymentStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    List<Bill> bills = (List<Bill>) request.getAttribute("bills");
    String successMessage = (String) request.getAttribute("success");
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill Management - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/bills.css">
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
        <a href="<%= ctx %>/dashboard">Dashboard</a> &gt; Bill Management
    </div>

    <div class="content-header">
        <h2>Bill Management</h2>
        <a href="<%= ctx %>/bills?action=new" class="btn btn-primary">Create New Bill</a>
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

    <% if (bills != null && !bills.isEmpty()) { %>
    <div class="bills-summary">
        <p>Found <%= bills.size() %> bill(s)</p>
    </div>

    <table class="table">
        <thead>
        <tr>
            <th>Bill #</th>
            <th>Customer</th>
            <th>Account #</th>
            <th>Total Amount</th>
            <th>Payment Status</th>
            <th>Payment Method</th>
            <th>Items</th>
            <th>Created Date</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (Bill bill : bills) { %>
        <tr>
            <td><strong>#<%= bill.getBillId() %>
            </strong></td>
            <td><%= bill.getCustomer() != null && bill.getCustomer().getName() != null ? bill.getCustomer().getName() : "Unknown" %>
            </td>
            <td><%= bill.getCustomer() != null && bill.getCustomer().getAccountNumber() != null ? bill.getCustomer().getAccountNumber() : "-" %>
            </td>
            <td class="amount">LKR <%= String.format("%.2f", bill.getTotalAmount()) %>
            </td>
            <td>
                <span class="status-badge status-<%= bill.getPaymentStatus().name().toLowerCase() %>">
                    <%= bill.getPaymentStatus().getDisplayName() %>
                </span>
            </td>
            <td><%= bill.getPaymentMethod().getDisplayName() %>
            </td>
            <td class="text-center">
                <%= bill.getItemCount() %> item(s)
                <br>
                <small>(<%= bill.getTotalQuantity() %> qty)</small>
            </td>
            <td><%= bill.getCreatedAt() != null ? bill.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "-" %>
            </td>
            <td>
                <div class="actions">
                    <a href="<%= ctx %>/bills?action=view&id=<%= bill.getBillId() %>"
                       class="btn btn-info btn-sm">View</a>
                    <a href="<%= ctx %>/bills?action=edit&id=<%= bill.getBillId() %>"
                       class="btn btn-warning btn-sm">Edit</a>
                    <% if (bill.getPaymentStatus() == PaymentStatus.PENDING) { %>
                    <a href="<%= ctx %>/bills?action=updateStatus&id=<%= bill.getBillId() %>&status=PAID"
                       class="btn btn-success btn-sm">Mark Paid</a>
                    <% } %>
                    <a href="<%= ctx %>/bills?action=delete&id=<%= bill.getBillId() %>"
                       class="btn btn-danger btn-sm">Delete</a>
                </div>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <div class="empty-state">
        <h3>No Bills Found</h3>
        <p>No bills have been created yet. Click "Create New Bill" to get started.</p>
    </div>
    <% } %>

    <div class="navigation-actions">
        <a href="<%= ctx %>/dashboard" class="btn btn-secondary">Back to Dashboard</a>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/bills.js"></script>
</body>
</html>
