package user;

import common.FileUtil;
import java.util.List;
import java.util.Scanner;

public class UserService {
    
    private static final String USER_FILE = "users.csv";
    private final Scanner scanner = new Scanner(System.in);
    
    public void updateInfo() {
        System.out.println("\n--- Update Personal Information ---");
        System.out.print("Enter your Student ID: ");
        String studentId = scanner.nextLine();
        
        List<String[]> userData = FileUtil.readCSV(USER_FILE);
        boolean found = false;
        
        for (int i = 1; i < userData.size(); i++) { // Skip header
            String[] user = userData.get(i);
            if (user.length > 0 && user[0].equals(studentId)) {
                found = true;
                System.out.println("User found: " + user[1]);
                System.out.println("Current Email: " + (user.length > 3 ? user[3] : "N/A"));
                System.out.println("Current Major: " + (user.length > 4 ? user[4] : "N/A"));
                
                System.out.print("Enter new Email (press enter to keep current): ");
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
                
                System.out.print("Enter new Major (press enter to keep current): ");
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
                System.out.println("Personal information updated successfully!");
                break;
            }
        }
        
        if (!found) {
            System.out.println("Error: User with Student ID " + studentId + " not found.");
        }
    }
}
