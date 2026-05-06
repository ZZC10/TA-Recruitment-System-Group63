package auth;

import common.FileUtil;
import java.util.List;

public class AuthService {

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }

    public boolean register(String studentId, String password, String name) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("Student ID cannot be empty");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password cannot be empty");
            return false;
        }
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty");
            return false;
        }

        if (!isValidPassword(password)) {
            System.out.println("Invalid password: At least 6 characters, must contain letters and digits");
            return false;
        }

        List<String[]> users = FileUtil.readCSV("users.csv");

        for (String[] user : users) {
            if (user.length > 0 && user[0].equals(studentId)) {
                System.out.println("Registration failed: Student ID " + studentId + " already exists");
                return false;
            }
        }

        String[] newUser = {studentId, password, name};
        users.add(newUser);

        FileUtil.writeCSV("users.csv", users);
        System.out.println("Registration successful! Welcome " + name);
        return true;
    }

    public boolean login(String studentId, String password) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("Student ID cannot be empty");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password cannot be empty");
            return false;
        }

        List<String[]> users = FileUtil.readCSV("users.csv");

        for (String[] user : users) {
            if (user.length >= 2 && user[0].equals(studentId) && user[1].equals(password)) {
                String name = user.length >= 3 ? user[2] : studentId;
                System.out.println("Login successful! Welcome back, " + name);
                return true;
            }
        }

        System.out.println("Login failed: Invalid student ID or password");
        return false;
    }
    
    public void register() {
        System.out.println("Register function called");
    }
    
    public void login() {
        System.out.println("Login function called");
    }
    
    public String getUserRole(String studentId) {
        List<String[]> users = FileUtil.readCSV("users.csv");
        for (String[] user : users) {
            if (user.length > 0 && user[0].equals(studentId)) {
                return user.length > 5 ? user[5] : "TA";
            }
        }
        return "TA";
    }
    
    public boolean hasRole(String studentId, String requiredRole) {
        String userRole = getUserRole(studentId);
        return userRole.equals(requiredRole);
    }
}