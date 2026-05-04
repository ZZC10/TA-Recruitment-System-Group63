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
     * 发布新职位 (Member 5 任务)
     * 1. 检查当前用户是否有权限 (MO/ADMIN)
     * 2. 验证关联的模块是否存在
     * 3. 将职位信息保存到 jobs.csv
     */
    public void publishJob() {
        System.out.println("\n--- Publish New Job Position ---");
        
        // 1. 权限检查 (Task 1)
        System.out.print("Enter your Student ID to verify permissions: ");
        String studentId = scanner.nextLine().trim();
        
        // 调用 Member 1 (AuthService) 的 getUserRole 方法
        String role = authService.getUserRole(studentId);
        if (!"MO".equals(role) && !"ADMIN".equals(role)) {
            System.out.println("Permission Denied: Only MO or ADMIN can publish jobs.");
            return;
        }

        // 2. 收集职位基本信息
        System.out.print("Enter Job ID (e.g., J004): ");
        String jobId = scanner.nextLine().trim();
        
        // 基础验证：检查 Job ID 是否已存在
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

        // 3. 验证模块是否存在 (Task 2)
        // 调用 Member 4 (ModuleService) 的 getModuleById 方法
        if (moduleService.getModuleById(moduleId) == null) {
            System.out.println("Error: Module ID " + moduleId + " does not exist. Please verify with ADMIN.");
            return;
        }

        System.out.print("Enter Deadline (YYYY-MM-DD): ");
        String deadline = scanner.nextLine().trim();
        
        System.out.print("Enter Description: ");
        String description = scanner.nextLine().trim();

        // 4. 保存到 jobs.csv (遵循 Standard 1.2 数据格式)
        String[] newJob = {jobId, jobTitle, jobCategory, moduleId, deadline, description};
        existingJobs.add(newJob);
        
        FileUtil.writeCSV("jobs.csv", existingJobs);
        System.out.println("Job published successfully!");
    }
}