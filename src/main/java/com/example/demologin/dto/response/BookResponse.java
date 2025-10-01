package com.example.demologin.dto.response;

import com.example.demologin.entity.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    
    @JsonProperty("bookId")
    private Long bookId;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("author")
    private String author;
    
    @JsonProperty("isbn")
    private String isbn;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("price")
    private BigDecimal price;
    
    @JsonProperty("stockQuantity")
    private Integer stockQuantity;
    
    @JsonProperty("isAvailable")
    private Boolean isAvailable;
    
    @JsonProperty("publishedDate")
    private LocalDate publishedDate;
    
    @JsonProperty("publisher")
    private String publisher;
    
    @JsonProperty("pages")
    private Integer pages;
    
    @JsonProperty("language")
    private String language;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    @JsonProperty("category")
    private CategoryResponse category;
    
    @JsonProperty("inStock")
    private Boolean inStock;
    
    public static BookResponse toBookResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setBookId(book.getBookId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setDescription(book.getDescription());
        response.setPrice(book.getPrice());
        response.setStockQuantity(book.getStockQuantity());
        response.setIsAvailable(book.getIsAvailable());
        response.setPublishedDate(book.getPublishedDate());
        response.setPublisher(book.getPublisher());
        response.setPages(book.getPages());
        response.setLanguage(book.getLanguage());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        response.setInStock(book.isInStock());
        
        if (book.getCategory() != null) {
            response.setCategory(CategoryResponse.toCategoryResponse(book.getCategory()));
        }
        
        return response;
    }
    
    public static BookResponse toBookResponseWithoutCategory(Book book) {
        BookResponse response = new BookResponse();
        response.setBookId(book.getBookId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setDescription(book.getDescription());
        response.setPrice(book.getPrice());
        response.setStockQuantity(book.getStockQuantity());
        response.setIsAvailable(book.getIsAvailable());
        response.setPublishedDate(book.getPublishedDate());
        response.setPublisher(book.getPublisher());
        response.setPages(book.getPages());
        response.setLanguage(book.getLanguage());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        response.setInStock(book.isInStock());
        
        return response;
    }
}