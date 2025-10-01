package com.example.demologin.dto.request.book;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {
    
    @NotBlank(message = "Book title is required")
    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    private String title;
    
    @NotBlank(message = "Author is required")
    @Size(min = 2, max = 100, message = "Author must be between 2 and 100 characters")
    private String author;
    
    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    private String isbn;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be at least 0")
    private Integer stockQuantity;
    
    private Boolean isAvailable = true;
    
    private LocalDate publishedDate;
    
    @Size(max = 100, message = "Publisher must not exceed 100 characters")
    private String publisher;
    
    @Min(value = 1, message = "Pages must be at least 1")
    private Integer pages;
    
    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
}