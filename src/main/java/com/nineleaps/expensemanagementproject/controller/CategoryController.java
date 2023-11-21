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
import java.util.List;
import java.util.Map;
import com.nineleaps.expensemanagementproject.DTO.CategoryDTO;
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
import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.service.ICategoryService;

@RestController
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
    public Category getCategoryById(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PutMapping("/updateCategory/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {

        return categoryService.updateCategory(categoryId,categoryDTO);
    }

    @PostMapping("/deleteCategory/{categoryId}")
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

