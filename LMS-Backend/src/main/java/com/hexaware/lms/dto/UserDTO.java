package com.hexaware.lms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private int userId;
    
    @NotBlank(message="Name cannot be empty")
    private String name;
    
    @Email(message="Invalid email format")
    private String email;
    
    @Size(min=6,message="Password must be at least 6 characters")
    private String password;
    
    @NotBlank(message="Phone number required")
    @Size(max=10,message="please enter a valid 10 digit phone number")
    private String phoneNumber;
    private String role;

    public UserDTO() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
}