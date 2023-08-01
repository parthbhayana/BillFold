package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    private Category category;

    @BeforeEach
    void setUp() {
        Long categoryId = 1L;
        String categoryDescription = "Test Category";
        long categoryTotal = 1000L;
        Boolean isHidden = false;
        List<Expense> expenseList = new ArrayList<>();

        category = new Category(categoryId, categoryDescription, categoryTotal, isHidden, expenseList);
    }

    @Test
    void getCategoryId() {
        Long categoryId = 1L;
        assertEquals(categoryId, category.getCategoryId());
    }

    @Test
    void setCategoryId() {
        Long newCategoryId = 2L;
        category.setCategoryId(newCategoryId);
        assertEquals(newCategoryId, category.getCategoryId());
    }

    @Test
    void getCategoryDescription() {
        String categoryDescription = "Test Category";
        assertEquals(categoryDescription, category.getCategoryDescription());
    }

    @Test
    void setCategoryDescription() {
        String newCategoryDescription = "New Category";
        category.setCategoryDescription(newCategoryDescription);
        assertEquals(newCategoryDescription, category.getCategoryDescription());
    }

    @Test
    void getCategoryTotal() {
        long categoryTotal = 1000L;
        assertEquals(categoryTotal, category.getCategoryTotal());
    }

    @Test
    void setCategoryTotal() {
        long newCategoryTotal = 2000L;
        category.setCategoryTotal(newCategoryTotal);
        assertEquals(newCategoryTotal, category.getCategoryTotal());
    }

    @Test
    void getIsHidden() {
        Boolean isHidden = false;
        assertFalse(category.getIsHidden());
    }

    @Test
    void setIsHidden() {
        category.setIsHidden(true);
        assertTrue(category.getIsHidden());
    }

    @Test
    void getExpenseList() {
        assertNotNull(category.getExpenseList());
        assertTrue(category.getExpenseList().isEmpty());
    }

    @Test
    void setExpenseList() {
        Expense expense = new Expense();
        List<Expense> newExpenseList = new ArrayList<>();
        newExpenseList.add(expense);
        category.setExpenseList(newExpenseList);
        assertEquals(newExpenseList, category.getExpenseList());
    }
}