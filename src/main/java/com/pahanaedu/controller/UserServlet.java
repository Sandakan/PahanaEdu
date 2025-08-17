package com.pahanaedu.controller;

import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewUserForm(request, response);
                break;
            case "edit":
                showEditUserForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                listUsers(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "create":
                createUser(request, response);
                break;
            case "update":
                updateUser(request, response);
                break;
            default:
                listUsers(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    private void showNewUserForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/user-form.jsp").forward(request, response);
    }

    private void showEditUserForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userDAO.getUserById(userId);

        if (user == null) {
            request.setAttribute("error", "User not found.");
            listUsers(request, response);
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/user-form.jsp").forward(request, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String role = request.getParameter("role");

        if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty() ||
                firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/user-form.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("role", role);
            request.getRequestDispatcher("/user-form.jsp").forward(request, response);
            return;
        }

        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "Email already exists.");
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("role", role);
            request.getRequestDispatcher("/user-form.jsp").forward(request, response);
            return;
        }

        User user = new User(email.trim(), password, firstName.trim(), lastName.trim(), role);
        User createdUser = userDAO.createUser(user);

        if (createdUser != null) {
            request.setAttribute("success", "User created successfully.");
        } else {
            request.setAttribute("error", "Failed to create user.");
        }

        listUsers(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String role = request.getParameter("role");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (email == null || email.trim().isEmpty() ||
                firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            User user = userDAO.getUserById(userId);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/user-form.jsp").forward(request, response);
            return;
        }

        if (password != null && !password.trim().isEmpty()) {
            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                request.setAttribute("error", "Please confirm the new password.");
                User user = userDAO.getUserById(userId);
                request.setAttribute("user", user);
                request.getRequestDispatcher("/user-form.jsp").forward(request, response);
                return;
            }

            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match.");
                User user = userDAO.getUserById(userId);
                request.setAttribute("user", user);
                request.getRequestDispatcher("/user-form.jsp").forward(request, response);
                return;
            }

            if (password.length() < 6) {
                request.setAttribute("error", "Password must be at least 6 characters long.");
                User user = userDAO.getUserById(userId);
                request.setAttribute("user", user);
                request.getRequestDispatcher("/user-form.jsp").forward(request, response);
                return;
            }
        }

        if (userDAO.emailExists(email, userId)) {
            request.setAttribute("error", "Email already exists.");
            User user = userDAO.getUserById(userId);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/user-form.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setUserId(userId);
        user.setEmail(email.trim());
        user.setFirstName(firstName.trim());
        user.setLastName(lastName.trim());
        user.setRole(role);

        boolean updated = userDAO.updateUser(user, password);

        if (updated) {
            String successMessage = "User updated successfully.";
            if (password != null && !password.trim().isEmpty()) {
                successMessage = "User and password updated successfully.";
            }
            request.setAttribute("success", successMessage);
        } else {
            request.setAttribute("error", "Failed to update user.");
        }

        listUsers(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser.getUserId() == userId) {
            request.setAttribute("error", "You cannot delete your own account.");
            listUsers(request, response);
            return;
        }

        boolean deleted = userDAO.deleteUser(userId);

        if (deleted) {
            request.setAttribute("success", "User deleted successfully.");
        } else {
            request.setAttribute("error", "Failed to delete user.");
        }

        listUsers(request, response);
    }
}
