package com.example.demologin.initializer;

import com.example.demologin.initializer.components.BookManagementDataInitializer;
import com.example.demologin.initializer.components.DefaultUserInitializer;
import com.example.demologin.initializer.components.PermissionRoleInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Main Data Initializer - Orchestrates all initialization processes
 * 
 * This class coordinates the execution of all data initialization components
 * in the correct order to ensure system integrity and proper dependencies.
 * 
 * Execution Order:
 * 1. PermissionRoleInitializer - Creates permissions and roles
 * 2. DefaultUserInitializer - Creates default users with assigned roles
 * 3. BookManagementDataInitializer - Creates sample categories and books
 * 4. Future initializers can be added here with proper ordering
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1) // Ensure this runs first among all CommandLineRunners
public class MainDataInitializer implements CommandLineRunner {

    private final PermissionRoleInitializer permissionRoleInitializer;
    private final DefaultUserInitializer defaultUserInitializer;
    private final BookManagementDataInitializer bookManagementDataInitializer;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 Starting Main Data Initialization Process...");
        
        try {
            // Step 1: Initialize Permissions and Roles
            log.info("📋 Step 1: Initializing Permissions and Roles...");
            permissionRoleInitializer.initializePermissionsAndRoles();
            log.info("✅ Permissions and Roles initialization completed");
            
            // Step 2: Initialize Default Users
            log.info("👥 Step 2: Initializing Default Users...");
            defaultUserInitializer.initializeDefaultUsers();
            log.info("✅ Default Users initialization completed");
            
            // Step 3: Initialize Sample Book Management Data
            log.info("📚 Step 3: Initializing Sample Book Management Data...");
            int[] sampleData = bookManagementDataInitializer.initializeSampleData();
            if (sampleData[0] > 0 || sampleData[1] > 0) {
                log.info("✅ Sample Book Management Data initialization completed");
            }
            
            // Future initialization steps can be added here
            // Example:
            // log.info("📊 Step 3: Initializing System Settings...");
            // systemSettingsInitializer.initializeSettings();
            
            log.info("🎉 Main Data Initialization Process completed successfully!");
            
        } catch (Exception e) {
            log.error("❌ Error during data initialization: {}", e.getMessage(), e);
            throw e; // Re-throw to prevent application startup with incomplete data
        }
    }
}
