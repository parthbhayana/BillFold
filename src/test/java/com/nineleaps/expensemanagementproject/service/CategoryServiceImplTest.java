package com.nineleaps.expensemanagementproject.service;

import com.nineleaps.expensemanagementproject.DTO.CategoryDTO;
import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import org.junit.jupiter.api.Assertions;
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

//    @Test
//    void testGetTotalAmountByMonthForAllCategories() {
//        Long testYear = 2023L;
//        // Create sample test data for expenses
//        List<Expense> mockExpenses = new ArrayList<>();
//        mockExpenses.add(new Expense(1L, "Expense1", LocalDate.of(2023, 1, 1), 100F));
//        mockExpenses.add(new Expense(2L, "Expense2", LocalDate.of(2023, 1, 15), 150F));
//        mockExpenses.add(new Expense(3L, "Expense3", LocalDate.of(2023, 2, 5), 200F));
//
//        // Mock the behavior of the expenseRepository
//        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(mockExpenses);
//
//        // Call the method to be tested
//        HashMap<String, Float> result = categoryService.getTotalAmountByMonthForAllCategories(testYear);
//
//        // Expected result
//        HashMap<String, Float> expected = new HashMap<>();
//        expected.put("Jan", 250F); // 100 + 150
//        expected.put("Feb", 200F); // Only one expense in Feb
//
//        // Assert the result
//        Assertions.assertEquals(expected, result);
//    }

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


    @Test
    void testGetCategoryAnalyticsYearly() {
        Long categoryId = 1L;

        // Mock category data
        Category mockCategory = new Category();
        mockCategory.setCategoryId(categoryId);
        mockCategory.setCategoryDescription("Test Category");

        // Mock expense data for the given category and year 2022
        List<Expense> expenseList = new ArrayList<>();
        Expense expense1 = new Expense();
        expense1.setExpenseId(101L);
        expense1.setCategory(mockCategory);
        expense1.setAmountINR(200F);
        expense1.setDate(LocalDate.of(2022, 7, 15));
        expense1.setIsReported(true);
        expenseList.add(expense1);

        // Mock expense data for the given category and year 2023
        Expense expense2 = new Expense();
        expense2.setExpenseId(102L);
        expense2.setCategory(mockCategory);
        expense2.setAmountINR(100F);
        expense2.setDate(LocalDate.of(2023, 5, 20));
        expense2.setIsReported(true);
        expenseList.add(expense2);

        // Mock the repository behavior
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));
        when(expenseRepository.findByCategoryAndIsReported(mockCategory, true)).thenReturn(expenseList);

        // Expected result
        HashMap<String, Object> expectedResult = new HashMap<>();
        HashMap<String, Float> categoryTotalAmountByYear = new HashMap<>();
        categoryTotalAmountByYear.put("2022", 200F);
        categoryTotalAmountByYear.put("2023", 100F);
        HashMap<String, Float> yearlyReimbursementRatio = new HashMap<>();
        yearlyReimbursementRatio.put("2022", 200F);
        yearlyReimbursementRatio.put("2023", 100F);
        expectedResult.put("categoryTotalAmountByYear", categoryTotalAmountByYear);
        expectedResult.put("yearlyReimbursementRatio", yearlyReimbursementRatio);

        // Actual result
        HashMap<String, Object> actualResult = categoryService.getCategoryAnalyticsYearly(categoryId);

        // Assertions
        // assertEquals(expectedResult, actualResult);
    }

    @Test
    void testGetMonthlyReimbursementRatio() {
        // Test data
        Long categoryId = 1L;
        Long year = 2023L;
        String categoryDescription = "Test Category";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryDescription(categoryDescription);

        Expense expense1 = new Expense();
        expense1.setAmountINR(100F);
        expense1.setIsReported(true);
        expense1.setDate(LocalDate.of(2023, 7, 1));
        expense1.setCategory(category);

        Expense expense2 = new Expense();
        expense2.setAmountINR(200F);
        expense2.setIsReported(true);
        expense2.setDate(LocalDate.of(2023, 7, 15));
        expense2.setCategory(category);

        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense1);
        expenseList.add(expense2);

        // Mocking the necessary methods
        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(category));
        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        // Call the method under test
        HashMap<String, Float> monthlyReimbursementRatio = categoryService.getMonthlyReimbursementRatio(categoryId, year);

        // Verify the results
        assertNotNull(monthlyReimbursementRatio);
        assertEquals(3, monthlyReimbursementRatio.size());

        // Verify the values for July 2023
        String july2023Key = "2023_Jul";
        assertTrue(monthlyReimbursementRatio.containsKey(july2023Key));
        assertEquals(300F, monthlyReimbursementRatio.get(july2023Key), 0.001);

        String july2023CountKey = "2023_Jul_count";
        assertTrue(monthlyReimbursementRatio.containsKey(july2023CountKey));
        assertEquals(2F, monthlyReimbursementRatio.get(july2023CountKey), 0.001);

        String july2023RatioKey = "2023_Jul_ratio";
        assertTrue(monthlyReimbursementRatio.containsKey(july2023RatioKey));
        assertEquals(150F, monthlyReimbursementRatio.get(july2023RatioKey), 0.001);
    }


    @Test
    void testGetCategoryAnalyticsMonthly() {
        // Test data
        Long categoryId = 1L;
        Long year = 2023L;
        String categoryDescription = "Test Category";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryDescription(categoryDescription);

        // Mocking the necessary methods
        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(category));

        // You may need to mock the response of 'getCategoryTotalAmountByMonthAndCategory'
        // and 'getMonthlyReimbursementRatio' methods as well.

        // Call the method under test
        HashMap<String, Object> analyticsData = categoryService.getCategoryAnalyticsMonthly(categoryId, year);

        // Verify the results
        assertNotNull(analyticsData);

        // Assuming you've already tested 'getCategoryTotalAmountByMonthAndCategory' and 'getMonthlyReimbursementRatio' methods,
        // you can simply check if the returned HashMaps are correctly placed in the analyticsData.
        assertTrue(analyticsData.containsKey("categoryTotalAmountByMonth"));
        assertTrue(analyticsData.containsKey("monthlyReimbursementRatio"));

        // You can further check if the data inside the HashMaps is as expected.
    }


    @Test
    void testGetYearlyCategoryAnalyticsForAllCategories() {
        // Mocking the necessary methods
        // You may need to mock 'getTotalAmountByYearForAllCategories' and 'getYearlyReimbursementRatioForAllCategories'
        // based on your implementation and test data.

        // Call the method under test
        HashMap<String, Object> analyticsData = categoryService.getYearlyCategoryAnalyticsForAllCategories();

        // Verify the results
        assertNotNull(analyticsData);

        // Assuming you've already tested 'getTotalAmountByYearForAllCategories' and 'getYearlyReimbursementRatioForAllCategories' methods,
        // you can simply check if the returned HashMaps are correctly placed in the analyticsData.
        assertTrue(analyticsData.containsKey("categoryTotalAmountByYear"));
        assertTrue(analyticsData.containsKey("yearlyReimbursementRatio"));

        // You can further check if the data inside the HashMaps is as expected.
    }

    @Test
    void testGetMonthlyCategoryAnalyticsForAllCategories() {
        // Test data
        Long year = 2023L;

        // Mocking the necessary methods
        // You may need to mock 'getTotalAmountByMonthForAllCategories' and 'getMonthlyReimbursementRatioForAllCategories'
        // based on your implementation and test data.

        // Call the method under test
        HashMap<String, Object> analyticsData = categoryService.getMonthlyCategoryAnalyticsForAllCategories(year);

        // Verify the results
        assertNotNull(analyticsData);

        // Assuming you've already tested 'getTotalAmountByMonthForAllCategories' and 'getMonthlyReimbursementRatioForAllCategories' methods,
        // you can simply check if the returned HashMaps are correctly placed in the analyticsData.
        assertTrue(analyticsData.containsKey("categoryTotalAmountByMonth"));
        assertTrue(analyticsData.containsKey("monthlyReimbursementRatio"));

        // You can further check if the data inside the HashMaps is as expected.
    }

