package com.hexaware.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hexaware.lms.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmailAndPassword(String email, String password);

	User findByEmail(String email);
}