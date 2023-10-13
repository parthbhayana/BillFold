package com.nineleaps.expense_management_project.entity;


import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CategoryTest {

    private Category category;

    @Before
    public void setUp() {
        category = new Category();
    }

    @Test
    public void testGetCategoryId() {
        category.setCategoryId(1L);
        assertEquals(1L, category.getCategoryId().longValue());
    }

    @Test
    public void testSetCategoryId() {
        category.setCategoryId(2L);
        assertEquals(2L, category.getCategoryId().longValue());
    }

    @Test
    public void testGetCategoryDescription() {
        category.setCategoryDescription("Food");
        assertEquals("Food", category.getCategoryDescription());
    }

    @Test
    public void testSetCategoryDescription() {
        category.setCategoryDescription("Travel");
        assertEquals("Travel", category.getCategoryDescription());
    }




    @Test
    public void testGetIsHidden() {
        category.setIsHidden(true);
        assertTrue(category.getIsHidden());
    }


    @Test
    public void testSetIsHidden() {
        // Create a Category object
        Category category = new Category(1L, "Test Category", 1000L, false, null);

        // Set the isHidden property to true using setIsHidden() method
        category.setIsHidden(true);

        // Get the updated isHidden property
        Boolean updatedIsHidden = category.getIsHidden();

        // Assert that the updated isHidden matches the expected value
        assertTrue(updatedIsHidden);
    }
    @Test
    public void testGetExpenseList() {
        // Create a Category object
        Category category = new Category(1L, "Test Category", 1000L, false, null);

        // Create a list of expenses
        List<Expense> expenses = new ArrayList<>();
        Expense expense1 = new Expense();
        Expense expense2 = new Expense();
        expenses.add(expense1);
        expenses.add(expense2);

        // Set the expenseList property using setExpenseList() method
        category.setExpenseList(expenses);

        // Get the list of expenses using the getExpenseList() method
        List<Expense> retrievedExpenses = category.getExpenseList();

        // Assert that the retrieved list of expenses matches the expected list of expenses
        assertEquals(expenses, retrievedExpenses);
    }

    @Test
    public void testSetExpenseList() {
        // Create a Category object
        Category category = new Category(1L, "Test Category", 1000L, false, null);

        // Create a list of expenses
        List<Expense> expenses = new ArrayList<>();
        Expense expense1 = new Expense();
        Expense expense2 = new Expense();
        expenses.add(expense1);
        expenses.add(expense2);

        // Set the expenseList property using setExpenseList() method
        category.setExpenseList(expenses);

        // Get the updated list of expenses
        List<Expense> retrievedExpenses = category.getExpenseList();

        // Assert that the retrieved list of expenses matches the expected list
        assertEquals(expenses, retrievedExpenses);
    }

    @Test
    public void testCategoryConstructor() {
        // Arrange
        Long categoryId = 1L;
        String categoryDescription = "Office Supplies";
        long categoryTotal = 5000L;
        Boolean isHidden = false;
        List<Expense> expenseList = new ArrayList<>(); // Create an empty list or add sample expenses

        // Act
        Category category = new Category(
                categoryId, categoryDescription, categoryTotal, isHidden, expenseList);

        // Assert
        assertNotNull(category);
        assertEquals(categoryId, category.getCategoryId());
        assertEquals(categoryDescription, category.getCategoryDescription());
        assertEquals(categoryTotal, category.getCategoryTotal());
        assertEquals(isHidden, category.getIsHidden());
        assertEquals(expenseList, category.getExpenseList());
    }

    @Test
    public void testGetCategoryTotal() {
        // Arrange
        Long categoryId = 1L;
        String categoryDescription = "Office Supplies";
        long categoryTotal = 5000L;
        Boolean isHidden = false;
        Category category = new Category(categoryId, categoryDescription, categoryTotal, isHidden, null);

        // Act
        long retrievedCategoryTotal = category.getCategoryTotal();

        // Assert
        assertEquals(categoryTotal, retrievedCategoryTotal);
    }

    @Test
    public void testSetCategoryTotal() {
        // Arrange
        Long categoryId = 1L;
        String categoryDescription = "Office Supplies";
        long initialCategoryTotal = 5000L;
        Boolean isHidden = false;
        Category category = new Category(categoryId, categoryDescription, initialCategoryTotal, isHidden, null);

        // Act
        long newCategoryTotal = 7500L;
        category.setCategoryTotal(newCategoryTotal);

        // Assert
        assertEquals(newCategoryTotal, category.getCategoryTotal());
    }

    @Test
    public void testGetAndSetExpenseList() {
        // Arrange
        Long categoryId = 1L;
        String categoryDescription = "Office Supplies";
        long categoryTotal = 5000L;
        Boolean isHidden = false;
        Category category = new Category(categoryId, categoryDescription, categoryTotal, isHidden, null);

        // Create a list of expenses
        List<Expense> initialExpenseList = new ArrayList<>();
        Expense expense1 = new Expense();
        Expense expense2 = new Expense();
        initialExpenseList.add(expense1);
        initialExpenseList.add(expense2);

        // Act
        category.setExpenseList(initialExpenseList);
        List<Expense> retrievedExpenseList = category.getExpenseList();

        // Assert
        assertEquals(initialExpenseList, retrievedExpenseList);
    }

    @Test
    public void testCategoryConstructor_NullExpenseList() {
        // Arrange
        Long categoryId = 1L;
        String categoryDescription = "Office Supplies";
        long categoryTotal = 5000L;
        Boolean isHidden = false;

        // Act
        Category category = new Category(categoryId, categoryDescription, categoryTotal, isHidden, null);

        // Assert
        assertNotNull(category);
        assertEquals(categoryId, category.getCategoryId());
        assertEquals(categoryDescription, category.getCategoryDescription());
        assertEquals(categoryTotal, category.getCategoryTotal());
        assertEquals(isHidden, category.getIsHidden());
        assertNull(category.getExpenseList());
    }
    @Test
    public void testGettersAndSetters() {
            Category category = new Category();

            // Test setter and getter for categoryId
            category.setCategoryId(1L);
            assertEquals(1L, category.getCategoryId().longValue());

            // Test setter and getter for categoryDescription
            category.setCategoryDescription("Test Category");
            assertEquals("Test Category", category.getCategoryDescription());

            // Test setter and getter for categoryTotal
            category.setCategoryTotal(1000L);
            assertEquals(1000L, category.getCategoryTotal());

            // Test setter and getter for isHidden
            category.setIsHidden(true);
            assertTrue(category.getIsHidden());

            // Test setter and getter for expenseList
            List<Expense> expenseList = new ArrayList<>();
            Expense expense1 = new Expense();
            Expense expense2 = new Expense();
            expenseList.add(expense1);
            expenseList.add(expense2);
            category.setExpenseList(expenseList);
            assertEquals(expenseList, category.getExpenseList());
        }

        @Test
        public void testParameterizedConstructor() {
            Long categoryId = 1L;
            String categoryDescription = "Test Category";
            long categoryTotal = 1000L;
            Boolean isHidden = true;
            List<Expense> expenseList = new ArrayList<>(); // Create an empty list or add sample expenses

            Category category = new Category(categoryId, categoryDescription, categoryTotal, isHidden, expenseList);

            assertEquals(categoryId, category.getCategoryId());
            assertEquals(categoryDescription, category.getCategoryDescription());
            assertEquals(categoryTotal, category.getCategoryTotal());
            assertEquals(isHidden, category.getIsHidden());
            assertEquals(expenseList, category.getExpenseList());
        }

    @Test
    public void testCategoryConstructorWithParameters() {
        Long categoryId = 1L;
        String categoryDescription = "Test Category";
        long categoryTotal = 1000L;
        Boolean isHidden = true;
        List<Expense> expenseList = new ArrayList<>();

        Category category = new Category(categoryId, categoryDescription, categoryTotal, isHidden, expenseList);

        assertEquals(categoryId, category.getCategoryId());
        assertEquals(categoryDescription, category.getCategoryDescription());
        assertEquals(categoryTotal, category.getCategoryTotal());
        assertEquals(isHidden, category.getIsHidden());
        assertEquals(expenseList, category.getExpenseList());
    }

    }



