<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Bill" %>
<%@ page import="com.pahanaedu.model.BillItem" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.math.BigDecimal" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    Bill bill = (Bill) request.getAttribute("bill");
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill #<%= bill != null ? bill.getBillId() : "Unknown" %> - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/bill-view.css">
</head>
<body>
<div class="header no-print">
    <h1>Pahana Edu Billing System</h1>
    <div class="user-info">
        <span class="user-name">Welcome, <%= user.getFullName() %></span>
        <span class="role-badge"><%= user.getRole() %></span>
        <a href="<%= ctx %>/logout" class="logout-btn">Logout</a>
    </div>
</div>

<div class="container-full">
    <div class="breadcrumb no-print">
        <a href="<%= ctx %>/dashboard">Dashboard</a> &gt; 
        <a href="<%= ctx %>/bills">Bill Management</a> &gt; 
        Bill Details
    </div>

    <div class="content-header no-print">
        <h2>Bill Details</h2>
        <div class="action-buttons">
            <button id="printBillBtn" class="btn btn-primary">Print Bill</button>
            <% if (bill != null) { %>
            <a href="<%= ctx %>/bills?action=edit&id=<%= bill.getBillId() %>" class="btn btn-warning">Edit Bill</a>
            <% } %>
        </div>
    </div>

    <% if (errorMessage != null) { %>
    <div class="alert alert-danger no-print">
        <%= errorMessage %>
    </div>
    <% } %>

    <% if (bill != null) { %>
    <!-- Printable Bill Content -->
    <div class="bill-container">
        <div class="company-header">
            <h1>Pahana Edu Billing System</h1>
            <p>Sri Lanka</p>
        </div>

        <div class="bill-header">
            <div class="bill-info">
                <h2>BILL #<%= bill.getBillId() %></h2>
                <div class="bill-status">
                    <span class="status-badge <%= bill.getPaymentStatus().getCssClass() %>">
                        <%= bill.getPaymentStatus().getDisplayName() %>
                    </span>
                </div>
            </div>
            <div class="bill-dates">
                <p><strong>Date:</strong> <%= bill.getCreatedAt() != null ? bill.getCreatedAt().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) : "N/A" %></p>
                <p><strong>Time:</strong> <%= bill.getCreatedAt() != null ? bill.getCreatedAt().format(DateTimeFormatter.ofPattern("hh:mm a")) : "N/A" %></p>
            </div>
        </div>

        <div class="customer-section">
            <h3>Bill To:</h3>
            <div class="customer-details">
                <p><strong><%= bill.getCustomer() != null && bill.getCustomer().getName() != null ? bill.getCustomer().getName() : "Unknown Customer" %></strong></p>
                <p>Account #: <%= bill.getCustomer() != null && bill.getCustomer().getAccountNumber() != null ? bill.getCustomer().getAccountNumber() : "N/A" %></p>
                <% if (bill.getCustomer() != null) { %>
                <p><%= bill.getCustomer().getAddress() != null ? bill.getCustomer().getAddress() : "" %></p>
                <p>Tel: <%= bill.getCustomer().getTelephone() != null ? bill.getCustomer().getTelephone() : "N/A" %></p>
                <% if (bill.getCustomer().getEmail() != null && !bill.getCustomer().getEmail().trim().isEmpty()) { %>
                <p>Email: <%= bill.getCustomer().getEmail() %></p>
                <% } %>
                <% } %>
            </div>
        </div>

        <div class="items-section">
            <h3>Items</h3>
            <table class="table items-table">
                <thead>
                <tr>
                    <th class="item-col">Item</th>
                    <th class="qty-col">Qty</th>
                    <th class="price-col">Unit Price</th>
                    <th class="total-col">Line Total</th>
                </tr>
                </thead>
                <tbody>
                <% 
                BigDecimal grandTotal = BigDecimal.ZERO;
                int totalQuantity = 0;
                if (bill.getBillItems() != null) {
                    for (BillItem item : bill.getBillItems()) {
                        grandTotal = grandTotal.add(item.getLineTotal());
                        totalQuantity += item.getQuantity();
                %>
                <tr>
                    <td class="item-col">
                        <div class="item-details">
                            <strong><%= item.getItemName() != null ? item.getItemName() : "Unknown Item" %></strong>
                            <% if (item.getItemDescription() != null && !item.getItemDescription().trim().isEmpty()) { %>
                            <br><small><%= item.getItemDescription() %></small>
                            <% } %>
                            <% if (item.getCategoryName() != null) { %>
                            <br><small class="category">Category: <%= item.getCategoryName() %></small>
                            <% } %>
                        </div>
                    </td>
                    <td class="qty-col"><%= item.getQuantity() %></td>
                    <td class="price-col">LKR <%= String.format("%.2f", item.getUnitPrice()) %></td>
                    <td class="total-col">LKR <%= String.format("%.2f", item.getLineTotal()) %></td>
                </tr>
                <%
                    }
                }
                %>
                </tbody>
            </table>
        </div>

        <div class="bill-summary">
            <div class="summary-row">
                <span>Total Items:</span>
                <span><%= bill.getItemCount() %></span>
            </div>
            <div class="summary-row">
                <span>Total Quantity:</span>
                <span><%= totalQuantity %></span>
            </div>
            <div class="summary-row total-row">
                <span><strong>Grand Total:</strong></span>
                <span><strong>LKR <%= String.format("%.2f", bill.getTotalAmount()) %></strong></span>
            </div>
        </div>

        <div class="payment-section">
            <h3>Payment Information</h3>
            <div class="payment-details">
                <p><strong>Payment Method:</strong> <%= bill.getPaymentMethod().getDisplayName() %></p>
                <p><strong>Payment Status:</strong> <%= bill.getPaymentStatus().getDisplayName() %></p>
                <% if (bill.getNotes() != null && !bill.getNotes().trim().isEmpty()) { %>
                <p><strong>Notes:</strong> <%= bill.getNotes() %></p>
                <% } %>
            </div>
        </div>

        <div class="bill-footer">
            <p>Generated by: <%= bill.getUser() != null && bill.getUser().getFullName() != null ? bill.getUser().getFullName() : "System" %></p>
            <p>Generated on: <%= java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")) %></p>
        </div>
    </div>
    <% } else { %>
    <div class="empty-state">
        <h3>Bill Not Found</h3>
        <p>The requested bill could not be found.</p>
    </div>
    <% } %>

    <div class="navigation-actions no-print">
        <a href="<%= ctx %>/bills" class="btn btn-secondary">Back to Bills</a>
        <a href="<%= ctx %>/dashboard" class="btn btn-secondary">Back to Dashboard</a>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/bill-view.js"></script>
</body>
</html>
