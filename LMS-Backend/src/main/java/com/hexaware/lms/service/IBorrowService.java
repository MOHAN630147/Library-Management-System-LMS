package com.hexaware.lms.service;

import com.hexaware.lms.entity.BorrowRecord;
import java.util.List;

public interface IBorrowService {

	
	public int borrowBook(int userId, int bookId);

	public int returnBook(int borrowId);

	public double calculateFine(int borrowId);

    List<BorrowRecord> getBorrowHistoryByUser(int userId);

    List<BorrowRecord> getAllBorrowRecords();

    public int markAsLost(int borrowId);

    public int requestReturn(int borrowId);
}