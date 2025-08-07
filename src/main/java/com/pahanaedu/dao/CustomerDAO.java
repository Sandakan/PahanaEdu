package com.pahanaedu.dao;

import com.pahanaedu.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends BaseDAO {

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE deleted_at IS NULL ORDER BY created_at DESC";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Customer customer = mapResultSetToCustomer(resultSet);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM customers WHERE customer_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToCustomer(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Customer getCustomerByAccountNumber(String accountNumber) {
        String query = "SELECT * FROM customers WHERE account_number = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToCustomer(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean createCustomer(Customer customer) {
        String query = "INSERT INTO customers (account_number, name, address, telephone, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, customer.getAccountNumber());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getTelephone());
            statement.setString(5, customer.getEmail());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    customer.setCustomerId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customers SET account_number = ?, name = ?, address = ?, telephone = ?, email = ? WHERE customer_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, customer.getAccountNumber());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getTelephone());
            statement.setString(5, customer.getEmail());
            statement.setInt(6, customer.getCustomerId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteCustomer(int customerId) {
        String query = "UPDATE customers SET deleted_at = CURRENT_TIMESTAMP WHERE customer_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, customerId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isAccountNumberExists(String accountNumber) {
        String query = "SELECT COUNT(*) FROM customers WHERE account_number = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isAccountNumberExistsForUpdate(String accountNumber, int customerId) {
        String query = "SELECT COUNT(*) FROM customers WHERE account_number = ? AND customer_id != ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountNumber);
            statement.setInt(2, customerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String generateAccountNumber() {
        String query = "SELECT MAX(CAST(SUBSTRING(account_number, 4) AS UNSIGNED)) as max_num FROM customers WHERE account_number LIKE 'ACC%'";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int maxNum = resultSet.getInt("max_num");
                return String.format("ACC%06d", maxNum + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "ACC000001"; // Default starting account number
    }

    private Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("customer_id"));
        customer.setAccountNumber(resultSet.getString("account_number"));
        customer.setName(resultSet.getString("name"));
        customer.setAddress(resultSet.getString("address"));
        customer.setTelephone(resultSet.getString("telephone"));
        customer.setEmail(resultSet.getString("email"));

        if (resultSet.getTimestamp("created_at") != null) {
            customer.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("updated_at") != null) {
            customer.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("deleted_at") != null) {
            customer.setDeletedAt(resultSet.getTimestamp("deleted_at").toLocalDateTime());
        }

        return customer;
    }
}
