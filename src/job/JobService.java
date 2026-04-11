package job;

import common.FileUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JobService {
    
    private Scanner scanner;
    private static final String JOBS_FILE = "jobs.csv";
    
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void showCategories() {
        System.out.println("\n==================================");
        System.out.println("       Browse Job Categories");
        System.out.println("==================================");
        
        List<String[]> jobs = FileUtil.readCSV(JOBS_FILE);
        if (jobs.isEmpty() || jobs.size() <= 1) {
            System.out.println("------------------------------------------");
            System.out.println("No job information available");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        String[] header = jobs.get(0);
        int categoryIndex = findCategoryIndex(header);
        if (categoryIndex == -1) {
            System.out.println("------------------------------------------");
            System.out.println("Data file error: cannot find jobCategory column");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        List<String> categories = new ArrayList<>();
        for (int i = 1; i < jobs.size(); i++) {
            String[] row = jobs.get(i);
            if (row.length > categoryIndex) {
                String category = row[categoryIndex].trim();
                if (!categories.contains(category) && !category.isEmpty()) {
                    categories.add(category);
                }
            }
        }
        
        System.out.println("------------------------------------------");
        System.out.println("Available Job Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        System.out.println("0. Back to main menu");
        System.out.println("------------------------------------------");
        System.out.print("Please select a category: ");
        
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            // Handle case where there's no more input
            System.out.println("------------------------------------------");
            System.out.println("Please enter a valid number");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        if (choice == 0) {
            System.out.println("==================================");
            return;
        }
        
        if (choice < 1 || choice > categories.size()) {
            System.out.println("------------------------------------------");
            System.out.println("Invalid choice");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        String selectedCategory = categories.get(choice - 1);
        displayJobsByCategory(jobs, header, selectedCategory);
    }
    
    private int findCategoryIndex(String[] header) {
        for (int i = 0; i < header.length; i++) {
            if ("jobCategory".equalsIgnoreCase(header[i].trim())) {
                return i;
            }
        }
        return -1;
    }
    
    private void displayJobsByCategory(List<String[]> jobs, String[] header, String category) {
        System.out.println("\n==================================");
        System.out.println("        Jobs in Category: " + category);
        System.out.println("==================================");
        
        int idIndex = findColumnIndex(header, "jobId");
        int titleIndex = findColumnIndex(header, "jobTitle");
        int moduleIdIndex = findColumnIndex(header, "moduleId");
        int deadlineIndex = findColumnIndex(header, "deadline");
        int descIndex = findColumnIndex(header, "description");
        int catIndex = findCategoryIndex(header);
        
        boolean found = false;
        for (int i = 1; i < jobs.size(); i++) {
            String[] row = jobs.get(i);
            if (row.length > catIndex && row[catIndex].trim().equals(category)) {
                found = true;
                System.out.println("------------------------------------------");
                if (idIndex != -1) System.out.println("Job ID: " + row[idIndex]);
                if (titleIndex != -1) System.out.println("Title: " + row[titleIndex]);
                if (moduleIdIndex != -1) System.out.println("Module ID: " + row[moduleIdIndex]);
                if (deadlineIndex != -1) System.out.println("Deadline: " + row[deadlineIndex]);
                if (descIndex != -1) System.out.println("Description: " + row[descIndex]);
            }
        }
        
        if (!found) {
            System.out.println("------------------------------------------");
            System.out.println("No jobs found in this category");
            System.out.println("------------------------------------------");
        }
        
        System.out.println("\n==================================");
        System.out.print("Press Enter to return to main menu...");
        scanner.nextLine();
    }
    
    private int findColumnIndex(String[] header, String columnName) {
        for (int i = 0; i < header.length; i++) {
            if (columnName.equalsIgnoreCase(header[i].trim())) {
                return i;
            }
        }
        return -1;
    }
}
