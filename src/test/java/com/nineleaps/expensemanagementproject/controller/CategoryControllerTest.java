package com.nineleaps.expensemanagementproject.controller;

import com.nineleaps.expensemanagementproject.DTO.CategoryDTO;
import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.service.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CategoryControllerTest {
    @Mock
    private ICategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCategory_ValidCategoryDTO_ReturnsCategory() {
        // Prepare test data
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryDescription("Test Category");

        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryDescription("Test Category");

        // Mock the categoryService
        when(categoryService.addCategory(categoryDTO)).thenReturn(category);

        // Call the addCategory method
        Category result = categoryController.addCategory(categoryDTO);

        // Verify the categoryService was called and the result
        verify(categoryService).addCategory(categoryDTO);
        assertNotNull(result);
        assertEquals(category, result);
    }

    @Test
    void getAllCategory_ReturnsListOfCategories() {
        // Prepare test data
        List<Category> categories = Arrays.asList(new Category(), new Category(), new Category());

        // Mock the categoryService
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Call the getAllCategory method
        List<Category> result = categoryController.getAllCategory();

        // Verify the categoryService was called and the result
        verify(categoryService).getAllCategories();
        assertNotNull(result);
        assertEquals(categories, result);
    }

    @Test
    void getCategoryById_WithValidCategoryId_ReturnsCategory() {
        // Prepare test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryDescription("Test Category");

        // Mock the categoryService
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        // Call the getCategoryById method
        Category result = categoryController.getCategoryById(categoryId);

        // Verify the categoryService was called and the result
        verify(categoryService).getCategoryById(categoryId);
        assertNotNull(result);
        assertEquals(category, result);
    }

    @Test
    void updateCategory_WithValidCategoryIdAndCategoryDTO_ReturnsUpdatedCategory() {
        // Prepare test data
        Long categoryId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryDescription("Updated Category");

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(categoryId);
        updatedCategory.setCategoryDescription("Updated Category");

        // Mock the categoryService
        when(categoryService.updateCategory(categoryId, categoryDTO)).thenReturn(updatedCategory);

        // Call the updateCategory method
        Category result = categoryController.updateCategory(categoryId, categoryDTO);

        // Verify the categoryService was called and the result
        verify(categoryService).updateCategory(categoryId, categoryDTO);
        assertNotNull(result);
        assertEquals(updatedCategory, result);
    }

    @Test
    void hideCategory_WithValidCategoryId_VerifyCategoryServiceCalled() {
        // Prepare test data
        Long categoryId = 1L;

        // Call the hideCategory method
        categoryController.hideCategory(categoryId);

        // Verify the categoryService was called
        verify(categoryService).hideCategory(categoryId);
    }

    @Test
    void testGetCategoryTotalAmount() {
        // Mock input parameters
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // Mock the expected result
        HashMap<String, Float> expected = new HashMap<>();
        expected.put("Category1", 100.0f);
        expected.put("Category2", 200.0f);

        // Mock the categoryService method
        when(categoryService.getCategoryTotalAmount(startDate, endDate)).thenReturn(expected);

        // Call the controller method
        Map<String, Float> result = categoryController.getCategoryTotalAmount(startDate, endDate);

        // Verify the result
        assertEquals(expected, result);
    }
    @Test
    void testGetCategoryAnalytics() {
        // Mock input parameter
        Long categoryId = 1L;

        // Mock the expected result
        HashMap<String, Object> expected = new HashMap<>();
        expected.put("Year", 2023);
        expected.put("Category1", 100.0f);
        expected.put("Category2", 200.0f);

        // Mock the categoryService method
        when(categoryService.getCategoryAnalyticsYearly(categoryId)).thenReturn(expected);

        // Call the controller method
        Map<String, Object> result = categoryController.getCategoryAnalytics(categoryId);

        // Verify the result
        assertEquals(expected, result);
    }

    @Test
    void testGetYearlyCategoryAnalyticsForAllCategories() {
        // Mock the expected result
        HashMap<String, Object> expected = new HashMap<>();
        expected.put("Year", 2023);
        expected.put("Category1", 100.0f);
        expected.put("Category2", 200.0f);

        // Mock the categoryService method
        when(categoryService.getYearlyCategoryAnalyticsForAllCategories()).thenReturn(expected);

        // Call the controller method
        Map<String, Object> result = categoryController.getYearlyCategoryAnalyticsForAllCategories();

        // Verify the result
        assertEquals(expected, result);
    }
    @Test
    void testGetMonthlyCategoryAnalyticsForAllCategories() {
        // Mock input parameter
        Long year = 2023L;

        // Mock the expected result
        HashMap<String, Object> expected = new HashMap<>();
        expected.put("Month", "January");
        expected.put("Category1", 100.0f);
        expected.put("Category2", 200.0f);

        // Mock the categoryService method
        when(categoryService.getMonthlyCategoryAnalyticsForAllCategories(year)).thenReturn(expected);

        // Call the controller method
        Map<String, Object> result = categoryController.getMonthlyCategoryAnalyticsForAllCategories(year);

        // Verify the result
        assertEquals(expected, result);
    }


}
