package com.example.demologin.controller;

import com.example.demologin.annotation.ApiResponse;
import com.example.demologin.annotation.PageResponse;
import com.example.demologin.annotation.PublicEndpoint;
import com.example.demologin.service.BookService;
import com.example.demologin.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/books")
@Tag(name = "Public Book API", description = "Public APIs for browsing books and categories")
public class PublicBookController {
    
    private final BookService bookService;
    private final CategoryService categoryService;
    
    @GetMapping
    @PageResponse
    @ApiResponse(message = "Books retrieved successfully")
    @PublicEndpoint
    @Operation(summary = "Browse books", description = "Public endpoint to browse available books")
    public Object browseBooks(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return bookService.getBooksByFilters(null, null, null, true, null, null, page, size);
    }
    
    @GetMapping("/search")
    @PageResponse
    @ApiResponse(message = "Books retrieved successfully")
    @PublicEndpoint
    @Operation(summary = "Search books", description = "Public endpoint to search available books")
    public Object searchBooks(
            @Parameter(description = "Book title to search") @RequestParam(required = false) String title,
            @Parameter(description = "Author name to search") @RequestParam(required = false) String author,
            @Parameter(description = "Category ID filter") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Minimum price filter") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price filter") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return bookService.getBooksByFilters(title, author, categoryId, true, 
                                           minPrice, maxPrice, page, size);
    }
    
    @GetMapping("/category/{categoryId}")
    @PageResponse
    @ApiResponse(message = "Books retrieved successfully")
    @PublicEndpoint
    @Operation(summary = "Get books by category", description = "Public endpoint to browse books by category")
    public Object getBooksByCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return bookService.getBooksByCategory(categoryId, page, size);
    }
    
    @GetMapping("/{bookId}")
    @ApiResponse(message = "Book retrieved successfully")
    @PublicEndpoint
    @Operation(summary = "Get book details", description = "Public endpoint to get book details")
    public Object getBookDetails(
            @Parameter(description = "Book ID") @PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }
    
    @GetMapping("/categories")
    @ApiResponse(message = "Categories retrieved successfully")
    @PublicEndpoint
    @Operation(summary = "Get categories", description = "Public endpoint to get all active categories")
    public Object getActiveCategories() {
        return categoryService.getActiveCategoriesForDropdown();
    }
}