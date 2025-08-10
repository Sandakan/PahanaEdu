package com.pahanaedu.dao;

import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO extends BaseDAO {

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT i.*, c.name as category_name FROM items i " +
                "LEFT JOIN categories c ON i.category_id = c.category_id " +
                "WHERE i.deleted_at IS NULL ORDER BY i.created_at DESC";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Item item = mapResultSetToItem(resultSet);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public Item getItemById(int itemId) {
        String query = "SELECT i.*, c.name as category_name FROM items i " +
                "LEFT JOIN categories c ON i.category_id = c.category_id " +
                "WHERE i.item_id = ? AND i.deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, itemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToItem(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Item> getItemsByCategory(int categoryId) {
        List<Item> items = new ArrayList<>();
        String query = "SELECT i.*, c.name as category_name FROM items i " +
                "LEFT JOIN categories c ON i.category_id = c.category_id " +
                "WHERE i.category_id = ? AND i.deleted_at IS NULL ORDER BY i.name";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Item item = mapResultSetToItem(resultSet);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public boolean createItem(Item item) {
        String query = "INSERT INTO items (name, description, category_id, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setInt(3, item.getCategoryId());
            statement.setBigDecimal(4, item.getUnitPrice());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    item.setItemId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateItem(Item item) {
        String query = "UPDATE items SET name = ?, description = ?, category_id = ?, unit_price = ? " +
                "WHERE item_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setInt(3, item.getCategoryId());
            statement.setBigDecimal(4, item.getUnitPrice());
            statement.setInt(5, item.getItemId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteItem(int itemId) {
        String query = "UPDATE items SET deleted_at = CURRENT_TIMESTAMP WHERE item_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, itemId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isItemNameExists(String name) {
        String query = "SELECT COUNT(*) FROM items WHERE name = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isItemNameExistsForUpdate(String name, int itemId) {
        String query = "SELECT COUNT(*) FROM items WHERE name = ? AND item_id != ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setInt(2, itemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getAllItemNames() {
        List<String> items = new ArrayList<>();
        String query = "SELECT name FROM items WHERE deleted_at IS NULL ORDER BY name";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                items.add(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    private Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setItemId(resultSet.getInt("item_id"));
        item.setName(resultSet.getString("name"));
        item.setDescription(resultSet.getString("description"));

        int categoryId = resultSet.getInt("category_id");
        String categoryName = resultSet.getString("category_name");
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setName(categoryName);
        item.setCategory(category);

        item.setUnitPrice(resultSet.getBigDecimal("unit_price"));

        if (resultSet.getTimestamp("created_at") != null) {
            item.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("updated_at") != null) {
            item.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("deleted_at") != null) {
            item.setDeletedAt(resultSet.getTimestamp("deleted_at").toLocalDateTime());
        }

        return item;
    }
}
