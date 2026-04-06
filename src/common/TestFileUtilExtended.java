package common;

import java.util.ArrayList;
import java.util.List;

public class TestFileUtilExtended {
    
    public static void main(String[] args) {
        System.out.println("========== Testing Extended FileUtil Methods ==========\n");
        
        testSearchByField();
        testAddRow();
        testUpdateRow();
        testDeleteRow();
        
        System.out.println("\n========== Extended FileUtil Methods Test Completed ==========");
    }
    
    public static void testSearchByField() {
        System.out.println("[Test 1] Testing searchByField() method");
        System.out.println("-----------------------------------");
        
        List<String[]> results = FileUtil.searchByField("users.csv", 0, "2023001");
        System.out.println("Searching users with studentId '2023001':");
        for (String[] row : results) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        results = FileUtil.searchByField("modules.csv", 0, "M001");
        System.out.println("\nSearching modules with moduleId 'M001':");
        for (String[] row : results) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        System.out.println("\nsearchByField() test passed\n");
    }
    
    public static void testAddRow() {
        System.out.println("[Test 2] Testing addRow() method");
        System.out.println("-----------------------------------");
        
        System.out.println("Before adding row:");
        List<String[]> before = FileUtil.readCSV("test_data.csv");
        for (String[] row : before) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        String[] newRow = {"3", "Test Data 3", "300"};
        FileUtil.addRow("test_data.csv", newRow);
        
        System.out.println("\nAfter adding row:");
        List<String[]> after = FileUtil.readCSV("test_data.csv");
        for (String[] row : after) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        System.out.println("\naddRow() test passed\n");
    }
    
    public static void testUpdateRow() {
        System.out.println("[Test 3] Testing updateRow() method");
        System.out.println("-----------------------------------");
        
        System.out.println("Before updating row:");
        List<String[]> before = FileUtil.readCSV("test_data.csv");
        for (String[] row : before) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        String[] updatedRow = {"1", "Updated Test Data 1", "150"};
        FileUtil.updateRow("test_data.csv", 1, updatedRow);
        
        System.out.println("\nAfter updating row:");
        List<String[]> after = FileUtil.readCSV("test_data.csv");
        for (String[] row : after) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        System.out.println("\nupdateRow() test passed\n");
    }
    
    public static void testDeleteRow() {
        System.out.println("[Test 4] Testing deleteRow() method");
        System.out.println("-----------------------------------");
        
        System.out.println("Before deleting row:");
        List<String[]> before = FileUtil.readCSV("test_data.csv");
        for (String[] row : before) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        FileUtil.deleteRow("test_data.csv", 1);
        
        System.out.println("\nAfter deleting row:");
        List<String[]> after = FileUtil.readCSV("test_data.csv");
        for (String[] row : after) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        System.out.println("\ndeleteRow() test passed\n");
    }
}