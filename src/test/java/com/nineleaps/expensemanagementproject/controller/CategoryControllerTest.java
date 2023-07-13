//package com.nineleaps.expensemanagementproject.controller;
//
//import com.nineleaps.expensemanagementproject.entity.Category;
//import com.nineleaps.expensemanagementproject.service.ICategoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class CategoryControllerTest {
//
//    @Mock
//    private ICategoryService categoryService;
//
//    private CategoryController categoryController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        categoryController = new CategoryController();
//        categoryController.setC(categoryService);
//    }
//
//    @Test
//    void addCategory() {
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setCategoryDescription("Groceries");
//
//        when(categoryService.addCategory(category)).thenReturn(category);
//
//        Category result = categoryController.addCategory(category);
//
//        assertEquals(category, result);
//        verify(categoryService, times(1)).addCategory(category);
//    }
//
//    @Test
//    void getAllCategory() {
//        Category category1 = new Category();
//        category1.setCategoryId(1L);
//        category1.setCategoryDescription("Groceries");
//
//        Category category2 = new Category();
//        category2.setCategoryId(2L);
//        category2.setCategoryDescription("Electronics");
//
//        List<Category> categories = Arrays.asList(category1, category2);
//
//        when(categoryService.getAllCategories()).thenReturn(categories);
//
//        List<Category> result = categoryController.getAllCategory();
//
//        assertEquals(categories, result);
//        verify(categoryService, times(1)).getAllCategories();
//    }
//
//    @Test
//    void getCategoryById() {
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setCategoryDescription("Groceries");
//
//        when(categoryService.getCategoryById(1L)).thenReturn(category);
//
//        Category result = categoryController.getCategoryById(1L);
//
//        assertEquals(category, result);
//        verify(categoryService, times(1)).getCategoryById(1L);
//    }
//    @Test
//    void updateCategory() {
//        Long categoryId = 1L;
//        Category existingCategory = new Category();
//        existingCategory.setCategoryId(categoryId);
//        existingCategory.setCategoryDescription("Groceries");
//
//        Category updatedCategory = new Category();
//        updatedCategory.setCategoryId(categoryId);
//        updatedCategory.setCategoryDescription("Food");
//
//        when(categoryService.getCategoryById(categoryId)).thenReturn(existingCategory);
//        when(categoryService.updateCategory(existingCategory)).thenReturn(updatedCategory);
//
//        Category result = categoryController.updateCategory(categoryId, updatedCategory);
//
//        assertEquals(updatedCategory, result);
//        assertEquals("Food", result.getCategoryDescription());
//        verify(categoryService, times(1)).getCategoryById(categoryId);
//        verify(categoryService, times(1)).updateCategory(existingCategory);
//    }
//
//    @Test
//    void hideCategory() {
//        Long categoryId = 1L;
//
//        categoryController.hideCategory(categoryId);
//
//        verify(categoryService, times(1)).hideCategory(categoryId);
//    }
//
//    @Test
//    void getCategoryTotalAmount() {
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 12, 31);
//
//        Map<String, Float> categoryTotalAmounts = new HashMap<>();
//        categoryTotalAmounts.put("Groceries", 1000.0f);
//        categoryTotalAmounts.put("Electronics", 2000.0f);
//
//        when(categoryService.getCategoryTotalAmount(startDate, endDate)).thenReturn((HashMap<String, Float>) categoryTotalAmounts);
//
//        Map<String, Float> result = categoryController.getCategoryTotalAmount(startDate, endDate);
//
//        assertEquals(categoryTotalAmounts, result);
//        verify(categoryService, times(1)).getCategoryTotalAmount(startDate, endDate);
//    }
//
//    @Test
//    void getCategoryAnalytics() {
//        Long categoryId = 1L;
//
//        Map<String, Object> categoryAnalytics = new HashMap<>();
//
//
//        when(categoryService.getCategoryAnalyticsYearly(categoryId)).thenReturn((HashMap<String, Object>) categoryAnalytics);
//
//        Map<String, Object> result = categoryController.getCategoryAnalytics(categoryId);
//
//        assertEquals(categoryAnalytics, result);
//        verify(categoryService, times(1)).getCategoryAnalyticsYearly(categoryId);
//    }
//
//    @Test
//    void testGetCategoryAnalytics() {
//        Long categoryId = 1L;
//        Long year = 2023L;
//
//        Map<String, Object> categoryAnalytics = new HashMap<>();
//        // Add test data to categoryAnalytics map
//
//        when(categoryService.getCategoryAnalyticsMonthly(categoryId, year)).thenReturn((HashMap<String, Object>) categoryAnalytics);
//
//        Map<String, Object> result = categoryController.getCategoryAnalytics(categoryId, year);
//
//        assertEquals(categoryAnalytics, result);
//        verify(categoryService, times(1)).getCategoryAnalyticsMonthly(categoryId, year);
//    }
//
//    @Test
//    void getYearlyCategoryAnalyticsForAllCategories() {
//        Map<String, Object> yearlyCategoryAnalytics = new HashMap<>();
//
//
//        when(categoryService.getYearlyCategoryAnalyticsForAllCategories()).thenReturn((HashMap<String, Object>) yearlyCategoryAnalytics);
//
//        Map<String, Object> result = categoryController.getYearlyCategoryAnalyticsForAllCategories();
//
//        assertEquals(yearlyCategoryAnalytics, result);
//        verify(categoryService, times(1)).getYearlyCategoryAnalyticsForAllCategories();
//    }
//
//    @Test
//    void getMonthlyCategoryAnalyticsForAllCategories() {
//        Long year = 2023L;
//
//        Map<String, Object> monthlyCategoryAnalytics = new HashMap<>();
//
//
//        when(categoryService.getMonthlyCategoryAnalyticsForAllCategories(year)).thenReturn((HashMap<String, Object>) monthlyCategoryAnalytics);
//
//        Map<String, Object> result = categoryController.getMonthlyCategoryAnalyticsForAllCategories(year);
//
//        assertEquals(monthlyCategoryAnalytics, result);
//        verify(categoryService, times(1)).getMonthlyCategoryAnalyticsForAllCategories(year);
//    }
//}