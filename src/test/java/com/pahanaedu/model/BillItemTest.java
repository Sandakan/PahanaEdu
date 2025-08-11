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
}
