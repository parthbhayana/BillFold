package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.DTO.CategoryDTO;
import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteCategoryById() {
        // Arrange
        Long categoryId = 1L;

        // Act
        categoryService.deleteCategoryById(categoryId);

        // Assert
        verify(categoryRepository).deleteById(categoryId);
    }


    @Test
    void testGetCategoryById_ExistingCategory() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        Category retrievedCategory = categoryService.getCategoryById(categoryId);

        // Assert
        assertEquals(category, retrievedCategory);
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void testGetCategoryById_NonExistingCategory() {
        // Arrange
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act
        Category retrievedCategory = categoryService.getCategoryById(categoryId);

        // Assert
        assertNull(retrievedCategory);
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setCategoryId(1L);
        category1.setIsHidden(false);

        Category category2 = new Category();
        category2.setCategoryId(2L);
        category2.setIsHidden(true);

        List<Category> allCategories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll()).thenReturn(allCategories);

        // Act
        List<Category> retrievedCategories = categoryService.getAllCategories();

        // Assert
        assertEquals(1, retrievedCategories.size());
        assertEquals(category1, retrievedCategories.get(0));
        verify(categoryRepository).findAll();
    }


    @Test
    void testHideCategory() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setIsHidden(false);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        categoryService.hideCategory(categoryId);

        // Assert
        assertTrue(category.getIsHidden());
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
    }


    @Test
    void testGetCategoryTotalAmountByYearAndCategory() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        Expense expense1 = new Expense();
        expense1.setAmountINR(100F);
        expense1.setDate(LocalDate.of(2022, 1, 1));
        expense1.setCategory(category);

        Expense expense2 = new Expense();
        expense2.setAmountINR(200F);
        expense2.setDate(LocalDate.of(2022, 2, 1));
        expense2.setCategory(category);

        List<Expense> expenseList = Arrays.asList(expense1, expense2);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(expenseRepository.findByCategoryAndIsReported(category, true)).thenReturn(expenseList);

        // Act
        HashMap<String, Float> categoryAmountMap = categoryService.getCategoryTotalAmountByYearAndCategory(categoryId);

        // Assert
        assertEquals(1, categoryAmountMap.size());
        assertEquals(300F, categoryAmountMap.get("2022"));
        assertNull(categoryAmountMap.get("2023")); // Adjusted assertion for the year 2023
        verify(categoryRepository).findById(categoryId);
        verify(expenseRepository).findByCategoryAndIsReported(category, true);
    }

    @Test
    void testGetCategoryTotalAmountByMonthAndCategory() {
        // Arrange
        Long categoryId = 1L;
        Long year = 2022L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        Expense expense1 = new Expense();
        expense1.setAmountINR(100F);
        expense1.setDate(LocalDate.of(2022, 1, 1));
        expense1.setCategory(category);

        Expense expense2 = new Expense();
        expense2.setAmountINR(200F);
        expense2.setDate(LocalDate.of(2022, 2, 1));
        expense2.setCategory(category);

        List<Expense> expenseList = Arrays.asList(expense1, expense2);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(expenseRepository.findByCategoryAndIsReported(category, true)).thenReturn(expenseList);

        // Act
        HashMap<String, Float> categoryAmountMap = categoryService.getCategoryTotalAmountByMonthAndCategory(categoryId, year);

        // Assert
        assertEquals(2, categoryAmountMap.size());
        assertEquals(100F, categoryAmountMap.get("Jan"));
        assertEquals(200F, categoryAmountMap.get("Feb"));
        verify(categoryRepository).findById(categoryId);
        verify(expenseRepository).findByCategoryAndIsReported(category, true);
    }

    @Test
    void testGetYearlyReimbursementRatio() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        Expense expense1 = new Expense();
        expense1.setAmountINR(100F);
        expense1.setDate(LocalDate.of(2022, 1, 1));
        expense1.setIsReported(true);
        expense1.setCategory(category);

        Expense expense2 = new Expense();
        expense2.setAmountINR(200F);
        expense2.setDate(LocalDate.of(2022, 2, 1));
        expense2.setIsReported(true);
        expense2.setCategory(category);

        List<Expense> expenseList = Arrays.asList(expense1, expense2);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        // Act
        HashMap<String, Float> reimbursementRatioMap = categoryService.getYearlyReimbursementRatio(categoryId);

        // Assert
        assertEquals(3, reimbursementRatioMap.size());
        assertEquals(300F, reimbursementRatioMap.get("2022"));
        assertEquals(2F, reimbursementRatioMap.get("2022_count"));
        assertEquals(150F, reimbursementRatioMap.get("2022_ratio"));
        verify(categoryRepository).findById(categoryId);
        verify(expenseRepository).findByCategory(category);
    }

    @Test
    void testGetMonthlyReimbursementRatio() {
        // Arrange
        Long categoryId = 1L;
        Long year = 2022L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        Expense expense1 = new Expense();
        expense1.setAmountINR(100F);
        expense1.setDate(LocalDate.of(2022, 1, 1));
        expense1.setIsReported(true);
        expense1.setCategory(category);

        Expense expense2 = new Expense();
        expense2.setAmountINR(200F);
        expense2.setDate(LocalDate.of(2022, 2, 1));
        expense2.setIsReported(true);
        expense2.setCategory(category);

        List<Expense> expenseList = Arrays.asList(expense1, expense2);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        // Act
        HashMap<String, Float> reimbursementRatioMap = categoryService.getMonthlyReimbursementRatio(categoryId, year);

        // Assert
        assertEquals(6, reimbursementRatioMap.size());
        assertEquals(100F, reimbursementRatioMap.get("2022_Jan"));
        assertEquals(200F, reimbursementRatioMap.get("2022_Feb"));
        verify(categoryRepository).findById(categoryId);
        verify(expenseRepository).findByCategory(category);
    }


    @Test
    void testGetTotalAmountByMonthForAllCategories() {
        // Arrange
        Long year = 2022L;

        Expense expense1 = new Expense();
        expense1.setAmountINR(100F);
        expense1.setDate(LocalDate.of(2022, 1, 1));
        expense1.setIsReported(true);
        expense1.setIsHidden(false);

        Expense expense2 = new Expense();
        expense2.setAmountINR(200F);
        expense2.setDate(LocalDate.of(2022, 2, 1));
        expense2.setIsReported(true);
        expense2.setIsHidden(false);

        List<Expense> expenseList = Arrays.asList(expense1, expense2);

        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenseList);

        // Act
        HashMap<String, Float> result = categoryService.getTotalAmountByMonthForAllCategories(year);

        // Assert
        assertEquals(2, result.size());
        assertEquals(100F, result.get("Jan"));
        assertEquals(200F, result.get("Feb"));
        verify(expenseRepository).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetYearlyReimbursementRatioForAllCategories() {
        // Arrange
        Expense expense1 = new Expense();
        expense1.setAmountINR(100F);
        expense1.setDate(LocalDate.of(2022, 1, 1));
        expense1.setIsReported(true);
        expense1.setIsHidden(false);

        Expense expense2 = new Expense();
        expense2.setAmountINR(200F);
        expense2.setDate(LocalDate.of(2022, 2, 1));
        expense2.setIsReported(true);
        expense2.setIsHidden(false);

        List<Expense> expenseList = Arrays.asList(expense1, expense2);

        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenseList);

        // Act
        HashMap<String, Float> result = categoryService.getYearlyReimbursementRatioForAllCategories();

        // Assert
        assertEquals(3, result.size());
        assertEquals(300F, result.get("2022"));
        assertEquals(2F, result.get("2022_count"));
        assertEquals(150F, result.get("2022_ratio"));
        verify(expenseRepository).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetMonthlyReimbursementRatioForAllCategories() {
        // Arrange
        Long year = 2022L;

        Expense expense1 = new Expense();
        expense1.setAmountINR(100F);
        expense1.setDate(LocalDate.of(2022, 1, 1));
        expense1.setIsReported(true);
        expense1.setIsHidden(false);

        Expense expense2 = new Expense();
        expense2.setAmountINR(200F);
        expense2.setDate(LocalDate.of(2022, 2, 1));
        expense2.setIsReported(true);
        expense2.setIsHidden(false);

        List<Expense> expenseList = Arrays.asList(expense1, expense2);

        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenseList);

        // Act
        HashMap<String, Float> result = categoryService.getMonthlyReimbursementRatioForAllCategories(year);

        // Assert
        assertEquals(6, result.size());
        assertEquals(100F, result.get("2022_Jan"));
        assertEquals(200F, result.get("2022_Feb"));
        verify(expenseRepository).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void updateCategory_WithValidInputs_ReturnsUpdatedCategory() {
        // Prepare test data
        long categoryId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryDescription("New Description");

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryDescription("Old Description");

        // Mock dependencies
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        // Call the method
        Category updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);

        // Verify the expected behavior
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);

        // Assert the updated values
        assertEquals(categoryDTO.getCategoryDescription(), updatedCategory.getCategoryDescription());
        assertEquals(categoryId, updatedCategory.getCategoryId());
    }

    @Test
    void addCategory_WithValidInputs_ReturnsAddedCategory() {
        // Prepare test data
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryDescription("New Category");

        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryDescription("New Category");

        // Mock the category repository
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Call the method
        Category addedCategory = categoryService.addCategory(categoryDTO);

        // Verify the interaction with the repository
        verify(categoryRepository).save(any(Category.class));

        // Assert the result
        assertNotNull(addedCategory);
        assertEquals(1L, addedCategory.getCategoryId());
        assertEquals("New Category", addedCategory.getCategoryDescription());
    }

    @Test
    void getCategoryTotalAmount_WithValidDateRange_ReturnsCategoryAmountMap() {
        // Prepare test data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Category category1 = new Category();
        category1.setCategoryDescription("Category 1");

        Category category2 = new Category();
        category2.setCategoryDescription("Category 2");

        Expense expense1 = new Expense();
        expense1.setCategory(category1);
        expense1.setAmountINR(100.0f);

        Expense expense2 = new Expense();
        expense2.setCategory(category2);
        expense2.setAmountINR(200.0f);

        Expense expense3 = new Expense();
        expense3.setCategory(category1);
        expense3.setAmountINR(150.0f);

        List<Expense> expenseList = Arrays.asList(expense1, expense2, expense3);

        // Mock the expense repository
        when(expenseRepository.findByDateBetweenAndIsReported(startDate, endDate, true)).thenReturn(expenseList);

        // Call the method
        HashMap<String, Float> categoryAmountMap = categoryService.getCategoryTotalAmount(startDate, endDate);

        // Verify the interaction with the repository
        verify(expenseRepository).findByDateBetweenAndIsReported(startDate, endDate, true);

        // Assert the result
        assertNotNull(categoryAmountMap);
        assertEquals(2, categoryAmountMap.size());
        assertEquals(250.0f, categoryAmountMap.get("Category 1"), 0.0f);
        assertEquals(200.0f, categoryAmountMap.get("Category 2"), 0.0f);
    }

    @Test
    void getTotalAmountByYearForAllCategories_ReturnsTotalAmountByYear() {
        // Prepare test data
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2021, 1, 1));
        expense1.setAmountINR(100.0f);

        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2021, 2, 1));
        expense2.setAmountINR(200.0f);

        Expense expense3 = new Expense();
        expense3.setDate(LocalDate.of(2022, 1, 1));
        expense3.setAmountINR(150.0f);

        List<Expense> expenses = Arrays.asList(expense1, expense2, expense3);

        // Mock the expense repository
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenses);

        // Call the method
        HashMap<String, Float> result = categoryService.getTotalAmountByYearForAllCategories();

        // Verify the interaction with the expense repository
        verify(expenseRepository).findByIsReportedAndIsHidden(true, false);

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(300.0f, result.get("2021"), 0.0f);
        assertEquals(150.0f, result.get("2022"), 0.0f);
    }

    @Test
    void getTotalAmountByYearForAllCategories_WithNoExpenses_ReturnsEmptyResult() {
        // Mock the expense repository to return an empty list
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(Collections.emptyList());

        // Call the method
        HashMap<String, Float> result = categoryService.getTotalAmountByYearForAllCategories();

        // Verify the interaction with the expense repository
        verify(expenseRepository).findByIsReportedAndIsHidden(true, false);

        // Assert the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
//    @Test
//    void testGetCategoryAnalyticsYearly() {
//        // Mock data
//        Long categoryId = 1L;
//
//        // Mock category
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//
//        // Mock category repository
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        // Mock expense repository
//        List<Expense> expenseList = new ArrayList<>();
//        Expense expense1 = new Expense();
//        expense1.setAmountINR(100.0f);
//        expense1.setDate(LocalDate.of(2022, 1, 1));
//        expense1.setIsReported(true);
//        expense1.setCategory(category);
//        expenseList.add(expense1);
//
//        Expense expense2 = new Expense();
//        expense2.setAmountINR(200.0f);
//        expense2.setDate(LocalDate.of(2022, 2, 1));
//        expense2.setIsReported(true);
//        expense2.setCategory(category);
//        expenseList.add(expense2);
//
//        when(expenseRepository.findByCategoryAndIsReported(category, true)).thenReturn(expenseList);
//
//        // Expected result
//        HashMap<String, Float> expectedCategoryTotalAmountByYear = new HashMap<>();
//        expectedCategoryTotalAmountByYear.put("2022", 300.0f);
//
//        HashMap<String, Float> expectedYearlyReimbursementRatio = new HashMap<>();
//        expectedYearlyReimbursementRatio.put("2022", 150.0f);
//
//        // Call the method
//        HashMap<String, Object> result = categoryService.getCategoryAnalyticsYearly(categoryId);
//
//        // Verify the results
//        assertEquals(expectedCategoryTotalAmountByYear, result.get("categoryTotalAmountByYear"));
//        assertEquals(expectedYearlyReimbursementRatio, result.get("yearlyReimbursementRatio"));
//
//        // Verify the method invocations
//        verify(categoryRepository, times(1)).findById(categoryId);
//        verify(expenseRepository, times(1)).findByCategoryAndIsReported(category, true);
//    }


}








