package user;

import common.FileUtil;
import java.util.List;
import java.util.Scanner;

public class UploadCV {
    
    private static final String USER_FILE = "users.csv";
    private Scanner scanner;
    
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void upload() {
        System.out.println("\n==================================");
        System.out.println("         Upload/Update CV");
        System.out.println("==================================");
        System.out.println("Please enter your Student ID to upload or update your CV:");
        System.out.println("------------------------------------------");
        
        System.out.print("Student ID (e.g., 2023001): ");
        String studentId = scanner.nextLine().trim();
        
        List<String[]> userData = FileUtil.readCSV(USER_FILE);
        boolean found = false;
        
        for (int i = 1; i < userData.size(); i++) { // Skip header
            String[] user = userData.get(i);
            if (user.length > 0 && user[0].equals(studentId)) {
                found = true;
                System.out.println("------------------------------------------");
                System.out.println("User found: " + user[1]);
                System.out.println("------------------------------------------");
                System.out.println("Please enter your CV information:");
                System.out.println("(Press enter to keep current information)");
                System.out.println("1. Intention (e.g., Software Development TA)");
                System.out.println("2. Personal Experience (e.g., Internship, Projects)");
                System.out.println("3. Personal Skills (e.g., Java, Python, Communication)");
                System.out.println("------------------------------------------");
                
                System.out.print("1. Intention (e.g., Software Development TA): ");
                String intention = scanner.nextLine().replace(",", ";").trim();
                
                System.out.print("2. Personal Experience (e.g., Internship, Projects): ");
                String experience = scanner.nextLine().replace(",", ";").trim();
                
                System.out.print("3. Personal Skills (e.g., Java, Python, Communication): ");
                String skills = scanner.nextLine().replace(",", ";").trim();
                
                // Prepare updated user record with 8 fields: 
                // studentId, name, password, email, major, intention, experience, skills
                String[] updatedUser = new String[8];
                // Copy existing fields
                for (int j = 0; j < Math.min(user.length, 5); j++) {
                    updatedUser[j] = user[j];
                }
                // Fill in N/A for missing intermediate fields if any
                for (int j = user.length; j < 5; j++) {
                    updatedUser[j] = "N/A";
                }
                
                updatedUser[5] = intention.isEmpty() ? (user.length > 5 ? user[5] : "N/A") : intention;
                updatedUser[6] = experience.isEmpty() ? (user.length > 6 ? user[6] : "N/A") : experience;
                updatedUser[7] = skills.isEmpty() ? (user.length > 7 ? user[7] : "N/A") : skills;
                
                userData.set(i, updatedUser);
                
                // Update header if necessary
                String[] header = userData.get(0);
                if (header.length < 8) {
                    String[] newHeader = new String[8];
                    System.arraycopy(header, 0, newHeader, 0, header.length);
                    if (header.length <= 5) newHeader[5] = "intention";
                    if (header.length <= 6) newHeader[6] = "experience";
                    if (header.length <= 7) newHeader[7] = "skills";
                    userData.set(0, newHeader);
                }
                
                FileUtil.writeCSV(USER_FILE, userData);
                System.out.println("------------------------------------------");
                System.out.println("CV uploaded/updated successfully!");
                System.out.println("------------------------------------------");
                break;
            }
        }
        
        if (!found) {
            System.out.println("------------------------------------------");
            System.out.println("Error: User with Student ID " + studentId + " not found.");
            System.out.println("Please register first before uploading CV.");
            System.out.println("------------------------------------------");
        }
        System.out.println("==================================");
    }
}
