package com.pahanaedu.controller;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.model.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {
    private CustomerDAO customerDAO;

    @Override
    public void init() {
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewCustomerForm(request, response);
                break;
            case "edit":
                showEditCustomerForm(request, response);
                break;
            case "view":
                viewCustomerDetails(request, response);
                break;
            case "delete":
                deleteCustomer(request, response);
                break;
            default:
                listCustomers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "create":
                createCustomer(request, response);
                break;
            case "update":
                updateCustomer(request, response);
                break;
            default:
                listCustomers(request, response);
                break;
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Customer> customers = customerDAO.getAllCustomers();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/customers.jsp").forward(request, response);
    }

    private void showNewCustomerForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accountNumber = customerDAO.generateAccountNumber();
        request.setAttribute("accountNumber", accountNumber);
        request.getRequestDispatcher("/customer-form.jsp").forward(request, response);
    }

    private void showEditCustomerForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int customerId = Integer.parseInt(request.getParameter("id"));
            Customer customer = customerDAO.getCustomerById(customerId);

            if (customer != null) {
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/customer-form.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Customer not found");
                listCustomers(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID");
            listCustomers(request, response);
        }
    }

    private void viewCustomerDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int customerId = Integer.parseInt(request.getParameter("id"));
            Customer customer = customerDAO.getCustomerById(customerId);

            if (customer != null) {
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/account-details.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Customer not found");
                listCustomers(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID");
            listCustomers(request, response);
        }
    }

    private void createCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accountNumber = request.getParameter("accountNumber");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email");

        // Validation
        if (isNullOrEmpty(accountNumber) || isNullOrEmpty(name) ||
                isNullOrEmpty(address) || isNullOrEmpty(telephone)) {
            request.setAttribute("error", "Account number, name, address, and telephone are required fields");
            request.setAttribute("accountNumber", accountNumber);
            request.getRequestDispatcher("/customer-form.jsp").forward(request, response);
            return;
        }

        // Check if account number already exists
        if (customerDAO.isAccountNumberExists(accountNumber)) {
            request.setAttribute("error", "Account number already exists");
            request.setAttribute("accountNumber", customerDAO.generateAccountNumber());
            request.getRequestDispatcher("/customer-form.jsp").forward(request, response);
            return;
        }

        Customer customer = new Customer(accountNumber, name, address, telephone,
                isNullOrEmpty(email) ? null : email);

        if (customerDAO.createCustomer(customer)) {
            request.setAttribute("success", "Customer created successfully");
        } else {
            request.setAttribute("error", "Failed to create customer");
        }

        listCustomers(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            String accountNumber = request.getParameter("accountNumber");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String telephone = request.getParameter("telephone");
            String email = request.getParameter("email");

            // Validation
            if (isNullOrEmpty(accountNumber) || isNullOrEmpty(name) ||
                    isNullOrEmpty(address) || isNullOrEmpty(telephone)) {
                request.setAttribute("error", "Account number, name, address, and telephone are required fields");
                Customer customer = new Customer(customerId, accountNumber, name, address, telephone, email);
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/customer-form.jsp").forward(request, response);
                return;
            }

            // Check if account number already exists for another customer
            if (customerDAO.isAccountNumberExistsForUpdate(accountNumber, customerId)) {
                request.setAttribute("error", "Account number already exists for another customer");
                Customer customer = new Customer(customerId, accountNumber, name, address, telephone, email);
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/customer-form.jsp").forward(request, response);
                return;
            }

            Customer customer = new Customer(customerId, accountNumber, name, address, telephone,
                    isNullOrEmpty(email) ? null : email);

            if (customerDAO.updateCustomer(customer)) {
                request.setAttribute("success", "Customer updated successfully");
            } else {
                request.setAttribute("error", "Failed to update customer");
            }

            listCustomers(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID");
            listCustomers(request, response);
        }
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int customerId = Integer.parseInt(request.getParameter("id"));

            if (customerDAO.deleteCustomer(customerId)) {
                request.setAttribute("success", "Customer deleted successfully");
            } else {
                request.setAttribute("error", "Failed to delete customer");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID");
        }

        listCustomers(request, response);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
