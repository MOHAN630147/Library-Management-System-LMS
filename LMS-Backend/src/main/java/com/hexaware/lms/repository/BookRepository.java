package com.hexaware.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.lms.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
	
	List<Book> findByCategory_CategoryId(int categoryId);
}
