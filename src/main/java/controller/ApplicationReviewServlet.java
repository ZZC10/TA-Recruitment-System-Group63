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
import java.util.List;
import java.util.stream.Collectors;


public class ApplicationReviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"mo".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String jobId = req.getParameter("jobId");
        if (jobId != null) {
            List<Application> apps = JsonUtil.findAppsByJob(jobId);
            req.setAttribute("applications", apps);
            req.setAttribute("jobId", jobId);
            req.getRequestDispatcher("/pages/mo/review.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/job");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"mo".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String appId = req.getParameter("appId");
        String status = req.getParameter("status"); // accepted, rejected
        String feedback = req.getParameter("feedback");
        String jobId = req.getParameter("jobId");

        if (appId != null && status != null) {
            JsonUtil.updateApplicationStatus(appId, status, feedback);
        }

        resp.sendRedirect(req.getContextPath() + "/review?jobId=" + jobId);
    }
}
