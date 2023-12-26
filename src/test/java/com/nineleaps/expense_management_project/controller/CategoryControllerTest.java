package com.nineleaps.expense_management_project.controller;


import com.nineleaps.expense_management_project.dto.CategoryDTO;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.service.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private ICategoryService categoryService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO("Food");
        Category addedCategory = new Category();
        addedCategory.setCategoryId(1L);
        addedCategory.setCategoryDescription("Food");

        when(categoryService.addCategory(categoryDTO)).thenReturn(addedCategory);

        ResponseEntity<Object> response = categoryController.addCategory(categoryDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(addedCategory, response.getBody());
    }

    @Test
    void testAddCategoryBadRequest() {
        CategoryDTO categoryDTO = new CategoryDTO(null);

        when(categoryService.addCategory(categoryDTO)).thenThrow(new IllegalArgumentException("Invalid category name"));

        ResponseEntity<Object> response = categoryController.addCategory(categoryDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid category name", response.getBody());
    }

    @Test
    void testGetAllCategory() {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setCategoryId(1L);
        category1.setCategoryDescription("Food");

        Category category2 = new Category();
        category2.setCategoryId(2L);
        category2.setCategoryDescription("Travel");

        categories.add(category1);
        categories.add(category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        List<Category> response = categoryController.getAllCategory();

        assertEquals(categories, response);
    }

    @Test
    void testGetCategoryById() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryDescription("Food");

        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        Category response = categoryController.getCategoryById(categoryId).getBody();

        assertEquals(category, response);
    }

    @Test
    void testUpdateCategory() {
        Long categoryId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO("Groceries");
        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(categoryId);
        updatedCategory.setCategoryDescription("Groceries");

        when(categoryService.updateCategory(categoryId, categoryDTO)).thenReturn(updatedCategory);

        Category response = categoryController.updateCategory(categoryId, categoryDTO);

        assertEquals(updatedCategory, response);
    }

    @Test
    void testHideCategory() {
        Long categoryId = 1L;

        assertDoesNotThrow(() -> categoryController.hideCategory(categoryId));

        verify(categoryService, times(1)).hideCategory(categoryId);
    }

    @Test
    void testGetCategoryTotalAmount() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        HashMap<String, Double> categoryTotalAmount = new HashMap<>();
        categoryTotalAmount.put("Food", 500.0);
        categoryTotalAmount.put("Travel", 1000.0);

        when(categoryService.getCategoryTotalAmount(startDate, endDate)).thenReturn(categoryTotalAmount);

        Map<String, Double> response = categoryController.getCategoryTotalAmount(startDate, endDate);

        assertEquals(categoryTotalAmount, response);
    }

    @Test
    void testGetCategoryAnalyticsYearly() {
        Long categoryId = 1L;
        Map<String, Object> analyticsData = createSampleAnalyticsData(); // Create sample analytics data

        when(categoryService.getCategoryAnalyticsYearly(categoryId)).thenReturn((HashMap<String, Object>) analyticsData);

        Map<String, Object> response = categoryController.getCategoryAnalytics(categoryId);

        assertEquals(analyticsData, response);
    }

    // Create a sample analytics data map for testing
    private Map<String, Object> createSampleAnalyticsData() {
        Map<String, Object> analyticsData = new HashMap<>();
        analyticsData.put("year", 2023);
        analyticsData.put("categoryName", "Food");
        analyticsData.put("totalExpenses", 1500.0);
        analyticsData.put("averageExpense", 500.0);
        // Add more data as needed for your specific test case

        return analyticsData;
    }

    @Test
    void testGetCategoryAnalyticsMonthly() {
        Long categoryId = 1L;
        Long year = 2023L;
        Map<String, Object> analyticsData = createSampleAnalyticsData(); // Create sample analytics data

        when(categoryService.getCategoryAnalyticsMonthly(categoryId, year)).thenReturn((HashMap<String, Object>) analyticsData);

        Map<String, Object> response = categoryController.getCategoryAnalytics(categoryId, year);

        assertEquals(analyticsData, response);
    }

    @Test
    void testGetYearlyCategoryAnalyticsForAllCategories() {
        Map<String, Object> yearlyAnalyticsData = createSampleYearlyAnalyticsData(); // Create sample yearly analytics data

        when(categoryService.getYearlyCategoryAnalyticsForAllCategories()).thenReturn((HashMap<String, Object>) yearlyAnalyticsData);

        Map<String, Object> response = categoryController.getYearlyCategoryAnalyticsForAllCategories();

        assertEquals(yearlyAnalyticsData, response);
    }

    @Test
    void testGetMonthlyCategoryAnalyticsForAllCategories() {
        Long year = 2023L;
        Map<String, Object> monthlyAnalyticsData = createSampleMonthlyAnalyticsData(); // Create sample monthly analytics data

        when(categoryService.getMonthlyCategoryAnalyticsForAllCategories(year)).thenReturn((HashMap<String, Object>) monthlyAnalyticsData);

        Map<String, Object> response = categoryController.getMonthlyCategoryAnalyticsForAllCategories(year);

        assertEquals(monthlyAnalyticsData, response);
    }

    private Map<String, Object> createSampleYearlyAnalyticsData() {
        Map<String, Object> yearlyAnalyticsData = new HashMap<>();
        yearlyAnalyticsData.put("year", 2023);
        yearlyAnalyticsData.put("totalExpenses", 1500.0);
        // Add more data as needed for your specific test case

        return yearlyAnalyticsData;
    }

    // Create a sample monthly analytics data map for testing
    private Map<String, Object> createSampleMonthlyAnalyticsData() {
        Map<String, Object> monthlyAnalyticsData = new HashMap<>();
        monthlyAnalyticsData.put("year", 2023);
        monthlyAnalyticsData.put("month", "January");
        monthlyAnalyticsData.put("totalExpenses", 500.0);
        // Add more data as needed for your specific test case

        return monthlyAnalyticsData;
    }
}