package com.example.demologin.service;

import com.example.demologin.dto.request.category.CreateCategoryRequest;
import com.example.demologin.dto.request.category.UpdateCategoryRequest;
import com.example.demologin.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    
    Page<CategoryResponse> getAllCategories(int page, int size);
    
    Page<CategoryResponse> getCategoriesByFilters(String name, Boolean isActive, int page, int size);
    
    CategoryResponse getCategoryById(Long categoryId);
    
    List<CategoryResponse> getActiveCategoriesForDropdown();
    
    CategoryResponse createCategory(CreateCategoryRequest request);
    
    CategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request);
    
    void deleteCategory(Long categoryId);
    
    void toggleCategoryStatus(Long categoryId);
}