package com.pahanaedu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BillItemDAO extends BaseDAO {

    public List<Integer> getAllBillItems() {
        List<Integer> billItems = new ArrayList<>();
        String query = "SELECT * FROM bill_items";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                billItems.add(resultSet.getInt("bill_item_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return billItems;
    }
}
