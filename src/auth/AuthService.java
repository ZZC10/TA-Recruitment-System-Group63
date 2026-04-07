package auth;

import common.FileUtil;
import java.util.List;

public class AuthService {

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }

    public boolean register(String studentId, String password, String name) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("学号不能为空");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("密码不能为空");
            return false;
        }
        if (name == null || name.trim().isEmpty()) {
            System.out.println("姓名不能为空");
            return false;
        }

        if (!isValidPassword(password)) {
            System.out.println("密码格式错误：至少6位，包含字母和数字");
            return false;
        }

        List<String[]> users = FileUtil.readCSV("users.csv");

        for (String[] user : users) {
            if (user.length > 0 && user[0].equals(studentId)) {
                System.out.println("注册失败：学号 " + studentId + " 已存在");
                return false;
            }
        }

        String[] newUser = {studentId, password, name};
        users.add(newUser);

        boolean success = FileUtil.writeCSV("users.csv", users);

        if (success) {
            System.out.println("注册成功！欢迎 " + name);
        } else {
            System.out.println("注册失败：文件写入错误");
        }

        return success;
    }

    public boolean login(String studentId, String password) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("学号不能为空");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("密码不能为空");
            return false;
        }

        List<String[]> users = FileUtil.readCSV("users.csv");

        for (String[] user : users) {
            if (user.length >= 2 && user[0].equals(studentId) && user[1].equals(password)) {
                String name = user.length >= 3 ? user[2] : studentId;
                System.out.println("登录成功！欢迎回来，" + name);
                return true;
            }
        }

        System.out.println("登录失败：学号或密码错误");
        return false;
    }
    
    // 保留无参数方法以兼容Main.java
    public void register() {
        System.out.println("Register function called");
    }
    
    public void login() {
        System.out.println("Login function called");
    }
}