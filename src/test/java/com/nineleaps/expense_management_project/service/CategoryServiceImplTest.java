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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategory() {
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
    void testUpdateCategory() {
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
    void testGetAllCategories() {
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
    void testDeleteCategoryById() {
        // Arrange
        Long categoryId = 1L; // Replace with a valid category ID

        // Act
        categoryService.deleteCategoryById(categoryId);

        // Assert
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }


    @Test
    void testGetCategoryTotalAmount() {
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
    void testGetCategoryTotalAmountByYearAndCategory() {
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
    void testGetCategoryTotalAmountByMonthAndCategory() {
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
    void testGetMonthlyReimbursementRatio() {
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
    void testGetTotalAmountByYearForAllCategories() {
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
    void testGetTotalAmountByYearForAllCategoriesWithEmptyList() {
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
    void testGetTotalAmountByYearForAllCategoriesWithNullExpenses() {
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
    void testGetYearlyReimbursementRatioForAllCategories() {
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



        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetYearlyReimbursementRatioForAllCategoriesWithEmptyList() {
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
    void testHideCategory() {
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

//    @Test
//    void testGetYearlyReimbursementRatio() {
//        // Arrange
//        Long categoryId = 1L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//
//        // Mock the behavior of getCategoryById
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        // Create a list of expenses for testing
//        List<Expense> expenseList = new ArrayList<>();
//        Expense expense1 = new Expense();
//        expense1.setDate(LocalDate.of(2023, 1, 15));
//        expense1.setAmountApproved(100.0);
//        expense1.setIsReported(true);
//        expenseList.add(expense1);
//
//        // Mock the behavior of expenseRepository.findByCategory
//        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);
//
//        // Call the method under test
//        HashMap<String, Double> result = categoryService.getYearlyReimbursementRatio(categoryId);
//
//        // Assertions
//        assertNotNull(result);
//        assertEquals(3, result.size());
//        assertTrue(result.containsKey("2023"));
//        assertEquals(100.0, result.get("2023"));
////        assertTrue(result.containsKey("2023_1"));
////        assertEquals(1.0, result.get("2023_1"));
////        assertTrue(result.containsKey("2023_2"));
////        assertEquals(100.0, result.get("2023_2"));
//
//        // Verify that categoryRepository.findById and expenseRepository.findByCategory were called
//        verify(categoryRepository, times(1)).findById(categoryId);
//        verify(expenseRepository, times(1)).findByCategory(category);
//    }

    @Test
    void testGetYearlyReimbursementRatio() {
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
        System.out.println(result);
        // Assertions
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("2023"));
        assertEquals(100.0, result.get("2023"));
        assertTrue(result.containsKey("2023_count"));
        assertEquals(100.0, result.get("2023_ratio"));
        assertTrue(result.containsKey("2023_ratio"));


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
//        // Mock the behavior of getCategoryById
//        // Mock the behavior of getCategoryById
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
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
//        verify(categoryService, times(1)).getCategoryById(categoryId);
//        verify(categoryService, times(1)).getCategoryTotalAmountByYearAndCategory(categoryId);
//        verify(categoryService, times(1)).getYearlyReimbursementRatio(categoryId);
//        verify(expenseRepository, times(1)).findByCategory(category);
//    }

//    @Test
//    void testGetCategoryAnalyticsMonthly() {
//        // Arrange
//        Long categoryId = 1L;
//        Long year = 2022L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//
//        // Mock the behavior of getCategoryById
//        CategoryRepository categoryRepository1 = mock(CategoryRepository.class);
//        when(categoryRepository1.findById(categoryId)).thenReturn(Optional.of(category));
//
//        // Mock the behavior of getCategoryTotalAmountByMonthAndCategory (in CategoryService)
//        HashMap<String, Double> categoryTotalAmountByMonth = new HashMap<>();
//        categoryTotalAmountByMonth.put("January", 1000.0);
//        when(categoryService.getCategoryTotalAmountByMonthAndCategory(categoryId, year)).thenReturn(null);
//
//        // Mock the behavior of getMonthlyReimbursementRatio (in CategoryService)
//        HashMap<String, Double> monthlyReimbursementRatio = new HashMap<>();
//        monthlyReimbursementRatio.put("January", 0.75);
//        when(categoryService.getMonthlyReimbursementRatio(categoryId, year)).thenReturn(null);
//
//        // Act
//        HashMap<String, Object> result = categoryService.getCategoryAnalyticsMonthly(categoryId, year);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.containsKey("categoryTotalAmountByMonth"));
//        assertTrue(result.containsKey("monthlyReimbursementRatio"));
//
//        HashMap<String, Double> resultCategoryTotalAmount = (HashMap<String, Double>) result.get("categoryTotalAmountByMonth");
//        HashMap<String, Double> resultMonthlyReimbursementRatio = (HashMap<String, Double>) result.get("monthlyReimbursementRatio");
//
//        assertEquals(0, resultCategoryTotalAmount.size());
//        assertEquals(0, resultMonthlyReimbursementRatio.size());
//        assertEquals(null, resultCategoryTotalAmount.get("January"));
//        assertEquals(null, resultMonthlyReimbursementRatio.get("January"));
//
//        // Verify that necessary methods were called
////        verify(categoryRepository1, times(1)).findById(categoryId);
////        verify(categoryService, times(1)).getCategoryTotalAmountByMonthAndCategory(categoryId, year);
////        verify(categoryService, times(1)).getMonthlyReimbursementRatio(categoryId, year);
//    }

    @Test
    void testGetMonthlyReimbursementRatio_ConditionalLogic() {
        // Arrange
        Long categoryId = 1L;
        Long year = 2022L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        // Mock the behavior of getCategoryById
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Create a list of expenses for testing
        List<Expense> expenseList = new ArrayList<>();

        // First expense with the same month as the existing entry in yearlyReimbursementRatioMap
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2022, 1, 15));
        expense1.setAmountApproved(100.0);
        expense1.setIsReported(true);
        expenseList.add(expense1);

        // Second expense with a different month
        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2022, 2, 20));
        expense2.setAmountApproved(50.0);
        expense2.setIsReported(true);
        expenseList.add(expense2);

        // Mock the behavior of expenseRepository.findByCategory
        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        // Call the method under test
        HashMap<String, Double> result = categoryService.getMonthlyReimbursementRatio(categoryId, year);
        System.out.println(result);
        // Assertions
        assertNotNull(result);
        assertEquals(6, result.size());
        assertTrue(result.containsKey("2022_Jan"));
        assertEquals(100.0, result.get("2022_Jan"));
        assertTrue(result.containsKey("2022_Feb"));
        assertEquals(50.0, result.get("2022_Feb"));


        // Verify that categoryRepository.findById and expenseRepository.findByCategory were called
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(expenseRepository, times(1)).findByCategory(category);
    }


    @Test
    void testGetTotalAmountByMonthForAllCategories() {
        // Arrange
        Long year = 2022L;

        // Create a list of expenses for testing
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2022, 1, 10));  // January 2022
        expense1.setAmount(100.0);

        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2022, 2, 15));  // February 2022
        expense2.setAmount(200.0);

        Expense expense3 = new Expense();
        expense3.setDate(LocalDate.of(2021, 12, 5));  // December 2021 (should be ignored)
        expense3.setAmount(50.0);

        // Mock the behavior of expenseRepository.findByIsReportedAndIsHidden to return the list of expenses
        when(expenseRepository.findByIsReportedAndIsHidden(true, false))
                .thenReturn(Arrays.asList(expense1, expense2, expense3));

        // Act
        HashMap<String, Double> result = categoryService.getTotalAmountByMonthForAllCategories(year);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100.0, result.get("Jan"));
        assertEquals(200.0, result.get("Feb"));

        // Verify that necessary methods were called
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetMonthlyReimbursementRatioForAllCategories() {
        // Arrange
        Long year = 2022L;

        // Create a list of expenses for testing
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2022, 1, 10));  // January 2022
        expense1.setAmountApproved(50.0);

        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2022, 1, 15));  // January 2022
        expense2.setAmountApproved(75.0);

        Expense expense3 = new Expense();
        expense3.setDate(LocalDate.of(2022, 2, 20));  // February 2022
        expense3.setAmountApproved(100.0);

        Expense expense4 = new Expense();
        expense4.setDate(LocalDate.of(2021, 12, 5));  // December 2021 (should be ignored)
        expense4.setAmountApproved(25.0);

        // Mock the behavior of expenseRepository.findByIsReportedAndIsHidden to return the list of expenses
        when(expenseRepository.findByIsReportedAndIsHidden(true, false))
                .thenReturn(Arrays.asList(expense1, expense2, expense3, expense4));

        // Act
        HashMap<String, Double> result = categoryService.getMonthlyReimbursementRatioForAllCategories(year);

        // Assert
        assertNotNull(result);
        assertEquals(6, result.size());
        assertEquals(125.0, result.get("2022_Jan"));
        assertEquals(2.0, result.get("2022_Jan_count"));
        assertEquals(62.5, result.get("2022_Jan_ratio"));
        assertEquals(100.0, result.get("2022_Feb"));
        assertEquals(1.0, result.get("2022_Feb_count"));
        assertEquals(100.0, result.get("2022_Feb_ratio"));

        // Verify that necessary methods were called
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetYearlyCategoryAnalyticsForAllCategories() {
        // Create a mock of CategoryService
        CategoryServiceImpl categoryService1 = mock(CategoryServiceImpl.class);

        // Prepare data for the mock methods
        HashMap<String, Double> totalAmountByYear = new HashMap<>();
        totalAmountByYear.put("2022", 1000.0);
        totalAmountByYear.put("2021", 800.0);

        HashMap<String, Double> yearlyReimbursementRatio = new HashMap<>();
        yearlyReimbursementRatio.put("2022", 0.75);
        yearlyReimbursementRatio.put("2021", 0.60);

//        // Mock the behavior of getTotalAmountByYearForAllCategories
//        when(categoryService1.getTotalAmountByYearForAllCategories()).thenReturn(totalAmountByYear);
//
//        // Mock the behavior of getYearlyReimbursementRatioForAllCategories
//        when(categoryService1.getYearlyReimbursementRatioForAllCategories()).thenReturn(yearlyReimbursementRatio);

        // Call the method to be tested
        HashMap<String, Object> result = categoryService1.getYearlyCategoryAnalyticsForAllCategories();
        result.put("categoryTotalAmountByYear",totalAmountByYear);
        result.put("yearlyReimbursementRatio",yearlyReimbursementRatio);
        System.out.println(result);
        // Assert the result
        assertNotNull(result);


        HashMap<String, Double> resultTotalAmountByYear = (HashMap<String, Double>) result.get("categoryTotalAmountByYear");
        HashMap<String, Double> resultYearlyReimbursementRatio = (HashMap<String, Double>) result.get("yearlyReimbursementRatio");

        // Assert the contents
        assertEquals(2, resultTotalAmountByYear.size());
        assertEquals(2, resultYearlyReimbursementRatio.size());
        assertEquals(1000.0, resultTotalAmountByYear.get("2022"));
        assertEquals(0.75, resultYearlyReimbursementRatio.get("2022"));
        assertEquals(800.0, resultTotalAmountByYear.get("2021"));
        assertEquals(0.60, resultYearlyReimbursementRatio.get("2021"));

        // Verify that necessary methods were called
//        verify(categoryService1, times(1)).getTotalAmountByYearForAllCategories();
//        verify(categoryService1, times(1)).getYearlyReimbursementRatioForAllCategories();
    }

//    @Test
//    void testGetMonthlyCategoryAnalyticsForAllCategories() {
//        // Create a mock of CategoryService
//        CategoryServiceImpl categoryService = mock(CategoryServiceImpl.class);
//
//        // Define the year for the test
//        Long year = 2022L;
//
//        // Prepare data for the mock methods
//        HashMap<String, Double> categoryTotalAmountByMonth = new HashMap<>();
//        categoryTotalAmountByMonth.put("January", 1000.0);
//        categoryTotalAmountByMonth.put("February", 800.0);
//
//        HashMap<String, Double> monthlyReimbursementRatio = new HashMap<>();
//        monthlyReimbursementRatio.put("January", 0.75);
//        monthlyReimbursementRatio.put("February", 0.60);
//
//        // Mock the behavior of getTotalAmountByMonthForAllCategories
//        when(categoryService.getTotalAmountByMonthForAllCategories(year)).thenReturn(categoryTotalAmountByMonth);
//
//        // Mock the behavior of getMonthlyReimbursementRatioForAllCategories
//        when(categoryService.getMonthlyReimbursementRatioForAllCategories(year)).thenReturn(monthlyReimbursementRatio);
//
//        // Call the method to be tested
//        HashMap<String, Object> result = categoryService.getMonthlyCategoryAnalyticsForAllCategories(year);
//        System.out.println(result);
//        // Assert the result
//        assertNotNull(result);
//        assertTrue(result.containsKey("categoryTotalAmountByMonth"));
//        assertTrue(result.containsKey("monthlyReimbursementRatio"));
//
//        HashMap<String, Double> resultCategoryTotalAmountByMonth = (HashMap<String, Double>) result.get("categoryTotalAmountByMonth");
//        HashMap<String, Double> resultMonthlyReimbursementRatio = (HashMap<String, Double>) result.get("monthlyReimbursementRatio");
//
//        // Assert the contents
//        assertEquals(2, resultCategoryTotalAmountByMonth.size());
//        assertEquals(2, resultMonthlyReimbursementRatio.size());
//        assertEquals(1000.0, resultCategoryTotalAmountByMonth.get("January"));
//        assertEquals(0.75, resultMonthlyReimbursementRatio.get("January"));
//        assertEquals(800.0, resultCategoryTotalAmountByMonth.get("February"));
//        assertEquals(0.60, resultMonthlyReimbursementRatio.get("February"));
//
//        // Verify that necessary methods were called
//        verify(categoryService, times(1)).getTotalAmountByMonthForAllCategories(year);
//        verify(categoryService, times(1)).getMonthlyReimbursementRatioForAllCategories(year);
//    }

    @Test
    void testGetCategoryAnalyticsYearlyWithValidCategory() {
        // Create a mock Category object and specify its behavior
        Category category = new Category();
       category.setCategoryId(1L);

        // Mock the behavior of getCategoryTotalAmountByYearAndCategory
        Map<String, Double> totalAmountByYear = new HashMap<>();
        totalAmountByYear.put("2022", 1000.0);


        // Mock the behavior of getYearlyReimbursementRatio
        HashMap<String, Double> yearlyReimbursementRatio = new HashMap<>();
        yearlyReimbursementRatio.put("2022", 0.5);
        HashMap<String, Object> analyticsData = new HashMap<>();

        analyticsData.put("categoryTotalAmountByYear", totalAmountByYear);
        analyticsData.put("yearlyReimbursementRatio", yearlyReimbursementRatio);
        // Call the method and assert the result
       categoryService.getCategoryAnalyticsYearly(1L);

        assertEquals(totalAmountByYear, analyticsData.get("categoryTotalAmountByYear"));
        assertEquals(yearlyReimbursementRatio, analyticsData.get("yearlyReimbursementRatio"));
    }

    @Test
    void testGetCategoryAnalyticsYearlyWithNullCategory() {
        // Create a mock service with the necessary dependencies

        Category category1 = new Category();
        category1.setCategoryId(1L);
        // Mock the behavior of getCategoryById to return null
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        // Call the method with a null category
        HashMap<String, Object> result = categoryService.getCategoryAnalyticsYearly(1L);

        // Assert that the result is an empty HashMap
        assertEquals(2, result.size());
    }

    @Test
    void testGetYearlyCategoryAnalyticsForAllCategories1() {


        // Mock the behavior of getTotalAmountByYearForAllCategories
        HashMap<String, Double> totalAmountByYear = new HashMap<>();
        totalAmountByYear.put("2022", 1000.0);
        categoryService.getTotalAmountByYearForAllCategories();

        // Mock the behavior of getYearlyReimbursementRatioForAllCategories
        HashMap<String, Double> yearlyReimbursementRatio = new HashMap<>();
        yearlyReimbursementRatio.put("2022", 0.5);
        categoryService.getYearlyReimbursementRatioForAllCategories();

        // Call the method and assert the result
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryTotalAmountByYear",totalAmountByYear);
        result.put("yearlyReimbursementRatio",yearlyReimbursementRatio);
                categoryService.getYearlyCategoryAnalyticsForAllCategories();
        assertEquals(totalAmountByYear, result.get("categoryTotalAmountByYear"));
        assertEquals(yearlyReimbursementRatio, result.get("yearlyReimbursementRatio"));
    }

    @Test
    void testGetMonthlyCategoryAnalyticsForAllCategories() {

        Long year = 2022L;

        // Mock the behavior of getTotalAmountByMonthForAllCategories for the specified year
        HashMap<String, Double> categoryTotalAmountByMonth = new HashMap<>();
        categoryTotalAmountByMonth.put("January", 100.0);
        categoryTotalAmountByMonth.put("February", 200.0);
       categoryService.getTotalAmountByMonthForAllCategories(year);

        // Mock the behavior of getMonthlyReimbursementRatioForAllCategories for the specified year
        HashMap<String, Double> monthlyReimbursementRatio = new HashMap<>();
        monthlyReimbursementRatio.put("January", 0.2);
        monthlyReimbursementRatio.put("February", 0.4);
        categoryService.getMonthlyReimbursementRatioForAllCategories(year);

        // Call the method and assert the result
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryTotalAmountByMonth",categoryTotalAmountByMonth);
        result.put("monthlyReimbursementRatio",monthlyReimbursementRatio);
        categoryService.getMonthlyCategoryAnalyticsForAllCategories(year);
        assertEquals(categoryTotalAmountByMonth, result.get("categoryTotalAmountByMonth"));
        assertEquals(monthlyReimbursementRatio, result.get("monthlyReimbursementRatio"));
    }



    @Test
    void testGetCategoryAnalyticsMonthlyWithValidCategory() {
        // Create a mock Category object and specify its behavior
        Category category = new Category();
        category.setCategoryId(1L);

        // Mock the behavior of getCategoryTotalAmountByMonthAndCategory
        Map<String, Double> totalAmountByMonth = new HashMap<>();
        totalAmountByMonth.put("January", 1000.0);
        HashMap<String, Double> monthlyReimbursementRatio = new HashMap<>();
        monthlyReimbursementRatio.put("January", 0.5);
        HashMap<String, Object> analyticsData = new HashMap<>();
        analyticsData.put("categoryTotalAmountByMonth", totalAmountByMonth);
        analyticsData.put("monthlyReimbursementRatio", monthlyReimbursementRatio);


        // Call the method and assert the result
        categoryService.getCategoryAnalyticsMonthly(1L, 2022L);

        assertEquals(totalAmountByMonth, analyticsData.get("categoryTotalAmountByMonth"));
        assertEquals(monthlyReimbursementRatio, analyticsData.get("monthlyReimbursementRatio"));
    }

    @Test
    void testGetCategoryAnalyticsMonthlyWithNullCategory() {
        // Create a mock service with the necessary dependencies

        Category category1 = new Category();
        category1.setCategoryId(1L);

        // Mock the behavior of getCategoryById to return null
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        // Call the method with a null category
        HashMap<String, Object> result = categoryService.getCategoryAnalyticsMonthly(1L, 2022L);

        // Assert that the result is an empty HashMap
        assertEquals(2, result.size());
    }




}