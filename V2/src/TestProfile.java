import auth.AuthService;
import user.UserService;

public class TestProfile {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        UserService userService = new UserService();
        
        String userId = "2023001";
        
        String name = authService.getUserName(userId);
        String role = authService.getUserRole(userId);
        String[] user = userService.getUserByStudentId(userId);
        
        System.out.println("userId: " + userId);
        System.out.println("name: " + name);
        System.out.println("role: " + role);
        
        if (user != null) {
            System.out.println("user array length: " + user.length);
            for (int i = 0; i < user.length; i++) {
                System.out.println("user[" + i + "]: " + user[i]);
            }
        } else {
            System.out.println("user is null");
        }
    }
}