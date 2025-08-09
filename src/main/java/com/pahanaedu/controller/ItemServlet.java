package com.pahanaedu.controller;

import com.pahanaedu.dao.CategoryDAO;
import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.model.Category;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/items")
public class ItemServlet extends HttpServlet {
    private ItemDAO itemDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        itemDAO = new ItemDAO();
        categoryDAO = new CategoryDAO();
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
                showNewItemForm(request, response);
                break;
            case "edit":
                showEditItemForm(request, response);
                break;
            case "delete":
                deleteItem(request, response);
                break;
            default:
                listItems(request, response);
                break;
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
                createItem(request, response);
                break;
            case "update":
                updateItem(request, response);
                break;
            default:
                listItems(request, response);
                break;
        }
    }

    private void listItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Item> items = itemDAO.getAllItems();
        request.setAttribute("items", items);
        request.getRequestDispatcher("/items.jsp").forward(request, response);
    }

    private void showNewItemForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only admins can create items
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            request.setAttribute("error", "You don't have permission to create items.");
            listItems(request, response);
            return;
        }

        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/item-form.jsp").forward(request, response);
    }

    private void showEditItemForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only admins can edit items
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            request.setAttribute("error", "You don't have permission to edit items.");
            listItems(request, response);
            return;
        }

        try {
            int itemId = Integer.parseInt(request.getParameter("id"));
            Item item = itemDAO.getItemById(itemId);

            if (item != null) {
                List<Category> categories = categoryDAO.getAllCategories();
                request.setAttribute("item", item);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/item-form.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Item not found");
                listItems(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid item ID");
            listItems(request, response);
        }
    }

    private void createItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only admins can create items
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            request.setAttribute("error", "You don't have permission to create items.");
            listItems(request, response);
            return;
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String categoryIdStr = request.getParameter("categoryId");
        String unitPriceStr = request.getParameter("unitPrice");

        // Validation
        if (isNullOrEmpty(name) || isNullOrEmpty(unitPriceStr) || isNullOrEmpty(categoryIdStr)) {
            request.setAttribute("error", "Name, category, and unit price are required fields");
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/item-form.jsp").forward(request, response);
            return;
        }

        try {
            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("error", "Unit price must be greater than 0");
                List<Category> categories = categoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/item-form.jsp").forward(request, response);
                return;
            }

            // Check if item name already exists
            if (itemDAO.isItemNameExists(name)) {
                request.setAttribute("error", "Item name already exists");
                List<Category> categories = categoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/item-form.jsp").forward(request, response);
                return;
            }

            int categoryId = 0;
            if (!isNullOrEmpty(categoryIdStr)) {
                categoryId = Integer.parseInt(categoryIdStr);
            }

            Item item = new Item(name, description, categoryId, unitPrice);

            if (itemDAO.createItem(item)) {
                request.setAttribute("success", "Item created successfully");
            } else {
                request.setAttribute("error", "Failed to create item");
            }

            listItems(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid unit price or category");
            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/item-form.jsp").forward(request, response);
        }
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only admins can update items
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            request.setAttribute("error", "You don't have permission to update items.");
            listItems(request, response);
            return;
        }

        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String categoryIdStr = request.getParameter("categoryId");
            String unitPriceStr = request.getParameter("unitPrice");

            if (isNullOrEmpty(name) || isNullOrEmpty(unitPriceStr) || isNullOrEmpty(categoryIdStr)) {
                request.setAttribute("error", "Name, category, and unit price are required fields");
                Item item = new Item(itemId, name, description, 0, null);
                request.setAttribute("item", item);
                List<Category> categories = categoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/item-form.jsp").forward(request, response);
                return;
            }

            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("error", "Unit price must be greater than 0");
                Item item = new Item(itemId, name, description, 0, unitPrice);
                request.setAttribute("item", item);
                List<Category> categories = categoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/item-form.jsp").forward(request, response);
                return;
            }

            // Check if item name already exists for another item
            if (itemDAO.isItemNameExistsForUpdate(name, itemId)) {
                request.setAttribute("error", "Item name already exists for another item");
                Item item = new Item(itemId, name, description, 0, unitPrice);
                request.setAttribute("item", item);
                List<Category> categories = categoryDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/item-form.jsp").forward(request, response);
                return;
            }

            int categoryId = Integer.parseInt(categoryIdStr);

            Item item = new Item(itemId, name, description, categoryId, unitPrice);

            if (itemDAO.updateItem(item)) {
                request.setAttribute("success", "Item updated successfully");
            } else {
                request.setAttribute("error", "Failed to update item");
            }

            listItems(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid item ID, unit price, or category");
            listItems(request, response);
        }
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only admins can delete items
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            request.setAttribute("error", "You don't have permission to delete items.");
            listItems(request, response);
            return;
        }

        try {
            int itemId = Integer.parseInt(request.getParameter("id"));

            if (itemDAO.deleteItem(itemId)) {
                request.setAttribute("success", "Item deleted successfully");
            } else {
                request.setAttribute("error", "Failed to delete item");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid item ID");
        }

        listItems(request, response);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
