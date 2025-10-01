package com.example.demologin.controller;

import com.example.demologin.annotation.ApiResponse;
import com.example.demologin.annotation.PageResponse;
import com.example.demologin.annotation.SecuredEndpoint;
import com.example.demologin.dto.request.category.CreateCategoryRequest;
import com.example.demologin.dto.request.category.UpdateCategoryRequest;
import com.example.demologin.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
@Tag(name = "Category Management", description = "APIs for managing book categories")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    @GetMapping
    @PageResponse
    @ApiResponse(message = "Categories retrieved successfully")
    @SecuredEndpoint("CATEGORY_VIEW")
    @Operation(summary = "Get all categories", description = "Retrieve paginated list of all categories")
    public Object getAllCategories(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return categoryService.getAllCategories(page, size);
    }
    
    @GetMapping("/search")
    @PageResponse
    @ApiResponse(message = "Categories retrieved successfully")
    @SecuredEndpoint("CATEGORY_VIEW")
    @Operation(summary = "Search categories", description = "Search categories by name and status")
    public Object searchCategories(
            @Parameter(description = "Category name to search") @RequestParam(required = false) String name,
            @Parameter(description = "Category status filter") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return categoryService.getCategoriesByFilters(name, isActive, page, size);
    }
    
    @GetMapping("/dropdown")
    @ApiResponse(message = "Active categories retrieved successfully")
    @SecuredEndpoint("CATEGORY_VIEW")
    @Operation(summary = "Get categories for dropdown", description = "Get all active categories for dropdown selection")
    public Object getActiveCategoriesForDropdown() {
        return categoryService.getActiveCategoriesForDropdown();
    }
    
    @GetMapping("/{categoryId}")
    @ApiResponse(message = "Category retrieved successfully")
    @SecuredEndpoint("CATEGORY_VIEW")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    public Object getCategoryById(
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }
    
    @PostMapping
    @ApiResponse(message = "Category created successfully")
    @SecuredEndpoint("CATEGORY_CREATE")
    @Operation(summary = "Create new category", description = "Create a new book category")
    public ResponseEntity<Object> createCategory(
            @Parameter(description = "Category creation request") @Valid @RequestBody CreateCategoryRequest request) {
        Object result = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    
    @PutMapping("/{categoryId}")
    @ApiResponse(message = "Category updated successfully")
    @SecuredEndpoint("CATEGORY_UPDATE")
    @Operation(summary = "Update category", description = "Update an existing category")
    public Object updateCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId,
            @Parameter(description = "Category update request") @Valid @RequestBody UpdateCategoryRequest request) {
        return categoryService.updateCategory(categoryId, request);
    }
    
    @DeleteMapping("/{categoryId}")
    @ApiResponse(message = "Category deleted successfully")
    @SecuredEndpoint("CATEGORY_DELETE")
    @Operation(summary = "Delete category", description = "Delete a category (only if it has no books)")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{categoryId}/toggle-status")
    @ApiResponse(message = "Category status updated successfully")
    @SecuredEndpoint("CATEGORY_UPDATE")
    @Operation(summary = "Toggle category status", description = "Toggle category active/inactive status")
    public ResponseEntity<Void> toggleCategoryStatus(
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {
        categoryService.toggleCategoryStatus(categoryId);
        return ResponseEntity.ok().build();
    }
}