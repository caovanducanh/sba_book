package com.example.demologin.initializer.components;

import com.example.demologin.entity.Book;
import com.example.demologin.entity.Category;
import com.example.demologin.repository.BookRepository;
import com.example.demologin.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Book Management Data Initializer
 *
 * Responsible for creating sample categories and books for the Book Management system.
 * This runs as part of the main data initialization process.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookManagementDataInitializer {
    
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    
    @Transactional
    public int[] initializeSampleData() {
        if (categoryRepository.count() == 0) {
            log.debug("Creating sample Book Management data...");
            int categoryCount = createSampleCategories();
            int bookCount = createSampleBooks();
            log.debug("Created {} categories and {} books", categoryCount, bookCount);
            return new int[]{categoryCount, bookCount};
        } else {
            log.debug("Categories already exist. Skipping sample data initialization.");
            return new int[]{0, 0};
        }
    }
    
    private int createSampleCategories() {
        List<Category> categories = Arrays.asList(
            createCategory("Fiction", "Fiction books including novels, short stories, and literary works"),
            createCategory("Non-Fiction", "Non-fiction books including biographies, history, science"),
            createCategory("Science & Technology", "Books about science, technology, programming, and engineering"),
            createCategory("Business & Economics", "Books about business, economics, finance, and management"),
            createCategory("Education", "Educational books, textbooks, and learning materials"),
            createCategory("Art & Design", "Books about art, design, photography, and creativity"),
            createCategory("Health & Fitness", "Books about health, fitness, nutrition, and wellness"),
            createCategory("Travel & Culture", "Travel guides, cultural studies, and geography books")
        );
        
        categoryRepository.saveAll(categories);
        return categories.size();
    }
    
    private Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setIsActive(true);
        return category;
    }
    
    private int createSampleBooks() {
        // Get categories for reference
        Category fiction = categoryRepository.findByName("Fiction").orElse(null);
        Category nonFiction = categoryRepository.findByName("Non-Fiction").orElse(null);
        Category sciTech = categoryRepository.findByName("Science & Technology").orElse(null);
        Category business = categoryRepository.findByName("Business & Economics").orElse(null);
        
        if (fiction == null || nonFiction == null || sciTech == null || business == null) {
            log.warn("Some categories not found. Skipping sample books creation.");
            return 0;
        }
        
        List<Book> books = Arrays.asList(
            createBook("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", 
                      "A classic American novel set in the Jazz Age", 
                      new BigDecimal("15.99"), 50, LocalDate.of(1925, 4, 10),
                      "Charles Scribner's Sons", 180, "English", fiction),
                      
            createBook("Clean Code", "Robert C. Martin", "9780132350884",
                      "A handbook of agile software craftsmanship",
                      new BigDecimal("45.00"), 30, LocalDate.of(2008, 8, 1),
                      "Prentice Hall", 464, "English", sciTech),
                      
            createBook("Sapiens", "Yuval Noah Harari", "9780062316097",
                      "A brief history of humankind",
                      new BigDecimal("24.99"), 25, LocalDate.of(2014, 2, 10),
                      "Harper", 443, "English", nonFiction),
                      
            createBook("The Lean Startup", "Eric Ries", "9780307887894",
                      "How today's entrepreneurs use continuous innovation to create radically successful businesses",
                      new BigDecimal("28.00"), 40, LocalDate.of(2011, 9, 13),
                      "Crown Business", 336, "English", business),
                      
            createBook("1984", "George Orwell", "9780451524935",
                      "A dystopian social science fiction novel",
                      new BigDecimal("13.99"), 35, LocalDate.of(1949, 6, 8),
                      "Secker & Warburg", 328, "English", fiction),
                      
            createBook("Design Patterns", "Gang of Four", "9780201633610",
                      "Elements of Reusable Object-Oriented Software",
                      new BigDecimal("54.99"), 20, LocalDate.of(1994, 10, 21),
                      "Addison-Wesley", 395, "English", sciTech)
        );
        
        bookRepository.saveAll(books);
        return books.size();
    }
    
    private Book createBook(String title, String author, String isbn, String description,
                           BigDecimal price, Integer stock, LocalDate publishedDate,
                           String publisher, Integer pages, String language, Category category) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setDescription(description);
        book.setPrice(price);
        book.setStockQuantity(stock);
        book.setIsAvailable(true);
        book.setPublishedDate(publishedDate);
        book.setPublisher(publisher);
        book.setPages(pages);
        book.setLanguage(language);
        book.setCategory(category);
        return book;
    }
}