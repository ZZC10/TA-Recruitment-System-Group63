package user;

import common.FileUtil; 
 import java.util.List; 
 import java.util.Scanner; 

 public class UserService { 
     
     private static final String USER_FILE = "../users.csv"; 
     private Scanner scanner; 
     
     public void setScanner(Scanner scanner) { 
         this.scanner = scanner; 
     } 
     
     public void updateInfo() { 
         System.out.println("\n=================================="); 
         System.out.println("     Update Personal Information"); 
         System.out.println("=================================="); 
         System.out.println("Please enter your User ID to update your information:"); 
         System.out.println("------------------------------------------"); 
         
         System.out.print("User ID (e.g., 2023001): "); 
         String userId = scanner.nextLine(); 
         
         List<String[]> userData = FileUtil.readCSV(USER_FILE); 
         boolean found = false; 
         
         for (int i = 1; i < userData.size(); i++) { // Skip header 
             String[] user = userData.get(i); 
             if (user.length > 0 && user[0].equals(userId)) { 
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
                 
                 System.out.print("2. New Major (e.g., Computer Science): "); 
                 String newMajor = scanner.nextLine(); 

                 // Ensure we have enough columns for 9 fields:
                 // studentId,name,password,email,major,intention,experience,skills,role
                 String[] updatedUser = new String[9];
                 for (int j = 0; j < Math.min(user.length, 9); j++) {
                     updatedUser[j] = user[j];
                 }
                 for (int j = user.length; j < 9; j++) {
                     updatedUser[j] = "N/A";
                 }

                 if (!newEmail.isEmpty()) {
                     updatedUser[3] = newEmail;
                 }
                 if (!newMajor.isEmpty()) {
                     updatedUser[4] = newMajor;
                 }

                 userData.set(i, updatedUser);
                 FileUtil.writeCSV(USER_FILE, userData);
                 System.out.println("------------------------------------------");
                 System.out.println("Information updated successfully!");
                 System.out.println("------------------------------------------");
             } 
         } 
         
         if (!found) { 
             System.out.println("------------------------------------------"); 
             System.out.println("Error: User with User ID " + userId + " not found."); 
             System.out.println("------------------------------------------"); 
         } 
         System.out.println("=================================="); 
     }

     public String[] getUserByUserId(String userId) {
         List<String[]> userData = FileUtil.readCSV(USER_FILE);
         for (int i = 1; i < userData.size(); i++) {
             String[] user = userData.get(i);
             if (user.length > 0 && user[0].equals(userId)) {
                 return user;
             }
         }
         return null;
     }

     public List<String[]> getAllUsers() {
         List<String[]> userData = FileUtil.readCSV(USER_FILE);
         if (userData.size() > 1) {
             return userData.subList(1, userData.size());
         }
         return userData;
     }

     public boolean updateUserRole(String userId, String newRole) {
         List<String[]> userData = FileUtil.readCSV(USER_FILE);
         for (int i = 1; i < userData.size(); i++) {
             String[] user = userData.get(i);
             if (user.length > 0 && user[0].equals(userId)) {
                 String[] updatedUser = new String[Math.max(user.length, 9)];
                 System.arraycopy(user, 0, updatedUser, 0, user.length);
                 for (int j = user.length; j < 9; j++) {
                     updatedUser[j] = "N/A";
                 }
                 updatedUser[8] = newRole;
                 userData.set(i, updatedUser);
                 FileUtil.writeCSV(USER_FILE, userData);
                 return true;
             }
         }
         return false;
     }
 }