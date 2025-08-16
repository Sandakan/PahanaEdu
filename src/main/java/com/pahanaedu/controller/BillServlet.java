package com.pahanaedu.controller;

import com.pahanaedu.dao.BillDAO;
import com.pahanaedu.dao.BillItemDAO;
import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.enums.PaymentMethod;
import com.pahanaedu.enums.PaymentStatus;
import com.pahanaedu.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/bills")
public class BillServlet extends HttpServlet {
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;

    @Override
    public void init() {
        billDAO = new BillDAO();
        billItemDAO = new BillItemDAO();
        customerDAO = new CustomerDAO();
        itemDAO = new ItemDAO();
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
                showNewBillForm(request, response);
                break;
            case "view":
                viewBillDetails(request, response);
                break;
            case "edit":
                showEditBillForm(request, response);
                break;
            case "delete":
                deleteBill(request, response);
                break;
            case "updateStatus":
                updatePaymentStatus(request, response);
                break;
            default:
                listBills(request, response);
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
                createBill(request, response);
                break;
            case "update":
                updateBill(request, response);
                break;
            default:
                listBills(request, response);
                break;
        }
    }

    private void listBills(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Bill> bills = billDAO.getAllBills();

        for (Bill bill : bills) {
            List<BillItem> billItems = billItemDAO.getBillItemsByBillId(bill.getBillId());
            bill.setBillItems(billItems);
        }

        request.setAttribute("bills", bills);
        request.getRequestDispatcher("/bills.jsp").forward(request, response);
    }

    private void showNewBillForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Customer> customers = customerDAO.getAllCustomers();
        List<Item> items = itemDAO.getAllItems();

        String customerIdParam = request.getParameter("customerId");
        if (customerIdParam != null) {
            try {
                int customerId = Integer.parseInt(customerIdParam);
                Customer selectedCustomer = customerDAO.getCustomerById(customerId);
                if (selectedCustomer != null) {
                    request.setAttribute("selectedCustomerId", customerId);
                }
            } catch (NumberFormatException e) {
            }
        }

        request.setAttribute("customers", customers);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/bill-form.jsp").forward(request, response);
    }

    private void viewBillDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int billId = Integer.parseInt(request.getParameter("id"));
            Bill bill = billDAO.getBillById(billId);

            if (bill != null) {
                List<BillItem> billItems = billItemDAO.getBillItemsByBillId(billId);
                bill.setBillItems(billItems);
                request.setAttribute("bill", bill);
                request.getRequestDispatcher("/bill-view.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Bill not found");
                listBills(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid bill ID");
            listBills(request, response);
        }
    }

    private void showEditBillForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int billId = Integer.parseInt(request.getParameter("id"));
            Bill bill = billDAO.getBillById(billId);

            if (bill != null) {
                List<BillItem> billItems = billItemDAO.getBillItemsByBillId(billId);
                bill.setBillItems(billItems);

                List<Customer> customers = customerDAO.getAllCustomers();
                List<Item> items = itemDAO.getAllItems();

                request.setAttribute("bill", bill);
                request.setAttribute("customers", customers);
                request.setAttribute("items", items);
                request.getRequestDispatcher("/bill-form.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Bill not found");
                listBills(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid bill ID");
            listBills(request, response);
        }
    }

    private void createBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int customerId = Integer.parseInt(request.getParameter("customerId"));
            String paymentMethod = request.getParameter("paymentMethod");
            String paymentStatus = request.getParameter("paymentStatus");
            String notes = request.getParameter("notes");

            if (customerId <= 0) {
                request.setAttribute("error", "Customer must be selected");
                showNewBillForm(request, response);
                return;
            }

            List<BillItem> billItems = parseBillItemsFromRequest(request);
            if (billItems.isEmpty()) {
                request.setAttribute("error", "At least one item must be added to the bill");
                showNewBillForm(request, response);
                return;
            }

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (BillItem item : billItems) {
                totalAmount = totalAmount.add(item.getLineTotal());
            }

            Bill bill = Bill.builder()
                    .customerId(customerId)
                    .userId(currentUser.getUserId())
                    .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                    .paymentStatus(PaymentStatus.valueOf(paymentStatus))
                    .notes(isNullOrEmpty(notes) ? null : notes)
                    .billItems(billItems)
                    .build();

            if (billDAO.createBill(bill)) {
                for (BillItem item : billItems) {
                    item.setBillId(bill.getBillId());
                }

                if (billItemDAO.createBillItems(billItems)) {
                    request.setAttribute("success", "Bill created successfully");
                    listBills(request, response);
                } else {
                    request.setAttribute("error", "Bill created but failed to add items");
                    listBills(request, response);
                }
            } else {
                request.setAttribute("error", "Failed to create bill");
                showNewBillForm(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID or item data");
            showNewBillForm(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid payment method or status");
            showNewBillForm(request, response);
        }
    }

    private void updateBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int billId = Integer.parseInt(request.getParameter("billId"));
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            String paymentMethod = request.getParameter("paymentMethod");
            String paymentStatus = request.getParameter("paymentStatus");
            String notes = request.getParameter("notes");

            Bill existingBill = billDAO.getBillById(billId);
            if (existingBill == null) {
                request.setAttribute("error", "Bill not found");
                listBills(request, response);
                return;
            }

            existingBill.setCustomerId(customerId);
            existingBill.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
            existingBill.setPaymentStatus(PaymentStatus.valueOf(paymentStatus));
            existingBill.setNotes(isNullOrEmpty(notes) ? null : notes);

            List<BillItem> billItems = parseBillItemsFromRequest(request);
            if (billItems.isEmpty()) {
                request.setAttribute("error", "At least one item must be added to the bill");
                showEditBillForm(request, response);
                return;
            }

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (BillItem item : billItems) {
                totalAmount = totalAmount.add(item.getLineTotal());
            }
            existingBill.setTotalAmount(totalAmount);

            if (billDAO.updateBill(existingBill)) {
                billItemDAO.deleteBillItemsByBillId(billId);

                for (BillItem item : billItems) {
                    item.setBillId(billId);
                }

                if (billItemDAO.createBillItems(billItems)) {
                    request.setAttribute("success", "Bill updated successfully");
                } else {
                    request.setAttribute("error", "Bill updated but failed to update items");
                }
            } else {
                request.setAttribute("error", "Failed to update bill");
            }

            listBills(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid bill ID or item data");
            listBills(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid payment method or status");
            listBills(request, response);
        }
    }

    private void deleteBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only admins can delete bills
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !currentUser.isAdmin()) {
            request.setAttribute("error", "You don't have permission to delete bills.");
            listBills(request, response);
            return;
        }

        try {
            int billId = Integer.parseInt(request.getParameter("id"));

            if (billDAO.deleteBill(billId)) {
                request.setAttribute("success", "Bill deleted successfully");
            } else {
                request.setAttribute("error", "Failed to delete bill");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid bill ID");
        }

        listBills(request, response);
    }

    private void updatePaymentStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int billId = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");

            PaymentStatus paymentStatus = PaymentStatus.valueOf(status);

            if (billDAO.updatePaymentStatus(billId, paymentStatus)) {
                request.setAttribute("success", "Payment status updated successfully");
            } else {
                request.setAttribute("error", "Failed to update payment status");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid bill ID");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid payment status");
        }

        listBills(request, response);
    }

    private List<BillItem> parseBillItemsFromRequest(HttpServletRequest request) {
        List<BillItem> billItems = new ArrayList<>();

        // Parse items in the format items[0].itemId, items[1].itemId
        int index = 0;
        while (true) {
            String itemIdParam = request.getParameter("items[" + index + "].itemId");
            String quantityParam = request.getParameter("items[" + index + "].quantity");
            String unitPriceParam = request.getParameter("items[" + index + "].unitPrice");

            // If any parameter is missing, break the loop
            if (itemIdParam == null || quantityParam == null || unitPriceParam == null) {
                break;
            }

            try {
                int itemId = Integer.parseInt(itemIdParam);
                int quantity = Integer.parseInt(quantityParam);
                BigDecimal unitPrice = new BigDecimal(unitPriceParam);

                if (itemId > 0 && quantity > 0 && unitPrice.compareTo(BigDecimal.ZERO) > 0) {
                    BillItem billItem = new BillItem();
                    billItem.setItemId(itemId);
                    billItem.setQuantity(quantity);
                    billItem.setUnitPrice(unitPrice);
                    billItem.updateLineTotal();
                    billItems.add(billItem);
                }
            } catch (NumberFormatException e) {
                // Skip invalid items but continue the loop
            }

            index++;
        }

        return billItems;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
