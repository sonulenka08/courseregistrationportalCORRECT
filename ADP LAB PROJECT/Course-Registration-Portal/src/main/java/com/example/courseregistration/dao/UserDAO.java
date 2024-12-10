package com.example.courseregistration.dao;

import java.util.List;

import com.example.courseregistration.model.User;


public interface UserDAO {
    public void saveUser(User user);
    public User getUserByUsername(String email);
    public User getUserById(Long id);
    public List<User> getAllUsers();
	void updateUser(User user);
}
