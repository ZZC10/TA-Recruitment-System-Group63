package position;

import common.FileUtil;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PositionService {
    
    private static final String JOBS_FILE = "jobs.csv";
    private static final String MODULES_FILE = "modules.csv";
    private final Scanner scanner = new Scanner(System.in);

    public void publishJob() {
        System.out.println("\n--- Publish New Job Position ---");
        
        System.out.print("Enter Job ID: ");
        String jobId = scanner.nextLine();
        
        // Check if Job ID already exists
        List<String[]> jobs = FileUtil.readCSV(JOBS_FILE);
        for (String[] job : jobs) {
            if (job.length > 0 && job[0].equals(jobId)) {
                System.out.println("Error: Job ID already exists!");
                return;
            }
        }
        
        System.out.print("Enter Job Title: ");
        String jobTitle = scanner.nextLine();
        
        System.out.print("Enter Job Category: ");
        String jobCategory = scanner.nextLine();
        
        System.out.print("Enter Module ID: ");
        String moduleId = scanner.nextLine();
        
        // Validate Module ID exists in modules.csv
        if (!isModuleIdValid(moduleId)) {
            System.out.println("Error: Invalid Module ID. Module does not exist.");
            return;
        }
        
        System.out.print("Enter Deadline (YYYY-MM-DD): ");
        String deadlineStr = scanner.nextLine();
        if (!isValidDeadline(deadlineStr)) {
            System.out.println("Error: Invalid deadline. Please use YYYY-MM-DD and ensure it is in the future.");
            return;
        }
        
        System.out.print("Enter Job Description: ");
        String description = scanner.nextLine();
        
        // Prepare new job data
        String[] newJob = {jobId, jobTitle, jobCategory, moduleId, deadlineStr, description};
        jobs.add(newJob);
        
        // Save back to jobs.csv
        FileUtil.writeCSV(JOBS_FILE, jobs);
        System.out.println("Successfully published job: " + jobTitle);
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