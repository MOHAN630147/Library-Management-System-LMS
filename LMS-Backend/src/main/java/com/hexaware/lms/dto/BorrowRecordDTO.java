package com.hexaware.lms.dto;

import java.time.LocalDate;

public class BorrowRecordDTO {

    private int borrowId;
    private int userId;
    private int bookId;
    private LocalDate borrowDate;
    private LocalDate actualReturnDate;
    private LocalDate returnDate;

    public BorrowRecordDTO() {}

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    

    public LocalDate getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(LocalDate actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}