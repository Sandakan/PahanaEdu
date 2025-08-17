package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.BillItem;

import java.util.List;

public interface BillItemDAOInterface {
    List<BillItem> getAllBillItems();

    List<BillItem> getBillItemsByBillId(int billId);

    BillItem getBillItemById(int billItemId);

    boolean createBillItem(BillItem billItem);

    boolean updateBillItem(BillItem billItem);

    boolean deleteBillItem(int billItemId);

    boolean deleteBillItemsByBillId(int billId);

    boolean createBillItems(List<BillItem> billItems);
}
