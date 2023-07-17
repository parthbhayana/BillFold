package com.nineleaps.expensemanagementproject.controller;

import com.nineleaps.expensemanagementproject.DTO.CategoryDTO;
import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.service.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CategoryControllerTest {
    @Mock
    private ICategoryService categoryService;

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





}
