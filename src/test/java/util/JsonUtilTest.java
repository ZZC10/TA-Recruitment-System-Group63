package com.bupt.ta.util;

import com.bupt.ta.model.User;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Test class for JsonUtil.
 * No testing framework (like JUnit) is required.
 */
public class JsonUtilTest {

    public static void main(String[] args) {
        System.out.println("=== STARTING JSON UTIL TEST ===");

        // 1. Add Test User
        String testUsername = "testUser_" + System.currentTimeMillis();
        User testUser = new User(
            testUsername,
            "password123",
            "test@bupt.edu.cn",
            "ta",
            "pending",
            LocalDateTime.now()
        );

        System.out.println("Adding user: " + testUsername);
        JsonUtil.addUser(testUser);

        // 2. Query User
        System.out.println("Querying user: " + testUsername);
        User foundUser = JsonUtil.findByUsername(testUsername);

        if (foundUser != null && foundUser.getUsername().equals(testUsername)) {
            System.out.println("SUCCESS: User found correctly.");
            System.out.println("Details: " + foundUser);
        } else {
            System.err.println("FAILURE: User not found or incorrect data.");
            return;
        }

        // 3. Modify User Status
        System.out.println("Modifying user status to 'active'...");
        List<User> allUsers = JsonUtil.loadUsers();
        boolean modified = false;
        for (User u : allUsers) {
            if (u.getUsername().equals(testUsername)) {
                u.setStatus("active");
                modified = true;
                break;
            }
        }

        if (modified) {
            JsonUtil.saveUsers(allUsers);
            User updatedUser = JsonUtil.findByUsername(testUsername);
            if ("active".equals(updatedUser.getStatus())) {
                System.out.println("SUCCESS: Status updated to 'active'.");
            } else {
                System.err.println("FAILURE: Status update failed.");
            }
        } else {
            System.err.println("FAILURE: User not found in list for modification.");
        }

        System.out.println("=== TEST COMPLETED SUCCESSFULLY ===");
    }
}
