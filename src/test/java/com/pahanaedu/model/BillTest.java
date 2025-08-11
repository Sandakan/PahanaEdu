package com.pahanaedu.model;

import com.pahanaedu.enums.PaymentMethod;
import com.pahanaedu.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BillTest {
    @Test
    void shouldCreateDefaultBill() {
        Bill bill = new Bill();
        assertNotNull(bill);
        assertEquals(0, bill.getBillId());
        assertEquals(0, bill.getCustomerId());
        assertEquals(0, bill.getUserId());
        assertEquals(BigDecimal.ZERO, bill.getTotalAmount());
        assertEquals(PaymentStatus.PENDING, bill.getPaymentStatus());
        assertEquals(PaymentMethod.CASH, bill.getPaymentMethod());
        assertNull(bill.getNotes());
        assertNull(bill.getBillItems());
    }

    @Test
    void shouldCreateBillWithParameters() {
        Bill bill = new Bill(1, 2, new BigDecimal("500.00"), PaymentStatus.PAID, PaymentMethod.CASH, "Test bill");
        assertEquals(1, bill.getCustomerId());
        assertEquals(2, bill.getUserId());
        assertEquals(new BigDecimal("500.00"), bill.getTotalAmount());
        assertEquals(PaymentStatus.PAID, bill.getPaymentStatus());
        assertEquals(PaymentMethod.CASH, bill.getPaymentMethod());
        assertEquals("Test bill", bill.getNotes());
    }

    @Test
    void shouldSetAndGetBillId() {
        Bill bill = new Bill();
        bill.setBillId(456);
        assertEquals(456, bill.getBillId());
    }

    @Test
    void shouldSetAndGetCustomerId() {
        Bill bill = new Bill();
        bill.setCustomerId(789);
        assertEquals(789, bill.getCustomerId());
    }

    @Test
    void shouldSetAndGetUserId() {
        Bill bill = new Bill();
        bill.setUserId(123);
        assertEquals(123, bill.getUserId());
    }

    @Test
    void shouldSetAndGetTotalAmount() {
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("500.00"), bill.getTotalAmount());
    }

    @Test
    void shouldSetAndGetPaymentStatus() {
        Bill bill = new Bill();
        bill.setPaymentStatus(PaymentStatus.PENDING);
        assertEquals(PaymentStatus.PENDING, bill.getPaymentStatus());
    }

    @Test
    void shouldSetAndGetPaymentMethod() {
        Bill bill = new Bill();
        bill.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        assertEquals(PaymentMethod.CREDIT_CARD, bill.getPaymentMethod());
    }

    @Test
    void shouldSetAndGetNotes() {
        Bill bill = new Bill();
        bill.setNotes("Important notes");
        assertEquals("Important notes", bill.getNotes());
    }

    @Test
    void shouldSetAndGetBillItems() {
        Bill bill = new Bill();
        List<BillItem> items = new ArrayList<>();
        items.add(new BillItem());
        bill.setBillItems(items);
        assertEquals(items, bill.getBillItems());
        assertEquals(1, bill.getBillItems().size());
    }

    @Test
    void shouldSetAndGetCustomer() {
        Bill bill = new Bill();
        Customer customer = new Customer("000123", "John Doe", "123 Main St", "555-0123", "john@example.com");
        bill.setCustomer(customer);
        assertEquals(customer, bill.getCustomer());
    }

    @Test
    void shouldSetAndGetUser() {
        Bill bill = new Bill();
        User user = new User();
        bill.setUser(user);
        assertEquals(user, bill.getUser());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        Bill bill = new Bill();
        LocalDateTime now = LocalDateTime.now();
        bill.setCreatedAt(now);
        assertEquals(now, bill.getCreatedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        Bill bill = new Bill();
        LocalDateTime now = LocalDateTime.now();
        bill.setUpdatedAt(now);
        assertEquals(now, bill.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetDeletedAt() {
        Bill bill = new Bill();
        LocalDateTime now = LocalDateTime.now();
        bill.setDeletedAt(now);
        assertEquals(now, bill.getDeletedAt());
    }

    @Test
    void shouldCalculateTotalFromBillItems() {
        Bill bill = new Bill();
        List<BillItem> items = new ArrayList<>();

        BillItem item1 = new BillItem();
        item1.setLineTotal(new BigDecimal("100.00"));

        BillItem item2 = new BillItem();
        item2.setLineTotal(new BigDecimal("150.00"));

        items.add(item1);
        items.add(item2);
        bill.setBillItems(items);

        assertEquals(new BigDecimal("250.00"), bill.calculateTotal());
    }

    @Test
    void shouldReturnZeroTotalForEmptyBillItems() {
        Bill bill = new Bill();
        assertEquals(BigDecimal.ZERO, bill.calculateTotal());
    }

    @Test
    void shouldCalculateAndReturnTotal() {
        Bill bill = new Bill();
        List<BillItem> items = new ArrayList<>();

        BillItem item = new BillItem();
        item.setLineTotal(new BigDecimal("75.00"));
        items.add(item);

        bill.setBillItems(items);

        assertEquals(new BigDecimal("75.00"), bill.calculateTotal());
    }

    @Test
    void shouldReturnItemCount() {
        Bill bill = new Bill();
        List<BillItem> items = new ArrayList<>();
        items.add(new BillItem());
        items.add(new BillItem());
        bill.setBillItems(items);
        assertEquals(2, bill.getItemCount());
    }

    @Test
    void shouldReturnTotalQuantity() {
        Bill bill = new Bill();
        List<BillItem> items = new ArrayList<>();

        BillItem item1 = new BillItem();
        item1.setQuantity(3);

        BillItem item2 = new BillItem();
        item2.setQuantity(5);

        items.add(item1);
        items.add(item2);
        bill.setBillItems(items);

        assertEquals(8, bill.getTotalQuantity());
    }
}
