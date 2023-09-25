package com.nineleaps.expense_management_project.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.nineleaps.expense_management_project.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.service.ICategoryService;

@RestController
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    // Endpoint to add a new category
    @PostMapping("/insertCategory")
    public ResponseEntity<Object> addCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category addedCategory = categoryService.addCategory(categoryDTO);
            return new ResponseEntity<>(addedCategory, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to retrieve all categories
    @GetMapping("/showAllCategories")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategories();
    }

    // Endpoint to find a category by its ID
    @GetMapping("/findCategory/{categoryId}")
    public Category getCategoryById(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    // Endpoint to update a category by its ID
    @PutMapping("/updateCategory/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryId, categoryDTO);
    }

    // Endpoint to hide a category by its ID
    @PostMapping("/hideCategory/{categoryId}")
    public void hideCategory(@PathVariable Long categoryId) {
        categoryService.hideCategory(categoryId);
    }

    // Endpoint to get the total amount spent in categories within a date range
    @GetMapping("/categoryTotalAmount")
    public Map<String, Double> getCategoryTotalAmount(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return categoryService.getCategoryTotalAmount(startDate, endDate);
    }

    // Endpoint to get yearly analytics for a specific category
    @GetMapping("/getCategoryAnalyticsYearly/{categoryId}")
    public Map<String, Object> getCategoryAnalytics(@PathVariable Long categoryId) {
        return categoryService.getCategoryAnalyticsYearly(categoryId);
    }

    // Endpoint to get monthly analytics for a specific category and year
    @GetMapping("/getCategoryAnalyticsMonthly/{categoryId}")
    public Map<String, Object> getCategoryAnalytics(@PathVariable Long categoryId, @RequestParam Long year) {
        return categoryService.getCategoryAnalyticsMonthly(categoryId, year);
    }

    // Endpoint to get yearly analytics for all categories
    @GetMapping("/getAllCategoryAnalyticsYearly")
    public Map<String, Object> getYearlyCategoryAnalyticsForAllCategories() {
        return categoryService.getYearlyCategoryAnalyticsForAllCategories();
    }

    // Endpoint to get monthly analytics for all categories for a specific year
    @GetMapping("/getAllCategoryAnalyticsMonthly")
    public Map<String, Object> getMonthlyCategoryAnalyticsForAllCategories(@RequestParam Long year) {
        return categoryService.getMonthlyCategoryAnalyticsForAllCategories(year);
    }
}
