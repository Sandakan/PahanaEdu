package com.pahanaedu.dao;

import com.pahanaedu.model.BillItem;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillItemDAO extends BaseDAO {

    public List<BillItem> getAllBillItems() {
        List<BillItem> billItems = new ArrayList<>();
        String query = "SELECT bi.*, i.name as item_name, i.description as item_description, " +
                "i.unit_price as item_unit_price, c.name as category_name " +
                "FROM bill_items bi " +
                "LEFT JOIN items i ON bi.item_id = i.item_id " +
                "LEFT JOIN categories c ON i.category_id = c.category_id " +
                "WHERE bi.deleted_at IS NULL " +
                "ORDER BY bi.created_at DESC";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                billItems.add(mapResultSetToBillItem(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return billItems;
    }

    public List<BillItem> getBillItemsByBillId(int billId) {
        List<BillItem> billItems = new ArrayList<>();
        String query = "SELECT bi.*, i.name as item_name, i.description as item_description, " +
                "i.unit_price as item_unit_price, c.name as category_name " +
                "FROM bill_items bi " +
                "LEFT JOIN items i ON bi.item_id = i.item_id " +
                "LEFT JOIN categories c ON i.category_id = c.category_id " +
                "WHERE bi.bill_id = ? AND bi.deleted_at IS NULL " +
                "ORDER BY bi.created_at ASC";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, billId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                billItems.add(mapResultSetToBillItem(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return billItems;
    }

    public BillItem getBillItemById(int billItemId) {
        String query = "SELECT bi.*, i.name as item_name, i.description as item_description, " +
                "i.unit_price as item_unit_price, c.name as category_name " +
                "FROM bill_items bi " +
                "LEFT JOIN items i ON bi.item_id = i.item_id " +
                "LEFT JOIN categories c ON i.category_id = c.category_id " +
                "WHERE bi.bill_item_id = ? AND bi.deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, billItemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToBillItem(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean createBillItem(BillItem billItem) {
        String query = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, line_total) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, billItem.getBillId());
            statement.setInt(2, billItem.getItemId());
            statement.setInt(3, billItem.getQuantity());
            statement.setBigDecimal(4, billItem.getUnitPrice());
            statement.setBigDecimal(5, billItem.getLineTotal());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    billItem.setBillItemId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateBillItem(BillItem billItem) {
        String query = "UPDATE bill_items " +
                "SET bill_id = ?, item_id = ?, quantity = ?, unit_price = ?, line_total = ?, " +
                "updated_at = CURRENT_TIMESTAMP " +
                "WHERE bill_item_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, billItem.getBillId());
            statement.setInt(2, billItem.getItemId());
            statement.setInt(3, billItem.getQuantity());
            statement.setBigDecimal(4, billItem.getUnitPrice());
            statement.setBigDecimal(5, billItem.getLineTotal());
            statement.setInt(6, billItem.getBillItemId());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteBillItem(int billItemId) {
        String query = "UPDATE bill_items SET deleted_at = CURRENT_TIMESTAMP WHERE bill_item_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, billItemId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteBillItemsByBillId(int billId) {
        String query = "UPDATE bill_items SET deleted_at = CURRENT_TIMESTAMP WHERE bill_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, billId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createBillItems(List<BillItem> billItems) {
        if (billItems == null || billItems.isEmpty()) {
            return true;
        }

        String query = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, line_total) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            for (BillItem billItem : billItems) {
                statement.setInt(1, billItem.getBillId());
                statement.setInt(2, billItem.getItemId());
                statement.setInt(3, billItem.getQuantity());
                statement.setBigDecimal(4, billItem.getUnitPrice());
                statement.setBigDecimal(5, billItem.getLineTotal());
                statement.addBatch();
            }

            int[] affectedRows = statement.executeBatch();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            int index = 0;
            while (generatedKeys.next() && index < billItems.size()) {
                billItems.get(index).setBillItemId(generatedKeys.getInt(1));
                index++;
            }

            return affectedRows.length == billItems.size();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private BillItem mapResultSetToBillItem(ResultSet resultSet) throws SQLException {
        BillItem billItem = new BillItem();

        billItem.setBillItemId(resultSet.getInt("bill_item_id"));
        billItem.setBillId(resultSet.getInt("bill_id"));
        billItem.setItemId(resultSet.getInt("item_id"));
        billItem.setQuantity(resultSet.getInt("quantity"));
        billItem.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        billItem.setLineTotal(resultSet.getBigDecimal("line_total"));

        if (resultSet.getTimestamp("created_at") != null) {
            billItem.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("updated_at") != null) {
            billItem.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("deleted_at") != null) {
            billItem.setDeletedAt(resultSet.getTimestamp("deleted_at").toLocalDateTime());
        }

        Item item = new Item();
        item.setItemId(resultSet.getInt("item_id"));
        item.setName(resultSet.getString("item_name"));
        item.setDescription(resultSet.getString("item_description"));
        item.setUnitPrice(resultSet.getBigDecimal("item_unit_price"));

        Category category = new Category();
        category.setName(resultSet.getString("category_name"));
        item.setCategory(category);

        billItem.setItem(item);

        return billItem;
    }
}
