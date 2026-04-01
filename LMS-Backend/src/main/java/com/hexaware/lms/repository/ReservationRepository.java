package com.hexaware.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hexaware.lms.entity.Reservation;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	List<Reservation> findByUser_UserId(int userId);

	List<Reservation> findByBook_BookId(int bookId);

    long countByUser_UserIdAndStatus(int userId, String status);

    java.util.Optional<Reservation> findByUser_UserIdAndBook_BookId(int userId, int bookId);
}