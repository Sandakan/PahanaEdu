package com.pahanaedu.dao;

import com.pahanaedu.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends BaseDAO {

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories WHERE deleted_at IS NULL ORDER BY name";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Category category = mapResultSetToCategory(resultSet);
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    public Category getCategoryById(int categoryId) {
        String query = "SELECT * FROM categories WHERE category_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToCategory(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean createCategory(Category category) {
        String query = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    category.setCategoryId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateCategory(Category category) {
        String query = "UPDATE categories SET name = ?, description = ? WHERE category_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getCategoryId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteCategory(int categoryId) {
        String query = "UPDATE categories SET deleted_at = CURRENT_TIMESTAMP WHERE category_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isCategoryNameExists(String name) {
        String query = "SELECT COUNT(*) FROM categories WHERE name = ? AND deleted_at IS NULL";

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

    public boolean isCategoryNameExistsForUpdate(String name, int categoryId) {
        String query = "SELECT COUNT(*) FROM categories WHERE name = ? AND category_id != ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setInt(2, categoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hasDependentItems(int categoryId) {
        String query = "SELECT COUNT(*) FROM items WHERE category_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getAllCategoryNames() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT name FROM categories WHERE deleted_at IS NULL ORDER BY name";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                categories.add(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    private Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setName(resultSet.getString("name"));
        category.setDescription(resultSet.getString("description"));

        if (resultSet.getTimestamp("created_at") != null) {
            category.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("updated_at") != null) {
            category.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("deleted_at") != null) {
            category.setDeletedAt(resultSet.getTimestamp("deleted_at").toLocalDateTime());
        }

        return category;
    }
}
