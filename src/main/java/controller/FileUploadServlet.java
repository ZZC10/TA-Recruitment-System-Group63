package com.bupt.ta.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * FileUploadServlet: Handles TA photo (JPG/PNG, 2MB) and CV (PDF, 10MB) uploads.
 * Saves files to webapp/uploads directory.
 */

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB (max CV size)
    maxRequestSize = 1024 * 1024 * 15    // 15MB
)
public class FileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";
    private static final String PHOTO_DIR = "photos";
    private static final String CV_DIR = "cvs";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String fileType = req.getParameter("type"); // "photo" or "cv"
        Part filePart = req.getPart("file");

        if (fileType == null || filePart == null || filePart.getSize() == 0) {
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Missing file or type\"}");
            return;
        }

        String fileName = getSubmittedFileName(filePart);
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // Validation based on file type
        if ("photo".equalsIgnoreCase(fileType)) {
            if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) {
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Invalid photo format (JPG/PNG required)\"}");
                return;
            }
            if (filePart.getSize() > 1024 * 1024 * 2) { // 2MB
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Photo size exceeds 2MB limit\"}");
                return;
            }
        } else if ("cv".equalsIgnoreCase(fileType)) {
            if (!extension.equals(".pdf")) {
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Invalid CV format (PDF required)\"}");
                return;
            }
            if (filePart.getSize() > 1024 * 1024 * 10) { // 10MB
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"CV size exceeds 10MB limit\"}");
                return;
            }
        } else {
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Invalid file type\"}");
            return;
        }

        // Generate unique filename and save path
        String subDir = "photo".equalsIgnoreCase(fileType) ? PHOTO_DIR : CV_DIR;
        String uploadPath = getServletContext().getRealPath("/") + UPLOAD_DIR + File.separator + subDir;
        
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String newFileName = UUID.randomUUID().toString() + extension;
        String fullPath = uploadPath + File.separator + newFileName;
        filePart.write(fullPath);

        // Return relative path for client use
        String relativePath = UPLOAD_DIR + "/" + subDir + "/" + newFileName;
        resp.getWriter().write("{\"status\":\"success\", \"path\":\"" + relativePath + "\"}");
    }

    private String getSubmittedFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
