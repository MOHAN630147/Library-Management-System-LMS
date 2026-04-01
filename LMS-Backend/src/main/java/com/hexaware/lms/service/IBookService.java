package com.hexaware.lms.service;

import com.hexaware.lms.entity.Book;
import java.util.List;

public interface IBookService {
	
	public int addBook(Book book);

    Book getBookById(int bookId);

    List<Book> getAllBooks();

    List<Book> getBooksByCategoryid( int categoryId);

    public int updateBook(Book book);

    public int updateAvailableQuantity(int bookId, int quantity);
    
    public int deleteBook(int bookId);

}
