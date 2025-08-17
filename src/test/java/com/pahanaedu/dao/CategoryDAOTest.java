package com.pahanaedu.dao;

import com.pahanaedu.dao.interfaces.CategoryDAOInterface;
import com.pahanaedu.helpers.DatabaseHelper;
import com.pahanaedu.model.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest {

    private CategoryDAOInterface categoryDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {

        try (Connection connection = DatabaseHelper.getInstance().getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            statement.execute("DELETE FROM categories");
            statement.execute("ALTER TABLE categories AUTO_INCREMENT = 1");

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

            statement
                    .execute("INSERT INTO categories (category_id, name, description, created_at, updated_at) VALUES " +
                            "(1, 'Books', 'Educational books and textbooks', NOW(), NOW()), " +
                            "(2, 'Stationery', 'School and office supplies', NOW(), NOW())");

        }
    }

    @BeforeEach
    void setUp() {
        categoryDAO = new CategoryDAO();
    }

    @Test
    void shouldRetrieveExistingCategories() {

        List<Category> categories = categoryDAO.getAllCategories();

        assertNotNull(categories, "Categories list should not be null");
        assertFalse(categories.isEmpty(), "Should have categories from test data");

        assertEquals(2, categories.size(), "Should have exactly 2 test categories");

        boolean hasBooks = categories.stream().anyMatch(c -> "Books".equals(c.getName()));
        boolean hasStationery = categories.stream().anyMatch(c -> "Stationery".equals(c.getName()));

        assertTrue(hasBooks, "Should have 'Books' category from test data");
        assertTrue(hasStationery, "Should have 'Stationery' category from test data");
    }

    @Test
    void shouldRetrieveCategoryById() {

        Category category = categoryDAO.getCategoryById(1);

        assertNotNull(category, "Should find category with ID 1");
        assertEquals("Books", category.getName(), "Category ID 1 should be 'Books'");
        assertEquals("Educational books and textbooks", category.getDescription());
    }

    @Test
    void shouldReturnNullForNonExistentCategory() {

        Category result = categoryDAO.getCategoryById(99999);
        assertNull(result, "Non-existent category should return null");
    }

    @Test
    void shouldHandleInvalidIds() {

        assertNull(categoryDAO.getCategoryById(-1), "Negative ID should return null");
        assertNull(categoryDAO.getCategoryById(0), "Zero ID should return null");
    }

    @Test
    void shouldCreateNewCategory() {

        Category newCategory = new Category();
        newCategory.setName("Test Category " + System.currentTimeMillis());
        newCategory.setDescription("Test description");

        boolean created = categoryDAO.createCategory(newCategory);
        assertTrue(created, "Should successfully create new category");

        List<Category> categories = categoryDAO.getAllCategories();
        boolean found = categories.stream()
                .anyMatch(c -> newCategory.getName().equals(c.getName()));
        assertTrue(found, "New category should appear in categories list");
    }

    @Test
    void shouldUpdateExistingCategory() {

        Category testCategory = new Category();
        testCategory.setName("Update Test Category " + System.currentTimeMillis());
        testCategory.setDescription("Original description");

        assertTrue(categoryDAO.createCategory(testCategory), "Should create test category");

        List<Category> categories = categoryDAO.getAllCategories();
        Category createdCategory = categories.stream()
                .filter(c -> testCategory.getName().equals(c.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdCategory, "Should find the created category");

        String newDescription = "Updated description " + System.currentTimeMillis();
        createdCategory.setDescription(newDescription);

        boolean updated = categoryDAO.updateCategory(createdCategory);
        assertTrue(updated, "Should successfully update category");

        Category updatedCategory = categoryDAO.getCategoryById(createdCategory.getCategoryId());
        assertNotNull(updatedCategory, "Should still find the updated category");
        assertEquals(newDescription, updatedCategory.getDescription(), "Description should be updated");
    }

    @Test
    void shouldDeleteCategory() {

        Category categoryToDelete = new Category();
        categoryToDelete.setName("Temp Delete Category " + System.currentTimeMillis());
        categoryToDelete.setDescription("Temporary category for deletion test");

        boolean created = categoryDAO.createCategory(categoryToDelete);
        assertTrue(created, "Should create temporary category");

        List<Category> categories = categoryDAO.getAllCategories();
        Category createdCategory = categories.stream()
                .filter(c -> categoryToDelete.getName().equals(c.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdCategory, "Should find the created category");

        boolean deleted = categoryDAO.deleteCategory(createdCategory.getCategoryId());
        assertTrue(deleted, "Should successfully delete category");

        Category deletedCategory = categoryDAO.getCategoryById(createdCategory.getCategoryId());
        assertNull(deletedCategory, "Deleted category should not be retrievable");
    }

    @Test
    void shouldCheckCategoryNameExists() {

        assertTrue(categoryDAO.isCategoryNameExists("Books"), "Should find existing 'Books' category");
        assertFalse(categoryDAO.isCategoryNameExists("NonExistentCategory"), "Should not find non-existent category");
    }

    @Test
    void shouldGetAllCategoryNames() {

        List<String> names = categoryDAO.getAllCategoryNames();

        assertNotNull(names, "Category names list should not be null");
        assertFalse(names.isEmpty(), "Should have category names from seed data");
        assertTrue(names.contains("Books"), "Should contain 'Books' from seed data");
        assertTrue(names.contains("Stationery"), "Should contain 'Stationery' from seed data");
    }

    @Test
    void shouldCheckDependentItems() {

        boolean hasItems = categoryDAO.hasDependentItems(1);
        assertTrue(hasItems, "Books category should have dependent items from seed data");

        assertFalse(categoryDAO.hasDependentItems(99999), "Non-existent category should not have items");
    }

    @Test
    void shouldHandleNullAndInvalidInputs() {

        assertFalse(categoryDAO.createCategory(null), "Creating null category should return false");
        assertFalse(categoryDAO.updateCategory(null), "Updating null category should return false");
        assertFalse(categoryDAO.deleteCategory(-1), "Deleting with negative ID should return false");
        assertFalse(categoryDAO.isCategoryNameExists(null), "Checking null name should return false");
        assertFalse(categoryDAO.isCategoryNameExists(""), "Checking empty name should return false");
    }
}
