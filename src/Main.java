import auth.AuthService;
import user.UserService;
import user.UploadCV;
import job.JobService;
import module.ModuleService;
import position.PositionService;
import java.util.Scanner;

public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final UserService userService = new UserService();
    private static final UploadCV uploadCV = new UploadCV();
    private static final JobService jobService = new JobService();
    private static final ModuleService moduleService = new ModuleService();
    private static final PositionService positionService = new PositionService();
    
    static {
        // Pass scanner to all service classes
        authService.setScanner(scanner);
        userService.setScanner(scanner);
        uploadCV.setScanner(scanner);
        jobService.setScanner(scanner);
        moduleService.setScanner(scanner);
        positionService.setScanner(scanner);
    }
    
    public static void main(String[] args) {
        while (true) {
            displayMenu();
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
                    authService.login();
                    break;
                case 3:
                    userService.updateInfo();
                    break;
                case 4:
                    jobService.showCategories();
                    break;
                case 5:
                    moduleService.createModule();
                    break;
                case 6:
                    positionService.publishJob();
                    break;
                case 7:
                    uploadCV.upload();
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
    
    private static void displayMenu() {
        System.out.println("\n===== EBU6304 Group63 Student Job Management System V1 =====");
        System.out.println("1. User Registration");
        System.out.println("2. User Login");
        System.out.println("3. Update Personal Info");
        System.out.println("4. Browse Job Categories");
        System.out.println("5. Create Module");
        System.out.println("6. Publish Job");
        System.out.println("7. Upload/Update CV");
        System.out.println("0. Exit System");
        System.out.print("Please select an operation: ");
    }
}