//    @Test
//    void testGetTotalAmountByMonthForAllCategories() {
//        // Test data
//        Long year = 2023L;
//
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setCategoryDescription("Test Category");
//
//        Expense expense1 = new Expense();
//        expense1.setExpenseId(1L);
//        expense1.setAmountINR(100F);
//        expense1.setIsReported(true);
//        expense1.setDate(LocalDate.of(2023, Month.JANUARY, 10));
//        expense1.setCategory(category);
//
//        Expense expense2 = new Expense();
//        expense2.setExpenseId(2L);
//        expense2.setAmountINR(150F);
//        expense2.setIsReported(false); // Changed to false
//        expense2.setDate(LocalDate.of(2023, Month.FEBRUARY, 20));
//        expense2.setCategory(category);
//
//        List<Expense> expenseList = new ArrayList<>();
//        expenseList.add(expense1);
//        expenseList.add(expense2);
//
//        // Mocking the necessary methods
//        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenseList); // Changed to false
//
//        // Call the method under test
//        HashMap<String, Float> result = categoryService.getTotalAmountByMonthForAllCategories(year);
//
//        // Verify the results
//        assertNotNull(result);
//
//        // Verify the data for January 2023
//        String january2023Key = "Jan 2023";
//        assertTrue(result.containsKey(january2023Key));
//        assertEquals(100F, result.get(january2023Key), 0.001);
//
//        // Verify the data for February 2023
//        String february2023Key = "Feb 2023";
//        assertTrue(result.containsKey(february2023Key));
//        assertEquals(150F, result.get(february2023Key), 0.001);
//
//        // Verify the data for other months (assuming there are no other expenses in the test data)
//        for (Month month : Month.values()) {
//            if (month != Month.JANUARY && month != Month.FEBRUARY) {
//                String monthYearKey = month.getDisplayName(TextStyle.SHORT, Locale.US) + " 2023";
//                assertFalse(result.containsKey(monthYearKey));
//            }
//        }
//    }

}



















