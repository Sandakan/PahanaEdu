package com.pahanaedu.dao;

import com.pahanaedu.dao.interfaces.BillDAOInterface;
import com.pahanaedu.dao.interfaces.BillItemDAOInterface;
import com.pahanaedu.helpers.DatabaseHelper;
import com.pahanaedu.model.BillItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillItemDAOTest {

    private BillItemDAOInterface billItemDAO;

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

            statement
                    .execute("INSERT INTO categories (category_id, name, description, created_at, updated_at) VALUES " +
                            "(1, 'Books', 'Educational books and textbooks', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO items (item_id, name, description, category_id, unit_price, created_at, updated_at) VALUES "
                            +
                            "(1, 'Mathematics Textbook', 'Grade 10 Mathematics textbook', 1, 1500.00, NOW(), NOW())");

            statement.execute(
                    "INSERT INTO bills (bill_id, customer_id, user_id, total_amount, payment_status, payment_method, notes, created_at, updated_at) VALUES "
                            +
                            "(1, 1, 1, 1700.00, 'PAID', 'CASH', 'Test bill', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO bill_items (bill_item_id, bill_id, item_id, quantity, unit_price, line_total, created_at, updated_at) VALUES "
                            +
                            "(1, 1, 1, 2, 850.00, 1700.00, NOW(), NOW())");

        }
    }

    @BeforeEach
    void setUp() {
        billItemDAO = new BillItemDAO();
    }

    @Test
    void shouldRetrieveBillItemsByBillId() {

        List<BillItem> billItems = billItemDAO.getBillItemsByBillId(1);

        assertNotNull(billItems, "Bill items list should not be null");
        assertFalse(billItems.isEmpty(), "Bill 1 should have items from seed data");

        BillItem firstItem = billItems.get(0);
        assertNotNull(firstItem.getItem(), "Bill item should have item populated");
        assertNotNull(firstItem.getItem().getName(), "Item should have name");
        assertTrue(firstItem.getQuantity() > 0, "Quantity should be positive");
        assertTrue(firstItem.getUnitPrice().compareTo(BigDecimal.ZERO) > 0, "Unit price should be positive");
        assertTrue(firstItem.getLineTotal().compareTo(BigDecimal.ZERO) > 0, "Line total should be positive");
    }

    @Test
    void shouldReturnEmptyListForBillWithNoItems() {

        List<BillItem> billItems = billItemDAO.getBillItemsByBillId(99999);

        assertNotNull(billItems, "Bill items list should not be null");
        assertTrue(billItems.isEmpty(), "Non-existent bill should have no items");
    }

    @Test
    void shouldRetrieveBillItemById() {

        BillItem billItem = billItemDAO.getBillItemById(1);

        assertNotNull(billItem, "Should find bill item with ID 1");
        assertEquals(1, billItem.getBillId(), "Bill item should belong to bill 1");
        assertEquals(1, billItem.getItemId(), "Bill item should reference item 1");
        assertEquals(2, billItem.getQuantity(), "Quantity should match seed data");
        assertEquals(new BigDecimal("850.00"), billItem.getUnitPrice(), "Unit price should match seed data");
        assertEquals(new BigDecimal("1700.00"), billItem.getLineTotal(), "Line total should match seed data");

        assertNotNull(billItem.getItem(), "Bill item should have item populated");
        assertEquals("Mathematics Textbook", billItem.getItem().getName(), "Item name should match");
    }

    @Test
    void shouldReturnNullForNonExistentBillItem() {

        BillItem result = billItemDAO.getBillItemById(99999);
        assertNull(result, "Non-existent bill item should return null");
    }

    @Test
    void shouldCreateNewBillItem() {

        BillDAOInterface billDAO = new BillDAO();
        com.pahanaedu.model.Bill testBill = new com.pahanaedu.model.Bill();
        testBill.setCustomerId(1);
        testBill.setUserId(1);
        testBill.setTotalAmount(new BigDecimal("500.00"));
        testBill.setPaymentStatus(com.pahanaedu.enums.PaymentStatus.PENDING);
        testBill.setPaymentMethod(com.pahanaedu.enums.PaymentMethod.CASH);
        testBill.setNotes("Test bill for item creation");

        assertTrue(billDAO.createBill(testBill), "Should create test bill");

        List<com.pahanaedu.model.Bill> bills = billDAO.getAllBills();
        com.pahanaedu.model.Bill createdBill = bills.stream()
                .filter(b -> "Test bill for item creation".equals(b.getNotes()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdBill, "Should find the created bill");

        BillItem newBillItem = new BillItem();
        newBillItem.setBillId(createdBill.getBillId());
        newBillItem.setItemId(1);
        newBillItem.setQuantity(3);
        newBillItem.setUnitPrice(new BigDecimal("100.00"));
        newBillItem.setLineTotal(new BigDecimal("300.00"));

        boolean created = billItemDAO.createBillItem(newBillItem);
        assertTrue(created, "Should successfully create new bill item");

        List<BillItem> billItems = billItemDAO.getBillItemsByBillId(createdBill.getBillId());
        assertFalse(billItems.isEmpty(), "Bill should have the new item");

        BillItem foundItem = billItems.get(0);
        assertEquals(3, foundItem.getQuantity(), "Quantity should match");
        assertEquals(new BigDecimal("100.00"), foundItem.getUnitPrice(), "Unit price should match");
        assertEquals(new BigDecimal("300.00"), foundItem.getLineTotal(), "Line total should match");
    }

    @Test
    void shouldUpdateExistingBillItem() {

        BillDAOInterface billDAO = new BillDAO();
        com.pahanaedu.model.Bill testBill = new com.pahanaedu.model.Bill();
        testBill.setCustomerId(1);
        testBill.setUserId(1);
        testBill.setTotalAmount(new BigDecimal("400.00"));
        testBill.setPaymentStatus(com.pahanaedu.enums.PaymentStatus.PENDING);
        testBill.setPaymentMethod(com.pahanaedu.enums.PaymentMethod.CASH);
        testBill.setNotes("Test bill for item update");

        assertTrue(billDAO.createBill(testBill), "Should create test bill");

        List<com.pahanaedu.model.Bill> bills = billDAO.getAllBills();
        com.pahanaedu.model.Bill createdBill = bills.stream()
                .filter(b -> "Test bill for item update".equals(b.getNotes()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdBill, "Should find the created bill");

        BillItem billItem = new BillItem();
        billItem.setBillId(createdBill.getBillId());
        billItem.setItemId(1);
        billItem.setQuantity(2);
        billItem.setUnitPrice(new BigDecimal("200.00"));
        billItem.setLineTotal(new BigDecimal("400.00"));

        assertTrue(billItemDAO.createBillItem(billItem), "Should create bill item");

        List<BillItem> billItems = billItemDAO.getBillItemsByBillId(createdBill.getBillId());
        BillItem createdBillItem = billItems.get(0);

        createdBillItem.setQuantity(5);
        createdBillItem.setUnitPrice(new BigDecimal("150.00"));
        createdBillItem.setLineTotal(new BigDecimal("750.00"));

        boolean updated = billItemDAO.updateBillItem(createdBillItem);
        assertTrue(updated, "Should successfully update bill item");

        BillItem updatedBillItem = billItemDAO.getBillItemById(createdBillItem.getBillItemId());
        assertNotNull(updatedBillItem, "Should find the updated bill item");
        assertEquals(5, updatedBillItem.getQuantity(), "Quantity should be updated");
        assertEquals(new BigDecimal("150.00"), updatedBillItem.getUnitPrice(), "Unit price should be updated");
        assertEquals(new BigDecimal("750.00"), updatedBillItem.getLineTotal(), "Line total should be updated");
    }

    @Test
    void shouldDeleteBillItem() {

        BillDAOInterface billDAO = new BillDAO();
        com.pahanaedu.model.Bill testBill = new com.pahanaedu.model.Bill();
        testBill.setCustomerId(1);
        testBill.setUserId(1);
        testBill.setTotalAmount(new BigDecimal("100.00"));
        testBill.setPaymentStatus(com.pahanaedu.enums.PaymentStatus.PENDING);
        testBill.setPaymentMethod(com.pahanaedu.enums.PaymentMethod.CASH);
        testBill.setNotes("Test bill for item deletion");

        assertTrue(billDAO.createBill(testBill), "Should create test bill");

        List<com.pahanaedu.model.Bill> bills = billDAO.getAllBills();
        com.pahanaedu.model.Bill createdBill = bills.stream()
                .filter(b -> "Test bill for item deletion".equals(b.getNotes()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdBill, "Should find the created bill");

        BillItem billItem = new BillItem();
        billItem.setBillId(createdBill.getBillId());
        billItem.setItemId(1);
        billItem.setQuantity(1);
        billItem.setUnitPrice(new BigDecimal("100.00"));
        billItem.setLineTotal(new BigDecimal("100.00"));

        assertTrue(billItemDAO.createBillItem(billItem), "Should create bill item");

        List<BillItem> billItems = billItemDAO.getBillItemsByBillId(createdBill.getBillId());
        BillItem createdBillItem = billItems.get(0);

        boolean deleted = billItemDAO.deleteBillItem(createdBillItem.getBillItemId());
        assertTrue(deleted, "Should successfully delete bill item");

        BillItem deletedBillItem = billItemDAO.getBillItemById(createdBillItem.getBillItemId());
        assertNull(deletedBillItem, "Deleted bill item should not be retrievable");
    }

    @Test
    void shouldDeleteAllBillItemsByBillId() {

        BillDAOInterface billDAO = new BillDAO();
        com.pahanaedu.model.Bill testBill = new com.pahanaedu.model.Bill();
        testBill.setCustomerId(1);
        testBill.setUserId(1);
        testBill.setTotalAmount(new BigDecimal("500.00"));
        testBill.setPaymentStatus(com.pahanaedu.enums.PaymentStatus.PENDING);
        testBill.setPaymentMethod(com.pahanaedu.enums.PaymentMethod.CASH);
        testBill.setNotes("Test bill for bulk item deletion");

        assertTrue(billDAO.createBill(testBill), "Should create test bill");

        List<com.pahanaedu.model.Bill> bills = billDAO.getAllBills();
        com.pahanaedu.model.Bill createdBill = bills.stream()
                .filter(b -> "Test bill for bulk item deletion".equals(b.getNotes()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdBill, "Should find the created bill");

        BillItem item1 = new BillItem();
        item1.setBillId(createdBill.getBillId());
        item1.setItemId(1);
        item1.setQuantity(1);
        item1.setUnitPrice(new BigDecimal("100.00"));
        item1.setLineTotal(new BigDecimal("100.00"));

        BillItem item2 = new BillItem();
        item2.setBillId(createdBill.getBillId());
        item2.setItemId(1);
        item2.setQuantity(2);
        item2.setUnitPrice(new BigDecimal("200.00"));
        item2.setLineTotal(new BigDecimal("400.00"));

        assertTrue(billItemDAO.createBillItem(item1), "Should create first bill item");
        assertTrue(billItemDAO.createBillItem(item2), "Should create second bill item");

        List<BillItem> billItems = billItemDAO.getBillItemsByBillId(createdBill.getBillId());
        assertEquals(2, billItems.size(), "Should have 2 bill items");

        boolean deleted = billItemDAO.deleteBillItemsByBillId(createdBill.getBillId());
        assertTrue(deleted, "Should successfully delete all bill items");

        List<BillItem> remainingItems = billItemDAO.getBillItemsByBillId(createdBill.getBillId());
        assertTrue(remainingItems.isEmpty(), "Should have no remaining items");
    }

    @Test
    void shouldHandleNullAndInvalidInputs() {

        assertFalse(billItemDAO.createBillItem(null), "Creating null bill item should return false");
        assertFalse(billItemDAO.updateBillItem(null), "Updating null bill item should return false");
        assertFalse(billItemDAO.deleteBillItem(-1), "Deleting with negative ID should return false");
        assertFalse(billItemDAO.deleteBillItemsByBillId(-1),
                "Deleting items with negative bill ID should return false");

        List<BillItem> invalidBillItems = billItemDAO.getBillItemsByBillId(99999);
        assertNotNull(invalidBillItems, "Should return empty list for invalid bill");
        assertTrue(invalidBillItems.isEmpty(), "Should return empty list for invalid bill");
    }

    @Test
    void shouldValidateBillItemCreationRequirements() {

        BillItem invalidBillItem = new BillItem();

        invalidBillItem.setItemId(1);
        invalidBillItem.setQuantity(1);
        invalidBillItem.setUnitPrice(new BigDecimal("100.00"));
        invalidBillItem.setLineTotal(new BigDecimal("100.00"));

        assertFalse(billItemDAO.createBillItem(invalidBillItem), "Bill item without bill ID should fail to create");

        BillItem billItemWithInvalidBill = new BillItem();
        billItemWithInvalidBill.setBillId(999);
        billItemWithInvalidBill.setItemId(1);
        billItemWithInvalidBill.setQuantity(1);
        billItemWithInvalidBill.setUnitPrice(new BigDecimal("100.00"));
        billItemWithInvalidBill.setLineTotal(new BigDecimal("100.00"));

        assertFalse(billItemDAO.createBillItem(billItemWithInvalidBill),
                "Bill item with non-existent bill ID should fail to create");

        BillItem billItemWithInvalidItem = new BillItem();
        billItemWithInvalidItem.setBillId(1);
        billItemWithInvalidItem.setItemId(999);
        billItemWithInvalidItem.setQuantity(1);
        billItemWithInvalidItem.setUnitPrice(new BigDecimal("100.00"));
        billItemWithInvalidItem.setLineTotal(new BigDecimal("100.00"));

        assertFalse(billItemDAO.createBillItem(billItemWithInvalidItem),
                "Bill item with non-existent item ID should fail to create");
    }
}
