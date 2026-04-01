package com.hexaware.lms.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="borrow_record")
public class BorrowRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int borrowId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private double fine;
    private String status;
    private boolean returnRequested;
    
    @ManyToOne
    @JoinColumn(name = "userId")   
    private User user;

    @ManyToOne
    @JoinColumn(name = "bookId")  
    private Book book;

    public BorrowRecord() {}

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReturnRequested() {
        return returnRequested;
    }

    public void setReturnRequested(boolean returnRequested) {
        this.returnRequested = returnRequested;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	@Override
	public String toString() {
		return "BorrowRecord [borrowId=" + borrowId + ", borrowDate="
				+ borrowDate + ", returnDate=" + returnDate + ", actualReturnDate=" + actualReturnDate + ", fine="
				+ fine + ", status=" + status + ", returnRequested=" + returnRequested + "]";
	}
    
    
}
