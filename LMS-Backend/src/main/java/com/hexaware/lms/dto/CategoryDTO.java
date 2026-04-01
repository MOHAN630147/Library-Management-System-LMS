package com.hexaware.lms.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    private int categoryId;
    
    @NotBlank(message="Category name cannot be empty")
    private String name;

    public CategoryDTO() {}

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}