package com.hexaware.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hexaware.lms.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}