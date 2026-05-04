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
        return register(studentId, password, name, "TA");
    }

    public boolean register(String studentId, String password, String name, String role) {
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
        if (role == null || role.trim().isEmpty()) {
            role = "TA";
        }

        if (!isValidPassword(password)) {
            System.out.println("Password format error: at least 6 characters, containing letters and numbers");
            return false;
        }

        List<String[]> users = FileUtil.readCSV("users.csv");
        if (users == null) {
            System.out.println("Registration failed: cannot read user data");
            return false;
        }
        
        for (String[] user : users) {
            if (user.length > 0 && user[0].equals(studentId)) {
                System.out.println("Registration failed: Student ID " + studentId + " already exists");
                return false;
            }
        }

        // Check current columns count
        int columnsCount = 9; // studentId,name,password,email,major,intention,experience,skills,role
        String[] newUser = new String[columnsCount];
        newUser[0] = studentId;
        newUser[1] = name;
        newUser[2] = password;
        newUser[3] = "";
        newUser[4] = "";
        newUser[5] = "";
        newUser[6] = "";
        newUser[7] = "";
        newUser[8] = role;

        users.add(newUser);

        FileUtil.writeCSV("users.csv", users);
        System.out.println("Registration successful! Welcome " + name + " (" + role + ")");

        return true;
    }

    public String login(String studentId, String password) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("Student ID cannot be empty");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password cannot be empty");
            return null;
        }

        List<String[]> users = FileUtil.readCSV("users.csv");
        if (users == null) {
            System.out.println("Login failed: cannot read user data");
            return null;
        }
        
        for (String[] user : users) {
            if (user.length >= 3 && user[0].equals(studentId) && user[2].equals(password)) {
                String name = user.length >= 2 ? user[1] : studentId;
                String role = user.length >= 9 ? user[8] : "TA";
                System.out.println("Login successful! Welcome back, " + name + " (" + role + ")");
                return role;  // 返回角色
            }
        }

        System.out.println("Login failed: incorrect student ID or password");
        return null;  // 登录失败返回 null
    }
    
    public String getUserRole(String studentId) {
        List<String[]> users = FileUtil.readCSV("users.csv");
        if (users == null) {
            return "TA"; // Default role
        }
        
        for (String[] user : users) {
            if (user.length >= 9 && user[0].equals(studentId)) {
                return user[8];
            }
        }
        
        return "TA"; // Default role
    }
    
    public boolean hasRole(String studentId, String requiredRole) {
        String userRole = getUserRole(studentId);
        return userRole.equals(requiredRole);
    }
    
    private java.util.Scanner scanner;
    
    public void setScanner(java.util.Scanner scanner) {
        this.scanner = scanner;
    }
    
    // No-argument method for Main.java call
    public void register() {
        System.out.println("\n==================================");
        System.out.println("         User Registration");
        System.out.println("==================================");
        System.out.println("Please enter the following information:");
        System.out.println("------------------------------------------");
        System.out.println("1. Student ID (e.g., 2023001)");
        System.out.println("2. Password (at least 6 chars, letters + numbers)");
        System.out.println("3. Full Name");
        System.out.println("4. Role (TA, MO, ADMIN) [Default: TA]");
        System.out.println("------------------------------------------");
        
        System.out.print("1. Student ID (e.g., 2023001): ");
        String studentId = scanner.nextLine().trim();
        
        System.out.print("2. Password (at least 6 chars, letters + numbers): ");
        String password = scanner.nextLine().trim();
        
        System.out.print("3. Full Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("4. Role (TA, MO, ADMIN) [Default: TA]: ");
        String role = scanner.nextLine().trim();
        if (role.isEmpty()) {
            role = "TA";
        }
        
        System.out.println("------------------------------------------");
        register(studentId, password, name, role);
        System.out.println("==================================");
    }
    
    public String login() {
        System.out.println("\n==================================");
        System.out.println("           User Login");
        System.out.println("==================================");
        System.out.println("Please enter your login credentials:");
        System.out.println("------------------------------------------");
        System.out.println("1. Student ID (e.g., 2023001)");
        System.out.println("2. Password");
        System.out.println("------------------------------------------");
        
        System.out.print("1. Student ID (e.g., 2023001): ");
        String studentId = scanner.nextLine().trim();
        
        System.out.print("2. Password: ");
        String password = scanner.nextLine().trim();
        
        System.out.println("------------------------------------------");
        String role = login(studentId, password);
        System.out.println("==================================");
        
        return role;
    }
}
