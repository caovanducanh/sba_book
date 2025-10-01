package com.example.demologin.repository;

import com.example.demologin.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByIsbn(String isbn);
    
    boolean existsByIsbn(String isbn);
    
    boolean existsByIsbnAndBookIdNot(String isbn, Long bookId);
    
    Page<Book> findByCategory_CategoryId(Long categoryId, Pageable pageable);
    
    Page<Book> findByIsAvailable(Boolean isAvailable, Pageable pageable);
    
    List<Book> findByStockQuantityGreaterThan(Integer quantity);
    
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:categoryId IS NULL OR b.category.categoryId = :categoryId) AND " +
           "(:isAvailable IS NULL OR b.isAvailable = :isAvailable) AND " +
           "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR b.price <= :maxPrice)")
    Page<Book> findBooksByFilters(
            @Param("title") String title,
            @Param("author") String author,
            @Param("categoryId") Long categoryId,
            @Param("isAvailable") Boolean isAvailable,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.category.categoryId = :categoryId")
    Long countBooksByCategory(@Param("categoryId") Long categoryId);
}