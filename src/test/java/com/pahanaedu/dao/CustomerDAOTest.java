package com.pahanaedu.dao;

import com.pahanaedu.dao.interfaces.CustomerDAOInterface;
import com.pahanaedu.helpers.DatabaseHelper;
import com.pahanaedu.model.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDAOTest {

    private CustomerDAOInterface customerDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {

        try (Connection connection = DatabaseHelper.getInstance().getConnection();
                Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            statement.execute("DELETE FROM customers");
            statement.execute("ALTER TABLE customers AUTO_INCREMENT = 1");

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

            statement.execute(
                    "INSERT INTO customers (customer_id, account_number, name, address, telephone, email, created_at, updated_at) VALUES "
                            +
                            "(1, 000001, 'John Doe', '123 Main St, Colombo', '0771234567', 'john.doe@email.com', NOW(), NOW()), "
                            +
                            "(2, 000002, 'Jane Smith', '456 Queen St, Kandy', '0779876543', 'jane.smith@email.com', NOW(), NOW())");

        }
    }

    @BeforeEach
    void setUp() {
        customerDAO = new CustomerDAO();
    }

    @Test
    void shouldRetrieveExistingCustomers() {

        List<Customer> customers = customerDAO.getAllCustomers();

        assertNotNull(customers, "Customers list should not be null");
        assertFalse(customers.isEmpty(), "Should have customers from seed data");

        assertTrue(customers.size() == 2, "Should have  2 seed customers");

        boolean hasJohnDoe = customers.stream().anyMatch(c -> "John Doe".equals(c.getName()));
        boolean hasJaneSmith = customers.stream().anyMatch(c -> "Jane Smith".equals(c.getName()));

        assertTrue(hasJohnDoe, "Should have 'John Doe' from focused test data");
        assertTrue(hasJaneSmith, "Should have 'Jane Smith' from focused test data");
    }

    @Test
    void shouldRetrieveCustomerById() {

        List<Customer> customers = customerDAO.getAllCustomers();
        assertFalse(customers.isEmpty(), "Should have customers to test with");

        Customer firstCustomer = customers.get(0);
        Customer retrievedCustomer = customerDAO.getCustomerById(firstCustomer.getCustomerId());

        assertNotNull(retrievedCustomer, "Should find customer by valid ID");
        assertEquals(firstCustomer.getName(), retrievedCustomer.getName(), "Retrieved customer should match");
        assertEquals(firstCustomer.getAccountNumber(), retrievedCustomer.getAccountNumber(),
                "Account numbers should match");
    }

    @Test
    void shouldRetrieveCustomerByAccountNumber() {

        Customer customer = customerDAO.getCustomerByAccountNumber("1");

        assertNotNull(customer, "Should find customer with account number 1");
        assertEquals("John Doe", customer.getName(), "Should be John Doe from focused test data");
        assertEquals("123 Main St, Colombo", customer.getAddress(), "Address should match focused test data");
        assertEquals("0771234567", customer.getTelephone(), "Telephone should match focused test data");
    }

    @Test
    void shouldCreateNewCustomer() {

        Customer newCustomer = new Customer();
        newCustomer.setName("Test School " + System.currentTimeMillis());
        newCustomer.setAddress("Test Address");
        newCustomer.setTelephone("+94-11-9999999");
        newCustomer.setEmail("test" + System.currentTimeMillis() + "@school.lk");

        String accountNumber = customerDAO.generateAccountNumber();
        newCustomer.setAccountNumber(accountNumber);

        boolean created = customerDAO.createCustomer(newCustomer);
        assertTrue(created, "Should successfully create new customer");

        List<Customer> customers = customerDAO.getAllCustomers();
        boolean found = customers.stream()
                .anyMatch(c -> newCustomer.getName().equals(c.getName()));
        assertTrue(found, "New customer should appear in customers list");
    }

    @Test
    void shouldUpdateExistingCustomer() {

        Customer testCustomer = new Customer();
        testCustomer.setName("Update Test School " + System.currentTimeMillis());
        testCustomer.setAddress("Original Address");
        testCustomer.setTelephone("+94-11-1111111");
        testCustomer.setEmail("original" + System.currentTimeMillis() + "@school.lk");

        String accountNumber = customerDAO.generateAccountNumber();
        testCustomer.setAccountNumber(accountNumber);

        assertTrue(customerDAO.createCustomer(testCustomer), "Should create test customer");

        List<Customer> customers = customerDAO.getAllCustomers();
        Customer createdCustomer = customers.stream()
                .filter(c -> testCustomer.getName().equals(c.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdCustomer, "Should find the created customer");

        String newAddress = "Updated Address " + System.currentTimeMillis();
        String newTelephone = "+94-11-2222222";
        createdCustomer.setAddress(newAddress);
        createdCustomer.setTelephone(newTelephone);

        boolean updated = customerDAO.updateCustomer(createdCustomer);
        assertTrue(updated, "Should successfully update customer");

        Customer updatedCustomer = customerDAO.getCustomerById(createdCustomer.getCustomerId());
        assertNotNull(updatedCustomer, "Should still find the updated customer");
        assertEquals(newAddress, updatedCustomer.getAddress(), "Address should be updated");
        assertEquals(newTelephone, updatedCustomer.getTelephone(), "Telephone should be updated");
    }

    @Test
    void shouldDeleteCustomer() {

        Customer customerToDelete = new Customer();
        customerToDelete.setName("Temp Delete School " + System.currentTimeMillis());
        customerToDelete.setAddress("Temporary address");
        customerToDelete.setTelephone("+94-11-0000000");
        customerToDelete.setEmail("delete" + System.currentTimeMillis() + "@school.lk");

        String accountNumber = customerDAO.generateAccountNumber();
        customerToDelete.setAccountNumber(accountNumber);

        boolean created = customerDAO.createCustomer(customerToDelete);
        assertTrue(created, "Should create temporary customer");

        List<Customer> customers = customerDAO.getAllCustomers();
        Customer createdCustomer = customers.stream()
                .filter(c -> customerToDelete.getName().equals(c.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdCustomer, "Should find the created customer");

        boolean deleted = customerDAO.deleteCustomer(createdCustomer.getCustomerId());
        assertTrue(deleted, "Should successfully delete customer");

        Customer deletedCustomer = customerDAO.getCustomerById(createdCustomer.getCustomerId());
        assertNull(deletedCustomer, "Deleted customer should not be retrievable");
    }

    @Test
    void shouldGenerateAccountNumber() {

        String accountNumber = customerDAO.generateAccountNumber();

        assertNotNull(accountNumber, "Generated account number should not be null");
        assertFalse(accountNumber.isEmpty(), "Generated account number should not be empty");
        assertTrue(accountNumber.matches("\\d+"), "Account number should be digits");

        String anotherAccountNumber = customerDAO.generateAccountNumber();
        assertEquals(accountNumber, anotherAccountNumber, "Generated account numbers should be the same");
    }

    @Test
    void shouldCheckAccountNumberExists() {

        assertTrue(customerDAO.isAccountNumberExists("1"), "Should find existing account number 1");
        assertTrue(customerDAO.isAccountNumberExists("2"), "Should find existing account number 2");
        assertFalse(customerDAO.isAccountNumberExists("999"), "Should not find non-existent account number");
    }

    @Test
    void shouldCheckAccountNumberExistsForUpdate() {

        List<Customer> customers = customerDAO.getAllCustomers();
        assertFalse(customers.isEmpty(), "Should have customers to test with");

        Customer firstCustomer = customers.get(0);

        assertFalse(
                customerDAO.isAccountNumberExistsForUpdate(firstCustomer.getAccountNumber(),
                        firstCustomer.getCustomerId()),
                "Same customer's account number should not be considered duplicate for update");

        if (customers.size() > 1) {
            Customer secondCustomer = customers.get(1);
            assertTrue(
                    customerDAO.isAccountNumberExistsForUpdate(secondCustomer.getAccountNumber(),
                            firstCustomer.getCustomerId()),
                    "Different customer's account number should be considered duplicate for update");
        }
    }

    @Test
    void shouldReturnNullForInvalidInputs() {

        assertNull(customerDAO.getCustomerById(-1), "Negative ID should return null");
        assertNull(customerDAO.getCustomerById(0), "Zero ID should return null");
        assertNull(customerDAO.getCustomerById(99999), "Non-existent ID should return null");
        assertNull(customerDAO.getCustomerByAccountNumber(null), "Null account number should return null");
        assertNull(customerDAO.getCustomerByAccountNumber(""), "Empty account number should return null");
        assertNull(customerDAO.getCustomerByAccountNumber("999"), "Non-existent account number should return null");
    }

    @Test
    void shouldHandleInvalidOperations() {

        assertFalse(customerDAO.createCustomer(null), "Creating null customer should return false");
        assertFalse(customerDAO.updateCustomer(null), "Updating null customer should return false");
        assertFalse(customerDAO.deleteCustomer(-1), "Deleting with negative ID should return false");
        assertFalse(customerDAO.deleteCustomer(0), "Deleting with zero ID should return false");
        assertFalse(customerDAO.isAccountNumberExists(null), "Checking null account number should return false");
        assertFalse(customerDAO.isAccountNumberExists(""), "Checking empty account number should return false");
    }
}
