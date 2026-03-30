package module;

import common.FileUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Module Management Service
 * Responsible: Guo Aozhi (Member 4)
 * Tasks: Create module, query module, modify module, bind MO account
 */
public class ModuleService {
    
    private static final String FILE_PATH = "modules.csv";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Main entry for module creation and management
     * Handles user input and CSV persistence
     */
    public void createModule() {
        System.out.println("\n--- Module Management ---");
        System.out.println("1. Create New Module");
        System.out.println("2. Query All Modules");
        System.out.println("3. Modify Module Info");
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
            default:
                System.out.println("Invalid option, returning to main menu.");
        }
    }

    private void performCreate() {
        System.out.print("Enter Module ID (e.g., M004): ");
        String moduleId = scanner.nextLine();
        
        if (isModuleExists(moduleId)) {
            System.out.println("Error: Module ID already exists!");
            return;
        }

        System.out.print("Enter Module Name: ");
        String moduleName = scanner.nextLine();
        
        System.out.print("Enter Module Leader (MO Account): ");
        String moduleLeader = scanner.nextLine();
        
        String creationDate = LocalDate.now().toString();

        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        data.add(new String[]{moduleId, moduleName, moduleLeader, creationDate});
        FileUtil.writeCSV(FILE_PATH, data);
        
        System.out.println("Module created successfully!");
    }

    private void displayModules() {
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        if (data.isEmpty() || (data.size() == 1 && data.get(0).length > 0 && data.get(0)[0].contains("moduleId"))) {
             if (data.size() <= 1) {
                System.out.println("No modules found.");
                return;
             }
        }

        System.out.println("\n--- Existing Modules ---");
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            // Skip header if it exists
            if (i == 0 && row[0].equalsIgnoreCase("moduleId")) continue;
            
            System.out.printf("%-10s | %-15s | %-10s | %-10s\n", 
                row[0], row[1], row[2], row[3]);
        }
    }

    private void performModify() {
        System.out.print("Enter Module ID to modify: ");
        String moduleId = scanner.nextLine();
        
        List<String[]> data = FileUtil.readCSV(FILE_PATH);
        boolean found = false;
        
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row[0].equalsIgnoreCase(moduleId)) {
                System.out.print("Enter new Module Name (leave blank to keep current): ");
                String newName = scanner.nextLine();
                if (!newName.isEmpty()) row[1] = newName;
                
                System.out.print("Enter new Module Leader (leave blank to keep current): ");
                String newLeader = scanner.nextLine();
                if (!newLeader.isEmpty()) row[2] = newLeader;
                
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
