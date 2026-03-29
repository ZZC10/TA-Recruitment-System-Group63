package com.bupt.ta.controller;

import com.bupt.ta.model.User;
import com.bupt.ta.model.Job;
import com.bupt.ta.model.Application;
import com.bupt.ta.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = user.getRole();
        
        if ("ta".equalsIgnoreCase(role)) {
            // Load open jobs
            List<Job> allJobs = JsonUtil.loadJobs();
            List<Job> openJobs = allJobs.stream()
                .filter(j -> "open".equals(j.getStatus()))
                .collect(Collectors.toList());
            req.setAttribute("jobs", openJobs);

            // Load user's applications
            List<Application> myApps = JsonUtil.loadApplications().stream()
                .filter(a -> a.getTaUsername().equals(user.getUsername()))
                .collect(Collectors.toList());
            req.setAttribute("applications", myApps);

            req.getRequestDispatcher("/pages/ta/dashboard.jsp").forward(req, resp);
        } else if ("mo".equalsIgnoreCase(role)) {
            resp.sendRedirect(req.getContextPath() + "/job");
        } else if ("admin".equalsIgnoreCase(role)) {
            req.getRequestDispatcher("/pages/admin/dashboard.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}