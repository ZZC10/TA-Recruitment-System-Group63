package com.bupt.ta.controller;

import com.bupt.ta.model.TaProfile;
import com.bupt.ta.model.User;
import com.bupt.ta.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"ta".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        TaProfile profile = JsonUtil.getProfileByUsername(user.getUsername());
        if (profile == null) {
            profile = new TaProfile();
            profile.setUsername(user.getUsername());
        }

        req.setAttribute("profile", profile);
        req.getRequestDispatcher("/pages/ta/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"ta".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        TaProfile profile = JsonUtil.getProfileByUsername(user.getUsername());
        if (profile == null) {
            profile = new TaProfile();
            profile.setId(UUID.randomUUID().toString());
            profile.setUsername(user.getUsername());
        }

        profile.setRealName(req.getParameter("realName"));
        profile.setStudentId(req.getParameter("studentId"));
        profile.setMajor(req.getParameter("major"));
        profile.setYearOfStudy(req.getParameter("yearOfStudy"));
        
        String gpaStr = req.getParameter("gpa");
        if (gpaStr != null && !gpaStr.isEmpty()) {
            profile.setGpa(Double.parseDouble(gpaStr));
        }
        
        profile.setSkills(req.getParameter("skills"));
        profile.setExperience(req.getParameter("experience"));
        
        // Photo and CV paths are usually updated via FileUploadServlet 
        // and then potentially passed back here or updated separately.
        // For now, we update the main text fields.
        String photoPath = req.getParameter("photoPath");
        if (photoPath != null && !photoPath.isEmpty()) {
            profile.setPhotoPath(photoPath);
        }
        
        String cvFilePath = req.getParameter("cvFilePath");
        if (cvFilePath != null && !cvFilePath.isEmpty()) {
            profile.setCvFilePath(cvFilePath);
        }

        JsonUtil.updateProfile(profile);
        
        req.setAttribute("message", "Profile updated successfully!");
        req.setAttribute("profile", profile);
        req.getRequestDispatcher("/pages/ta/profile.jsp").forward(req, resp);
    }
}
