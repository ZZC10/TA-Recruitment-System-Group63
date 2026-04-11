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
        if (users == null) {
            System.out.println("Login failed: cannot read user data");
            return false;
        }
        
        for (String[] user : users) {
            if (user.length >= 2 && user[0].equals(studentId) && user[1].equals(password)) {
                String name = user.length >= 3 ? user[2] : studentId;
                System.out.println("Login successful! Welcome back, " + name);
                return true;
            }
        }

        System.out.println("Login failed: incorrect student ID or password");
        return false;
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
        System.out.println("------------------------------------------");
        
        System.out.print("1. Student ID (e.g., 2023001): ");
        String studentId = scanner.nextLine().trim();
        
        System.out.print("2. Password (at least 6 chars, letters + numbers): ");
        String password = scanner.nextLine().trim();
        
        System.out.print("3. Full Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.println("------------------------------------------");
        register(studentId, password, name);
        System.out.println("==================================");
    }
    
    public void login() {
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
        login(studentId, password);
        System.out.println("==================================");
    }
}
