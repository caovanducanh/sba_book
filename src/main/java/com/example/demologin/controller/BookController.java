package com.example.demologin.controller;

import com.example.demologin.annotation.ApiResponse;
import com.example.demologin.annotation.PageResponse;
import com.example.demologin.annotation.SecuredEndpoint;
import com.example.demologin.dto.request.book.CreateBookRequest;
import com.example.demologin.dto.request.book.UpdateBookRequest;
import com.example.demologin.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/books")
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {
    
    private final BookService bookService;
    
    @GetMapping
    @PageResponse
    @ApiResponse(message = "Books retrieved successfully")
    @SecuredEndpoint("BOOK_VIEW")
    @Operation(summary = "Get all books", description = "Retrieve paginated list of all books")
    public Object getAllBooks(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return bookService.getAllBooks(page, size);
    }
    
    @GetMapping("/search")
    @PageResponse
    @ApiResponse(message = "Books retrieved successfully")
    @SecuredEndpoint("BOOK_VIEW")
    @Operation(summary = "Search books", description = "Search books with various filters")
    public Object searchBooks(
            @Parameter(description = "Book title to search") @RequestParam(required = false) String title,
            @Parameter(description = "Author name to search") @RequestParam(required = false) String author,
            @Parameter(description = "Category ID filter") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Availability status filter") @RequestParam(required = false) Boolean isAvailable,
            @Parameter(description = "Minimum price filter") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price filter") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return bookService.getBooksByFilters(title, author, categoryId, isAvailable, 
                                           minPrice, maxPrice, page, size);
    }
    
    @GetMapping("/category/{categoryId}")
    @PageResponse
    @ApiResponse(message = "Books retrieved successfully")
    @SecuredEndpoint("BOOK_VIEW")
    @Operation(summary = "Get books by category", description = "Retrieve books from a specific category")
    public Object getBooksByCategory(
            @Parameter(description = "Category ID") @PathVariable Long categoryId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return bookService.getBooksByCategory(categoryId, page, size);
    }
    
    @GetMapping("/{bookId}")
    @ApiResponse(message = "Book retrieved successfully")
    @SecuredEndpoint("BOOK_VIEW")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    public Object getBookById(
            @Parameter(description = "Book ID") @PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }
    
    @PostMapping
    @ApiResponse(message = "Book created successfully")
    @SecuredEndpoint("BOOK_CREATE")
    @Operation(summary = "Create new book", description = "Create a new book")
    public Object createBook(
            @Parameter(description = "Book creation request") @Valid @RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }
    
    @PutMapping("/{bookId}")
    @ApiResponse(message = "Book updated successfully")
    @SecuredEndpoint("BOOK_UPDATE")
    @Operation(summary = "Update book", description = "Update an existing book")
    public Object updateBook(
            @Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "Book update request") @Valid @RequestBody UpdateBookRequest request) {
        return bookService.updateBook(bookId, request);
    }
    
    @DeleteMapping("/{bookId}")
    @ApiResponse(message = "Book deleted successfully")
    @SecuredEndpoint("BOOK_DELETE")
    @Operation(summary = "Delete book", description = "Delete a book")
    public Object deleteBook(
            @Parameter(description = "Book ID") @PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return null;
    }
    
    @PatchMapping("/{bookId}/toggle-availability")
    @ApiResponse(message = "Book availability updated successfully")
    @SecuredEndpoint("BOOK_UPDATE")
    @Operation(summary = "Toggle book availability", description = "Toggle book available/unavailable status")
    public Object toggleBookAvailability(
            @Parameter(description = "Book ID") @PathVariable Long bookId) {
        bookService.toggleBookAvailability(bookId);
        return null;
    }
    
    @PatchMapping("/{bookId}/stock")
    @ApiResponse(message = "Book stock updated successfully")
    @SecuredEndpoint("BOOK_UPDATE")
    @Operation(summary = "Update book stock", description = "Update book stock quantity")
    public Object updateBookStock(
            @Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "New stock quantity") @RequestParam Integer quantity) {
        bookService.updateBookStock(bookId, quantity);
        return null;
    }
    
    @PatchMapping("/{bookId}/stock/decrease")
    @ApiResponse(message = "Book stock decreased successfully")
    @SecuredEndpoint("BOOK_UPDATE")
    @Operation(summary = "Decrease book stock", description = "Decrease book stock by specified quantity")
    public Object decreaseBookStock(
            @Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "Quantity to decrease") @RequestParam Integer quantity) {
        bookService.decreaseBookStock(bookId, quantity);
        return null;
    }
    
    @PatchMapping("/{bookId}/stock/increase")
    @ApiResponse(message = "Book stock increased successfully")
    @SecuredEndpoint("BOOK_UPDATE")
    @Operation(summary = "Increase book stock", description = "Increase book stock by specified quantity")
    public Object increaseBookStock(
            @Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "Quantity to increase") @RequestParam Integer quantity) {
        bookService.increaseBookStock(bookId, quantity);
        return null;
    }
}