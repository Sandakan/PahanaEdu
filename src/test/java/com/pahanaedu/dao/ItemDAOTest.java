package com.pahanaedu.dao;

import com.pahanaedu.dao.interfaces.ItemDAOInterface;
import com.pahanaedu.helpers.DatabaseHelper;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {

    private ItemDAOInterface itemDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {

        try (Connection connection = DatabaseHelper.getInstance().getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            statement.execute("DELETE FROM items");
            statement.execute("DELETE FROM categories");
            statement.execute("ALTER TABLE items AUTO_INCREMENT = 1");
            statement.execute("ALTER TABLE categories AUTO_INCREMENT = 1");

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

            statement
                    .execute("INSERT INTO categories (category_id, name, description, created_at, updated_at) VALUES " +
                            "(1, 'Books', 'Educational books and textbooks', NOW(), NOW()), " +
                            "(2, 'Stationery', 'School and office supplies', NOW(), NOW())");

            statement.execute(
                    "INSERT INTO items (item_id, name, description, category_id, unit_price, created_at, updated_at) VALUES "
                            +
                            "(1, 'Mathematics Textbook', 'Grade 10 Mathematics textbook', 1, 1500.00, NOW(), NOW()), " +
                            "(2, 'Blue Pen', 'Blue ballpoint pen', 2, 25.00, NOW(), NOW()), " +
                            "(3, 'Notebook', 'A4 ruled notebook', 2, 150.00, NOW(), NOW())");

        }
    }

    @BeforeEach
    void setUp() {
        itemDAO = new ItemDAO();
    }

    @Test
    void shouldRetrieveExistingItems() {

        List<Item> items = itemDAO.getAllItems();

        assertNotNull(items, "Items list should not be null");
        assertFalse(items.isEmpty(), "Should have items from seed data");

        assertTrue(items.size() >= 3, "Should have at least 3 focused test items");

        boolean hasMathBook = items.stream().anyMatch(i -> "Mathematics Textbook".equals(i.getName()));
        boolean hasPen = items.stream().anyMatch(i -> "Blue Pen".equals(i.getName()));

        assertTrue(hasMathBook, "Should have 'Mathematics Textbook' item from focused test data");
        assertTrue(hasPen, "Should have 'Blue Pen' item from focused test data");

        Item firstItem = items.get(0);
        assertNotNull(firstItem.getCategory(), "Item should have category object populated");
        assertNotNull(firstItem.getCategory().getName(), "Item category should have name");
    }

    @Test
    void shouldRetrieveItemById() {

        Item item = itemDAO.getItemById(1);

        assertNotNull(item, "Should find item with ID 1");
        assertEquals("Mathematics Textbook", item.getName(), "Item ID 1 should be 'Mathematics Textbook'");
        assertEquals("Grade 10 Mathematics textbook", item.getDescription());
        assertEquals(new BigDecimal("1500.00"), item.getUnitPrice());
        assertNotNull(item.getCategory(), "Item should have category populated");
        assertEquals("Books", item.getCategory().getName(), "Item should be in Books category");
    }

    @Test
    void shouldReturnNullForNonExistentItem() {

        Item result = itemDAO.getItemById(99999);
        assertNull(result, "Non-existent item should return null");
    }

    @Test
    void shouldHandleInvalidIds() {

        assertNull(itemDAO.getItemById(-1), "Negative ID should return null");
        assertNull(itemDAO.getItemById(0), "Zero ID should return null");
    }

    @Test
    void shouldCreateNewItem() {

        Item newItem = new Item();
        newItem.setName("Test Item " + System.currentTimeMillis());
        newItem.setDescription("Test description");
        newItem.setUnitPrice(new BigDecimal("999.99"));

        Category category = new Category();
        category.setCategoryId(1);
        newItem.setCategory(category);

        boolean created = itemDAO.createItem(newItem);
        assertTrue(created, "Should successfully create new item");

        List<Item> items = itemDAO.getAllItems();
        boolean found = items.stream()
                .anyMatch(i -> newItem.getName().equals(i.getName()));
        assertTrue(found, "New item should appear in items list");
    }

    @Test
    void shouldUpdateExistingItem() {

        Item testItem = new Item();
        testItem.setName("Update Test Item " + System.currentTimeMillis());
        testItem.setDescription("Original description");
        testItem.setUnitPrice(new BigDecimal("500.00"));

        Category category = new Category();
        category.setCategoryId(1);
        testItem.setCategory(category);

        assertTrue(itemDAO.createItem(testItem), "Should create test item");

        List<Item> items = itemDAO.getAllItems();
        Item createdItem = items.stream()
                .filter(i -> testItem.getName().equals(i.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdItem, "Should find the created item");

        String newDescription = "Updated description " + System.currentTimeMillis();
        BigDecimal newPrice = new BigDecimal("750.00");
        createdItem.setDescription(newDescription);
        createdItem.setUnitPrice(newPrice);

        boolean updated = itemDAO.updateItem(createdItem);
        assertTrue(updated, "Should successfully update item");

        Item updatedItem = itemDAO.getItemById(createdItem.getItemId());
        assertNotNull(updatedItem, "Should still find the updated item");
        assertEquals(newDescription, updatedItem.getDescription(), "Description should be updated");
        assertEquals(newPrice, updatedItem.getUnitPrice(), "Price should be updated");
    }

    @Test
    void shouldDeleteItem() {

        Item itemToDelete = new Item();
        itemToDelete.setName("Temp Delete Item " + System.currentTimeMillis());
        itemToDelete.setDescription("Temporary item for deletion test");
        itemToDelete.setUnitPrice(new BigDecimal("100.00"));

        Category category = new Category();
        category.setCategoryId(1);
        itemToDelete.setCategory(category);

        boolean created = itemDAO.createItem(itemToDelete);
        assertTrue(created, "Should create temporary item");

        List<Item> items = itemDAO.getAllItems();
        Item createdItem = items.stream()
                .filter(i -> itemToDelete.getName().equals(i.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdItem, "Should find the created item");

        boolean deleted = itemDAO.deleteItem(createdItem.getItemId());
        assertTrue(deleted, "Should successfully delete item");

        Item deletedItem = itemDAO.getItemById(createdItem.getItemId());
        assertNull(deletedItem, "Deleted item should not be retrievable");
    }

    @Test
    void shouldCheckItemNameExists() {

        assertTrue(itemDAO.isItemNameExists("Mathematics Textbook"),
                "Should find existing 'Mathematics Textbook' item");
        assertFalse(itemDAO.isItemNameExists("NonExistentItem"), "Should not find non-existent item");
    }

    @Test
    void shouldGetItemsByCategory() {

        List<Item> booksItems = itemDAO.getItemsByCategory(1);

        assertNotNull(booksItems, "Books items list should not be null");
        assertFalse(booksItems.isEmpty(), "Books category should have items from seed data");

        for (Item item : booksItems) {
            assertNotNull(item.getCategory(), "Item should have category populated");
            assertEquals("Books", item.getCategory().getName(), "All items should be in Books category");
        }
    }

    @Test
    void shouldHandleNullAndInvalidInputs() {

        assertFalse(itemDAO.createItem(null), "Creating null item should return false");
        assertFalse(itemDAO.updateItem(null), "Updating null item should return false");
        assertFalse(itemDAO.deleteItem(-1), "Deleting with negative ID should return false");
        assertFalse(itemDAO.isItemNameExists(null), "Checking null name should return false");
        assertFalse(itemDAO.isItemNameExists(""), "Checking empty name should return false");

        List<Item> invalidCategoryItems = itemDAO.getItemsByCategory(99999);
        assertNotNull(invalidCategoryItems, "Should return empty list for invalid category");
        assertTrue(invalidCategoryItems.isEmpty(), "Should return empty list for invalid category");
    }

    @Test
    void shouldHandleItemsWithoutCategory() {

        List<Item> allItems = itemDAO.getAllItems();

        for (Item item : allItems) {
            assertNotNull(item.getCategory(), "All items should have categories");
            assertNotNull(item.getCategory().getName(), "All categories should have names");
        }
    }

    @Test
    void shouldValidateItemCreationRequirements() {

        Item invalidItem = new Item();

        invalidItem.setDescription("Description");
        invalidItem.setUnitPrice(new BigDecimal("100.00"));
        Category category = new Category();
        category.setCategoryId(1);
        invalidItem.setCategory(category);

        assertFalse(itemDAO.createItem(invalidItem), "Item without name should fail to create");

        Item itemWithoutCategory = new Item();
        itemWithoutCategory.setName("Test Item " + System.currentTimeMillis());
        itemWithoutCategory.setDescription("Description");
        itemWithoutCategory.setUnitPrice(new BigDecimal("100.00"));

        assertFalse(itemDAO.createItem(itemWithoutCategory), "Item without category should fail to create");
    }
}
