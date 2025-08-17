package com.pahanaedu.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    @Test
    void shouldCreateDefaultItem() {
        Item item = new Item();
        assertNotNull(item);
        assertEquals(0, item.getItemId());
        assertNull(item.getName());
        assertNull(item.getDescription());
        assertNull(item.getCategoryId());
        assertNull(item.getUnitPrice());
        assertNull(item.getCategory());
    }

    @Test
    void shouldCreateItemWithBasicParameters() {
        Item item = new Item("Laptop", "Gaming laptop", 1, new BigDecimal("1500.00"));
        assertEquals("Laptop", item.getName());
        assertEquals("Gaming laptop", item.getDescription());
        assertEquals(1, item.getCategoryId());
        assertEquals(new BigDecimal("1500.00"), item.getUnitPrice());
    }

    @Test
    void shouldCreateItemWithCategory() {
        Category category = new Category("Electronics", "Electronic devices");
        Item item = new Item("Smartphone", "Latest model smartphone", category, new BigDecimal("800.00"));
        assertEquals("Smartphone", item.getName());
        assertEquals("Latest model smartphone", item.getDescription());
        assertEquals(category, item.getCategory());
        assertEquals(new BigDecimal("800.00"), item.getUnitPrice());
    }

    @Test
    void shouldSetAndGetItemId() {
        Item item = new Item();
        item.setItemId(123);
        assertEquals(123, item.getItemId());
    }

    @Test
    void shouldSetAndGetName() {
        Item item = new Item();
        item.setName("Tablet");
        assertEquals("Tablet", item.getName());
    }

    @Test
    void shouldSetAndGetDescription() {
        Item item = new Item();
        item.setDescription("10-inch tablet");
        assertEquals("10-inch tablet", item.getDescription());
    }

    @Test
    void shouldSetAndGetCategoryId() {
        Item item = new Item();
        item.setCategoryId(5);
        assertEquals(5, item.getCategoryId());
    }

    @Test
    void shouldSetAndGetUnitPrice() {
        Item item = new Item();
        item.setUnitPrice(new BigDecimal("299.99"));
        assertEquals(new BigDecimal("299.99"), item.getUnitPrice());
    }

    @Test
    void shouldSetAndGetCategory() {
        Item item = new Item();
        Category category = new Category("Books", "Educational books");
        item.setCategory(category);
        assertEquals(category, item.getCategory());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        Item item = new Item();
        LocalDateTime now = LocalDateTime.now();
        item.setCreatedAt(now);
        assertEquals(now, item.getCreatedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        Item item = new Item();
        LocalDateTime now = LocalDateTime.now();
        item.setUpdatedAt(now);
        assertEquals(now, item.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetDeletedAt() {
        Item item = new Item();
        LocalDateTime now = LocalDateTime.now();
        item.setDeletedAt(now);
        assertEquals(now, item.getDeletedAt());
    }

    @Test
    void shouldReturnCategoryName() {
        Category category = new Category("Software", "Software applications");
        Item item = new Item();
        item.setCategory(category);
        assertEquals("Software", item.getCategoryName());
    }

    @Test
    void shouldReturnNullCategoryNameWhenNoCategory() {
        Item item = new Item();
        assertNull(item.getCategoryName());
    }
}
