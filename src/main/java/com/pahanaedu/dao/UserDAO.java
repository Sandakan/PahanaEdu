package com.pahanaedu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BaseDAO {

    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        // Using try-with-resources to ensure that resources are closed automatically
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                users.add(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
