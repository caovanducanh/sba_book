package com.example.demologin.repository;

import com.example.demologin.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    boolean existsByName(String name);
    
    boolean existsByNameAndCategoryIdNot(String name, Long categoryId);
    
    Page<Category> findByIsActive(Boolean isActive, Pageable pageable);
    
    List<Category> findByIsActiveOrderByName(Boolean isActive);
    
    @Query("SELECT c FROM Category c WHERE " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive)")
    Page<Category> findByNameContainingIgnoreCaseAndIsActive(
            @Param("name") String name, 
            @Param("isActive") Boolean isActive, 
            Pageable pageable);
}