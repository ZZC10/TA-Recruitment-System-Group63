package src.job;

import src.common.FileUtil;
import src.auth.AuthService;

import java.util.*;

public class JobService {

    private AuthService authService;

    public JobService() {
        this.authService = new AuthService();
    }

    public void applyForJob(String studentId, String jobId) {
        if (!authService.hasRole(studentId, "TA")) {
            System.out.println("Permission denied: Only TA can apply for jobs.");
            return;
        }

        if (!jobExists(jobId)) {
            System.out.println("Error: Job ID " + jobId + " does not exist.");
            return;
        }

        // TODO: Call ApplicationService.saveApplication() when available (Member 6)
        System.out.println("Application submitted successfully for Job ID: " + jobId);
    }

    public void viewApplicants(String jobId) {
        if (!jobExists(jobId)) {
            System.out.println("Error: Job ID " + jobId + " does not exist.");
            return;
        }

        String jobTitle = getJobTitleById(jobId);
        System.out.println("\n===== Applicants for Job: " + jobTitle + " =====");
        
        // TODO: Call ApplicationService.getApplicantsByJobId() when available (Member 6)
        System.out.println("(ApplicationService not yet implemented - pending Member 6)");
        
        System.out.println("=====================================");
    }

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
