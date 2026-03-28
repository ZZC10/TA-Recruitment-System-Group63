package com.bupt.ta.service;

import com.bupt.ta.dao.UserDao;
import com.bupt.ta.model.User;

import java.time.LocalDateTime;

public class UserService {
    private UserDao userDao = new UserDao();

    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean register(String username, String password, String email, String role) {
        if (userDao.findByUsername(username) != null) {
            return false;
        }
        User newUser = new User(username, password, email, role, "active", LocalDateTime.now());
        userDao.save(newUser);
        return true;
    }
}
