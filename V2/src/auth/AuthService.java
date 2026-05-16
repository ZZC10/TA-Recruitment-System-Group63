package auth;

import common.FileUtil;
import java.util.List;

public class AuthService {

    private static final String USERS_FILE = "../users.csv";
    private static final String DEFAULT_ROLE = "TA";

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

    public boolean register(String userId, String password, String name) {
        return register(userId, password, name, DEFAULT_ROLE);
    }

    public boolean register(String userId, String password, String name, String role) {
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("User ID cannot be empty");
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
            role = DEFAULT_ROLE;
        }

        if (!isValidPassword(password)) {
            System.out.println("Invalid password: At least 6 characters, must contain letters and digits");
            return false;
        }

        List<String[]> users = FileUtil.readCSV(USERS_FILE);

        for (String[] user : users) {
            if (user.length > 0 && user[0].equals(userId)) {
                System.out.println("Registration failed: User ID " + userId + " already exists");
                return false;
            }
        }

        String[] newUser = {userId, name, password, "", "", role, "", "", ""};
        users.add(newUser);

        FileUtil.writeCSV(USERS_FILE, users);
        System.out.println("Registration successful! Welcome " + name);
        return true;
    }

    public boolean login(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("User ID cannot be empty");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password cannot be empty");
            return false;
        }

        List<String[]> users = FileUtil.readCSV(USERS_FILE);

        for (String[] user : users) {
            if (user.length >= 3 && user[0].equals(userId) && user[2].equals(password)) {
                String name = user.length >= 2 ? user[1] : userId;
                System.out.println("Login successful! Welcome back, " + name);
                return true;
            }
        }

        System.out.println("Login failed: Invalid User ID or password");
        return false;
    }
    
    public void register() {
        System.out.println("Register function called");
    }
    
    public void login() {
        System.out.println("Login function called");
    }
    
    public String getUserRole(String userId) {
        List<String[]> users = FileUtil.readCSV(USERS_FILE);
        for (String[] user : users) {
            if (user.length > 0 && user[0].equals(userId)) {
                return user.length > 5 ? user[5] : DEFAULT_ROLE;
            }
        }
        return DEFAULT_ROLE;
    }
    
    public boolean hasRole(String userId, String requiredRole) {
        String userRole = getUserRole(userId);
        return userRole.equals(requiredRole);
    }
    
    public String getUserName(String userId) {
        List<String[]> users = FileUtil.readCSV(USERS_FILE);
        for (String[] user : users) {
            if (user.length > 0 && user[0].equals(userId)) {
                return user.length > 1 ? user[1] : userId;
            }
        }
        return userId;
    }
    
    public static List<String[]> getUsers() {
        return FileUtil.readCSV(USERS_FILE);
    }
}