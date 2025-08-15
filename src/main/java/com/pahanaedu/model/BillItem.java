package com.pahanaedu.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillItem {
    private int billItemId;
    private int billId;
    private int itemId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Item item;

    public BillItem() {
        this.quantity = 0;
        this.unitPrice = BigDecimal.ZERO;
        this.lineTotal = BigDecimal.ZERO;
    }

    public BillItem(int billId, int itemId, int quantity, BigDecimal unitPrice) {
        this.billId = billId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = calculateLineTotal();
    }

    public BillItem(int billId, Item item, int quantity) {
        this.billId = billId;
        this.item = item;
        if (item != null) {
            this.itemId = item.getItemId();
            this.unitPrice = item.getUnitPrice();
        }
        this.quantity = quantity;
        this.lineTotal = calculateLineTotal();
    }

    public int getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(int billItemId) {
        this.billItemId = billItemId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lineTotal = calculateLineTotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        this.lineTotal = calculateLineTotal();
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        if (item != null) {
            this.itemId = item.getItemId();
            if (this.unitPrice == null || this.unitPrice.equals(BigDecimal.ZERO)) {
                this.unitPrice = item.getUnitPrice();
            }
            this.lineTotal = calculateLineTotal();
        }
    }

    public String getItemName() {
        return item != null ? item.getName() : null;
    }

    public String getItemDescription() {
        return item != null ? item.getDescription() : null;
    }

    public String getCategoryName() {
        return item != null ? item.getCategoryName() : null;
    }

    public BigDecimal calculateLineTotal() {
        if (quantity > 0 && unitPrice != null) {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    public void updateLineTotal() {
        this.lineTotal = calculateLineTotal();
    }

    public String toJson(int id) {
        String name = item != null && item.getName() != null ? item.getName().replace("\"", "\\\"").replace("'", "\\'")
                : "";
        String category = item != null && item.getCategoryName() != null
                ? item.getCategoryName().replace("\"", "\\\"").replace("'", "\\'")
                : "";

        return String.format(
                "{\"id\":%d,\"itemId\":%d,\"name\":\"%s\",\"category\":\"%s\",\"unitPrice\":%.1f,\"quantity\":%d,\"lineTotal\":%.1f}",
                id,
                itemId,
                name,
                category,
                unitPrice != null ? unitPrice.doubleValue() : 0.0,
                quantity,
                lineTotal != null ? lineTotal.doubleValue() : 0.0);
    }
}
