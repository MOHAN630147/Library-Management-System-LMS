package com.hexaware.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.lms.entity.Category;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.repository.CategoryRepository;

@Service
public class ICategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public int addCategory(Category category) {

        categoryRepository.save(category);
        return 1;
    }

    @Override
    public Category getCategoryById(int categoryId) {

    	return categoryRepository.findById(categoryId)
    	        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
    
    @Override
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }
    
    @Override
    public int updateCategory(Category category) {

        if (categoryRepository.existsById(category.getCategoryId())) {

            categoryRepository.save(category);
            return 1;
        }

        return 0;
    }
  
    @Override
    public int deleteCategory(int categoryId) {

        if (categoryRepository.existsById(categoryId)) {

            categoryRepository.deleteById(categoryId);
            return 1;
        }

        return 0;
    }
}