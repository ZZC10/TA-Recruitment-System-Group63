import auth.AuthService;
import user.UserService;
import user.UploadCV;
import job.JobService;
import module.ModuleService;
import position.PositionService;
import application.ApplicationService;
import java.util.Scanner;

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
    
    static {
        // Pass scanner to all service classes
        authService.setScanner(scanner);
        userService.setScanner(scanner);
        uploadCV.setScanner(scanner);
        jobService.setScanner(scanner);
        moduleService.setScanner(scanner);
        positionService.setScanner(scanner);
        applicationService.setScanner(scanner);
    }
    
    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                displayMainMenu();
                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    scanner.nextLine(); // Clear the invalid input
                    System.out.println("Invalid input, please enter a number");
                    continue;
                }
                
                switch (choice) {
                    case 1:
                        authService.register();
                        break;
                    case 2:
                        login();
                        break;
                    case 0:
                        System.out.println("Thank you for using. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option, please select again!");
                }
            } else {
                showRoleMenu();
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n===== EBU6304 Group63 Student Job Management System V1 =====");
        System.out.println("1. User Registration");
        System.out.println("2. User Login");
        System.out.println("0. Exit System");
        System.out.print("Please select an operation: ");
    }
    
    private static void login() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        boolean success = authService.login(studentId, password);
        if (success) {
            currentUser = studentId;
            currentRole = authService.getUserRole(studentId);
            System.out.println("Login successful! Welcome " + studentId + " (" + currentRole + ")");
        }
    }
    
    private static void showRoleMenu() {
        if (currentRole == null) {
            currentUser = null;
            return;
        }
        
        switch (currentRole) {
            case "TA":
                showTAMenu();
                break;
            case "MO":
                showMOMenu();
                break;
            case "ADMIN":
                showAdminMenu();
                break;
            default:
                System.out.println("Unknown role, logging out...");
                currentUser = null;
                currentRole = null;
        }
    }
    
    private static void showTAMenu() {
        while (currentUser != null && currentRole.equals("TA")) {
            System.out.println("\n=== TA Menu ===");
            System.out.println("1. Update Personal Info");
            System.out.println("2. Browse Job Categories");
            System.out.println("3. Apply for Job");
            System.out.println("4. Check Application Status");
            System.out.println("5. Upload/Update CV");
            System.out.println("0. Logout");
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
                    userService.updateInfo();
                    break;
                case 2:
                    jobService.showCategories();
                    break;
                case 3:
                case 4:
                    applicationService.showApplicationsMenu(currentUser, currentRole);
                    break;
                case 5:
                    uploadCV.upload();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    currentUser = null;
                    currentRole = null;
                    break;
                default:
                    System.out.println("Invalid option, please select again!");
            }
        }
    }
    
    private static void showMOMenu() {
        while (currentUser != null && currentRole.equals("MO")) {
            System.out.println("\n=== MO Menu ===");
            System.out.println("1. Update Personal Info");
            System.out.println("2. Browse Job Categories");
            System.out.println("3. Publish Job");
            System.out.println("4. View Job Applicants");
            System.out.println("5. Approve/Reject Application");
            System.out.println("0. Logout");
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
                    userService.updateInfo();
                    break;
                case 2:
                    jobService.showCategories();
                    break;
                case 3:
                    positionService.publishJob();
                    break;
                case 4:
                case 5:
                    applicationService.showApplicationsMenu(currentUser, currentRole);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    currentUser = null;
                    currentRole = null;
                    break;
                default:
                    System.out.println("Invalid option, please select again!");
            }
        }
    }
    
    private static void showAdminMenu() {
        while (currentUser != null && currentRole.equals("ADMIN")) {
            System.out.println("\n=== ADMIN Menu ===");
            System.out.println("1. Update Personal Info");
            System.out.println("2. Browse Job Categories");
            System.out.println("3. Create Module");
            System.out.println("4. Publish Job");
            System.out.println("5. View TA Workload");
            System.out.println("0. Logout");
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
                    userService.updateInfo();
                    break;
                case 2:
                    jobService.showCategories();
                    break;
                case 3:
                    moduleService.createModule();
                    break;
                case 4:
                    positionService.publishJob();
                    break;
                case 5:
                    applicationService.showApplicationsMenu(currentUser, currentRole);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    currentUser = null;
                    currentRole = null;
                    break;
                default:
                    System.out.println("Invalid option, please select again!");
            }
        }
    }
}