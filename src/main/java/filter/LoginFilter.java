package com.bupt.ta.filter;

import com.bupt.ta.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LoginFilter: Handles authentication and role-based authorization.
 * Limits access to specific directories (ta, mo, admin) based on User.role.
 */

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        
        // 1. Exclude login and register pages/servlets
        if (uri.endsWith("login.jsp") || uri.endsWith("register.jsp") || 
            uri.endsWith("/login") || uri.endsWith("/register") ||
            uri.contains("/static/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Authentication Check
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        // 3. Role-based Authorization (Directory-based)
        String role = user.getRole(); // ta, mo, admin
        
        boolean authorized = true;
        if (uri.contains("/pages/ta/") && !"ta".equalsIgnoreCase(role)) {
            authorized = false;
        } else if (uri.contains("/pages/mo/") && !"mo".equalsIgnoreCase(role)) {
            authorized = false;
        } else if (uri.contains("/pages/admin/") && !"admin".equalsIgnoreCase(role)) {
            authorized = false;
        }

        if (!authorized) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You do not have permission to view this page.");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
