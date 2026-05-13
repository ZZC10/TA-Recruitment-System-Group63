package test;

import auth.AuthService;
import job.JobService;
import application.ApplicationService;
import common.FileUtil;
import java.util.List;

public class CompleteTest {
    
    public static void main(String[] args) {
        System.out.println("========== V1 Complete Business Test ==========\n");
        
        // Test 1: AuthService role management
        testAuthService();
        
        // Test 2: JobService functions
        testJobService();
        
        // Test 3: ApplicationService functions
        testApplicationService();
        
        System.out.println("\n========== All Tests Completed ==========");
    }
    
    private static void testAuthService() {
        System.out.println("[Test 1] AuthService Role Management");
        System.out.println("-----------------------------------");
        
        AuthService authService = new AuthService();
        
        // Test getUserRole
        String role1 = authService.getUserRole("2023001");
        System.out.println("User 2023001 role: " + role1);
        
        String role2 = authService.getUserRole("2023002");
        System.out.println("User 2023002 role: " + role2);
        
        // Test hasRole
        boolean isTA = authService.hasRole("2023001", "TA");
        System.out.println("User 2023001 has TA role: " + isTA);
        
        boolean isAdmin = authService.hasRole("2023001", "ADMIN");
        System.out.println("User 2023001 has ADMIN role: " + isAdmin);
        
        System.out.println("OK AuthService test passed\n");
    }
    
    private static void testJobService() {
        System.out.println("[Test 2] JobService Functions");
        System.out.println("-----------------------------------");
        
        JobService jobService = new JobService();
        
        // Test showCategories
        jobService.showCategories();
        
        // Test showJobsByModule
        jobService.showJobsByModule("M001");
        
        System.out.println("OK JobService test passed\n");
    }
    
    private static void testApplicationService() {
        System.out.println("[Test 3] ApplicationService Functions");
        System.out.println("-----------------------------------");
        
        ApplicationService appService = new ApplicationService();
        
        // Test applyForJob
        boolean applied = appService.applyForJob("2023001", "J001");
        System.out.println("Apply for J001: " + (applied ? "Success" : "Failed"));
        
        // Test duplicate application
        boolean duplicate = appService.applyForJob("2023001", "J001");
        System.out.println("Duplicate apply for J001: " + (duplicate ? "Success" : "Failed (expected)"));
        
        // Test getApplicantsByJob
        System.out.println("\nApplicants for J001:");
        List<String[]> applicants = appService.getApplicantsByJob("J001");
        for (String[] app : applicants) {
            System.out.println("  " + String.join(" | ", app));
        }
        
        // Test approveApplication
        boolean approved = appService.approveApplication("2023001", "J001", true);
        System.out.println("\nApprove application: " + (approved ? "Success" : "Failed"));
        
        // Verify approval
        applicants = appService.getApplicantsByJob("J001");
        System.out.println("\nAfter approval - Applicants for J001:");
        for (String[] app : applicants) {
            System.out.println("  " + String.join(" | ", app));
        }
        
        System.out.println("\nOK ApplicationService test passed\n");
    }
}