package com.nineleaps.expense_management_project.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.nineleaps.expense_management_project.dto.CategoryDTO;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.service.ICategoryService;

@RestController
@ApiResponses(
        value = {
                @ApiResponse(code = 200, message = "SUCCESS"),
                @ApiResponse(code = 401, message = "UNAUTHORIZED"),
                @ApiResponse(code = 403, message = "ACCESS_FORBIDDEN"),
                @ApiResponse(code = 404, message = "NOT_FOUND"),
                @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
        })

public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/insertCategory")
    public ResponseEntity<Object> addCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category addedCategory = categoryService.addCategory(categoryDTO);
            return new ResponseEntity<>(addedCategory, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/showAllCategories")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/findCategory/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateCategory/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {

        return categoryService.updateCategory(categoryId,categoryDTO);
    }

    @PostMapping("/hideCategory/{categoryId}")
    public void hideCategory(@PathVariable Long categoryId) {
        categoryService.hideCategory(categoryId);
    }

    @GetMapping("/categoryTotalAmount")
    public Map<String, Double> getCategoryTotalAmount(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return categoryService.getCategoryTotalAmount(startDate, endDate);
    }


    @GetMapping("/getCategoryAnalyticsYearly/{categoryId}")
    public Map<String, Object> getCategoryAnalytics(@PathVariable Long categoryId) {
        return categoryService.getCategoryAnalyticsYearly(categoryId);
    }

    @GetMapping("/getCategoryAnalyticsMonthly/{categoryId}")
    public Map<String, Object> getCategoryAnalytics(@PathVariable Long categoryId, @RequestParam Long year) {
        return categoryService.getCategoryAnalyticsMonthly(categoryId, year);
    }


    @GetMapping("/getAllCategoryAnalyticsYearly")
    public Map<String, Object> getYearlyCategoryAnalyticsForAllCategories() {
        return categoryService.getYearlyCategoryAnalyticsForAllCategories();
    }

    @GetMapping("/getAllCategoryAnalyticsMonthly")
    public Map<String, Object> getMonthlyCategoryAnalyticsForAllCategories(@RequestParam Long year) {
        return categoryService.getMonthlyCategoryAnalyticsForAllCategories(year);
    }

}

