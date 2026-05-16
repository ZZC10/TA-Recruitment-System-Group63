import auth.AuthService;
import user.UserService;
import job.JobService;
import position.PositionService;

public class TestProfile {
    public static void main(String[] args) {
        System.out.println("========== TA Recruitment System - Test Programs (测试程序) ==========");
        
        // 1. Registration Test (注册测试)
        testRegistration();
        
        // 2. Login Test (登录测试)
        testLogin();
        
        // 3. Job Posting Test (岗位发布测试)
        testJobPosting();
        
        // 4. Job Application Test (岗位申请测试)
        testJobApplication();
        
        System.out.println("\n==================== All Tests Finished ====================");
    }

    private static void testRegistration() {
        System.out.println("\n[1] Registration Test (注册测试)");
        AuthService authService = new AuthService();
        String testUserId = "test_" + (System.currentTimeMillis() % 10000);
        // register(String userId, String password, String name, String role)
        boolean success = authService.register(testUserId, "Test123456", "Test User", "TA");
        System.out.println("Registration Result for " + testUserId + ": " + (success ? "SUCCESS" : "FAILED"));
    }

    private static void testLogin() {
        System.out.println("\n[2] Login Test (登录测试)");
        AuthService authService = new AuthService();
        // login(String userId, String password)
        // Using existing user 2023001
        boolean success = authService.login("2023001", "123456");
        System.out.println("Login Result (User 2023001): " + (success ? "SUCCESS" : "FAILED"));
    }

    private static void testJobPosting() {
        System.out.println("\n[3] Job Posting Test (岗位发布测试)");
        PositionService positionService = new PositionService();
        // PositionService needs currentUserId to check permissions
        positionService.setCurrentUserId("MO001"); 
        
        // publishJob(String moduleId, String title, int hours, String requirements, String description)
        boolean success = positionService.publishJob("M001", "Test Job " + (System.currentTimeMillis() % 1000), 10, "Java skills", "Help with testing");
        System.out.println("Job Posting Result: " + (success ? "SUCCESS" : "FAILED"));
    }

    private static void testJobApplication() {
        System.out.println("\n[4] Job Application Test (岗位申请测试)");
        JobService jobService = new JobService();
        // applyForJob(String userId, String jobId)
        // Using 2023001 (TA) and J001 (Job)
        boolean success = jobService.applyForJob("2023001", "J001");
        System.out.println("Job Application Result: " + (success ? "SUCCESS" : "FAILED"));
    }
}