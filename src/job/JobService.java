package job;

import common.FileUtil;
import auth.AuthService;
import application.ApplicationService;

import java.util.*;

public class JobService {

    private AuthService authService;
    private ApplicationService applicationService;

    public JobService() {
        this.authService = new AuthService();
        this.applicationService = new ApplicationService();
    }

    // Browse job categories
    public void showCategories() {
        System.out.println("\n===== Job Categories =====");
        
        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
        if (jobs.isEmpty()) {
            System.out.println("No jobs available.");
            System.out.println("==========================");
            return;
        }
        
        System.out.printf("%-8s %-30s %-15s %-12s\n", "Job ID", "Job Title", "Category", "Deadline");
        System.out.println("--------------------------------------------------------------");
        
        for (String[] job : jobs) {
            String jobId = job.length > 0 ? job[0] : "N/A";
            String title = job.length > 1 ? job[1] : "Unknown";
            String category = job.length > 2 ? job[2] : "Uncategorized";
            String deadline = job.length > 4 ? job[4] : "N/A";
            
            System.out.printf("%-8s %-30s %-15s %-12s\n", jobId, title, category, deadline);
        }
        
        System.out.println("==========================");
    }

        // Get all jobs as List (for GUI table)
    public List<String[]> getAllJobs() {
        return FileUtil.readCSV("jobs.csv");
    }

    // TA apply for a job
    public void applyForJob(String studentId, String jobId) {
        if (!authService.hasRole(studentId, "TA")) {
            System.out.println("Permission denied: Only TA can apply for jobs.");
            return;
        }

        if (!jobExists(jobId)) {
            System.out.println("Error: Job ID " + jobId + " does not exist.");
            return;
        }

        boolean success = applicationService.applyForJob(studentId, jobId);
        if (success) {
            System.out.println("Application submitted successfully for Job ID: " + jobId);
        } else {
            System.out.println("Failed to apply for this job.");
        }
    }

    // MO view applicants for a job
    public void viewApplicants(String jobId) {
        if (!jobExists(jobId)) {
            System.out.println("Error: Job ID " + jobId + " does not exist.");
            return;
        }

        String jobTitle = getJobTitleById(jobId);
        System.out.println("\n===== Applicants for Job: " + jobTitle + " =====");

        List<String[]> applicants = applicationService.getApplicantsByJob(jobId);
        if (applicants.isEmpty()) {
            System.out.println("No applicants yet.");
        } else {
            System.out.printf("%-12s %-10s %-12s\n", "Student ID", "Status", "Apply Date");
            System.out.println("------------------------------------------------");
            for (String[] app : applicants) {
                String status = app.length > 2 ? app[2] : "PENDING";
                String applyDate = app.length > 3 ? app[3] : "N/A";
                System.out.printf("%-12s %-10s %-12s\n", app[0], status, applyDate);
            }
        }
        System.out.println("=====================================");
    }

    // MO view jobs by module
    public void showJobsByModule(String moduleId) {
        if (!moduleExists(moduleId)) {
            System.out.println("Error: Module ID " + moduleId + " does not exist.");
            return;
        }

        String moduleName = getModuleNameById(moduleId);
        System.out.println("\n===== Jobs for Module: " + moduleName + " =====");

        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
        List<String[]> filteredJobs = new ArrayList<>();

        for (String[] job : jobs) {
            if (job.length > 3 && job[3].equals(moduleId)) {
                filteredJobs.add(job);
            }
        }

        if (filteredJobs.isEmpty()) {
            System.out.println("No jobs found for this module.");
        } else {
            System.out.printf("%-8s %-30s %-12s\n", "Job ID", "Job Title", "Deadline");
            System.out.println("------------------------------------------------------");
            for (String[] job : filteredJobs) {
                String deadline = job.length > 4 ? job[4] : "N/A";
                System.out.printf("%-8s %-30s %-12s\n", job[0], job[1], deadline);
            }
        }
        System.out.println("=====================================");
    }

    private boolean jobExists(String jobId) {
        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
        for (String[] job : jobs) {
            if (job.length > 0 && job[0].equals(jobId)) {
                return true;
            }
        }
        return false;
    }

    private String getJobTitleById(String jobId) {
        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
        for (String[] job : jobs) {
            if (job.length > 0 && job[0].equals(jobId)) {
                return job.length > 1 ? job[1] : "Unknown";
            }
        }
        return "Unknown Job";
    }

    private boolean moduleExists(String moduleId) {
        List<String[]> modules = FileUtil.readCSV("modules.csv");
        for (int i = 1; i < modules.size(); i++) {
            String[] module = modules.get(i);
            if (module.length > 0 && module[0].equals(moduleId)) {
                return true;
            }
        }
        return false;
    }

    private String getModuleNameById(String moduleId) {
        List<String[]> modules = FileUtil.readCSV("modules.csv");
        for (int i = 1; i < modules.size(); i++) {
            String[] module = modules.get(i);
            if (module.length > 0 && module[0].equals(moduleId)) {
                return module.length > 1 ? module[1] : "Unknown";
            }
        }
        return "Unknown Module";
    }
}
