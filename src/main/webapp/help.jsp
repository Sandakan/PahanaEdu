<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help - Pahana Edu Billing System</title>
    <link rel="stylesheet" href="<%= ctx %>/css/common.css">
    <link rel="stylesheet" href="<%= ctx %>/css/help.css">
</head>
<body>
<div class="header">
    <h1>Pahana Edu Billing System</h1>
    <div class="user-info">
        <span class="user-name">Welcome, <%= user.getFullName() %></span>
        <span class="role-badge"><%= user.getRole() %></span>
        <a href="<%= request.getContextPath() %>/logout" class="logout-btn">Logout</a>
    </div>
</div>

<div class="container-full">
    <div class="breadcrumb">
        <a href="<%= ctx %>/dashboard">Dashboard</a> > Help
    </div>

    <div class="content-header">
        <h2>System Help & User Guide</h2>
    </div>

    <div class="help-content">
        <div class="help-section">
            <h3>Getting Started</h3>
            <div class="help-card">
                <h4>Welcome to Pahana Edu Billing System</h4>
                <p>This system is designed to help educational institutions in Sri Lanka manage their billing operations efficiently. All monetary values are displayed in Sri Lankan Rupees (LKR).</p>
                <ul>
                    <li>Access different modules from the dashboard</li>
                    <li>All changes are automatically saved to the database</li>
                    <li>Use the logout button to securely end your session</li>
                </ul>
            </div>
        </div>

        <div class="help-section">
            <h3>Customer Management</h3>
            <div class="help-card">
                <h4>Managing Customer Accounts</h4>
                <p>Create and manage customer profiles with automatic account number generation.</p>
                <ul>
                    <li><strong>Add Customer:</strong> Click "Add New Customer" and fill in the required details</li>
                    <li><strong>Account Numbers:</strong> Automatically generated as 6-digit zero-padded numbers (000001, 000002, etc.)</li>
                    <li><strong>Edit Customer:</strong> Click the "Edit" button next to any customer to modify their information</li>
                    <li><strong>Delete Customer:</strong> Click "Delete" to remove a customer (soft delete - maintains data integrity)</li>
                    <li><strong>Required Fields:</strong> Name, Address, Telephone, and Email are mandatory</li>
                </ul>
            </div>
        </div>

        <div class="help-section">
            <h3>Category & Item Management</h3>
            <div class="help-card">
                <h4>Organizing Your Inventory</h4>
                <p>Categories help organize items systematically. Items must be assigned to categories.</p>
                <ul>
                    <li><strong>Categories:</strong> Create categories first (e.g., "Books", "Stationery", "Uniforms")</li>
                    <li><strong>Items:</strong> Each item must be assigned to a category</li>
                    <li><strong>Pricing:</strong> Item prices are in LKR with automatic formatting (0.00)</li>
                    <li><strong>Validation:</strong> Item names must be unique, prices must be greater than 0</li>
                    <li><strong>Dependencies:</strong> Cannot delete categories that have items assigned to them</li>
                </ul>
            </div>
        </div>

        <div class="help-section">
            <h3>Bill Management</h3>
            <div class="help-card">
                <h4>Creating and Managing Bills</h4>
                <p>The complete billing system with dynamic item management and real-time calculations.</p>
                <ul>
                    <li><strong>Create Bill:</strong> Select a customer first, then add items dynamically</li>
                    <li><strong>Add Items:</strong> Use the dropdown to select items, specify quantities</li>
                    <li><strong>Real-time Calculations:</strong> Line totals and bill totals update automatically</li>
                    <li><strong>Payment Methods:</strong> Cash, Credit Card, Debit Card, Bank Transfer</li>
                    <li><strong>Payment Status:</strong> Pending, Paid, Cancelled</li>
                    <li><strong>Print Bills:</strong> Use the "View/Print" option for clean printable format</li>
                    <li><strong>Edit Bills:</strong> Modify existing bills including items and payment details</li>
                </ul>
            </div>
        </div>

        <div class="help-section">
            <h3>Navigation & Security</h3>
            <div class="help-card">
                <h4>System Navigation</h4>
                <ul>
                    <li><strong>Dashboard:</strong> Central hub for accessing all modules</li>
                    <li><strong>Breadcrumbs:</strong> Show your current location in the system</li>
                    <li><strong>Back Buttons:</strong> Return to previous pages or dashboard</li>
                    <li><strong>Auto-logout:</strong> Sessions expire after 30 minutes of inactivity</li>
                    <li><strong>Security:</strong> All pages except login require authentication</li>
                </ul>
            </div>
        </div>

        <div class="help-section">
            <h3>Data Management</h3>
            <div class="help-card">
                <h4>Data Integrity & Safety</h4>
                <ul>
                    <li><strong>Soft Deletes:</strong> Items are marked as deleted but preserved in database</li>
                    <li><strong>Validation:</strong> Multi-layer validation prevents invalid data entry</li>
                    <li><strong>Referential Integrity:</strong> Related data is maintained across modules</li>
                    <li><strong>Auto-save:</strong> Changes are immediately saved to the database</li>
                    <li><strong>Error Handling:</strong> Clear error messages guide you through issues</li>
                </ul>
            </div>
        </div>

        <div class="help-section">
            <h3>Troubleshooting</h3>
            <div class="help-card">
                <h4>Common Issues & Solutions</h4>
                <ul>
                    <li><strong>Cannot delete category:</strong> Remove all items from the category first</li>
                    <li><strong>Duplicate email error:</strong> Each customer must have a unique email address</li>
                    <li><strong>Session expired:</strong> Re-login if you see the login page unexpectedly</li>
                    <li><strong>Form validation errors:</strong> Check all required fields are filled correctly</li>
                    <li><strong>Print issues:</strong> Use the dedicated print view for best results</li>
                </ul>
            </div>
        </div>

        <div class="help-section">
            <h3>User Roles</h3>
            <div class="help-card">
                <h4>System Access Levels</h4>
                <ul>
                    <li><strong>Admin:</strong> Full access to all system features and data management</li>
                    <li><strong>Cashier:</strong> Access to billing, customer management, and basic operations</li>
                    <li><strong>Role Badge:</strong> Your current role is displayed in the header</li>
                </ul>
            </div>
        </div>

        <div class="help-section">
            <h3>Contact Information</h3>
            <div class="help-card">
                <h4>Need Additional Help?</h4>
                <p>For technical support or system-related issues, please contact your system administrator.</p>
                <ul>
                    <li>System Version: Pahana Edu Billing System v1.0</li>
                    <li>Built with Jakarta EE and MySQL</li>
                    <li>Optimized for educational institutions in Sri Lanka</li>
                </ul>
            </div>
        </div>
    </div>

    <div class="navigation-actions">
        <a href="<%= ctx %>/dashboard" class="btn btn-secondary">Back to Dashboard</a>
    </div>
</div>

</body>
</html>
