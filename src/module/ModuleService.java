package module;

import common.FileUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ModuleService {
    
    private static final String FILE_PATH = "modules.csv";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Main entry for module creation and management
     * Handles user input and CSV persistence
     */
    public void createModule() {
        ensureHeader(); // Ensure CSV header exists
        System.out.println("\n--- Module Management ---");
        System.out.println("1. Create New Module");
        System.out.println("2. Query All Modules");
        System.out.println("3. Modify Module Info");
        System.out.println("4. Delete Module");
        System.out.println("5. Search Module");
        System.out.print("Please select an option: ");
        
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                performCreate();
                break;
            case "2":
                displayModules();
                break;
            case "3":
                performModify();
                break;
            case "4":
                performDelete();
                break;
            case "5":
                performSearch();
                break;
            default:
                System.out.println("Invalid option, returning to main menu.");
        }
    }

    private void ensureHeader() {
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        if (data.isEmpty()) {
            List<String[]> newData = new ArrayList<>();
            newData.add(new String[]{"moduleId", "moduleName", "moduleLeader", "creationDate"});
            FileUtil.writeCSV(FILE_PATH, newData);
        }
    }

    private void performCreate() {
        System.out.print("Enter Module ID (e.g., M004): ");
        String moduleId = scanner.nextLine().trim();
        
        // ID Format validation (M + 3 digits)
        if (!moduleId.matches("M\\d{3}")) {
            System.out.println("Error: Module ID must start with 'M' followed by 3 digits (e.g., M001).");
            return;
        }

        if (isModuleExists(moduleId)) {
            System.out.println("Error: Module ID already exists!");
            return;
        }

        System.out.print("Enter Module Name: ");
        String moduleName = scanner.nextLine().trim();
        if (moduleName.isEmpty()) {
            System.out.println("Error: Module Name cannot be empty.");
            return;
        }
        
        System.out.print("Enter Module Leader (Student ID): ");
        String moduleLeader = scanner.nextLine().trim();
        if (moduleLeader.isEmpty()) {
            System.out.println("Error: Module Leader cannot be empty.");
            return;
        }

        // MO Account validation (check users.csv)
        if (!isValidMO(moduleLeader)) {
            System.out.println("Error: Module Leader ID not found in system or unauthorized.");
            return;
        }
        
        String creationDate = LocalDate.now().toString();

        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        data.add(new String[]{moduleId, moduleName, moduleLeader, creationDate});
        FileUtil.writeCSV(FILE_PATH, data);
        
        System.out.println("Module created successfully!");
    }

    private void displayModules() {
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        if (data.size() <= 1) {
            System.out.println("No modules found.");
            return;
        }

        System.out.println("\n--- Existing Modules ---");
        System.out.printf("%-10s | %-20s | %-15s | %-10s\n", "ID", "Module Name", "Leader (ID)", "Date");
        System.out.println("------------------------------------------------------------------");
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            System.out.printf("%-10s | %-20s | %-15s | %-10s\n", 
                row[0], row[1], row[2], row[3]);
        }
    }

    private void performDelete() {
        System.out.print("Enter Module ID to delete: ");
        String moduleId = scanner.nextLine().trim();
        
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        int deleteIndex = -1;
        
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i)[0].equalsIgnoreCase(moduleId)) {
                deleteIndex = i;
                break;
            }
        }
        
        if (deleteIndex != -1) {
            System.out.print("Are you sure you want to delete module " + moduleId + "? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                data.remove(deleteIndex);
                FileUtil.writeCSV(FILE_PATH, data);
                System.out.println("Module deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("Error: Module ID not found.");
        }
    }

    private void performSearch() {
        System.out.println("\n--- Search Module ---");
        System.out.println("1. Search by Name (Fuzzy)");
        System.out.println("2. Search by Leader ID");
        System.out.print("Select search type: ");
        
        String type = scanner.nextLine();
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().toLowerCase().trim();
        
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        boolean found = false;
        
        System.out.println("\n--- Search Results ---");
        System.out.printf("%-10s | %-20s | %-15s | %-10s\n", "ID", "Module Name", "Leader (ID)", "Date");
        System.out.println("------------------------------------------------------------------");
        
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            boolean match = false;
            if (type.equals("1") && row[1].toLowerCase().contains(keyword)) {
                match = true;
            } else if (type.equals("2") && row[2].equalsIgnoreCase(keyword)) {
                match = true;
            }
            
            if (match) {
                System.out.printf("%-10s | %-20s | %-15s | %-10s\n", 
                    row[0], row[1], row[2], row[3]);
                found = true;
            }
        }
        
        if (!found) System.out.println("No matching modules found.");
    }

    private void performModify() {
        System.out.print("Enter Module ID to modify: ");
        String moduleId = scanner.nextLine().trim();
        
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        boolean found = false;
        
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row[0].equalsIgnoreCase(moduleId)) {
                System.out.print("Enter new Module Name (leave blank to keep current): ");
                String newName = scanner.nextLine().trim();
                if (!newName.isEmpty()) row[1] = newName;
                
                System.out.print("Enter new Module Leader (Student ID) (leave blank to keep current): ");
                String newLeader = scanner.nextLine().trim();
                if (!newLeader.isEmpty()) {
                    if (isValidMO(newLeader)) {
                        row[2] = newLeader;
                    } else {
                        System.out.println("Error: Unauthorized or invalid Student ID. Leader not updated.");
                    }
                }
                
                found = true;
                break;
            }
        }

        if (found) {
            FileUtil.writeCSV(FILE_PATH, data);
            System.out.println("Module updated successfully!");
        } else {
            System.out.println("Module not found.");
        }
    }

    private boolean isValidMO(String studentId) {
        List<String[]> users = FileUtil.readCSV("users.csv");
        for (int i = 1; i < users.size(); i++) {
            // Check if studentId exists in users.csv (Column 0)
            if (users.get(i)[0].equalsIgnoreCase(studentId)) {
                // Future: Add role check here when auth module adds it
                return true;
            }
        }
        return false;
    }

    private boolean isModuleExists(String moduleId) {
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i)[0].equalsIgnoreCase(moduleId)) {
                return true;
            }
        }
        return false;
    }
}
