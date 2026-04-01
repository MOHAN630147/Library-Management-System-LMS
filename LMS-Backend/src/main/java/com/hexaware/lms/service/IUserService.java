package com.hexaware.lms.service;

import com.hexaware.lms.entity.User;
import java.util.List;

public interface IUserService {

    public int registerUser(User user);

    User loginUser(String email, String password);

    User getUserById(int userId);

    List<User> getAllUsers();

    public int updateUser(User user);

    public int deleteUser(int userId);
}
