package com.hexaware.lms.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.BorrowRecord;
import com.hexaware.lms.entity.User;
import com.hexaware.lms.repository.BookRepository;
import com.hexaware.lms.repository.BorrowRepository;
import com.hexaware.lms.repository.ReservationRepository;
import com.hexaware.lms.entity.Reservation;
import com.hexaware.lms.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IBorrowServiceImpl implements IBorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public int borrowBook(int userId, int bookId) {
    	
    	log.info("User {} trying to borrow book {}", userId, bookId);

        long borrowedCount = borrowRepository.countByUser_UserIdAndStatus(userId, "BORROWED");
        long reservedCount = reservationRepository.countByUser_UserIdAndStatus(userId, "ACTIVE");

        if (borrowedCount + reservedCount >= 5) {
            log.warn("User {} has reached the limit of 5 books", userId);
            return 0;
        }

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if(userOpt.isPresent() && bookOpt.isPresent()) {

            User user = userOpt.get();
            Book book = bookOpt.get();

            if(book.getAvailableQuantity() <= 0) {
                return 0;
            }

            BorrowRecord record = new BorrowRecord();

            record.setUser(user);
            record.setBook(book);

            record.setBorrowDate(LocalDate.now());
            record.setActualReturnDate(LocalDate.now().plusDays(10));

            record.setStatus("BORROWED");
            record.setFine(0);

            borrowRepository.save(record);

            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            bookRepository.save(book);
            
            log.info("Book borrowed successfully");
            
            // Cleanup reservation if it exists
            java.util.Optional<Reservation> resOpt = reservationRepository.findByUser_UserIdAndBook_BookId(userId, bookId);
            if(resOpt.isPresent()) {
            	reservationRepository.delete(resOpt.get());
            	log.info("Related reservation removed for user {} and book {}", userId, bookId);
            }
               
            return 1;
            }

        return 0;
    }
 
    @Override
    public int returnBook(int borrowId) {

        Optional<BorrowRecord> recordOpt=
                borrowRepository.findById(borrowId);


        if(recordOpt.isPresent()) {

            BorrowRecord record = recordOpt.get();

            record.setReturnDate(LocalDate.now());
            record.setStatus("RETURNED");

            borrowRepository.save(record);

            Book book = record.getBook();
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            bookRepository.save(book);

            return 1;
        }

        return 0;
    }

    @Override
    public double calculateFine(int borrowId) {

        log.debug("Calculating fine for borrowId {}", borrowId);

        Optional<BorrowRecord> optional = borrowRepository.findById(borrowId);

        double fine = 0.0;

        if (optional.isPresent()) {

            BorrowRecord record = optional.get();
            
            LocalDate calculationDate = record.getReturnDate();
            if (calculationDate == null) {
                // If not returned yet, calculate fine up to today
                calculationDate = LocalDate.now();
            }

            long daysKept = ChronoUnit.DAYS.between(
                    record.getBorrowDate(),
                    calculationDate);

            if (daysKept > 10) {
                fine = (daysKept - 10) * 5.0;
            }
            
            // Update the record with the latest fine
            record.setFine(fine);
            borrowRepository.save(record);
        }

        log.debug("Fine calculated: {}", fine);

        return fine;
    }

    @Override
    public List<BorrowRecord> getBorrowHistoryByUser(int userId) {

        return borrowRepository.findByUser_UserId(userId);
    }

    
    @Override
    public List<BorrowRecord> getAllBorrowRecords() {

        return borrowRepository.findAll();
    }

    @Override
    public int markAsLost(int borrowId) {
        Optional<BorrowRecord> optional = borrowRepository.findById(borrowId);
        if (optional.isPresent()) {
            BorrowRecord record = optional.get();
            calculateFine(borrowId); 
            record.setStatus("LOST");
            borrowRepository.save(record);
            return 1;
        }
        return 0;
    }

    @Override
    public int requestReturn(int borrowId) {
        Optional<BorrowRecord> optional = borrowRepository.findById(borrowId);
        if (optional.isPresent()) {
            BorrowRecord record = optional.get();
            record.setReturnRequested(true);
            borrowRepository.save(record);
            return 1;
        }
        return 0;
    }
}