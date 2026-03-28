package com.bupt.ta.controller;

import com.bupt.ta.model.User;
import com.bupt.ta.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * RegisterServlet: Handles user registration and saves data to users.json.
 */

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/common/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String role = req.getParameter("role");

        // Null check and validation
        if (username == null || password == null || email == null || role == null ||
            username.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "All fields are required");
            req.getRequestDispatcher("/pages/common/register.jsp").forward(req, resp);
            return;
        }

        // Check if user already exists
        if (JsonUtil.findByUsername(username) != null) {
            req.setAttribute("error", "Username already exists");
            req.getRequestDispatcher("/pages/common/register.jsp").forward(req, resp);
            return;
        }

        // Create new user with initial status and current time
        User newUser = new User(
            username, 
            password, 
            email, 
            role, 
            "pending", // Initial status for new users
            LocalDateTime.now()
        );

        // Save to users.json
        JsonUtil.addUser(newUser);

        // Redirect to login after successful registration
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
