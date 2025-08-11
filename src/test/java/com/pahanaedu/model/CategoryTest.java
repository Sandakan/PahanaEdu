package com.pahanaedu.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    @Test
    void shouldCreateDefaultCategory() {
        Category category = new Category();
        assertNotNull(category);
        assertEquals(0, category.getCategoryId());
        assertNull(category.getName());
        assertNull(category.getDescription());
    }

    @Test
    void shouldCreateCategoryWithParameters() {
        Category category = new Category("Electronics", "Electronic devices and accessories");
        assertEquals("Electronics", category.getName());
        assertEquals("Electronic devices and accessories", category.getDescription());
    }

    @Test
    void shouldSetAndGetCategoryId() {
        Category category = new Category();
        category.setCategoryId(789);
        assertEquals(789, category.getCategoryId());
    }

    @Test
    void shouldSetAndGetName() {
        Category category = new Category();
        category.setName("Books");
        assertEquals("Books", category.getName());
    }

    @Test
    void shouldSetAndGetDescription() {
        Category category = new Category();
        category.setDescription("Educational books and materials");
        assertEquals("Educational books and materials", category.getDescription());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        Category category = new Category();
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        assertEquals(now, category.getCreatedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        Category category = new Category();
        LocalDateTime now = LocalDateTime.now();
        category.setUpdatedAt(now);
        assertEquals(now, category.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetDeletedAt() {
        Category category = new Category();
        LocalDateTime now = LocalDateTime.now();
        category.setDeletedAt(now);
        assertEquals(now, category.getDeletedAt());
    }
}
