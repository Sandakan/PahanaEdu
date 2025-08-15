package com.pahanaedu.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BillItemTest {
    @Test
    void shouldCreateDefaultBillItem() {
        BillItem billItem = new BillItem();
        assertNotNull(billItem);
        assertEquals(0, billItem.getBillItemId());
        assertEquals(0, billItem.getBillId());
        assertEquals(0, billItem.getItemId());
        assertEquals(0, billItem.getQuantity());
        assertEquals(BigDecimal.ZERO, billItem.getUnitPrice());
        assertEquals(BigDecimal.ZERO, billItem.getLineTotal());
        assertNull(billItem.getItem());
    }

    @Test
    void shouldCreateBillItemWithBasicParameters() {
        BillItem billItem = new BillItem(1, 2, 5, new BigDecimal("10.00"));
        assertEquals(1, billItem.getBillId());
        assertEquals(2, billItem.getItemId());
        assertEquals(5, billItem.getQuantity());
        assertEquals(new BigDecimal("10.00"), billItem.getUnitPrice());
        assertEquals(new BigDecimal("50.00"), billItem.getLineTotal());
    }

    @Test
    void shouldCreateBillItemWithItem() {
        Item item = new Item("Test Item", "Test Description", 1, new BigDecimal("15.00"));
        item.setItemId(123);
        BillItem billItem = new BillItem(1, item, 3);
        assertEquals(1, billItem.getBillId());
        assertEquals(123, billItem.getItemId());
        assertEquals(3, billItem.getQuantity());
        assertEquals(new BigDecimal("15.00"), billItem.getUnitPrice());
        assertEquals(new BigDecimal("45.00"), billItem.getLineTotal());
        assertEquals(item, billItem.getItem());
    }

    @Test
    void shouldSetAndGetBillItemId() {
        BillItem billItem = new BillItem();
        billItem.setBillItemId(456);
        assertEquals(456, billItem.getBillItemId());
    }

    @Test
    void shouldSetAndGetBillId() {
        BillItem billItem = new BillItem();
        billItem.setBillId(789);
        assertEquals(789, billItem.getBillId());
    }

    @Test
    void shouldSetAndGetItemId() {
        BillItem billItem = new BillItem();
        billItem.setItemId(101);
        assertEquals(101, billItem.getItemId());
    }

    @Test
    void shouldSetAndGetQuantity() {
        BillItem billItem = new BillItem();
        billItem.setQuantity(7);
        assertEquals(7, billItem.getQuantity());
    }

    @Test
    void shouldSetAndGetUnitPrice() {
        BillItem billItem = new BillItem();
        billItem.setUnitPrice(new BigDecimal("25.50"));
        assertEquals(new BigDecimal("25.50"), billItem.getUnitPrice());
    }

    @Test
    void shouldSetAndGetLineTotal() {
        BillItem billItem = new BillItem();
        billItem.setLineTotal(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), billItem.getLineTotal());
    }

    @Test
    void shouldSetAndGetItem() {
        BillItem billItem = new BillItem();
        Item item = new Item("Product", "Description", 1, new BigDecimal("20.00"));
        item.setItemId(555);
        billItem.setItem(item);
        assertEquals(item, billItem.getItem());
        assertEquals(555, billItem.getItemId());
        assertEquals(new BigDecimal("20.00"), billItem.getUnitPrice());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        BillItem billItem = new BillItem();
        LocalDateTime now = LocalDateTime.now();
        billItem.setCreatedAt(now);
        assertEquals(now, billItem.getCreatedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        BillItem billItem = new BillItem();
        LocalDateTime now = LocalDateTime.now();
        billItem.setUpdatedAt(now);
        assertEquals(now, billItem.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetDeletedAt() {
        BillItem billItem = new BillItem();
        LocalDateTime now = LocalDateTime.now();
        billItem.setDeletedAt(now);
        assertEquals(now, billItem.getDeletedAt());
    }

    @Test
    void shouldCalculateLineTotal() {
        BillItem billItem = new BillItem();
        billItem.setQuantity(4);
        billItem.setUnitPrice(new BigDecimal("12.50"));
        assertEquals(new BigDecimal("50.00"), billItem.calculateLineTotal());
    }

    @Test
    void shouldReturnZeroLineTotalForZeroQuantity() {
        BillItem billItem = new BillItem();
        billItem.setQuantity(0);
        billItem.setUnitPrice(new BigDecimal("10.00"));
        assertEquals(BigDecimal.ZERO, billItem.calculateLineTotal());
    }

    @Test
    void shouldUpdateLineTotal() {
        BillItem billItem = new BillItem();
        billItem.setQuantity(6);
        billItem.setUnitPrice(new BigDecimal("8.00"));
        billItem.updateLineTotal();
        assertEquals(new BigDecimal("48.00"), billItem.getLineTotal());
    }

    @Test
    void shouldReturnItemName() {
        Item item = new Item("Laptop", "Gaming laptop", 1, new BigDecimal("1500.00"));
        BillItem billItem = new BillItem();
        billItem.setItem(item);
        assertEquals("Laptop", billItem.getItemName());
    }

    @Test
    void shouldReturnNullItemNameWhenNoItem() {
        BillItem billItem = new BillItem();
        assertNull(billItem.getItemName());
    }

    @Test
    void shouldReturnItemDescription() {
        Item item = new Item("Mouse", "Wireless optical mouse", 1, new BigDecimal("25.00"));
        BillItem billItem = new BillItem();
        billItem.setItem(item);
        assertEquals("Wireless optical mouse", billItem.getItemDescription());
    }

    @Test
    void shouldReturnNullItemDescriptionWhenNoItem() {
        BillItem billItem = new BillItem();
        assertNull(billItem.getItemDescription());
    }

    @Test
    void shouldReturnCategoryName() {
        Category category = new Category("Electronics", "Electronic devices");
        Item item = new Item("Tablet", "10-inch tablet", category, new BigDecimal("300.00"));
        BillItem billItem = new BillItem();
        billItem.setItem(item);
        assertEquals("Electronics", billItem.getCategoryName());
    }

    @Test
    void shouldReturnNullCategoryNameWhenNoItem() {
        BillItem billItem = new BillItem();
        assertNull(billItem.getCategoryName());
    }

    @Test
    void shouldRecalculateLineTotalWhenQuantityChanges() {
        BillItem billItem = new BillItem(1, 2, 3, new BigDecimal("10.00"));
        assertEquals(new BigDecimal("30.00"), billItem.getLineTotal());

        billItem.setQuantity(5);
        assertEquals(new BigDecimal("50.00"), billItem.getLineTotal());
    }

    @Test
    void shouldRecalculateLineTotalWhenUnitPriceChanges() {
        BillItem billItem = new BillItem(1, 2, 4, new BigDecimal("5.00"));
        assertEquals(new BigDecimal("20.00"), billItem.getLineTotal());

        billItem.setUnitPrice(new BigDecimal("7.50"));
        assertEquals(new BigDecimal("30.00"), billItem.getLineTotal());
    }

    @Test
    void shouldGenerateCorrectJsonRepresentation() {
        // Create category and item
        Category category = new Category("Electronics", "Electronic devices");
        Item item = new Item("Laptop", "Gaming laptop", category, new BigDecimal("1500.50"));
        item.setItemId(123);

        // Create bill item
        BillItem billItem = new BillItem(1, item, 2);
        billItem.setBillItemId(456);

        // Generate JSON
        String json = billItem.toJson(1);

        // Verify JSON structure
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"itemId\":123"));
        assertTrue(json.contains("\"name\":\"Laptop\""));
        assertTrue(json.contains("\"category\":\"Electronics\""));
        assertTrue(json.contains("\"unitPrice\":1500.5"));
        assertTrue(json.contains("\"quantity\":2"));
        assertTrue(json.contains("\"lineTotal\":3001.0"));
    }

    @Test
    void shouldEscapeSpecialCharactersInJson() {
        Category category = new Category("Books & Media", "Books and media");
        Item item = new Item("Java \"Programming\" Guide", "Advanced programming", category, new BigDecimal("45.99"));
        item.setItemId(789);

        BillItem billItem = new BillItem(1, item, 1);

        String json = billItem.toJson(2);

        assertTrue(json.contains("\"name\":\"Java \\\"Programming\\\" Guide\""));
        assertTrue(json.contains("\"category\":\"Books & Media\""));
        assertTrue(json.contains("\"id\":2"));
    }

    @Test
    void shouldHandleNullItemInJson() {
        BillItem billItem = new BillItem();
        billItem.setItemId(999);
        billItem.setQuantity(3);
        billItem.setUnitPrice(new BigDecimal("10.00"));
        billItem.setLineTotal(new BigDecimal("30.00"));

        String json = billItem.toJson(5);

        assertTrue(json.contains("\"id\":5"));
        assertTrue(json.contains("\"itemId\":999"));
        assertTrue(json.contains("\"name\":\"\""));
        assertTrue(json.contains("\"category\":\"\""));
        assertTrue(json.contains("\"unitPrice\":10.0"));
        assertTrue(json.contains("\"quantity\":3"));
        assertTrue(json.contains("\"lineTotal\":30.0"));
    }
}
