package com.bupt.ta.dao;

import com.bupt.ta.model.User;
import com.bupt.ta.util.JsonUtil;

import java.util.List;

public class UserDao {
    public List<User> findAll() {
        return JsonUtil.loadUsers();
    }

    public User findByUsername(String username) {
        return JsonUtil.findByUsername(username);
    }

    public void save(User user) {
        JsonUtil.addUser(user);
    }
}
