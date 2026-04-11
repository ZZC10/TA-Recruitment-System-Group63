package module;

import common.FileUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ModuleService {
    
    private static final String FILE_PATH = "modules.csv";
    private Scanner scanner;

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Main entry for module creation and management
     * Handles user input and CSV persistence
     */
    public void createModule() {
        ensureHeader(); // Ensure CSV header exists
        System.out.println("\n==================================");
        System.out.println("         Module Management");
        System.out.println("==================================");
        System.out.println("Please select an option:");
        System.out.println("------------------------------------------");
        System.out.println("1. Create New Module");
        System.out.println("2. Query All Modules");
        System.out.println("3. Modify Module Info");
        System.out.println("4. Delete Module");
        System.out.println("5. Search Module");
        System.out.println("0. Back to main menu");
        System.out.println("------------------------------------------");
        System.out.print("Your choice: ");
        
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
            case "0":
                System.out.println("==================================");
                return;
            default:
                System.out.println("------------------------------------------");
                System.out.println("Invalid option, returning to main menu.");
                System.out.println("==================================");
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
        System.out.println("\n==================================");
        System.out.println("        Create New Module");
        System.out.println("==================================");
        System.out.println("Please enter the following information:");
        System.out.println("------------------------------------------");
        
        System.out.print("Module ID (e.g., M001): ");
        String moduleId = scanner.nextLine().trim();
        
        // ID Format validation (M + 3 digits)
        if (!moduleId.matches("M\\d{3}")) {
            System.out.println("------------------------------------------");
            System.out.println("Error: Module ID must start with 'M' followed by 3 digits (e.g., M001).");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }

        if (isModuleExists(moduleId)) {
            System.out.println("------------------------------------------");
            System.out.println("Error: Module ID already exists!");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }

        System.out.print("Module Name: ");
        String moduleName = scanner.nextLine().trim();
        if (moduleName.isEmpty()) {
            System.out.println("------------------------------------------");
            System.out.println("Error: Module Name cannot be empty.");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        System.out.print("Module Leader (Student ID): ");
        String moduleLeader = scanner.nextLine().trim();
        if (moduleLeader.isEmpty()) {
            System.out.println("------------------------------------------");
            System.out.println("Error: Module Leader cannot be empty.");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }

        // MO Account validation (check users.csv)
        if (!isValidMO(moduleLeader)) {
            System.out.println("------------------------------------------");
            System.out.println("Error: Module Leader ID not found in system or unauthorized.");
            System.out.println("Please register the user first.");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        String creationDate = LocalDate.now().toString();

        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        data.add(new String[]{moduleId, moduleName, moduleLeader, creationDate});
        FileUtil.writeCSV(FILE_PATH, data);
        
        System.out.println("------------------------------------------");
        System.out.println("Module created successfully!");
        System.out.println("Module ID: " + moduleId);
        System.out.println("Module Name: " + moduleName);
        System.out.println("Module Leader: " + moduleLeader);
        System.out.println("Creation Date: " + creationDate);
        System.out.println("------------------------------------------");
        System.out.println("==================================");
    }

    private void displayModules() {
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        System.out.println("\n==================================");
        System.out.println("         Existing Modules");
        System.out.println("==================================");
        
        if (data.size() <= 1) {
            System.out.println("------------------------------------------");
            System.out.println("No modules found.");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }

        System.out.println("------------------------------------------");
        System.out.printf("%-10s | %-20s | %-15s | %-10s\n", "ID", "Module Name", "Leader (ID)", "Date");
        System.out.println("------------------------------------------------------------------");
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            System.out.printf("%-10s | %-20s | %-15s | %-10s\n", 
                row[0], row[1], row[2], row[3]);
        }
        System.out.println("------------------------------------------------------------------");
        System.out.println("Total modules: " + (data.size() - 1));
        System.out.println("==================================");
    }

    private void performDelete() {
        System.out.println("\n==================================");
        System.out.println("         Delete Module");
        System.out.println("==================================");
        System.out.println("Please enter the Module ID to delete:");
        System.out.println("------------------------------------------");
        
        System.out.print("Module ID: ");
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
            System.out.println("------------------------------------------");
            System.out.println("Warning: This action cannot be undone!");
            System.out.print("Are you sure you want to delete module " + moduleId + "? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                data.remove(deleteIndex);
                FileUtil.writeCSV(FILE_PATH, data);
                System.out.println("------------------------------------------");
                System.out.println("Module deleted successfully.");
                System.out.println("------------------------------------------");
            } else {
                System.out.println("------------------------------------------");
                System.out.println("Deletion cancelled.");
                System.out.println("------------------------------------------");
            }
        } else {
            System.out.println("------------------------------------------");
            System.out.println("Error: Module ID not found.");
            System.out.println("------------------------------------------");
        }
        System.out.println("==================================");
    }

    private void performSearch() {
        System.out.println("\n==================================");
        System.out.println("          Search Module");
        System.out.println("==================================");
        System.out.println("Please select search type:");
        System.out.println("------------------------------------------");
        System.out.println("1. Search by Name (Fuzzy)");
        System.out.println("2. Search by Leader ID");
        System.out.println("0. Back to module menu");
        System.out.println("------------------------------------------");
        System.out.print("Your choice: ");
        
        String type = scanner.nextLine();
        
        if (type.equals("0")) {
            System.out.println("==================================");
            return;
        }
        
        if (!type.equals("1") && !type.equals("2")) {
            System.out.println("------------------------------------------");
            System.out.println("Invalid search type");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().toLowerCase().trim();
        
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        boolean found = false;
        
        System.out.println("\n==================================");
        System.out.println("          Search Results");
        System.out.println("==================================");
        System.out.println("------------------------------------------");
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
        
        if (!found) {
            System.out.println("------------------------------------------------------------------");
            System.out.println("No matching modules found.");
            System.out.println("------------------------------------------------------------------");
        }
        System.out.println("==================================");
    }

    private void performModify() {
        System.out.println("\n==================================");
        System.out.println("         Modify Module Info");
        System.out.println("==================================");
        System.out.println("Please enter the Module ID to modify:");
        System.out.println("------------------------------------------");
        
        System.out.print("Module ID: ");
        String moduleId = scanner.nextLine().trim();
        
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        boolean found = false;
        
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row[0].equalsIgnoreCase(moduleId)) {
                System.out.println("------------------------------------------");
                System.out.println("Current module information:");
                System.out.println("Module ID: " + row[0]);
                System.out.println("Module Name: " + row[1]);
                System.out.println("Module Leader: " + row[2]);
                System.out.println("Creation Date: " + row[3]);
                System.out.println("------------------------------------------");
                System.out.println("Enter new information (press enter to keep current):");
                
                System.out.print("New Module Name: ");
                String newName = scanner.nextLine().trim();
                if (!newName.isEmpty()) row[1] = newName;
                
                System.out.print("New Module Leader (Student ID): ");
                String newLeader = scanner.nextLine().trim();
                if (!newLeader.isEmpty()) {
                    if (isValidMO(newLeader)) {
                        row[2] = newLeader;
                    } else {
                        System.out.println("------------------------------------------");
                        System.out.println("Error: Unauthorized or invalid Student ID. Leader not updated.");
                        System.out.println("------------------------------------------");
                    }
                }
                
                found = true;
                break;
            }
        }

        if (found) {
            FileUtil.writeCSV(FILE_PATH, data);
            System.out.println("------------------------------------------");
            System.out.println("Module updated successfully!");
            System.out.println("------------------------------------------");
        } else {
            System.out.println("------------------------------------------");
            System.out.println("Error: Module not found.");
            System.out.println("------------------------------------------");
        }
        System.out.println("==================================");
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
