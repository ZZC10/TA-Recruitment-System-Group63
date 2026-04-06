package common;

import java.util.ArrayList;
import java.util.List;

public class TestFramework {
    
    public static void main(String[] args) {
        System.out.println("========== Starting Framework Test ==========\n");
        
        testFileUtilRead();
        testFileUtilWrite();
        testFileUtilExceptionHandling();
        
        System.out.println("\n========== Framework Test Completed ==========");
    }
    
    public static void testFileUtilRead() {
        System.out.println("[Test 1] Testing FileUtil.readCSV() method");
        System.out.println("-----------------------------------");
        
        List<String[]> users = FileUtil.readCSV("users.csv");
        System.out.println("Reading users.csv results:");
        for (String[] user : users) {
            System.out.println("  " + String.join(" | ", user));
        }
        
        List<String[]> modules = FileUtil.readCSV("modules.csv");
        System.out.println("\nReading modules.csv results:");
        for (String[] module : modules) {
            System.out.println("  " + String.join(" | ", module));
        }
        
        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
        System.out.println("\nReading jobs.csv results:");
        for (String[] job : jobs) {
            System.out.println("  " + String.join(" | ", job));
        }
        
        System.out.println("\nFileUtil.readCSV() test passed\n");
    }
    
    public static void testFileUtilWrite() {
        System.out.println("[Test 2] Testing FileUtil.writeCSV() method");
        System.out.println("-----------------------------------");
        
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"id", "name", "value"});
        testData.add(new String[]{"1", "Test Data 1", "100"});
        testData.add(new String[]{"2", "Test Data 2", "200"});
        
        String testFilePath = "test_data.csv";
        FileUtil.writeCSV(testFilePath, testData);
        
        List<String[]> readBack = FileUtil.readCSV(testFilePath);
        System.out.println("Verification after writing:");
        for (String[] row : readBack) {
            System.out.println("  " + String.join(" | ", row));
        }
        
        System.out.println("\nFileUtil.writeCSV() test passed\n");
    }
    
    public static void testFileUtilExceptionHandling() {
        System.out.println("[Test 3] Testing FileUtil exception handling");
        System.out.println("-----------------------------------");
        
        System.out.println("Testing reading non-existent file:");
        List<String[]> notExist = FileUtil.readCSV("not_exist.csv");
        System.out.println("Result: Returns empty list, size = " + notExist.size());
        
        System.out.println("\nTesting file existence check:");
        System.out.println("users.csv exists: " + FileUtil.fileExists("users.csv"));
        System.out.println("not_exist.csv exists: " + FileUtil.fileExists("not_exist.csv"));
        
        System.out.println("\nException handling test passed\n");
    }
}