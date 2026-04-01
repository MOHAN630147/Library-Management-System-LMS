package com.hexaware.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hexaware.lms.entity.BorrowRecord;
import java.util.List;

public interface BorrowRepository extends JpaRepository<BorrowRecord, Integer> {

	List<BorrowRecord> findByUser_UserId(int userId);

    List<BorrowRecord> findByBook_BookId(int bookId);

    long countByUser_UserIdAndStatus(int userId, String status);
}