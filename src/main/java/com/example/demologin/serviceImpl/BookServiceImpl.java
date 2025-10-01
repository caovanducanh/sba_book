package com.example.demologin.serviceImpl;

import com.example.demologin.dto.request.book.CreateBookRequest;
import com.example.demologin.dto.request.book.UpdateBookRequest;
import com.example.demologin.dto.response.BookResponse;
import com.example.demologin.entity.Book;
import com.example.demologin.entity.Category;
import com.example.demologin.exception.exceptions.BadRequestException;
import com.example.demologin.exception.exceptions.ConflictException;
import com.example.demologin.exception.exceptions.NotFoundException;
import com.example.demologin.repository.BookRepository;
import com.example.demologin.repository.CategoryRepository;
import com.example.demologin.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    
    @Override
    public Page<BookResponse> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(BookResponse::toBookResponse);
    }
    
    @Override
    public Page<BookResponse> getBooksByFilters(String title, String author, Long categoryId, 
                                              Boolean isAvailable, BigDecimal minPrice, 
                                              BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<Book> books = bookRepository.findBooksByFilters(
                title, author, categoryId, isAvailable, minPrice, maxPrice, pageable);
        return books.map(BookResponse::toBookResponse);
    }
    
    @Override
    public Page<BookResponse> getBooksByCategory(Long categoryId, int page, int size) {
        // Validate category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Category not found with id: " + categoryId);
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<Book> books = bookRepository.findByCategory_CategoryId(categoryId, pageable);
        return books.map(BookResponse::toBookResponse);
    }
    
    @Override
    public BookResponse getBookById(Long bookId) {
        Book book = findBookById(bookId);
        return BookResponse.toBookResponse(book);
    }
    
    @Override
    @Transactional
    public BookResponse createBook(CreateBookRequest request) {
        validateBookData(request.getIsbn(), null, request.getCategoryId());
        
        Category category = findCategoryById(request.getCategoryId());
        
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setStockQuantity(request.getStockQuantity());
        book.setIsAvailable(request.getIsAvailable());
        book.setPublishedDate(request.getPublishedDate());
        book.setPublisher(request.getPublisher());
        book.setPages(request.getPages());
        book.setLanguage(request.getLanguage());
        book.setCategory(category);
        
        Book savedBook = bookRepository.save(book);
        return BookResponse.toBookResponse(savedBook);
    }
    
    @Override
    @Transactional
    public BookResponse updateBook(Long bookId, UpdateBookRequest request) {
        Book book = findBookById(bookId);
        validateBookData(request.getIsbn(), bookId, request.getCategoryId());
        
        Category category = findCategoryById(request.getCategoryId());
        
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setStockQuantity(request.getStockQuantity());
        if (request.getIsAvailable() != null) {
            book.setIsAvailable(request.getIsAvailable());
        }
        book.setPublishedDate(request.getPublishedDate());
        book.setPublisher(request.getPublisher());
        book.setPages(request.getPages());
        book.setLanguage(request.getLanguage());
        book.setCategory(category);
        
        Book updatedBook = bookRepository.save(book);
        return BookResponse.toBookResponse(updatedBook);
    }
    
    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = findBookById(bookId);
        bookRepository.delete(book);
    }
    
    @Override
    @Transactional
    public void toggleBookAvailability(Long bookId) {
        Book book = findBookById(bookId);
        book.setIsAvailable(!book.getIsAvailable());
        bookRepository.save(book);
    }
    
    @Override
    @Transactional
    public void updateBookStock(Long bookId, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new BadRequestException("Stock quantity cannot be negative");
        }
        
        Book book = findBookById(bookId);
        book.setStockQuantity(newQuantity);
        bookRepository.save(book);
    }
    
    @Override
    @Transactional
    public void decreaseBookStock(Long bookId, Integer quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        
        Book book = findBookById(bookId);
        if (book.getStockQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock. Available: " + book.getStockQuantity() + ", Requested: " + quantity);
        }
        
        book.decreaseStock(quantity);
        bookRepository.save(book);
    }
    
    @Override
    @Transactional
    public void increaseBookStock(Long bookId, Integer quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        
        Book book = findBookById(bookId);
        book.increaseStock(quantity);
        bookRepository.save(book);
    }
    
    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + bookId));
    }
    
    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));
    }
    
    private void validateBookData(String isbn, Long excludeBookId, Long categoryId) {
        // Validate ISBN uniqueness if provided
        if (StringUtils.hasText(isbn)) {
            boolean isbnExists = (excludeBookId == null) 
                ? bookRepository.existsByIsbn(isbn)
                : bookRepository.existsByIsbnAndBookIdNot(isbn, excludeBookId);
                
            if (isbnExists) {
                throw new ConflictException("ISBN already exists: " + isbn);
            }
        }
        
        // Validate category exists and is active
        Category category = findCategoryById(categoryId);
        if (!category.getIsActive()) {
            throw new BadRequestException("Cannot assign book to inactive category: " + category.getName());
        }
    }
}