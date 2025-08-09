<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Customer" %>
<%@ page import="com.pahanaedu.model.Bill" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    Customer customer = (Customer) request.getAttribute("customer");
    List<Bill> customerBills = (List<Bill>) request.getAttribute("customerBills");
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Details - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/account-details.css">
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
        <a href="<%= ctx %>/dashboard">Dashboard</a> &gt;
        <a href="<%= ctx %>/customers">Customer Management</a> &gt;
        Account Details
    </div>

    <div class="content-header">
        <h2>Account Details</h2>
        <% if (customer != null) { %>
        <a href="<%= ctx %>/customers?action=edit&id=<%= customer.getCustomerId() %>" class="btn btn-primary">Edit
            Customer</a>
        <% } %>
    </div>

    <% if (errorMessage != null) { %>
    <div class="alert alert-danger">
        <%= errorMessage %>
    </div>
    <% } %>

    <% if (customer != null) { %>
    <div class="account-details-container">
        <div class="account-header">
            <h3>Customer Information</h3>
            <div class="account-number-badge">
                #<%= customer.getAccountNumber() %>
            </div>
        </div>

        <div class="details-grid">
            <div class="detail-item">
                <label>Full Name:</label>
                <div class="detail-value"><%= customer.getName() %>
                </div>
            </div>

            <div class="detail-item">
                <label>Account Number:</label>
                <div class="detail-value"><%= customer.getAccountNumber() %>
                </div>
            </div>

            <div class="detail-item">
                <label>Address:</label>
                <div class="detail-value"><%= customer.getAddress() %>
                </div>
            </div>

            <div class="detail-item">
                <label>Telephone:</label>
                <div class="detail-value"><%= customer.getTelephone() %>
                </div>
            </div>

            <div class="detail-item">
                <label>Email:</label>
                <div class="detail-value">
                    <%= customer.getEmail() != null && !customer.getEmail().trim().isEmpty() ? customer.getEmail() : "Not provided" %>
                </div>
            </div>

            <div class="detail-item">
                <label>Account Created:</label>
                <div class="detail-value">
                    <%= customer.getCreatedAt() != null ? customer.getCreatedAt().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a")) : "Not available" %>
                </div>
            </div>

            <% if (customer.getUpdatedAt() != null && !customer.getUpdatedAt().equals(customer.getCreatedAt())) { %>
            <div class="detail-item">
                <label>Last Updated:</label>
                <div class="detail-value">
                    <%= customer.getUpdatedAt().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a")) %>
                </div>
            </div>
            <% } %>
        </div>

        <div class="account-actions">
            <a href="<%= ctx %>/customers?action=edit&id=<%= customer.getCustomerId() %>" class="btn btn-warning">Edit
                Customer</a>
            <a href="<%= ctx %>/customers?action=delete&id=<%= customer.getCustomerId() %>"
               class="btn btn-danger">Delete Customer</a>
        </div>
    </div>

    <div class="purchase-history-container">
        <div class="section-header">
            <h3>Purchase History</h3>
            <% if (customerBills != null && !customerBills.isEmpty()) { %>
            <span class="total-orders-badge"><%= customerBills.size() %> orders</span>
            <% } %>
        </div>

        <% if (customerBills != null && !customerBills.isEmpty()) { %>
        <div class="bills-table-container">
            <table class="table bills-table">
                <thead>
                <tr>
                    <th>Bill ID</th>
                    <th>Date</th>
                    <th>Total Amount</th>
                    <th>Payment Method</th>
                    <th>Payment Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% for (Bill bill : customerBills) { %>
                <tr>
                    <td class="bill-id">#<%= bill.getBillId() %>
                    </td>
                    <td class="bill-date">
                        <%= bill.getCreatedAt() != null ? bill.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "-" %>
                    </td>
                    <td class="bill-amount">
                        LKR <%= String.format("%,.2f", bill.getTotalAmount()) %>
                    </td>
                    <td class="payment-method">
                                <span class="payment-badge <%= bill.getPaymentMethod().getCssClass() %>">
                                    <%= bill.getPaymentMethod().getDisplayName() %>
                                </span>
                    </td>
                    <td class="payment-status">
                                <span class="status-badge <%= bill.getPaymentStatus().getCssClass() %>">
                                    <%= bill.getPaymentStatus().getDisplayName() %>
                                </span>
                    </td>
                    <td class="bill-actions">
                        <a href="<%= ctx %>/bills?action=view&id=<%= bill.getBillId() %>"
                           class="btn btn-info btn-sm">View</a>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div class="purchase-summary">
            <div class="summary-stats">
                <div class="stat-item">
                    <label>Total Orders:</label>
                    <span class="stat-value"><%= customerBills.size() %></span>
                </div>
                <div class="stat-item">
                    <label>Total Spent:</label>
                    <span class="stat-value">
                            LKR <%= String.format("%,.2f", customerBills.stream()
                            .map(Bill::getTotalAmount)
                            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)) %>
                        </span>
                </div>
                <div class="stat-item">
                    <label>Last Order:</label>
                    <span class="stat-value">
                            <%= customerBills.get(0).getCreatedAt() != null ?
                                    customerBills.get(0).getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) :
                                    "Never" %>
                        </span>
                </div>
            </div>
        </div>
        <% } else { %>
        <div class="empty-state">
            <h4>No Purchase History</h4>
            <p>This customer hasn't made any purchases yet.</p>
            <a href="<%= ctx %>/bills?action=new&customerId=<%= customer.getCustomerId() %>"
               class="btn btn-primary">Create First Bill</a>
        </div>
        <% } %>
    </div>
    <% } else { %>
    <div class="empty-state">
        <h3>Customer Not Found</h3>
        <p>The requested customer account could not be found.</p>
    </div>
    <% } %>

    <div class="navigation-actions">
        <a href="<%= ctx %>/customers" class="btn btn-secondary">Back to Customer List</a>
        <a href="<%= ctx %>/dashboard" class="btn btn-secondary">Back to Dashboard</a>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/account-details.js"></script>
</body>
</html>
