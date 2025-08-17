package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.Category;

import java.util.List;

public interface CategoryDAOInterface {
    List<Category> getAllCategories();

    Category getCategoryById(int categoryId);

    boolean createCategory(Category category);

    boolean updateCategory(Category category);

    boolean deleteCategory(int categoryId);

    boolean isCategoryNameExists(String name);

    boolean isCategoryNameExistsForUpdate(String name, int categoryId);

    boolean hasDependentItems(int categoryId);

    List<String> getAllCategoryNames();
}
