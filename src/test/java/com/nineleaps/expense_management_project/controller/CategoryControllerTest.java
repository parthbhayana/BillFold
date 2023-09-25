package com.nineleaps.expense_management_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private ObjectMapper objectMapper = new ObjectMapper();

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

        Category response = categoryController.getCategoryById(categoryId);

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
        Map<String, Double> categoryTotalAmount = new HashMap<>();
        categoryTotalAmount.put("Food", 500.0);
        categoryTotalAmount.put("Travel", 1000.0);

        when(categoryService.getCategoryTotalAmount(startDate, endDate)).thenReturn((HashMap<String, Double>) categoryTotalAmount);

        Map<String, Double> response = categoryController.getCategoryTotalAmount(startDate, endDate);

        assertEquals(categoryTotalAmount, response);
    }

    // Add tests for other endpoints as needed...
}
