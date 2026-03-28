package com.bupt.ta.controller;

import com.bupt.ta.model.Job;
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
import java.util.List;
import java.util.UUID;


public class JobServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"mo".equals(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Job> moJobs = JsonUtil.findJobsByMo(user.getUsername());
        req.setAttribute("jobs", moJobs);
        req.getRequestDispatcher("/pages/mo/dashboard.jsp").forward(req, resp);
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

        String action = req.getParameter("action");
        if ("post".equals(action)) {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String requirements = req.getParameter("requirements");
            int numRequired = Integer.parseInt(req.getParameter("numRequired"));
            double salary = Double.parseDouble(req.getParameter("salary"));

            Job job = new Job();
            job.setId(UUID.randomUUID().toString());
            job.setMoUsername(user.getUsername());
            job.setTitle(title);
            job.setDescription(description);
            job.setRequirements(requirements);
            job.setNumRequired(numRequired);
            job.setSalary(salary);
            job.setStatus("open");
            job.setCreateTime(LocalDateTime.now());

            JsonUtil.addJob(job);
        }

        resp.sendRedirect(req.getContextPath() + "/job");
    }
}
