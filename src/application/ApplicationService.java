package application;

import common.FileUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicationService {
    
    private Scanner scanner;
    
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public boolean applyForJob(String studentId, String jobId) {
        System.out.println("Applying for job: " + jobId);
        
        // Check if job exists
        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
        if (jobs == null || jobs.size() <= 1) {
            System.out.println("Job not found: " + jobId);
            return false;
        }
        
        boolean jobExists = false;
        for (int i = 1; i < jobs.size(); i++) {
            String[] job = jobs.get(i);
            if (job.length > 0 && job[0].equals(jobId)) {
                jobExists = true;
                break;
            }
        }
        
        if (!jobExists) {
            System.out.println("Job not found: " + jobId);
            return false;
        }
        
        // Check if user exists
        List<String[]> users = FileUtil.readCSV("users.csv");
        if (users == null || users.size() <= 1) {
            System.out.println("User not found: " + studentId);
            return false;
        }
        
        boolean userExists = false;
        for (int i = 1; i < users.size(); i++) {
            String[] user = users.get(i);
            if (user.length > 0 && user[0].equals(studentId)) {
                userExists = true;
                break;
            }
        }
        
        if (!userExists) {
            System.out.println("User not found: " + studentId);
            return false;
        }
        
        // Check if application already exists
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        if (applications == null) {
            applications = new ArrayList<>();
            applications.add(new String[]{"studentId", "jobId", "status", "applyDate", "decisionDate"});
        }
        
        for (int i = 1; i < applications.size(); i++) {
            String[] app = applications.get(i);
            if (app.length >= 2 && app[0].equals(studentId) && app[1].equals(jobId)) {
                System.out.println("Application already exists for this job");
                return false;
            }
        }
        
        // Create new application
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String[] newApplication = {
            studentId,
            jobId,
            "PENDING",
            currentDate,
            ""
        };
        
        applications.add(newApplication);
        
        FileUtil.writeCSV("applications.csv", applications);
        System.out.println("Application submitted successfully!");
        return true;
    }
    
    public String getApplicationStatus(String studentId, String jobId) {
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        if (applications == null || applications.size() <= 1) {
            System.out.println("No applications found");
            return "NOT_FOUND";
        }
        
        for (int i = 1; i < applications.size(); i++) {
            String[] app = applications.get(i);
            if (app.length >= 3 && app[0].equals(studentId) && app[1].equals(jobId)) {
                System.out.println("Application status: " + app[2]);
                return app[2];
            }
        }
        
        System.out.println("Application not found");
        return "NOT_FOUND";
    }
    
    public List<String[]> getApplicantsByJob(String jobId) {
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        List<String[]> applicants = new ArrayList<>();
        
        if (applications == null || applications.size() <= 1) {
            System.out.println("No applications found for job: " + jobId);
            return applicants;
        }
        
        for (int i = 1; i < applications.size(); i++) {
            String[] app = applications.get(i);
            if (app.length >= 2 && app[1].equals(jobId)) {
                applicants.add(app);
            }
        }
        
        System.out.println("Found " + applicants.size() + " applicants for job: " + jobId);
        return applicants;
    }
    
    public boolean approveApplication(String studentId, String jobId, boolean approved) {
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        if (applications == null || applications.size() <= 1) {
            System.out.println("No applications found");
            return false;
        }
        
        boolean found = false;
        for (int i = 1; i < applications.size(); i++) {
            String[] app = applications.get(i);
            if (app.length >= 5 && app[0].equals(studentId) && app[1].equals(jobId)) {
                if (!app[2].equals("PENDING")) {
                    System.out.println("Application already processed");
                    return false;
                }
                
                app[2] = approved ? "ACCEPTED" : "REJECTED";
                app[4] = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("Application not found");
            return false;
        }
        
        FileUtil.writeCSV("applications.csv", applications);
        System.out.println("Application " + (approved ? "approved" : "rejected") + " successfully");
        return true;
    }
    
    public int getTAWorkload(String studentId) {
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        int workload = 0;
        
        if (applications == null || applications.size() <= 1) {
            System.out.println("No applications found for TA: " + studentId);
            return 0;
        }
        
        for (int i = 1; i < applications.size(); i++) {
            String[] app = applications.get(i);
            if (app.length >= 3 && app[0].equals(studentId) && app[2].equals("ACCEPTED")) {
                workload++;
            }
        }
        
        System.out.println("TA " + studentId + " has " + workload + " accepted applications");
        return workload;
    }
    
    public void showApplicationsMenu(String studentId, String role) {
        while (true) {
            System.out.println("\n=== Application Menu ===");
            
            if (role.equals("TA")) {
                System.out.println("1. Apply for Job");
                System.out.println("2. Check Application Status");
                System.out.println("0. Back");
            } else if (role.equals("MO")) {
                System.out.println("1. View Job Applicants");
                System.out.println("2. Approve/Reject Application");
                System.out.println("0. Back");
            } else if (role.equals("ADMIN")) {
                System.out.println("1. View TA Workload");
                System.out.println("0. Back");
            }
            
            System.out.print("Enter your choice: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Invalid input, please enter a number");
                continue;
            }
            
            switch (choice) {
                case 1:
                    if (role.equals("TA")) {
                        applyForJobMenu(studentId);
                    } else if (role.equals("MO")) {
                        viewApplicantsMenu();
                    } else if (role.equals("ADMIN")) {
                        viewTAWorkloadMenu();
                    }
                    break;
                case 2:
                    if (role.equals("TA")) {
                        checkStatusMenu(studentId);
                    } else if (role.equals("MO")) {
                        approveApplicationMenu();
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option, please select again!");
            }
        }
    }
    
    private void applyForJobMenu(String studentId) {
        System.out.print("Enter job ID: ");
        String jobId = scanner.nextLine();
        applyForJob(studentId, jobId);
    }
    
    private void checkStatusMenu(String studentId) {
        System.out.print("Enter job ID: ");
        String jobId = scanner.nextLine();
        getApplicationStatus(studentId, jobId);
    }
    
    private void viewApplicantsMenu() {
        System.out.print("Enter job ID: ");
        String jobId = scanner.nextLine();
        List<String[]> applicants = getApplicantsByJob(jobId);
        
        for (String[] applicant : applicants) {
            System.out.println("Student ID: " + applicant[0] + ", Status: " + applicant[2] + ", Apply Date: " + applicant[3]);
        }
    }
    
    private void approveApplicationMenu() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter job ID: ");
        String jobId = scanner.nextLine();
        System.out.print("Approve? (yes/no): ");
        String answer = scanner.nextLine();
        boolean approved = answer.equalsIgnoreCase("yes");
        approveApplication(studentId, jobId, approved);
    }
    
    private void viewTAWorkloadMenu() {
        System.out.print("Enter TA student ID: ");
        String studentId = scanner.nextLine();
        getTAWorkload(studentId);
    }
}