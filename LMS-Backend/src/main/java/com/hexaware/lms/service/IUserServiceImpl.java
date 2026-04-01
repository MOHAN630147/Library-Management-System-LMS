package com.hexaware.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.lms.entity.User;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.UserRepository;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    
    @Override
    public int registerUser(User user) {

        userRepository.save(user);
        return 1;
    }

 
    @Override
    public User loginUser(String email, String password) {

        return userRepository.findByEmailAndPassword(email, password);
    }

   
    @Override
    public User getUserById(int userId) {

    	return userRepository.findById(userId)
    	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    
    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    
    @Override
    public int updateUser(User user) {

        if (userRepository.existsById(user.getUserId())) {
            userRepository.save(user);
            return 1;
        }

        return 0;
    }

   
    @Override
    public int deleteUser(int userId) {

        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return 1;
        }

        return 0;
    }
}