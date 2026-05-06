import auth.AuthService;
import user.UserService;
import user.UploadCV;
import job.JobService;
import module.ModuleService;
import position.PositionService;
import application.ApplicationService;
import common.FileUtil;
import java.util.Scanner;
import java.util.List;

public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final UserService userService = new UserService();
    private static final UploadCV uploadCV = new UploadCV();
    private static final JobService jobService = new JobService();
    private static final ModuleService moduleService = new ModuleService();
    private static final PositionService positionService = new PositionService();
    private static final ApplicationService applicationService = new ApplicationService();
    
    private static String currentUser = null;
    private static String currentRole = null;
    
    public static void main(String[] args) {
        userService.setScanner(scanner);
        uploadCV.setScanner(scanner);
        moduleService.setScanner(scanner);
        positionService.setScanner(scanner);
        
        while (true) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    handleRegister();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    userService.updateInfo();
                    break;
                case 4:
                    uploadCV.upload();
                    break;
                case 5:
                    jobService.showCategories();
                    break;
                case 6:
                    handleApplyForJob();
                    break;
                case 7:
                    handleViewApplications();
                    break;
                case 8:
                    positionService.publishJob();
                    break;
                case 9:
                    handleViewApplicants();
                    break;
                case 10:
                    handleApproveApplication();
                    break;
                case 11:
                    moduleService.createModule();
                    break;
                case 12:
                    handleViewTAWorkload();
                    break;
                case 0:
                    System.out.println("Thank you for using. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option, please select again!");
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n===== EBU6304 Group63 Student Job Management System V1 =====");
        System.out.println("=== User Authentication ===");
        System.out.println("1. User Registration");
        System.out.println("2. User Login");
        System.out.println("=== TA Functions ===");
        System.out.println("3. Update Personal Info");
        System.out.println("4. Upload/Update CV");
        System.out.println("5. Browse Job Categories");
        System.out.println("6. Apply for Job");
        System.out.println("7. View My Applications");
        System.out.println("=== MO Functions ===");
        System.out.println("8. Publish Job Position");
        System.out.println("9. View Applicants");
        System.out.println("10. Approve/Reject Applications");
        System.out.println("=== Admin Functions ===");
        System.out.println("11. Module Management");
        System.out.println("12. View TA Workload");
        System.out.println("=== System ===");
        System.out.println("0. Exit System");
        if (currentUser != null) {
            System.out.println("Current User: " + currentUser + " (" + currentRole + ")");
        }
        System.out.print("Please select an operation: ");
    }
    
    private static void handleRegister() {
        System.out.println("\n===== User Registration =====");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        System.out.print("Enter Password (6+ chars with letters and digits): ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Major: ");
        String major = scanner.nextLine().trim();
        System.out.print("Enter Role (TA/MO/ADMIN): ");
        String role = scanner.nextLine().trim().toUpperCase();
        
        if (!role.equals("TA") && !role.equals("MO") && !role.equals("ADMIN")) {
            System.out.println("Invalid role! Defaulting to TA.");
            role = "TA";
        }
        
        // Build user data
        List<String[]> users = FileUtil.readCSV("users.csv");
        String[] newUser = {studentId, password, name, email, major, "N/A", "N/A", "N/A", role};
        users.add(newUser);
        FileUtil.writeCSV("users.csv", users);
        System.out.println("Registration successful! Welcome " + name);
    }
    
    private static void handleLogin() {
        System.out.println("\n===== User Login =====");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        List<String[]> users = FileUtil.readCSV("users.csv");
        for (String[] user : users) {
            if (user.length >= 2 && user[0].equals(studentId) && user[1].equals(password)) {
                String name = user.length >= 3 ? user[2] : studentId;
                currentUser = studentId;
                currentRole = user.length > 8 ? user[8] : "TA";
                System.out.println("Login successful! Welcome back, " + name);
                System.out.println("Your role: " + currentRole);
                return;
            }
        }
        System.out.println("Login failed: Invalid student ID or password");
    }
    
    private static void handleApplyForJob() {
        if (currentUser == null) {
            System.out.println("Please login first!");
            return;
        }
        if (!"TA".equals(currentRole)) {
            System.out.println("Permission denied: Only TA can apply for jobs.");
            return;
        }
        
        System.out.println("\n===== Apply for Job =====");
        jobService.showCategories();
        System.out.print("Enter Job ID to apply: ");
        String jobId = scanner.nextLine().trim();
        
        boolean success = applicationService.applyForJob(currentUser, jobId);
        if (success) {
            System.out.println("Application submitted successfully!");
        }
    }
    
    private static void handleViewApplications() {
        if (currentUser == null) {
            System.out.println("Please login first!");
            return;
        }
        
        System.out.println("\n===== My Applications =====");
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        boolean found = false;
        
        System.out.printf("%-8s %-30s %-12s %-12s\n", "Job ID", "Job Title", "Status", "Apply Date");
        System.out.println("--------------------------------------------------------------");
        
        for (String[] app : applications) {
            if (app.length > 0 && app[0].equals(currentUser)) {
                found = true;
                String jobId = app[1];
                String status = app.length > 2 ? app[2] : "PENDING";
                String applyDate = app.length > 3 ? app[3] : "N/A";
                String jobTitle = getJobTitle(jobId);
                
                System.out.printf("%-8s %-30s %-12s %-12s\n", jobId, jobTitle, status, applyDate);
            }
        }
        
        if (!found) {
            System.out.println("No applications found.");
        }
        System.out.println("==========================");
    }
    
    private static void handleViewApplicants() {
        if (currentUser == null) {
            System.out.println("Please login first!");
            return;
        }
        if (!"MO".equals(currentRole) && !"ADMIN".equals(currentRole)) {
            System.out.println("Permission denied: Only MO or ADMIN can view applicants.");
            return;
        }
        
        System.out.println("\n===== View Applicants =====");
        System.out.print("Enter Job ID: ");
        String jobId = scanner.nextLine().trim();
        jobService.viewApplicants(jobId);
    }
    
    private static void handleApproveApplication() {
        if (currentUser == null) {
            System.out.println("Please login first!");
            return;
        }
        if (!"MO".equals(currentRole) && !"ADMIN".equals(currentRole)) {
            System.out.println("Permission denied: Only MO or ADMIN can approve applications.");
            return;
        }
        
        System.out.println("\n===== Approve/Reject Application =====");
        System.out.print("Enter Job ID: ");
        String jobId = scanner.nextLine().trim();
        System.out.print("Enter Student ID of applicant: ");
        String studentId = scanner.nextLine().trim();
        System.out.print("Approve (Y/N): ");
        String choice = scanner.nextLine().trim().toUpperCase();
        
        boolean approved = "Y".equals(choice) || "YES".equals(choice);
        boolean success = applicationService.approveApplication(studentId, jobId, approved);
        
        if (success) {
            System.out.println("Application " + (approved ? "approved" : "rejected") + " successfully!");
        } else {
            System.out.println("Failed to process application.");
        }
    }
    
    private static void handleViewTAWorkload() {
        if (currentUser == null) {
            System.out.println("Please login first!");
            return;
        }
        if (!"ADMIN".equals(currentRole)) {
            System.out.println("Permission denied: Only ADMIN can view TA workload.");
            return;
        }
        
        System.out.println("\n===== TA Workload Statistics =====");
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        List<String[]> users = FileUtil.readCSV("users.csv");
        
        // Count accepted applications per TA
        java.util.Map<String, Integer> workload = new java.util.HashMap<>();
        
        for (String[] app : applications) {
            if (app.length > 2 && "ACCEPTED".equals(app[2])) {
                String studentId = app[0];
                workload.put(studentId, workload.getOrDefault(studentId, 0) + 1);
            }
        }
        
        System.out.printf("%-12s %-20s %-15s\n", "Student ID", "Name", "Accepted Count");
        System.out.println("--------------------------------------------------");
        
        for (String[] user : users) {
            if (user.length > 0 && (user.length <= 8 || "TA".equals(user[8]))) {
                String studentId = user[0];
                String name = user.length > 2 ? user[2] : "N/A";
                int count = workload.getOrDefault(studentId, 0);
                System.out.printf("%-12s %-20s %-15d\n", studentId, name, count);
            }
        }
        System.out.println("==================================");
    }
    
    private static String getJobTitle(String jobId) {
        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
        for (String[] job : jobs) {
            if (job.length > 1 && job[0].equals(jobId)) {
                return job[1];
            }
        }
        return "Unknown";
    }
}