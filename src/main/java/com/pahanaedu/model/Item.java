package com.pahanaedu.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Item {
    private int itemId;
    private String name;
    private String description;
    private Category category;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Item() {
    }

    public Item(String name, String description, Category category, BigDecimal unitPrice) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    public Item(int itemId, String name, String description, Category category, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    public Item(String name, String description, int categoryId, BigDecimal unitPrice) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;

        this.category = new Category();
        this.category.setCategoryId(categoryId);
    }
 
    public Item(int itemId, String name, String description, int categoryId, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        
        this.category = new Category();
        this.category.setCategoryId(categoryId);
    }
 
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return category != null ? category.getCategoryId() : null;
    }

    public void setCategoryId(Integer categoryId) {
        if (categoryId != null) {
            if (this.category == null) {
                this.category = new Category();
            }
            this.category.setCategoryId(categoryId);
        } else {
            this.category = null;
        }
    }

    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
}
