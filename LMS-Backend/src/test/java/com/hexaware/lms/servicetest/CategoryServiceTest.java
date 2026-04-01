package com.hexaware.lms.servicetest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hexaware.lms.entity.Category;
import com.hexaware.lms.exception.ResourceNotFoundException;
import com.hexaware.lms.service.ICategoryService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryServiceTest {

    @Autowired
    private ICategoryService categoryService;

    private static int categoryId;

    // 1️ Add Category
    @Test
    @Order(1)
    void testAddCategory() {

        Category category = new Category();
        category.setName("Technology");

        int result = categoryService.addCategory(category);

        assertEquals(1, result);

        categoryId = category.getCategoryId();
        assertTrue(categoryId > 0);
    }

    // 2️ Get Category By Id
    @Test
    @Order(2)
    void testGetCategoryById() {

        Category category =
                categoryService.getCategoryById(categoryId);

        assertNotNull(category);
        assertEquals("Technology", category.getName());
    }

    // 3️ Update Category
    @Test
    @Order(3)
    void testUpdateCategory() {

        Category category =
                categoryService.getCategoryById(categoryId);

        category.setName("Updated Technology");

        int result =
                categoryService.updateCategory(category);

        assertEquals(1, result);

        Category updated =
                categoryService.getCategoryById(categoryId);

        assertEquals("Updated Technology", updated.getName());
    }

    // 4️ Get All Categories
    @Test
    @Order(4)
    void testGetAllCategories() {

        List<Category> list =
                categoryService.getAllCategories();

        assertTrue(list.size() > 0);
    }

    @Test
    @Order(5)
    void testDeleteCategory() {

        int result =
                categoryService.deleteCategory(categoryId);

        assertEquals(1, result);

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });
    }
}
