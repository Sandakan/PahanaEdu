<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Bill" %>
<%@ page import="com.pahanaedu.model.BillItem" %>
<%@ page import="com.pahanaedu.model.Customer" %>
<%@ page import="com.pahanaedu.model.Item" %>
<%@ page import="com.pahanaedu.enums.PaymentMethod" %>
<%@ page import="com.pahanaedu.enums.PaymentStatus" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
    Bill bill = (Bill) request.getAttribute("bill");
    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
    List<Item> items = (List<Item>) request.getAttribute("items");
    String errorMessage = (String) request.getAttribute("error");
    Integer selectedCustomerId = (Integer) request.getAttribute("selectedCustomerId");

    boolean isEdit = bill != null;
    String pageTitle = isEdit ? "Edit Bill" : "Create New Bill";
    String submitText = isEdit ? "Update Bill" : "Create Bill";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %> - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/bill-form.css">
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
        <a href="<%= ctx %>/bills">Bill Management</a> &gt;
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

        <form method="POST" action="<%= ctx %>/bills" id="billForm">
            <input type="hidden" name="action" value="<%= isEdit ? "update" : "create" %>">
            <% if (isEdit) { %>
            <input type="hidden" name="billId" value="<%= bill.getBillId() %>">
            <% } %>

            <div class="form-row">
                <div class="form-group">
                    <label for="customerId">Customer <span class="required">*</span></label>
                    <select id="customerId" name="customerId" required>
                        <option value="">-- Select Customer --</option>
                        <% if (customers != null) {
                            for (Customer customer : customers) {
                                boolean selected = (isEdit && bill.getCustomerId() == customer.getCustomerId()) ||
                                                 (!isEdit && selectedCustomerId != null && selectedCustomerId == customer.getCustomerId());
                        %>
                        <option value="<%= customer.getCustomerId() %>" <%= selected ? "selected" : "" %>>
                            <%= customer.getAccountNumber() %> - <%= customer.getName() %>
                        </option>
                        <% }
                        } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="paymentMethod">Payment Method <span class="required">*</span></label>
                    <select id="paymentMethod" name="paymentMethod" required>
                        <% for (PaymentMethod method : PaymentMethod.values()) { %>
                        <option value="<%= method.name() %>" <%= isEdit && bill.getPaymentMethod() == method ? "selected" : "" %>>
                            <%= method.getDisplayName() %>
                        </option>
                        <% } %>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="paymentStatus">Payment Status <span class="required">*</span></label>
                    <select id="paymentStatus" name="paymentStatus" required>
                        <% for (PaymentStatus status : PaymentStatus.values()) { %>
                        <option value="<%= status.name() %>" <%= isEdit && bill.getPaymentStatus() == status ? "selected" : "" %>>
                            <%= status.getDisplayName() %>
                        </option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="notes">Notes</label>
                    <textarea id="notes" name="notes" rows="3"
                              placeholder="Optional notes about this bill..."><%= isEdit && bill.getNotes() != null ? bill.getNotes() : "" %></textarea>
                </div>
            </div>

            <div class="items-section">
                <h3>Bill Items</h3>
                <p>Add items to this bill. At least one item is required.</p>

                <div class="items-container" id="items-container">
                    <!-- Bill Items will be added here dynamically using javascript -->
                </div>

                <div class="add-item-controls">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="itemSelect">Select Item</label>
                            <select id="itemSelect">
                                <option value="">-- Select Item to Add --</option>
                                <% if (items != null) {
                                    for (Item item : items) {
                                %>
                                <option value="<%= item.getItemId() %>"
                                        data-name="<%= item.getName() %>"
                                        data-price="<%= item.getUnitPrice() %>"
                                        data-category="<%= item.getCategoryName() %>">
                                    <%= item.getName() %> - LKR <%= String.format("%.2f", item.getUnitPrice()) %>
                                    <% if (item.getCategoryName() != null) { %>
                                    (<%= item.getCategoryName() %>)
                                    <% } %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="quantityInput">Quantity</label>
                            <input type="number" id="quantityInput" min="1" value="1">
                        </div>
                        <div class="form-group">
                            <button type="button" class="btn btn-secondary add-item-to-bill" onclick="addItemToBill()">Add Item</button>
                        </div>
                    </div>
                </div>

                <div class="bill-total">
                    <h4>Total Amount: LKR <span id="totalAmount">0.00</span></h4>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">
                    <%= submitText %>
                </button>
                <a href="<%= ctx %>/bills" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script src="<%= ctx %>/js/common.js"></script>
<script src="<%= ctx %>/js/bill-form.js"></script>

<% if (isEdit && bill.getBillItems() != null && !bill.getBillItems().isEmpty()) {
    List<BillItem> billItems = bill.getBillItems();
%>
<script>
    // Load existing bill items for editing
    document.addEventListener('DOMContentLoaded', function () {
        const existingItems = [
            <% for (int i = 0; i < billItems.size(); i++) {
                BillItem billItem = billItems.get(i);
                if (billItem.getItem() != null) {
                int id = i + 1;
                int itemId = billItem.getItemId();
                int quantity = billItem.getQuantity();
                double unitPrice = billItem.getUnitPrice().doubleValue();
                double lineTotal = billItem.getLineTotal().doubleValue();
                String name = billItem.getItem().getName().replace("'", "\\'");
                String category = billItem.getItem().getCategoryName();
                %>
            {
                id: <%= id %>,
                itemId: <%= itemId %>,
                name: '<%= name %>',
                category: '<%= category %>',
                unitPrice: <%= unitPrice %>,
                quantity: <%= quantity %>,
                lineTotal: <%= lineTotal %>
            },
            <% }
            } %>
        ];

        if (existingItems.length > 0) {
            loadExistingBillItems(existingItems);
        }
    });
</script>
<% } %>
</body>
</html>
