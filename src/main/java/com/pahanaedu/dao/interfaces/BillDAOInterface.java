package com.pahanaedu.dao.interfaces;

import com.pahanaedu.enums.PaymentStatus;
import com.pahanaedu.model.Bill;

import java.math.BigDecimal;
import java.util.List;

public interface BillDAOInterface {
    List<Bill> getAllBills();

    Bill getBillById(int billId);

    List<Bill> getBillsByCustomer(int customerId);

    boolean createBill(Bill bill);

    boolean updateBill(Bill bill);

    boolean deleteBill(int billId);

    boolean updatePaymentStatus(int billId, PaymentStatus status);

    BigDecimal calculateTotalAmount(int billId);
}
