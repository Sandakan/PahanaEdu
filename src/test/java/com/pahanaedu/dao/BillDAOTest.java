package com.pahanaedu.dao;

import com.pahanaedu.helpers.DatabaseHelper;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.enums.PaymentMethod;
import com.pahanaedu.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillDAOTest {

    private BillDAO billDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {

        try (Connection connection = DatabaseHelper.getInstance().getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            statement.execute("DELETE FROM bill_items");
            statement.execute("DELETE FROM bills");
            statement.execute("DELETE FROM items");
            statement.execute("DELETE FROM categories");
            statement.execute("DELETE FROM customers");
            statement.execute("DELETE FROM users");
            statement.execute("ALTER TABLE bill_items AUTO_INCREMENT = 1");
            statement.execute("ALTER TABLE bills AUTO_INCREMENT = 1");
            statement.execute("ALTER TABLE items AUTO_INCREMENT = 1");
            statement.execute("ALTER TABLE categories AUTO_INCREMENT = 1");
            statement.execute("ALTER TABLE customers AUTO_INCREMENT = 1");
            statement.execute("ALTER TABLE users AUTO_INCREMENT = 1");

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

            statement.execute(
                    "INSERT INTO users (user_id, email, password, first_name, last_name, role, created_at, updated_at) VALUES "
                            +
                            "(1, 'admin@pahanaedu.com', 'admin123', 'Admin', 'User', 'ADMIN', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO customers (customer_id, account_number, name, address, telephone, email, created_at, updated_at) VALUES "
                            +
                            "(1, 000001, 'John Doe', '123 Main St, Colombo', '0771234567', 'john.doe@email.com', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO bills (bill_id, customer_id, user_id, total_amount, payment_status, payment_method, notes, created_at, updated_at) VALUES "
                            +
                            "(1, 1, 1, 1700.00, 'PAID', 'CASH', 'Test bill', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO categories (category_id, name, description, created_at, updated_at) VALUES "
                            +
                            "(1, 'Books', 'Academic books and materials', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO items (item_id, category_id, name, description, unit_price, created_at, updated_at) VALUES "
                            +
                            "(1, 1, 'Mathematics Textbook', 'Grade 10 Mathematics Textbook', 850.00, NOW(), NOW())");

            statement.execute(
                    "INSERT INTO bill_items (bill_item_id, bill_id, item_id, quantity, unit_price, line_total, created_at, updated_at) VALUES "
                            +
                            "(1, 1, 1, 2, 850.00, 1700.00, NOW(), NOW())");

        }
    }

    @BeforeEach
    void setUp() {
        billDAO = new BillDAO();
    }

    @Test
    void shouldRetrieveExistingBills() {
        List<Bill> bills = billDAO.getAllBills();

        assertNotNull(bills, "Bills list should not be null");
        assertFalse(bills.isEmpty(), "Should have bills from seed data");

        assertTrue(bills.size() >= 1, "Should have at least 1 focused test bill");

        Bill firstBill = bills.get(0);
        assertNotNull(firstBill.getCustomer(), "Bill should have customer populated");
        assertNotNull(firstBill.getUser(), "Bill should have user populated");
        assertNotNull(firstBill.getCustomer().getName(), "Customer should have name");
        assertNotNull(firstBill.getUser().getFirstName(), "User should have first name");
    }

    @Test
    void shouldRetrieveBillById() {
        Bill bill = billDAO.getBillById(1);

        assertNotNull(bill, "Should find bill with ID 1");
        assertEquals(new BigDecimal("1700.00"), bill.getTotalAmount(), "Bill total should match focused test data");
        assertEquals(PaymentStatus.PAID, bill.getPaymentStatus(), "Payment status should match focused test data");
        assertEquals(PaymentMethod.CASH, bill.getPaymentMethod(), "Payment method should match focused test data");
        assertEquals("Test bill", bill.getNotes(), "Notes should match focused test data");

        assertNotNull(bill.getCustomer(), "Bill should have customer populated");
        assertNotNull(bill.getUser(), "Bill should have user populated");
        assertEquals("John Doe", bill.getCustomer().getName(), "Customer name should match focused test data");
        assertEquals("Admin", bill.getUser().getFirstName(), "User first name should match focused test data");
    }

    @Test
    void shouldReturnNullForNonExistentBill() {
        Bill result = billDAO.getBillById(99999);
        assertNull(result, "Non-existent bill should return null");
    }

    @Test
    void shouldHandleInvalidIds() {
        assertNull(billDAO.getBillById(-1), "Negative ID should return null");
        assertNull(billDAO.getBillById(0), "Zero ID should return null");
    }

    @Test
    void shouldCreateNewBill() {
        Bill newBill = new Bill();
        newBill.setCustomerId(1);
        newBill.setUserId(1);
        newBill.setTotalAmount(new BigDecimal("500.00"));
        newBill.setPaymentStatus(PaymentStatus.PENDING);
        newBill.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        newBill.setNotes("Test bill creation");

        boolean created = billDAO.createBill(newBill);
        assertTrue(created, "Should successfully create new bill");

        List<Bill> bills = billDAO.getAllBills();
        boolean found = bills.stream()
                .anyMatch(b -> "Test bill creation".equals(b.getNotes()));
        assertTrue(found, "New bill should appear in bills list");
    }

    @Test
    void shouldUpdateExistingBill() {

        Bill testBill = new Bill();
        testBill.setCustomerId(1);
        testBill.setUserId(1);
        testBill.setTotalAmount(new BigDecimal("300.00"));
        testBill.setPaymentStatus(PaymentStatus.PENDING);
        testBill.setPaymentMethod(PaymentMethod.CASH);
        testBill.setNotes("Update test bill " + System.currentTimeMillis());

        assertTrue(billDAO.createBill(testBill), "Should create test bill");

        List<Bill> bills = billDAO.getAllBills();
        Bill createdBill = bills.stream()
                .filter(b -> testBill.getNotes().equals(b.getNotes()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdBill, "Should find the created bill");

        BigDecimal newTotal = new BigDecimal("450.00");
        String newNotes = "Updated bill notes " + System.currentTimeMillis();
        createdBill.setTotalAmount(newTotal);
        createdBill.setPaymentStatus(PaymentStatus.PAID);
        createdBill.setNotes(newNotes);

        boolean updated = billDAO.updateBill(createdBill);
        assertTrue(updated, "Should successfully update bill");

        Bill updatedBill = billDAO.getBillById(createdBill.getBillId());
        assertNotNull(updatedBill, "Should still find the updated bill");
        assertEquals(newTotal, updatedBill.getTotalAmount(), "Total amount should be updated");
        assertEquals(PaymentStatus.PAID, updatedBill.getPaymentStatus(), "Payment status should be updated");
        assertEquals(newNotes, updatedBill.getNotes(), "Notes should be updated");
    }

    @Test
    void shouldDeleteBill() {

        Bill billToDelete = new Bill();
        billToDelete.setCustomerId(1);
        billToDelete.setUserId(1);
        billToDelete.setTotalAmount(new BigDecimal("100.00"));
        billToDelete.setPaymentStatus(PaymentStatus.PENDING);
        billToDelete.setPaymentMethod(PaymentMethod.CASH);
        billToDelete.setNotes("Temp delete bill " + System.currentTimeMillis());

        boolean created = billDAO.createBill(billToDelete);
        assertTrue(created, "Should create temporary bill");

        List<Bill> bills = billDAO.getAllBills();
        Bill createdBill = bills.stream()
                .filter(b -> billToDelete.getNotes().equals(b.getNotes()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdBill, "Should find the created bill");

        boolean deleted = billDAO.deleteBill(createdBill.getBillId());
        assertTrue(deleted, "Should successfully delete bill");

        Bill deletedBill = billDAO.getBillById(createdBill.getBillId());
        assertNull(deletedBill, "Deleted bill should not be retrievable");
    }

    @Test
    void shouldGetBillsByCustomer() {

        List<Bill> customerBills = billDAO.getBillsByCustomer(1);

        assertNotNull(customerBills, "Customer bills list should not be null");
        assertFalse(customerBills.isEmpty(), "Customer 1 should have bills from seed data");

        for (Bill bill : customerBills) {
            assertEquals(1, bill.getCustomerId(), "All bills should belong to customer 1");
            assertNotNull(bill.getCustomer(), "Bill should have customer object populated");
            assertEquals("John Doe", bill.getCustomer().getName(), "Customer name should match focused test data");
        }
    }

    @Test
    void shouldUpdatePaymentStatus() {

        Bill testBill = new Bill();
        testBill.setCustomerId(1);
        testBill.setUserId(1);
        testBill.setTotalAmount(new BigDecimal("200.00"));
        testBill.setPaymentStatus(PaymentStatus.PENDING);
        testBill.setPaymentMethod(PaymentMethod.CASH);
        testBill.setNotes("Payment status test bill " + System.currentTimeMillis());

        assertTrue(billDAO.createBill(testBill), "Should create test bill");

        List<Bill> bills = billDAO.getAllBills();
        Bill createdBill = bills.stream()
                .filter(b -> testBill.getNotes().equals(b.getNotes()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdBill, "Should find the created bill");

        boolean updated = billDAO.updatePaymentStatus(createdBill.getBillId(), PaymentStatus.PAID);
        assertTrue(updated, "Should successfully update payment status");

        Bill updatedBill = billDAO.getBillById(createdBill.getBillId());
        assertNotNull(updatedBill, "Should still find the updated bill");
        assertEquals(PaymentStatus.PAID, updatedBill.getPaymentStatus(), "Payment status should be updated to PAID");
    }

    @Test
    void shouldCalculateTotalAmount() {

        BigDecimal calculatedTotal = billDAO.calculateTotalAmount(1);

        assertNotNull(calculatedTotal, "Calculated total should not be null");
        assertTrue(calculatedTotal.compareTo(BigDecimal.ZERO) > 0, "Calculated total should be positive");

        assertEquals(new BigDecimal("1700.00"), calculatedTotal, "Calculated total should match sum of line items");
    }

    @Test
    void shouldHandleNullAndInvalidInputs() {

        assertFalse(billDAO.createBill(null), "Creating null bill should return false");
        assertFalse(billDAO.updateBill(null), "Updating null bill should return false");
        assertFalse(billDAO.deleteBill(-1), "Deleting with negative ID should return false");

        List<Bill> invalidCustomerBills = billDAO.getBillsByCustomer(99999);

        assertNotNull(invalidCustomerBills, "Should return empty list for invalid customer");
        assertTrue(invalidCustomerBills.isEmpty(), "Should return empty list for invalid customer");

        assertFalse(billDAO.updatePaymentStatus(99999, PaymentStatus.PAID), "Invalid bill ID should return false");

        BigDecimal invalidTotal = billDAO.calculateTotalAmount(99999);
        assertEquals(BigDecimal.ZERO, invalidTotal, "Invalid bill should return zero total");
    }

    @Test
    void shouldValidateBillCreationRequirements() {

        Bill invalidBill = new Bill();

        invalidBill.setUserId(1);
        invalidBill.setTotalAmount(new BigDecimal("100.00"));
        invalidBill.setPaymentStatus(PaymentStatus.PENDING);
        invalidBill.setPaymentMethod(PaymentMethod.CASH);

        assertFalse(billDAO.createBill(invalidBill), "Bill without customer should fail to create");

        Bill billWithoutUser = new Bill();
        billWithoutUser.setCustomerId(1);
        billWithoutUser.setTotalAmount(new BigDecimal("100.00"));
        billWithoutUser.setPaymentStatus(PaymentStatus.PENDING);
        billWithoutUser.setPaymentMethod(PaymentMethod.CASH);

        assertFalse(billDAO.createBill(billWithoutUser), "Bill without user should fail to create");
    }

    @Test
    void shouldLoadBillWithItems() {

        Bill billWithItems = billDAO.getBillById(1);

        assertNotNull(billWithItems, "Should find bill with ID 1");

        if (billWithItems.getBillItems() != null) {
            assertFalse(billWithItems.getBillItems().isEmpty(), "Bill should have bill items");

            for (BillItem item : billWithItems.getBillItems()) {
                assertNotNull(item.getItem(), "Bill item should have item object");
                assertNotNull(item.getItem().getName(), "Item should have name");
                assertTrue(item.getQuantity() > 0, "Quantity should be positive");
                assertTrue(item.getUnitPrice().compareTo(BigDecimal.ZERO) > 0, "Unit price should be positive");
                assertTrue(item.getLineTotal().compareTo(BigDecimal.ZERO) > 0, "Line total should be positive");
            }
        }
    }
}
