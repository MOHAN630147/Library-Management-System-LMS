package com.hexaware.lms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.lms.dto.BookDTO;
import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.Category;
import com.hexaware.lms.service.IBookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private IBookService bookService;

    @PostMapping("/addbook")
    public Map<String, String> addBook(@Valid @RequestBody BookDTO bookDTO) {

        Book book = new Book();

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setAvailableQuantity(bookDTO.getAvailableQuantity());
        book.setDescription(bookDTO.getDescription());
        book.setEdition(bookDTO.getEdition());
        book.setIsbn(bookDTO.getIsbn());
        book.setLanguage(bookDTO.getLanguage());
        book.setPageCount(bookDTO.getPageCount());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setPublisher(bookDTO.getPublisher());
        book.setBookPrice(bookDTO.getBookPrice());

        Category category = new Category();
        category.setCategoryId(bookDTO.getCategoryId());

        book.setCategory(category);

        int result = bookService.addBook(book);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Book Added Successfully");
        else
            response.put("message", "Book Addition Failed");

        return response;
    }

    @GetMapping("/getbook/{bookId}")
    public Book getBookById(@PathVariable int bookId) {
        return bookService.getBookById(bookId);
    }

    @GetMapping("/getallbooks")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/bookbycategory/{categoryId}")
    public List<Book> getBooksByCategory(@PathVariable int categoryId) {
        return bookService.getBooksByCategoryid(categoryId);
    }

    @PutMapping("/updatebook")
    public Map<String, String> updateBook(@RequestBody BookDTO bookDTO) {

        Book book = new Book();

        book.setBookId(bookDTO.getBookId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setAvailableQuantity(bookDTO.getAvailableQuantity());
        book.setDescription(bookDTO.getDescription());
        book.setEdition(bookDTO.getEdition());
        book.setIsbn(bookDTO.getIsbn());
        book.setLanguage(bookDTO.getLanguage());
        book.setPageCount(bookDTO.getPageCount());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setPublisher(bookDTO.getPublisher());
        book.setBookPrice(bookDTO.getBookPrice());

        Category category = new Category();
        category.setCategoryId(bookDTO.getCategoryId());

        book.setCategory(category);

        int result = bookService.updateBook(book);
        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Book Updated Successfully");
        else
            response.put("message", "Book Update Failed");

        return response;
    }

    @PutMapping("/updateQuantity")
    public Map<String, String> updateAvailableQuantity(@RequestParam int bookId,
            @RequestParam int quantity) {
        int result = bookService.updateAvailableQuantity(bookId, quantity);
        Map<String, String> response = new HashMap<>();
        if (result == 1)
            response.put("message", "Quantity Updated Successfully");
        else
            response.put("message", "Quantity Update Failed");
        return response;
    }

    @DeleteMapping("/deletebook/{bookId}")
    public Map<String, String> deleteBook(@PathVariable int bookId) {

        int result = bookService.deleteBook(bookId);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Book Deleted Successfully");
        else
            response.put("message", "Book Deletion Failed");

        return response;
    }
}