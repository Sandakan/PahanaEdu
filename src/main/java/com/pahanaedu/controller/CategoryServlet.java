package com.pahanaedu.controller;

import com.pahanaedu.dao.CategoryDAO;
import com.pahanaedu.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        this.categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewCategoryForm(request, response);
                break;
            case "edit":
                showEditCategoryForm(request, response);
                break;
            case "delete":
                deleteCategory(request, response);
                break;
            default:
                listCategories(request, response);
                break;
        }
    }

    protected void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/categories.jsp").forward(request, response);
    }

    protected void showNewCategoryForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/category-form.jsp").forward(request, response);
    }

    protected void showEditCategoryForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            Category category = categoryDAO.getCategoryById(categoryId);

            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/category-form.jsp").forward(request, response);
                return;
            }

            request.setAttribute("error", "Category not found");
            listCategories(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category id");
            listCategories(request, response);
        }
    }

    protected void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));

            if (categoryDAO.hasDependentItems(categoryId)) {
                request.setAttribute("error", "Cannot delete category. There are items associated with this category.");
                listCategories(request, response);
                return;
            }

            if (categoryDAO.deleteCategory(categoryId)) {
                request.setAttribute("success", "Category deleted successfully");
            } else {
                request.setAttribute("error", "Failed to delete category");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category id");
        } finally {
            listCategories(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "create":
                createCategory(request, response);
                break;
            case "update":
                updateCategory(request, response);
                break;
            default:
                listCategories(request, response);
                break;
        }
    }

    protected void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Validation
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Category name is required");
            request.getRequestDispatcher("/category-form.jsp").forward(request, response);
            return;
        }

        name = name.trim();
        if (description != null) {
            description = description.trim();
            if (description.isEmpty()) {
                description = null;
            }
        }

        // Check if category name already exists
        if (categoryDAO.isCategoryNameExists(name)) {
            request.setAttribute("error", "Category name already exists");
            Category category = new Category(name, description);
            request.setAttribute("category", category);
            request.getRequestDispatcher("/category-form.jsp").forward(request, response);
            return;
        }

        Category category = new Category(name, description);

        if (categoryDAO.createCategory(category)) {
            request.setAttribute("success", "Category created successfully");
            listCategories(request, response);
        } else {
            request.setAttribute("error", "Failed to create category");
            request.setAttribute("category", category);
            request.getRequestDispatcher("/category-form.jsp").forward(request, response);
        }
    }

    protected void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            // Validation
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Category name is required");
                Category category = categoryDAO.getCategoryById(categoryId);
                request.setAttribute("category", category);
                request.getRequestDispatcher("/category-form.jsp").forward(request, response);
                return;
            }

            name = name.trim();
            if (description != null) {
                description = description.trim();
                if (description.isEmpty()) {
                    description = null;
                }
            }

            // Check if category name already exists for other categories
            if (categoryDAO.isCategoryNameExistsForUpdate(name, categoryId)) {
                request.setAttribute("error", "Category name already exists");
                Category category = new Category(categoryId, name, description);
                request.setAttribute("category", category);
                request.getRequestDispatcher("/category-form.jsp").forward(request, response);
                return;
            }

            Category category = new Category(categoryId, name, description);

            if (categoryDAO.updateCategory(category)) {
                request.setAttribute("success", "Category updated successfully");
                listCategories(request, response);
            } else {
                request.setAttribute("error", "Failed to update category");
                request.setAttribute("category", category);
                request.getRequestDispatcher("/category-form.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category ID");
            listCategories(request, response);
        }
    }
}
