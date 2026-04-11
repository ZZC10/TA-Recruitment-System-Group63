package user;

import common.FileUtil;
import java.util.List;
import java.util.Scanner;

public class UserService {
    
    private static final String USER_FILE = "users.csv";
    private Scanner scanner;
    
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void updateInfo() {
        System.out.println("\n==================================");
        System.out.println("     Update Personal Information");
        System.out.println("==================================");
        System.out.println("Please enter your Student ID to update your information:");
        System.out.println("------------------------------------------");
        
        System.out.print("Student ID (e.g., 2023001): ");
        String studentId = scanner.nextLine();
        
        List<String[]> userData = FileUtil.readCSV(USER_FILE);
        boolean found = false;
        
        for (int i = 1; i < userData.size(); i++) { // Skip header
            String[] user = userData.get(i);
            if (user.length > 0 && user[0].equals(studentId)) {
                found = true;
                System.out.println("------------------------------------------");
                System.out.println("User found: " + user[1]);
                System.out.println("Current Email: " + (user.length > 3 ? user[3] : "N/A"));
                System.out.println("Current Major: " + (user.length > 4 ? user[4] : "N/A"));
                System.out.println("------------------------------------------");
                System.out.println("Enter new information (press enter to keep current):");
                System.out.println("1. New Email (e.g., student@example.com)");
                System.out.println("2. New Major (e.g., Computer Science)");
                System.out.println("------------------------------------------");
                
                System.out.print("1. New Email (e.g., student@example.com): ");
                String newEmail = scanner.nextLine();
                if (!newEmail.isEmpty()) {
                    if (user.length > 3) {
                        user[3] = newEmail;
                    } else {
                        // Handle case where row might be short
                        String[] newUser = new String[5];
                        System.arraycopy(user, 0, newUser, 0, user.length);
                        newUser[3] = newEmail;
                        userData.set(i, newUser);
                        user = newUser;
                    }
                }
                
                System.out.print("2. New Major (e.g., Computer Science): ");
                String newMajor = scanner.nextLine();
                if (!newMajor.isEmpty()) {
                    if (user.length > 4) {
                        user[4] = newMajor;
                    } else {
                        String[] newUser = new String[5];
                        System.arraycopy(user, 0, newUser, 0, user.length);
                        newUser[4] = newMajor;
                        userData.set(i, newUser);
                    }
                }
                
                FileUtil.writeCSV(USER_FILE, userData);
                System.out.println("------------------------------------------");
                System.out.println("Personal information updated successfully!");
                System.out.println("------------------------------------------");
                break;
            }
        }
        
        if (!found) {
            System.out.println("------------------------------------------");
            System.out.println("Error: User with Student ID " + studentId + " not found.");
            System.out.println("------------------------------------------");
        }
        System.out.println("==================================");
    }
}
