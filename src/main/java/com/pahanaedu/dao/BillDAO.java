package com.pahanaedu.dao;

import com.pahanaedu.enums.PaymentMethod;
import com.pahanaedu.enums.PaymentStatus;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO extends BaseDAO {

    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT b.*, c.account_number, c.name as customer_name, c.address, c.telephone, c.email, " +
                "u.first_name, u.last_name, u.email as user_email " +
                "FROM bills b " +
                "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                "LEFT JOIN users u ON b.user_id = u.user_id " +
                "WHERE b.deleted_at IS NULL " +
                "ORDER BY b.created_at DESC";

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                bills.add(mapResultSetToBill(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bills;
    }

    public Bill getBillById(int billId) {
        String query = "SELECT b.*, c.account_number, c.name as customer_name, c.address, c.telephone, c.email, " +
                "u.first_name, u.last_name, u.email as user_email " +
                "FROM bills b " +
                "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                "LEFT JOIN users u ON b.user_id = u.user_id " +
                "WHERE b.bill_id = ? AND b.deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, billId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToBill(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Bill> getBillsByCustomer(int customerId) {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT b.*, c.account_number, c.name as customer_name, c.address, c.telephone, c.email, " +
                "u.first_name, u.last_name, u.email as user_email " +
                "FROM bills b " +
                "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                "LEFT JOIN users u ON b.user_id = u.user_id " +
                "WHERE b.customer_id = ? AND b.deleted_at IS NULL " +
                "ORDER BY b.created_at DESC";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bills.add(mapResultSetToBill(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bills;
    }

    public boolean createBill(Bill bill) {
        String query = "INSERT INTO bills (customer_id, user_id, total_amount, payment_status, payment_method, notes) "
                +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, bill.getCustomerId());
            statement.setInt(2, bill.getUserId());
            statement.setBigDecimal(3, bill.getTotalAmount());
            statement.setString(4, bill.getPaymentStatus().name());
            statement.setString(5, bill.getPaymentMethod().name());
            statement.setString(6, bill.getNotes());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    bill.setBillId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateBill(Bill bill) {
        String query = "UPDATE bills " +
                "SET customer_id = ?, user_id = ?, total_amount = ?, payment_status = ?, " +
                "payment_method = ?, notes = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE bill_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bill.getCustomerId());
            statement.setInt(2, bill.getUserId());
            statement.setBigDecimal(3, bill.getTotalAmount());
            statement.setString(4, bill.getPaymentStatus().name());
            statement.setString(5, bill.getPaymentMethod().name());
            statement.setString(6, bill.getNotes());
            statement.setInt(7, bill.getBillId());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteBill(int billId) {
        String query = "UPDATE bills SET deleted_at = CURRENT_TIMESTAMP WHERE bill_id = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, billId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updatePaymentStatus(int billId, PaymentStatus status) {
        String query = "UPDATE bills " +
                "SET payment_status = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE bill_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status.name());
            statement.setInt(2, billId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public BigDecimal calculateTotalAmount(int billId) {
        String query = "SELECT SUM(line_total) as total " +
                "FROM bill_items " +
                "WHERE bill_id = ? AND deleted_at IS NULL";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, billId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BigDecimal total = resultSet.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    private Bill mapResultSetToBill(ResultSet resultSet) throws SQLException {
        Bill bill = new Bill();

        bill.setBillId(resultSet.getInt("bill_id"));
        bill.setCustomerId(resultSet.getInt("customer_id"));
        bill.setUserId(resultSet.getInt("user_id"));
        bill.setTotalAmount(resultSet.getBigDecimal("total_amount"));

        try {
            bill.setPaymentStatus(PaymentStatus.valueOf(resultSet.getString("payment_status")));
        } catch (IllegalArgumentException e) {
            bill.setPaymentStatus(PaymentStatus.PENDING);
        }

        try {
            bill.setPaymentMethod(PaymentMethod.valueOf(resultSet.getString("payment_method")));
        } catch (IllegalArgumentException e) {
            bill.setPaymentMethod(PaymentMethod.CASH);
        }

        bill.setNotes(resultSet.getString("notes"));

        if (resultSet.getTimestamp("created_at") != null) {
            bill.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("updated_at") != null) {
            bill.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        }
        if (resultSet.getTimestamp("deleted_at") != null) {
            bill.setDeletedAt(resultSet.getTimestamp("deleted_at").toLocalDateTime());
        }

        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("customer_id"));
        customer.setAccountNumber(resultSet.getString("account_number"));
        customer.setName(resultSet.getString("customer_name"));
        customer.setAddress(resultSet.getString("address"));
        customer.setTelephone(resultSet.getString("telephone"));
        customer.setEmail(resultSet.getString("email"));
        bill.setCustomer(customer);

        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("user_email"));
        bill.setUser(user);

        return bill;
    }
}
