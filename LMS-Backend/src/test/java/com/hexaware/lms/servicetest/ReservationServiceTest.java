package com.hexaware.lms.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.Category;
import com.hexaware.lms.entity.Reservation;
import com.hexaware.lms.entity.User;
import com.hexaware.lms.service.IBookService;
import com.hexaware.lms.service.ICategoryService;
import com.hexaware.lms.service.IReservationService;
import com.hexaware.lms.service.IUserService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationServiceTest {

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    private static int userId;
    private static long bookId;
    private static int reservationId;

    // 1️ Create User + Book and Reserve
    @Test
    @Order(1)
    void testReserveBook() {

        // Create user
        User user = new User();
        user.setName("ReservationUser");
        user.setEmail("reserve@test.com");
        user.setPassword("123");
        userService.registerUser(user);

        userId = user.getUserId();

        // Create category
        Category category = new Category();
        category.setName("Fiction");
        categoryService.addCategory(category);

        // Create book
        Book book = new Book();
        book.setTitle("Harry Potter");
        book.setAvailableQuantity(3);
        book.setCategory(category);
        bookService.addBook(book);

        bookId = book.getBookId();

        int result = reservationService.reserveBook(userId, bookId);

        assertEquals(1, result);
    }

    // 2️ Get Reservations By User
    @Test
    @Order(2)
    void testGetReservationsByUser() {

        List<Reservation> list =
                reservationService.getReservationsByUser(userId);

        assertFalse(list.isEmpty());

        reservationId = list.get(0).getReservationId();
    }

    // 3️ Get All Reservations
    @Test
    @Order(3)
    void testGetAllReservations() {

        List<Reservation> list =
                reservationService.getAllReservations();

        assertTrue(list.size() > 0);
    }

    // 4️ Cancel Reservation
    @Test
    @Order(4)
    void testCancelReservation() {

        int result =
                reservationService.cancelReservation(reservationId);

        assertEquals(1, result);
    }
}