package com.nineleaps.expense_management_project.service;

import com.nineleaps.expense_management_project.dto.CategoryDTO;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;


    @Mock
    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO("Test Category");
        Category category = new Category();
        category.setCategoryDescription(categoryDTO.getCategoryDescription());

        when(categoryRepository.existsByCategoryDescription(categoryDTO.getCategoryDescription())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.addCategory(categoryDTO);

        assertNotNull(savedCategory);
        assertEquals(categoryDTO.getCategoryDescription(), savedCategory.getCategoryDescription());
    }

    @Test
    public void testUpdateCategory() {
        Long categoryId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO("Updated Category");
        Category existingCategory = new Category();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setCategoryDescription("Existing Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        Category updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);

        assertNotNull(updatedCategory);
        assertEquals(categoryDTO.getCategoryDescription(), updatedCategory.getCategoryDescription());
        assertEquals(categoryId, updatedCategory.getCategoryId());
    }

    @Test
    public void testGetAllCategories() {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setCategoryDescription("Category 1");
        Category category2 = new Category();
        category2.setCategoryDescription("Category 2");
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteCategoryById() {
        // Arrange
        Long categoryId = 1L; // Replace with a valid category ID

        // Act
        categoryService.deleteCategoryById(categoryId);

        // Assert
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }


    @Test
    public void testGetCategoryTotalAmount() {
        // Create test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Category category1 = new Category();
        category1.setCategoryDescription("Category1");

        Category category2 = new Category();
        category2.setCategoryDescription("Category2");

        Expense expense1 = new Expense();
        expense1.setAmount(100.0);
        expense1.setCategory(category1);
        expense1.setIsReported(true);

        Expense expense2 = new Expense();
        expense2.setAmount(200.0);
        expense2.setCategory(category2);
        expense2.setIsReported(true);

        Expense expense3 = new Expense();
        expense3.setAmount(150.0);
        expense3.setCategory(category1);
        expense3.setIsReported(true);

        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense1);
        expenseList.add(expense2);
        expenseList.add(expense3);

        // Mock the behavior of the expenseRepository
        when(expenseRepository.findByDateBetweenAndIsReported(startDate, endDate, true))
                .thenReturn(expenseList);

        // Call the method to be tested
        HashMap<String, Double> result = categoryService.getCategoryTotalAmount(startDate, endDate);

        // Verify the result
        assertEquals(2, result.size()); // There are two categories

        // Check category totals
        assertEquals(250.0, result.get("Category1"));
        assertEquals(200.0, result.get("Category2"));
    }

    @Test
    public void testGetCategoryTotalAmountByYearAndCategory() {
        // Create test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        Expense expense1 = new Expense();
        expense1.setAmount(100.0);
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setCategory(category);
        expense1.setIsReported(true);

        Expense expense2 = new Expense();
        expense2.setAmount(200.0);
        expense2.setDate(LocalDate.of(2023, 2, 20));
        expense2.setCategory(category);
        expense2.setIsReported(true);

        Expense expense3 = new Expense();
        expense3.setAmount(150.0);
        expense3.setDate(LocalDate.of(2022, 12, 10));
        expense3.setCategory(category);
        expense3.setIsReported(true);

        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense1);
        expenseList.add(expense2);
        expenseList.add(expense3);

        // Mock the behavior of the categoryRepository
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Mock the behavior of the expenseRepository
        when(expenseRepository.findByCategoryAndIsReported(category, true))
                .thenReturn(expenseList);

        // Call the method to be tested
        Map<String, Double> result = categoryService.getCategoryTotalAmountByYearAndCategory(categoryId);

        // Verify the result
        assertEquals(2, result.size()); // There are two years

        // Check year-wise totals
        assertEquals(300.0, result.get("2023"));
        assertEquals(150.0, result.get("2022"));
    }

    @Test
    public void testGetCategoryTotalAmountByMonthAndCategory() {
        // Create test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        LocalDate currentDate = LocalDate.now();
        Long year = (long) currentDate.getYear();

        Expense expense1 = new Expense();
        expense1.setAmount(100.0);
        expense1.setDate(LocalDate.of(currentDate.getYear(), 1, 15));
        expense1.setCategory(category);
        expense1.setIsReported(true);

        Expense expense2 = new Expense();
        expense2.setAmount(200.0);
        expense2.setDate(LocalDate.of(currentDate.getYear(), 2, 20));
        expense2.setCategory(category);
        expense2.setIsReported(true);

        Expense expense3 = new Expense();
        expense3.setAmount(150.0);
        expense3.setDate(LocalDate.of(currentDate.getYear() - 1, 12, 10)); // Last year
        expense3.setCategory(category);
        expense3.setIsReported(true);

        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense1);
        expenseList.add(expense2);
        expenseList.add(expense3);

        // Mock the behavior of the categoryRepository
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Mock the behavior of the expenseRepository
        when(expenseRepository.findByCategoryAndIsReported(category, true))
                .thenReturn(expenseList);

        // Call the method to be tested
        Map<String, Double> result = categoryService.getCategoryTotalAmountByMonthAndCategory(categoryId, year);

        // Verify the result
        assertEquals(2, result.size()); // There are two months in the current year

        // Check month-wise totals
        assertEquals(100.0, result.get("Jan"));
        assertEquals(200.0, result.get("Feb"));
    }

    @Test
    public void testGetMonthlyReimbursementRatio() {
        // Create test data
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        Long year = 2023L;

        Expense expense1 = new Expense();
        expense1.setAmountApproved(100.0);
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setCategory(category);
        expense1.setIsReported(true);

        Expense expense2 = new Expense();
        expense2.setAmountApproved(200.0);
        expense2.setDate(LocalDate.of(2023, 2, 20));
        expense2.setCategory(category);
        expense2.setIsReported(true);

        Expense expense3 = new Expense();
        expense3.setAmountApproved(150.0);
        expense3.setDate(LocalDate.of(2022, 12, 10)); // Not in the specified year
        expense3.setCategory(category);
        expense3.setIsReported(true);

        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense1);
        expenseList.add(expense2);
        expenseList.add(expense3);

        // Mock the behavior of the categoryRepository
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Mock the behavior of the expenseRepository
        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        // Call the method to be tested
        Map<String, Double> result = categoryService.getMonthlyReimbursementRatio(categoryId, year);

        // Verify the result
        assertEquals(6, result.size());

        // Check month-wise ratios

    }

    @Test
    public void testGetTotalAmountByYearForAllCategories() {
        // Create a list of expenses for testing
        List<Expense> expenses = new ArrayList<>();
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setAmount(100.0);
        expenses.add(expense1);

        // Mock the behavior of the expenseRepository
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenses);

        // Call the method under test
        HashMap<String, Double> result = categoryService.getTotalAmountByYearForAllCategories();

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("2023"));
        assertEquals(100.0, result.get("2023"));

        // Verify that the expenseRepository method was called
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    public void testGetTotalAmountByYearForAllCategoriesWithEmptyList() {
        // Mock the behavior of the expenseRepository to return an empty list
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(new ArrayList<>());

        // Call the method under test
        HashMap<String, Double> result =  categoryService.getTotalAmountByYearForAllCategories();

        // Assertions
        assertNotNull(result);
        assertEquals(0, result.size());

        // Verify that the expenseRepository method was called
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    public void testGetTotalAmountByYearForAllCategoriesWithNullExpenses() {
        // Mock the behavior of the expenseRepository to return null
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(null);

        // Call the method under test and expect a NullPointerException
        assertThrows(NullPointerException.class, () -> {
            categoryService.getTotalAmountByYearForAllCategories();
        });

        // Verify that the expenseRepository method was called
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    // Add more test cases for edge cases and scenarios with different data

    @Test
    public void testGetYearlyReimbursementRatioForAllCategories() {
        // Create a list of expenses for testing
        List<Expense> expenses = new ArrayList<>();
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setAmountApproved(100.0);
        expenses.add(expense1);

        // Mock the behavior of the expenseRepository
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenses);

        // Call the method under test
        HashMap<String, Double> result = categoryService.getYearlyReimbursementRatioForAllCategories();

        // Assertions
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("2023"));
        assertEquals(100.0, result.get("2023"));
//        assertTrue(result.containsKey("2023_1"));
//        assertEquals(1.0, result.get("2023_1"));
//        assertTrue(result.containsKey("2023_2"));
//        assertEquals(100.0, result.get("2023_2"));

        // Verify that the expenseRepository method was called
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    public void testGetYearlyReimbursementRatioForAllCategoriesWithEmptyList() {
        // Mock the behavior of the expenseRepository to return an empty list
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(new ArrayList<>());

        // Call the method under test
        HashMap<String, Double> result = categoryService.getYearlyReimbursementRatioForAllCategories();

        // Assertions
        assertNotNull(result);
        assertEquals(0, result.size());

        // Verify that the expenseRepository method was called
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    public void testHideCategory() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setIsHidden(false); // Initially not hidden

        // Mock the behavior of categoryRepository.findById
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Call the method under test
        categoryService.hideCategory(categoryId);

        // Assert that the category's isHidden property is set to true
        assertTrue(category.getIsHidden());

        // Verify that categoryRepository.save() was called
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testGetYearlyReimbursementRatio() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        // Mock the behavior of getCategoryById
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Create a list of expenses for testing
        List<Expense> expenseList = new ArrayList<>();
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setAmountApproved(100.0);
        expense1.setIsReported(true);
        expenseList.add(expense1);

        // Mock the behavior of expenseRepository.findByCategory
        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        // Call the method under test
        HashMap<String, Double> result = categoryService.getYearlyReimbursementRatio(categoryId);

        // Assertions
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("2023"));
        assertEquals(100.0, result.get("2023"));
//        assertTrue(result.containsKey("2023_1"));
//        assertEquals(1.0, result.get("2023_1"));
//        assertTrue(result.containsKey("2023_2"));
//        assertEquals(100.0, result.get("2023_2"));

        // Verify that categoryRepository.findById and expenseRepository.findByCategory were called
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(expenseRepository, times(1)).findByCategory(category);
    }

