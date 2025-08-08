package com.pahanaedu.model;

import com.pahanaedu.enums.PaymentMethod;
import com.pahanaedu.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Bill {
    private int billId;
    private int customerId;
    private int userId;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Customer customer;
    private User user;
    private List<BillItem> billItems;

    public Bill() {
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentMethod = PaymentMethod.CASH;
        this.totalAmount = BigDecimal.ZERO;
    }

    public Bill(int customerId, int userId, BigDecimal totalAmount) {
        this();
        this.customerId = customerId;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    public Bill(int customerId, int userId, BigDecimal totalAmount, PaymentStatus paymentStatus,
            PaymentMethod paymentMethod, String notes) {
        this.customerId = customerId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.customerId = customer.getCustomerId();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getUserId();
        }
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    public BigDecimal calculateTotal() {
        if (billItems == null || billItems.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (BillItem item : billItems) {
            total = total.add(item.getLineTotal());
        }
        return total;
    }

    public int getItemCount() {
        return billItems != null ? billItems.size() : 0;
    }

    public int getTotalQuantity() {
        if (billItems == null || billItems.isEmpty()) {
            return 0;
        }

        int totalQuantity = 0;
        for (BillItem item : billItems) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }
}
