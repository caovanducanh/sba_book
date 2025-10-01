package com.example.demologin.service;

import com.example.demologin.dto.request.book.CreateBookRequest;
import com.example.demologin.dto.request.book.UpdateBookRequest;
import com.example.demologin.dto.response.BookResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface BookService {
    
    Page<BookResponse> getAllBooks(int page, int size);
    
    Page<BookResponse> getBooksByFilters(String title, String author, Long categoryId, 
                                       Boolean isAvailable, BigDecimal minPrice, 
                                       BigDecimal maxPrice, int page, int size);
    
    Page<BookResponse> getBooksByCategory(Long categoryId, int page, int size);
    
    BookResponse getBookById(Long bookId);
    
    BookResponse createBook(CreateBookRequest request);
    
    BookResponse updateBook(Long bookId, UpdateBookRequest request);
    
    void deleteBook(Long bookId);
    
    void toggleBookAvailability(Long bookId);
    
    void updateBookStock(Long bookId, Integer newQuantity);
    
    void decreaseBookStock(Long bookId, Integer quantity);
    
    void increaseBookStock(Long bookId, Integer quantity);
}