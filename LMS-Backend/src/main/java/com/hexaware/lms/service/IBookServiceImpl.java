package com.hexaware.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.lms.entity.Book;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IBookServiceImpl implements IBookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public int addBook(Book book) {

    	log.info("Adding new book: {}", book.getTitle());
    	
        bookRepository.save(book);
        log.info("Book saved successfully");
        
        return 1;
    }

    @Override
    public Book getBookById(int bookId) {
    	
    	 log.info("Fetching book with id {}", bookId);

    	 return bookRepository.findById(bookId)
                 .orElseThrow(() -> {
                     log.error("Book not found with id {}", bookId);
                     return new ResourceNotFoundException("Book not found");
                 });
    }

    @Override
    public List<Book> getAllBooks() {
    	
    	log.info("Fetching all books");

        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksByCategoryid(int categoryId) {

        return bookRepository.findByCategory_CategoryId(categoryId);
    }

    @Override
	public int updateAvailableQuantity(int bookId, int quantity) {
	
	    Optional<Book> optional =
	            bookRepository.findById(bookId);
	
	    if (optional.isPresent()) {
	        Book book = optional.get();
	        book.setAvailableQuantity(quantity);
	        bookRepository.save(book);
	        return 1;
	    }
	
	    return 0;
	}

	@Override
    public int updateBook(Book book) {

        Optional<Book> optional =
                bookRepository.findById(book.getBookId());

        if (optional.isPresent()) {
            bookRepository.save(book);
            return 1;
        }

        return 0;
    }

    @Override
    public int deleteBook(int bookId) {

        if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId);
            return 1;
        }

        return 0;
    }
}