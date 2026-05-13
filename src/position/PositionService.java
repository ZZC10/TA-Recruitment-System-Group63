package position;

import common.FileUtil;
import module.ModuleService;
import auth.AuthService;
import java.util.List;
import java.util.Scanner;

public class PositionService {
    private Scanner scanner;
    private final ModuleService moduleService = new ModuleService();
    private final AuthService authService = new AuthService();

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Publish a new job position (Member 5 Task)
     * 1. Check if current user has permission (MO/ADMIN)
     * 2. Verify associated module exists
     * 3. Save job information to jobs.csv
     */
    public void publishJob() {
        System.out.println("\n--- Publish New Job Position ---");
        
        // 1. Permission check (Task 1)
        System.out.print("Enter your Student ID to verify permissions: ");
        String studentId = scanner.nextLine().trim();
        
        // Call Member 1 (AuthService) getUserRole method
        String role = authService.getUserRole(studentId);
        if (!"MO".equals(role) && !"ADMIN".equals(role)) {
            System.out.println("Permission Denied: Only MO or ADMIN can publish jobs.");
            return;
        }

        // 2. Collect job basic information
        System.out.print("Enter Job ID (e.g., J004): ");
        String jobId = scanner.nextLine().trim();
        
        // Basic validation: Check if Job ID already exists
        List<String[]> existingJobs = FileUtil.readCSV("jobs.csv");
        for (String[] job : existingJobs) {
            if (job.length > 0 && job[0].equals(jobId)) {
                System.out.println("Error: Job ID " + jobId + " already exists.");
                return;
            }
        }

        System.out.print("Enter Job Title: ");
        String jobTitle = scanner.nextLine().trim();
        
        System.out.print("Enter Job Category: ");
        String jobCategory = scanner.nextLine().trim();
        
        System.out.print("Enter Module ID (e.g., M001): ");
        String moduleId = scanner.nextLine().trim();

        // 3. Verify module exists (Task 2)
        // Call Member 4 (ModuleService) getModuleById method
        if (moduleService.getModuleById(moduleId) == null) {
            System.out.println("Error: Module ID " + moduleId + " does not exist. Please verify with ADMIN.");
            return;
        }

        System.out.print("Enter Deadline (YYYY-MM-DD): ");
        String deadline = scanner.nextLine().trim();
        
        System.out.print("Enter Description: ");
        String description = scanner.nextLine().trim();

        // 4. Save to jobs.csv (Follow Standard 1.2 data format)
        String[] newJob = {jobId, jobTitle, jobCategory, moduleId, deadline, description};
        existingJobs.add(newJob);
        
        FileUtil.writeCSV("jobs.csv", existingJobs);
        System.out.println("Job published successfully!");
    }
}