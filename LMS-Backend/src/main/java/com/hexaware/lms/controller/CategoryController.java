package com.hexaware.lms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.lms.entity.Category;
import com.hexaware.lms.service.ICategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/addcategory")
    public Map<String, String> addCategory(@Valid @RequestBody Category category) {

        int result = categoryService.addCategory(category);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Category Added Successfully");
        else
            response.put("message", "Category Addition Failed");

        return response;
    }

    @GetMapping("/getcategory/{id}")
    public Category getCategoryById(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/getallcategories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/updatecategory")
    public Map<String, String> updateCategory(@RequestBody Category category) {

        int result = categoryService.updateCategory(category);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Category Updated Successfully");
        else
            response.put("message", "Category Update Failed");

        return response;
    }

    @DeleteMapping("/deletecategory/{id}")
    public Map<String, String> deleteCategory(@PathVariable int id) {

        int result = categoryService.deleteCategory(id);

        Map<String, String> response = new HashMap<>();

        if (result == 1)
            response.put("message", "Category Deleted Successfully");
        else
            response.put("message", "Category Deletion Failed");

        return response;
    }
}