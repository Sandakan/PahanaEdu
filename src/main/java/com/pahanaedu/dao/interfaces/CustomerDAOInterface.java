package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.Customer;

import java.util.List;

public interface CustomerDAOInterface {
    List<Customer> getAllCustomers();

    Customer getCustomerById(int customerId);

    Customer getCustomerByAccountNumber(String accountNumber);

    boolean createCustomer(Customer customer);

    boolean updateCustomer(Customer customer);

    boolean deleteCustomer(int customerId);

    boolean isAccountNumberExists(String accountNumber);

    boolean isAccountNumberExistsForUpdate(String accountNumber, int customerId);

    String generateAccountNumber();
}
