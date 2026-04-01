package com.hexaware.lms.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hexaware.lms.entity.User;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.service.IUserService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    private static int userId;

    // 1️ Register User
    @Test
    @Order(1)
    void testRegisterUser() {

        User user = new User();
        user.setName("Mohan");
        user.setEmail("mohan@test.com");
        user.setPassword("12345");
        user.setPhoneNo("4455667788");
        user.setRole("admin");

        int result = userService.registerUser(user);

        assertEquals(1, result);

        userId = user.getUserId();
        assertTrue(userId > 0);
    }

    // 2️ Login User
    @Test
    @Order(2)
    void testLoginUser() {

        User user =
                userService.loginUser("mohan@test.com", "12345");

        assertNotNull(user);
        assertEquals("Mohan", user.getName());
    }

    // 3️ Get User By Id
    @Test
    @Order(3)
    void testGetUserById() {

        User user =
                userService.getUserById(userId);

        assertNotNull(user);
        assertEquals("mohan@test.com", user.getEmail());
    }

    // 4️ Update User
    @Test
    @Order(4)
    void testUpdateUser() {

        User user =
                userService.getUserById(userId);

        user.setName("Updated Mohan");

        int result =
                userService.updateUser(user);

        assertEquals(1, result);

        User updated =
                userService.getUserById(userId);

        assertEquals("Updated Mohan", updated.getName());
    }

    // 5️ Get All Users
    @Test
    @Order(5)
    void testGetAllUsers() {

        List<User> list =
                userService.getAllUsers();

        assertTrue(list.size() > 0);
    }

    @Test
    @Order(6)
    void testDeleteUser() {

        int result =
                userService.deleteUser(userId);

        assertEquals(1, result);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
    }
}