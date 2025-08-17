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

    @Test
    void shouldCreateBillUsingBuilder() {
        Bill bill = Bill.builder()
                .customerId(123)
                .userId(456)
                .totalAmount(new BigDecimal("750.00"))
                .paymentStatus(PaymentStatus.PAID)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .notes("Test bill using builder")
                .build();

        assertNotNull(bill, "Builder should create a Bill object");
        assertEquals(123, bill.getCustomerId(), "Customer ID should be set correctly");
        assertEquals(456, bill.getUserId(), "User ID should be set correctly");
        assertEquals(new BigDecimal("750.00"), bill.getTotalAmount(), "Total amount should be set correctly");
        assertEquals(PaymentStatus.PAID, bill.getPaymentStatus(), "Payment status should be set correctly");
        assertEquals(PaymentMethod.CREDIT_CARD, bill.getPaymentMethod(), "Payment method should be set correctly");
        assertEquals("Test bill using builder", bill.getNotes(), "Notes should be set correctly");
    }

    @Test
    void shouldCreateBillWithDefaultValuesUsingBuilder() {
        Bill bill = Bill.builder()
                .customerId(100)
                .userId(200)
                .build();

        assertNotNull(bill, "Builder should create a Bill object");
        assertEquals(100, bill.getCustomerId(), "Customer ID should be set");
        assertEquals(200, bill.getUserId(), "User ID should be set");
        assertEquals(BigDecimal.ZERO, bill.getTotalAmount(), "Total amount should default to zero");
        assertEquals(PaymentStatus.PENDING, bill.getPaymentStatus(), "Payment status should default to PENDING");
        assertEquals(PaymentMethod.CASH, bill.getPaymentMethod(), "Payment method should default to CASH");
        assertNull(bill.getNotes(), "Notes should be null by default");
        assertNotNull(bill.getBillItems(), "Bill items should be initialized as empty list");
        assertTrue(bill.getBillItems().isEmpty(), "Bill items should be empty by default");
    }

    @Test
    void shouldSupportFluentInterfaceWithBuilder() {
        Bill bill = Bill.builder()
                .customerId(10)
                .userId(20)
                .totalAmount(new BigDecimal("500.00"))
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(PaymentMethod.DEBIT_CARD)
                .notes("Fluent interface test")
                .build();

        assertEquals(10, bill.getCustomerId());
        assertEquals(20, bill.getUserId());
        assertEquals(new BigDecimal("500.00"), bill.getTotalAmount());
        assertEquals(PaymentStatus.PENDING, bill.getPaymentStatus());
        assertEquals(PaymentMethod.DEBIT_CARD, bill.getPaymentMethod());
        assertEquals("Fluent interface test", bill.getNotes());
    }

    @Test
    void shouldAddSingleBillItemUsingBuilder() {
        BillItem item = new BillItem();
        item.setItemId(1);
        item.setQuantity(2);
        item.setLineTotal(new BigDecimal("100.00"));

        Bill bill = Bill.builder()
                .customerId(5)
                .userId(10)
                .addBillItem(item)
                .build();

        assertNotNull(bill.getBillItems(), "Bill items should not be null");
        assertEquals(1, bill.getBillItems().size(), "Should have one bill item");
        assertEquals(item, bill.getBillItems().get(0), "Should contain the added item");
    }

    @Test
    void shouldAddMultipleBillItemsUsingBuilder() {
        BillItem item1 = new BillItem();
        item1.setItemId(1);
        item1.setLineTotal(new BigDecimal("50.00"));

        BillItem item2 = new BillItem();
        item2.setItemId(2);
        item2.setLineTotal(new BigDecimal("75.00"));

        Bill bill = Bill.builder()
                .customerId(15)
                .userId(25)
                .addBillItem(item1)
                .addBillItem(item2)
                .build();

        assertNotNull(bill.getBillItems(), "Bill items should not be null");
        assertEquals(2, bill.getBillItems().size(), "Should have two bill items");
        assertTrue(bill.getBillItems().contains(item1), "Should contain first item");
        assertTrue(bill.getBillItems().contains(item2), "Should contain second item");
    }

    @Test
    void shouldSetBillItemsListUsingBuilder() {
        BillItem item1 = new BillItem();
        item1.setItemId(1);
        item1.setLineTotal(new BigDecimal("30.00"));

        BillItem item2 = new BillItem();
        item2.setItemId(2);
        item2.setLineTotal(new BigDecimal("70.00"));

        List<BillItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Bill bill = Bill.builder()
                .customerId(8)
                .userId(16)
                .billItems(items)
                .build();

        assertNotNull(bill.getBillItems(), "Bill items should not be null");
        assertEquals(2, bill.getBillItems().size(), "Should have two bill items");
        assertEquals(items, bill.getBillItems(), "Should contain the provided items list");

        items.clear();
        assertEquals(2, bill.getBillItems().size(), "Bill items should be independent of original list");
    }

    @Test
    void shouldCalculateTotalFromBillItemsWhenTotalIsZero() {
        BillItem item1 = new BillItem();
        item1.setLineTotal(new BigDecimal("100.00"));

        BillItem item2 = new BillItem();
        item2.setLineTotal(new BigDecimal("150.00"));

        Bill bill = Bill.builder()
                .customerId(12)
                .userId(24)
                .addBillItem(item1)
                .addBillItem(item2)
                .build();

        assertEquals(new BigDecimal("250.00"), bill.getTotalAmount(),
                "Builder should auto-calculate total from bill items when total is zero");
    }

    @Test
    void shouldNotOverrideExplicitTotalAmountWithBuilder() {
        BillItem item1 = new BillItem();
        item1.setLineTotal(new BigDecimal("100.00"));

        Bill bill = Bill.builder()
                .customerId(12)
                .userId(24)
                .totalAmount(new BigDecimal("500.00"))
                .addBillItem(item1)
                .build();

        assertEquals(new BigDecimal("500.00"), bill.getTotalAmount(),
                "Builder should not override explicitly set total amount");
    }

    @Test
    void shouldCreateIndependentBillInstancesFromBuilder() {
        Bill.Builder builder = Bill.builder()
                .customerId(99)
                .userId(88)
                .paymentStatus(PaymentStatus.PAID);

        Bill bill1 = builder.totalAmount(new BigDecimal("100.00")).build();
        Bill bill2 = builder.totalAmount(new BigDecimal("200.00")).build();

        assertNotSame(bill1, bill2, "Builder should create different instances");
        assertEquals(99, bill1.getCustomerId(), "First bill should have correct customer ID");
        assertEquals(99, bill2.getCustomerId(), "Second bill should have correct customer ID");
        assertEquals(new BigDecimal("200.00"), bill2.getTotalAmount(), "Second bill should have updated total");
    }

    @Test
    void shouldHandleNullNotesGracefullyInBuilder() {
        Bill bill = Bill.builder()
                .customerId(1)
                .userId(1)
                .notes(null)
                .build();

        assertNull(bill.getNotes(), "Builder should handle null notes gracefully");
    }

    @Test
    void shouldReturnBuilderInstanceFromStaticBuilderMethod() {
        Bill.Builder builder = Bill.builder();

        assertNotNull(builder, "Static builder() method should return a Builder instance");
        assertTrue(builder instanceof Bill.Builder, "Should return correct Builder type");
    }
}
