package com.hexaware.lms.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.lms.dto.LoginRequestDTO;
import com.hexaware.lms.dto.UserDTO;
import com.hexaware.lms.entity.User;
import com.hexaware.lms.security.JwtUtil;
import com.hexaware.lms.service.IUserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Map<String,String> registerUser(@Valid @RequestBody UserDTO dto) {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNo(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setProfilePic("default.png");

        int result = userService.registerUser(user);

        Map<String,String> response = new HashMap<>();

        if(result == 1)
            response.put("message","User Registered Successfully");
        else
            response.put("message","User Registration Failed");

        return response;
    }

    @PostMapping("/userlogin")
    public User loginUser(@RequestParam String email,
                          @RequestParam String password) {

        return userService.loginUser(email, password);
    }

    @GetMapping("/getby/{id}")
    public User getUserById(@PathVariable int id) {

        return userService.getUserById(id);
    }

    @GetMapping("/getall")
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }

    @PutMapping("/update")
    public Map<String,String> updateUser(@RequestBody UserDTO dto){

        User user = new User();

        user.setUserId(dto.getUserId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNo(dto.getPhoneNumber());
        user.setProfilePic(dto.getRole());

        int result = userService.updateUser(user);

        Map<String,String> response = new HashMap<>();

        if(result == 1)
            response.put("message","User Updated Successfully");
        else
            response.put("message","User Update Failed");

        return response;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String,String> deleteUser(@PathVariable int id){

        int result = userService.deleteUser(id);

        Map<String,String> response = new HashMap<>();

        if(result == 1)
            response.put("message","User Deleted Successfully");
        else
            response.put("message","User Deletion Failed");

        return response;
    }
    
    @PostMapping("/login")
    public Map<String,String> login(@RequestBody LoginRequestDTO request){

        User user =
                userService.loginUser(request.getEmail(),
                                      request.getPassword());

        Map<String,String> response = new HashMap<>();

        if(user != null){
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getUserId());
            response.put("token", token);
            response.put("role", user.getRole());
            response.put("userId", String.valueOf(user.getUserId()));
            response.put("name", user.getName());
        }
        else{
            response.put("message","Invalid Credentials");
        }

        return response;
    }
}