package com.hexaware.lms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.lms.entity.Book;
import com.hexaware.lms.entity.Reservation;
import com.hexaware.lms.entity.User;
import com.hexaware.lms.repository.BookRepository;
import com.hexaware.lms.repository.BorrowRepository;
import com.hexaware.lms.repository.ReservationRepository;
import com.hexaware.lms.repository.UserRepository;

@Service
public class IReservationServiceImpl implements IReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    
    @Autowired
    private BorrowRepository borrowRepository;

    @Override
    public int reserveBook(int userId, long bookId) {

        long borrowedCount = borrowRepository.countByUser_UserIdAndStatus(userId, "BORROWED");
        long reservedCount = reservationRepository.countByUser_UserIdAndStatus(userId, "ACTIVE");

        if (borrowedCount + reservedCount >= 5) {
            return 0;
        }

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById((int) bookId);

        if (userOpt.isPresent() && bookOpt.isPresent()) {
            // ...

            Reservation reservation = new Reservation();
            reservation.setUser(userOpt.get());
            reservation.setBook(bookOpt.get());
            reservation.setReservationDate(LocalDate.now());
            
            reservation.setStatus("Active");

            reservationRepository.save(reservation);

            return 1;
        }

        return 0;
    }

    
    @Override
    public List<Reservation> getReservationsByUser(int userId) {

        return reservationRepository.findByUser_UserId(userId);
    }

    
    @Override
    public List<Reservation> getAllReservations() {

        return reservationRepository.findAll();
    }

    
    @Override
    public int cancelReservation(int reservationId) {
        Optional<Reservation> optional = reservationRepository.findById(reservationId);
        if (optional.isPresent()) {
            Reservation res = optional.get();
            res.setStatus("CANCELLED");
            reservationRepository.save(res);
            return 1;
        }
        return 0;
    }
}