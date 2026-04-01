package com.hexaware.lms.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.Category;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.service.IBookService;
import com.hexaware.lms.service.ICategoryService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceTest {

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    private static int savedBookId;

    @Test
    @Order(1)
    public void testAddBook() {

        Category category = new Category();
        category.setName("Tech");
        categoryService.addCategory(category);
        int categoryId = category.getCategoryId();
        
        Book book = new Book();
        book.setTitle("Spring Boot Guide");
        book.setAuthor("Mohan");
        book.setAvailableQuantity(10);
        book.setCategory(category);
        book.setBookPrice(1.0);
        int result = bookService.addBook(book);
        
        int bookId = book.getBookId();

        System.out.println("Saved Book ID: " + bookId);

        assertTrue(bookId>0);
    }

    @Test
    @Order(2)
    public void testGetBookById() {

        List<Book> books = bookService.getAllBooks();

        assertFalse(books.isEmpty());

        savedBookId = books.get(0).getBookId();

        Book book = bookService.getBookById(savedBookId);

        assertNotNull(book);
    }

    @Test
    @Order(3)
    public void testGetAllBooks() {

        List<Book> books = bookService.getAllBooks();

        assertTrue(books.size() > 0);
    }

    
    @Test
    @Order(4)
    public void testGetBooksByCategoryId() {

        List<Book> books = bookService.getAllBooks();

        int categoryId = books.get(0).getCategory().getCategoryId();

        List<Book> filtered =
                bookService.getBooksByCategoryid(categoryId);

        assertFalse(filtered.isEmpty());
    }

    
    @Test
    @Order(5)
    public void testUpdateBook() {

        Book book = bookService.getBookById(savedBookId);

        book.setTitle("Updated Title");

        int result = bookService.updateBook(book);

        assertEquals(1, result);

        Book updated = bookService.getBookById(savedBookId);

        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    @Order(6)
    public void testUpdateAvailableQuantity() {

        int result =
                bookService.updateAvailableQuantity(savedBookId, 5);

        assertEquals(1, result);

        Book book = bookService.getBookById(savedBookId);

        assertEquals(5, book.getAvailableQuantity());
    }

   
     @Test
    @Order(7)
    public void testDeleteBook() {

        int result =
                bookService.deleteBook(savedBookId);

        assertEquals(1, result);

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(savedBookId);
        });
    }
}
