package com.example.demologin.serviceImpl;

import com.example.demologin.dto.request.category.CreateCategoryRequest;
import com.example.demologin.dto.request.category.UpdateCategoryRequest;
import com.example.demologin.dto.response.CategoryResponse;
import com.example.demologin.entity.Category;
import com.example.demologin.exception.exceptions.ConflictException;
import com.example.demologin.exception.exceptions.NotFoundException;
import com.example.demologin.repository.BookRepository;
import com.example.demologin.repository.CategoryRepository;
import com.example.demologin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    
    @Override
    public Page<CategoryResponse> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(CategoryResponse::toCategoryResponse);
    }
    
    @Override
    public Page<CategoryResponse> getCategoriesByFilters(String name, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Category> categories = categoryRepository.findByNameContainingIgnoreCaseAndIsActive(
                name, isActive, pageable);
        return categories.map(CategoryResponse::toCategoryResponse);
    }
    
    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = findCategoryById(categoryId);
        return CategoryResponse.toCategoryResponse(category);
    }
    
    @Override
    public List<CategoryResponse> getActiveCategoriesForDropdown() {
        List<Category> categories = categoryRepository.findByIsActiveOrderByName(true);
        return categories.stream()
                .map(CategoryResponse::toCategoryResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        validateCategoryNameUnique(request.getName(), null);
        
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIsActive(request.getIsActive());
        
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.toCategoryResponse(savedCategory);
    }
    
    @Override
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
        Category category = findCategoryById(categoryId);
        validateCategoryNameUnique(request.getName(), categoryId);
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }
        
        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.toCategoryResponse(updatedCategory);
    }
    
    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        
        // Check if category has books
        Long bookCount = bookRepository.countBooksByCategory(categoryId);
        if (bookCount > 0) {
            throw new ConflictException("Cannot delete category because it contains " + bookCount + " book(s)");
        }
        
        categoryRepository.delete(category);
    }
    
    @Override
    @Transactional
    public void toggleCategoryStatus(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.setIsActive(!category.getIsActive());
        categoryRepository.save(category);
    }
    
    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));
    }
    
    private void validateCategoryNameUnique(String name, Long excludeCategoryId) {
        boolean exists = (excludeCategoryId == null) 
            ? categoryRepository.existsByName(name)
            : categoryRepository.existsByNameAndCategoryIdNot(name, excludeCategoryId);
            
        if (exists) {
            throw new ConflictException("Category name already exists: " + name);
        }
    }
}