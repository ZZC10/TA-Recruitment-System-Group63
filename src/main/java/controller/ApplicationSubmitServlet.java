package com.bupt.ta.controller;

import com.bupt.ta.model.Application;
import com.bupt.ta.model.User;
import com.bupt.ta.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class ApplicationSubmitServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !"ta".equalsIgnoreCase(currentUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String jobId = req.getParameter("jobId");
        
        if (jobId != null && !jobId.trim().isEmpty()) {
            // Create new Application record
            Application application = new Application(
                UUID.randomUUID().toString(),
                jobId,
                currentUser.getUsername(),
                "pending",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "", 
                "" // CV path would come from upload
            );

            JsonUtil.addApplication(application);
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}