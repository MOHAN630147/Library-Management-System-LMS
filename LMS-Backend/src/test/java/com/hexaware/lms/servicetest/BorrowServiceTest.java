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
import com.hexaware.lms.entity.BorrowRecord;
import com.hexaware.lms.entity.Category;
import com.hexaware.lms.entity.User;
import com.hexaware.lms.service.IBookService;
import com.hexaware.lms.service.IBorrowService;
import com.hexaware.lms.service.ICategoryService;
import com.hexaware.lms.service.IUserService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BorrowServiceTest {

    @Autowired
    private IBorrowService borrowService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    private static int userId;
    private static int bookId;
    private static int borrowId;

    // 1️ Create User + Book and Borrow
    @Test
    @Order(1)
    public void testBorrowBook() {

        // Create user
        User user = new User();
        user.setName("Mohan");
        user.setEmail("borrow@test.com");
        user.setPassword("123");
        userService.registerUser(user);

        userId = user.getUserId();

        // Create category
        Category category = new Category();
        category.setName("Science");
        categoryService.addCategory(category);

        // Create book
        Book book = new Book();
        book.setTitle("Physics");
        book.setAvailableQuantity(5);
        book.setCategory(category);
        bookService.addBook(book);

        bookId = book.getBookId();

        int result = borrowService.borrowBook(userId, bookId);

        assertEquals(1, result);
    }

    // 2️ Get Borrow History
    @Test
    @Order(2)
    public void testGetBorrowHistoryByUser() {

        List<BorrowRecord> records =
                borrowService.getBorrowHistoryByUser(userId);

        assertFalse(records.isEmpty());

        borrowId = records.get(0).getBorrowId();
    }

    // 3️ Calculate Fine (should be 0 initially)
    @Test
    @Order(3)
    public void testCalculateFine() {

        double fine =
                borrowService.calculateFine(borrowId);

        assertTrue(fine >= 0);
    }

    // 4️ Return Book
    @Test
    @Order(4)
    public void testReturnBook() {

        int result =
                borrowService.returnBook(borrowId);

        assertEquals(1, result);
    }

    // 5️ Get All Borrow Records
    @Test
    @Order(5)
    public void testGetAllBorrowRecords() {

        List<BorrowRecord> list =
                borrowService.getAllBorrowRecords();

        assertTrue(list.size() > 0);
    }
}