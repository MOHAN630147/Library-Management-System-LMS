package com.hexaware.lms.service;

import com.hexaware.lms.entity.Category;
import java.util.List;

public interface ICategoryService {
	
    public int addCategory(Category category);

    Category getCategoryById(int categoryId);

    List<Category> getAllCategories();

    public int updateCategory(Category category);

    public int deleteCategory(int categoryId);

}
