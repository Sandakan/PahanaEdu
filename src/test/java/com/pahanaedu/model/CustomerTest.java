package com.pahanaedu.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void shouldCreateDefaultCustomer() {
        Customer customer = new Customer();
        assertNotNull(customer);
        assertEquals(0, customer.getCustomerId());
        assertNull(customer.getAccountNumber());
        assertNull(customer.getName());
        assertNull(customer.getAddress());
        assertNull(customer.getTelephone());
        assertNull(customer.getEmail());
    }

    @Test
    void shouldCreateCustomerWithParameters() {
        Customer customer = new Customer("000123", "John Doe", "123 Main St", "555-0123", "john@example.com");
        assertEquals("000123", customer.getAccountNumber());
        assertEquals("John Doe", customer.getName());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals("555-0123", customer.getTelephone());
        assertEquals("john@example.com", customer.getEmail());
    }

    @Test
    void shouldSetAndGetCustomerId() {
        Customer customer = new Customer();
        customer.setCustomerId(456);
        assertEquals(456, customer.getCustomerId());
    }

    @Test
    void shouldSetAndGetAccountNumber() {
        Customer customer = new Customer();
        customer.setAccountNumber("000789");
        assertEquals("000789", customer.getAccountNumber());
    }

    @Test
    void shouldSetAndGetName() {
        Customer customer = new Customer();
        customer.setName("Alice Smith");
        assertEquals("Alice Smith", customer.getName());
    }

    @Test
    void shouldSetAndGetAddress() {
        Customer customer = new Customer();
        customer.setAddress("456 Oak Ave");
        assertEquals("456 Oak Ave", customer.getAddress());
    }

    @Test
    void shouldSetAndGetTelephone() {
        Customer customer = new Customer();
        customer.setTelephone("555-9876");
        assertEquals("555-9876", customer.getTelephone());
    }

    @Test
    void shouldSetAndGetEmail() {
        Customer customer = new Customer();
        customer.setEmail("alice@example.com");
        assertEquals("alice@example.com", customer.getEmail());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        Customer customer = new Customer();
        LocalDateTime now = LocalDateTime.now();
        customer.setCreatedAt(now);
        assertEquals(now, customer.getCreatedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        Customer customer = new Customer();
        LocalDateTime now = LocalDateTime.now();
        customer.setUpdatedAt(now);
        assertEquals(now, customer.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetDeletedAt() {
        Customer customer = new Customer();
        LocalDateTime now = LocalDateTime.now();
        customer.setDeletedAt(now);
        assertEquals(now, customer.getDeletedAt());
    }
}
