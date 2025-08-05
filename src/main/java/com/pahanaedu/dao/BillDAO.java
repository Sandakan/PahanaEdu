package com.pahanaedu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BillDAO extends BaseDAO {

    public List<Integer> getAllBills() {
        List<Integer> bills = new ArrayList<>();
        String query = "SELECT * FROM bills";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                bills.add(resultSet.getInt("bill_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bills;
    }
}
