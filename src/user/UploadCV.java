package user;

import common.FileUtil;
import java.util.List;
import java.util.Scanner;

public class UploadCV {
    
    private static final String USER_FILE = "users.csv";
    private final Scanner scanner = new Scanner(System.in);
    
    public void upload() {
        System.out.println("\n--- Upload/Update CV ---");
        System.out.print("Enter your Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        List<String[]> userData = FileUtil.readCSV(USER_FILE);
        boolean found = false;
        
        for (int i = 1; i < userData.size(); i++) { // Skip header
            String[] user = userData.get(i);
            if (user.length > 0 && user[0].equals(studentId)) {
                found = true;
                
                System.out.println("User found: " + user[1]);
                
                System.out.print("Enter Intention (意向): ");
                String intention = scanner.nextLine().replace(",", ";").trim();
                
                System.out.print("Enter Personal Experience (个人经历): ");
                String experience = scanner.nextLine().replace(",", ";").trim();
                
                System.out.print("Enter Personal Skills (个人技能): ");
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
                System.out.println("CV uploaded/updated successfully!");
                break;
            }
        }
        
        if (!found) {
            System.out.println("Error: User with Student ID " + studentId + " not found. Please register first.");
        }
    }
}
