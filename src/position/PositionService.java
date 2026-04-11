package position;

import common.FileUtil;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PositionService {
    
    private static final String JOBS_FILE = "jobs.csv";
    private static final String MODULES_FILE = "modules.csv";
    private Scanner scanner;
    
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void publishJob() {
        System.out.println("\n==================================");
        System.out.println("      Publish New Job Position");
        System.out.println("==================================");
        System.out.println("Please enter the following job information:");
        System.out.println("------------------------------------------");
        System.out.println("1. Job ID (e.g., J001)");
        System.out.println("2. Job Title (e.g., Teaching Assistant)");
        System.out.println("3. Job Category (e.g., Teaching, Research)");
        System.out.println("4. Module ID (e.g., M001)");
        System.out.println("5. Deadline (YYYY-MM-DD, e.g., 2026-12-31)");
        System.out.println("6. Job Description");
        System.out.println("------------------------------------------");
        
        System.out.print("1. Job ID (e.g., J001): ");
        String jobId = scanner.nextLine();
        
        // Check if Job ID already exists
        List<String[]> jobs = FileUtil.readCSV(JOBS_FILE);
        for (String[] job : jobs) {
            if (job.length > 0 && job[0].equals(jobId)) {
                System.out.println("------------------------------------------");
                System.out.println("Error: Job ID already exists!");
                System.out.println("------------------------------------------");
                System.out.println("==================================");
                return;
            }
        }
        
        System.out.print("2. Job Title (e.g., Teaching Assistant): ");
        String jobTitle = scanner.nextLine();
        
        System.out.print("3. Job Category (e.g., Teaching, Research): ");
        String jobCategory = scanner.nextLine();
        
        System.out.print("4. Module ID (e.g., M001): ");
        String moduleId = scanner.nextLine();
        
        // Validate Module ID exists in modules.csv
        if (!isModuleIdValid(moduleId)) {
            System.out.println("------------------------------------------");
            System.out.println("Error: Invalid Module ID. Module does not exist.");
            System.out.println("Please create the module first.");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        System.out.print("5. Deadline (YYYY-MM-DD, e.g., 2026-12-31): ");
        String deadlineStr = scanner.nextLine();
        if (!isValidDeadline(deadlineStr)) {
            System.out.println("------------------------------------------");
            System.out.println("Error: Invalid deadline.");
            System.out.println("Please use YYYY-MM-DD format and ensure it is in the future.");
            System.out.println("------------------------------------------");
            System.out.println("==================================");
            return;
        }
        
        System.out.print("6. Job Description: ");
        String description = scanner.nextLine();
        
        // Prepare new job data
        String[] newJob = {jobId, jobTitle, jobCategory, moduleId, deadlineStr, description};
        jobs.add(newJob);
        
        // Save back to jobs.csv
        FileUtil.writeCSV(JOBS_FILE, jobs);
        System.out.println("------------------------------------------");
        System.out.println("Successfully published job: " + jobTitle);
        System.out.println("Job ID: " + jobId);
        System.out.println("Category: " + jobCategory);
        System.out.println("Module: " + moduleId);
        System.out.println("Deadline: " + deadlineStr);
        System.out.println("------------------------------------------");
        System.out.println("==================================");
    }
    
    private boolean isModuleIdValid(String moduleId) {
        List<String[]> modules = FileUtil.readCSV(MODULES_FILE);
        for (String[] module : modules) {
            if (module.length > 0 && module[0].equals(moduleId)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isValidDeadline(String deadlineStr) {
        try {
            LocalDate deadline = LocalDate.parse(deadlineStr);
            return deadline.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}