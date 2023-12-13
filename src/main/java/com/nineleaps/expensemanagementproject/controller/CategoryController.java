package com.nineleaps.expensemanagementproject.controller;


/**
          CategoryController

             Manages operations related to expense categories including creation, retrieval, and analytics.

            ## Endpoints

              ### POST /insertCategory

             - **Description:** Adds a new category.
             - **Request Body:** CategoryDTO object containing category details.
             - **Returns:** ResponseEntity with the added category details or error message.

              ### GET /showAllCategories

              - **Description:** Retrieves all available categories.
                - **Returns:** List of all available Category objects.

              ### GET /findCategory/{categoryId}

              - **Description:** Retrieves a category by its ID.
              - **Path Variable:** categoryId (Long) - The ID of the category to be retrieved.
              - **Returns:** The Category object matching the provided ID.

              ### PUT /updateCategory/{categoryId}

              - **Description:** Updates a category based on the provided ID and details.
              - **Path Variable:** categoryId (Long) - The ID of the category to be updated.
              - **Request Body:** CategoryDTO object containing updated category details.
              - **Returns:** The updated Category object.

              ### POST /deleteCategory/{categoryId}

              - **Description:** Hides a category by its ID.
              - **Path Variable:** categoryId (Long) - The ID of the category to be hidden.

              ### GET /categoryTotalAmount

              - **Description:** Retrieves total category expenditure within a specified date range.
              - **Request Parameters:**
                  - startDate (LocalDate) - The start date of the date range.
                  - endDate (LocalDate) - The end date of the date range.
              - **Returns:** Map containing category names as keys and total amounts spent as values.

              ### GET /getCategoryAnalyticsYearly/{categoryId}

              - **Description:** Retrieves yearly analytics for a specific category.
              - **Path Variable:** categoryId (Long) - The ID of the category.
              - **Returns:** Map containing yearly analytics data for the category.

              ### GET /getCategoryAnalyticsMonthly/{categoryId}

              - **Description:** Retrieves monthly analytics for a specific category in a given year.
              - **Path Variable:** categoryId (Long) - The ID of the category.
              - **Request Parameter:** year (Long) - The year for which analytics data is requested.
              - **Returns:** Map containing monthly analytics data for the category in the specified year.

              ### GET /getAllCategoryAnalyticsYearly

              - **Description:** Retrieves yearly analytics for all categories.
              - **Returns:** Map containing yearly analytics data for all categories.

              ### GET /getAllCategoryAnalyticsMonthly

              - **Description:** Retrieves monthly analytics for all categories in a given year.
              - **Request Parameter:** year (Long) - The year for which analytics data is requested.
              - **Returns:** Map containing monthly analytics data for all categories in the specified year.
 */

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import com.nineleaps.expensemanagementproject.DTO.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.service.ICategoryService;

@RestController
@Slf4j
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/insertCategory")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN')")
    public ResponseEntity<Object> addCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            log.info("Received a request to add a category: {}", categoryDTO);

            Category addedCategory = categoryService.addCategory(categoryDTO);

            log.info("Category added successfully: {}", addedCategory);

            return new ResponseEntity<>(addedCategory, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error adding category: {}", e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            return new ResponseEntity<>("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/showAllCategories")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public List<Category> getAllCategory() {
        log.info("Fetching all categories.");
        List<Category> categories = categoryService.getAllCategories();
        log.info("Retrieved {} categories.", categories.size());
        return categories;
    }

    @GetMapping("/findCategory/{categoryId}")
    public Category getCategoryById(@PathVariable("categoryId") Long categoryId) {
        log.info("Fetching category with ID: {}", categoryId);
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            log.info("Category found: {}", category);
        } else {
            log.warn("Category with ID {} not found.", categoryId);
        }
        return category;
    }

    @PutMapping("/updateCategory/{categoryId}")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN')")
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        log.info("Updating category with ID: {}", categoryId);

        Category updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);

        if (updatedCategory != null) {
            log.info("Category updated successfully: {}", updatedCategory);
        } else {
            log.error("Failed to update category with ID: {}", categoryId);
        }

        return updatedCategory;
    }

    @PostMapping("/hideCategory/{categoryId}")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN')")
    public void hideCategory(@PathVariable Long categoryId) {
        log.info("Hiding category with ID: {}", categoryId);

        categoryService.hideCategory(categoryId);

        log.info("Category with ID {} has been hidden.", categoryId);
    }

    @GetMapping("/categoryTotalAmount")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN')")
    public HashMap<String, Double> getCategoryTotalAmount(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        log.info("Fetching total amount for categories between {} and {}", startDate, endDate);

        HashMap<String, Double> totalAmount = categoryService.getCategoryTotalAmount(startDate, endDate);

        log.info("Retrieved total amount for categories between {} and {}: {}", startDate, endDate, totalAmount);

        return totalAmount;
    }


    @GetMapping("/getCategoryAnalyticsYearly/{categoryId}")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN')")
    public Map<String, Object> getCategoryAnalytics(@PathVariable Long categoryId) {
        log.info("Fetching yearly analytics for category with ID: {}", categoryId);

        Map<String, Object> analytics = categoryService.getCategoryAnalyticsYearly(categoryId);

        log.info("Retrieved yearly analytics for category with ID {}: {}", categoryId, analytics);

        return analytics;
    }


    @GetMapping("/getCategoryAnalyticsMonthly/{categoryId}")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN')")
    public Map<String, Object> getCategoryAnalytics(
            @PathVariable Long categoryId,
            @RequestParam Long year) {

        log.info("Fetching monthly analytics for category with ID: {} for the year: {}", categoryId, year);

        Map<String, Object> analytics = categoryService.getCategoryAnalyticsMonthly(categoryId, year);

        log.info("Retrieved monthly analytics for category with ID {} for the year {}: {}", categoryId, year, analytics);

        return analytics;
    }


    @GetMapping("/getAllCategoryAnalyticsYearly")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN')")
    public Map<String, Object> getYearlyCategoryAnalyticsForAllCategories() {
        log.info("Fetching yearly analytics for all categories");

        Map<String, Object> yearlyAnalytics = categoryService.getYearlyCategoryAnalyticsForAllCategories();

        log.info("Retrieved yearly analytics for all categories: {}", yearlyAnalytics);

        return yearlyAnalytics;
    }

    @GetMapping("/getAllCategoryAnalyticsMonthly")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN')")
    public Map<String, Object> getMonthlyCategoryAnalyticsForAllCategories(@RequestParam Long year) {
        log.info("Fetching monthly analytics for all categories for the year: {}", year);

        Map<String, Object> monthlyAnalytics = categoryService.getMonthlyCategoryAnalyticsForAllCategories(year);

        log.info("Retrieved monthly analytics for all categories for the year {}: {}", year, monthlyAnalytics);

        return monthlyAnalytics;
    }

}