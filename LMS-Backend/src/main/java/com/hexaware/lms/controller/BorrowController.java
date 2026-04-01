package com.hexaware.lms.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.lms.entity.BorrowRecord;
import com.hexaware.lms.service.IBorrowService;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    @Autowired
    private IBorrowService borrowService;

    @PostMapping("/borrowbook")
    public org.springframework.http.ResponseEntity<?> borrowBook(@RequestParam int userId,
                                          @RequestParam int bookId) {
    	
    	BorrowRecord record = new BorrowRecord();

    	record.setBorrowDate(LocalDate.now());
    	record.setActualReturnDate(LocalDate.now().plusDays(7));
    	record.setStatus("BORROWED");
    	record.setFine(0);

        int result = borrowService.borrowBook(userId, bookId);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Book Borrowed Successfully");
        else
            return org.springframework.http.ResponseEntity.badRequest().body(java.util.Map.of("message", "can't reserve/borrow more than 5 books remove unwanted to add new"));

        return org.springframework.http.ResponseEntity.ok(response);
    }

    @PutMapping("/return/{borrowId}")
    public Map<String, String> returnBook(@PathVariable int borrowId) {

    	BorrowRecord record = new BorrowRecord();
    	
    	record.setReturnDate(LocalDate.now());
    	record.setStatus("RETURNED");
    	
        int result = borrowService.returnBook(borrowId);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Book Returned Successfully");
        else
            response.put("message", "Return Failed");

        return response;
    }

    @GetMapping("/fine/{borrowId}")
    public double calculateFine(@PathVariable int borrowId) {
        return borrowService.calculateFine(borrowId);
    }

    @GetMapping("/Borrowhistoryofuser/{userId}")
    public List<BorrowRecord> getBorrowHistoryByUser(@PathVariable int userId) {
        return borrowService.getBorrowHistoryByUser(userId);
    }

    @GetMapping("/getallborrowrecords")
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowService.getAllBorrowRecords();
    }

    @PutMapping("/lost/{borrowId}")
    public Map<String, String> markAsLost(@PathVariable int borrowId) {
        int result = borrowService.markAsLost(borrowId);
        Map<String, String> response = new HashMap<>();
        if (result == 1)
            response.put("message", "Book Marked as Lost");
        else
            response.put("message", "Failed to Update Status");
        return response;
    }

    @PutMapping("/request-return/{borrowId}")
    public Map<String, String> requestReturn(@PathVariable int borrowId) {
        int result = borrowService.requestReturn(borrowId);
        Map<String, String> response = new HashMap<>();
        if (result == 1)
            response.put("message", "Return Requested Successfully");
        else
            response.put("message", "Failed to Request Return");
        return response;
    }
}