//    @Test
//    public void testGetCategoryAnalyticsYearly() {
//        // Arrange
//        Long categoryId = 1L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//
//        // Mock the behavior of getCategoryById
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        // Mock the behavior of getCategoryTotalAmountByYearAndCategory
//        Map<String, Double> categoryTotalAmountByYear = new HashMap<>();
//        categoryTotalAmountByYear.put("2022", 1000.0);
//        when(categoryService.getCategoryTotalAmountByYearAndCategory(categoryId)).thenReturn(categoryTotalAmountByYear);
//
//        // Mock the behavior of getYearlyReimbursementRatio
//        HashMap<String, Double> yearlyReimbursementRatio = new HashMap<>();
//        yearlyReimbursementRatio.put("2022", 0.75);
//        when(categoryService.getYearlyReimbursementRatio(categoryId)).thenReturn(yearlyReimbursementRatio);
//
//        // Mock the behavior of expenseRepository.findByCategory
//        List<Expense> expenseList = new ArrayList<>();
//        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);
//
//        // Act
//        Map<String, Object> result = categoryService.getCategoryAnalyticsYearly(categoryId);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.containsKey("categoryTotalAmountByYear"));
//        assertTrue(result.containsKey("yearlyReimbursementRatio"));
//
//        Map<String, Double> resultCategoryTotalAmount = (Map<String, Double>) result.get("categoryTotalAmountByYear");
//        Map<String, Double> resultYearlyReimbursementRatio = (Map<String, Double>) result.get("yearlyReimbursementRatio");
//
//        assertEquals(1, resultCategoryTotalAmount.size());
//        assertEquals(1, resultYearlyReimbursementRatio.size());
//        assertEquals(1000.0, resultCategoryTotalAmount.get("2022"));
//        assertEquals(0.75, resultYearlyReimbursementRatio.get("2022"));
//
//        // Verify that necessary methods were called
//        verify(categoryRepository, times(1)).findById(categoryId);
//        verify(categoryService, times(1)).getCategoryTotalAmountByYearAndCategory(categoryId);
//        verify(categoryService, times(1)).getYearlyReimbursementRatio(categoryId);
//        verify(expenseRepository, times(1)).findByCategory(category);
//    }



}