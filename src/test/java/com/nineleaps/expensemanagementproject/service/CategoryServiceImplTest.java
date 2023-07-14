package com.nineleaps.expensemanagementproject.service;

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

//    @Test
//    void testUpdateCategory() {
//        // Arrange
//        Category category = new Category();
//        category.setCategoryId(1L);
//
//        when(categoryRepository.save(category)).thenReturn(category);
//
//        // Act
//        Category updatedCategory = categoryService.updateCategory(category);
//
//        // Assert
//        assertEquals(category, updatedCategory);
//        verify(categoryRepository).save(category);
//    }

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

//    @Test
//    void testAddCategory() {
//        // Arrange
//        Category category = new Category();
//
//        when(categoryRepository.save(category)).thenReturn(category);
//
//        // Act
//        Category addedCategory = categoryService.addCategory(category);
//
//        // Assert
//        assertEquals(category, addedCategory);
//        verify(categoryRepository).save(category);
//    }

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

//    @Test
//    void testGetCategoryTotalAmount() {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2022, 1, 1);
//        LocalDate endDate = LocalDate.of(2022, 1, 31);
//
//        Expense expense1 = new Expense();
//        expense1.setAmountINR(100F);
//        expense1.setCategory(new Category("Category 1"));
//
//        Expense expense2 = new Expense();
//        expense2.setAmountINR(200F);
//        expense2.setCategory(new Category("Category 2"));
//
//        List<Expense> expenseList = Arrays.asList(expense1, expense2);
//
//        when(expenseRepository.findByDateBetweenAndIsReported(startDate, endDate, true)).thenReturn(expenseList);
//
//        // Act
//        HashMap<String, Float> categoryAmountMap = categoryService.getCategoryTotalAmount(startDate, endDate);
//
//        // Assert
//        assertEquals(2, categoryAmountMap.size());
//        assertEquals(100F, categoryAmountMap.get("Category 1"));
//        assertEquals(200F, categoryAmountMap.get("Category 2"));
//        verify(expenseRepository).findByDateBetweenAndIsReported(startDate, endDate, true);
//    }

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

//    @Test
//    void testGetCategoryAnalyticsYearly() {
//        // Arrange
//        Long categoryId = 1L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//
//        HashMap<String, Float> categoryTotalAmountByYear = new HashMap<>();
//        categoryTotalAmountByYear.put("2022", 100F);
//
//        HashMap<String, Float> yearlyReimbursementRatio = new HashMap<>();
//        yearlyReimbursementRatio.put("2022", 0.5F);
//
//        Optional<Category> optionalCategory = Optional.of(category);
//
//        when(categoryService.getCategoryById(categoryId)).thenReturn(optionalCategory);
//        when(categoryService.getCategoryTotalAmountByYearAndCategory(categoryId)).thenReturn(categoryTotalAmountByYear);
//        when(categoryService.getYearlyReimbursementRatio(categoryId)).thenReturn(yearlyReimbursementRatio);
//
//        // Act
//        HashMap<String, Object> analyticsData = categoryService.getCategoryAnalyticsYearly(categoryId);
//
//        // Assert
//        assertEquals(2, analyticsData.size());
//        assertEquals(categoryTotalAmountByYear, analyticsData.get("categoryTotalAmountByYear"));
//        assertEquals(yearlyReimbursementRatio, analyticsData.get("yearlyReimbursementRatio"));
//        verify(categoryService).getCategoryById(categoryId);
//        verify(categoryService).getCategoryTotalAmountByYearAndCategory(categoryId);
//        verify(categoryService).getYearlyReimbursementRatio(categoryId);
//    }



//    @Test
//    void testGetCategoryAnalyticsMonthly() {
//        // Arrange
//        Long categoryId = 1L;
//        Long year = 2022L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//
//        HashMap<String, Float> categoryTotalAmountByMonth = new HashMap<>();
//        categoryTotalAmountByMonth.put("Jan", 100F);
//
//        HashMap<String, Float> monthlyReimbursementRatio = new HashMap<>();
//        monthlyReimbursementRatio.put("Jan", 0.5F);
//
//        when(categoryService.getCategoryById(categoryId)).thenReturn(category);
//        when(categoryService.getCategoryTotalAmountByMonthAndCategory(categoryId, year)).thenReturn(categoryTotalAmountByMonth);
//        when(categoryService.getMonthlyReimbursementRatio(categoryId, year)).thenReturn(monthlyReimbursementRatio);
//
//        // Act
//        HashMap<String, Object> analyticsData = categoryService.getCategoryAnalyticsMonthly(categoryId, year);
//
//        // Assert
//        assertEquals(2, analyticsData.size());
//        assertEquals(categoryTotalAmountByMonth, analyticsData.get("categoryTotalAmountByMonth"));
//        assertEquals(monthlyReimbursementRatio, analyticsData.get("monthlyReimbursementRatio"));
//        verify(categoryService).getCategoryById(categoryId);
//        verify(categoryService).getCategoryTotalAmountByMonthAndCategory(categoryId, year);
//        verify(categoryService).getMonthlyReimbursementRatio(categoryId, year);
//    }

//    @Test
//    void testGetTotalAmountByYearForAllCategories() {
//        // Arrange
//        Expense expense1 = new Expense();
//        expense1.setAmountINR(100F);
//        expense1.setDate(LocalDate.of(2022, 1, 1));
//        expense1.setIsReported(true);
//        expense1.setIsHidden(false);
//
//        Expense expense2 = new Expense();
//        expense2.setAmountINR(200F);
//        expense2.setDate(LocalDate.of(2022, 2, 1));
//        expense2.setIsReported(true);
//        expense2.setIsHidden(false);
//
//        List<Expense> expenseList = Arrays.asList(expense1, expense2);
//
//        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenseList);
//
//        // Act
//        HashMap<String, Float> result = categoryService.getTotalAmountByYearForAllCategories();
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals(300F, result.get("2022"));
//        verify(expenseRepository).findByIsReportedAndIsHidden(true, false);
//    }

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

//    @Test
//    void testGetYearlyCategoryAnalyticsForAllCategories() {
//        // Arrange
//        List<Expense> expenses = new ArrayList<>();
//        Expense expense1 = new Expense();
//        expense1.setAmountINR(100F);
//        expense1.setDate(LocalDate.of(2022, 1, 1));
//        expense1.setIsReported(true);
//        expense1.setIsHidden(false);
//        expenses.add(expense1);
//
//        Expense expense2 = new Expense();
//        expense2.setAmountINR(200F);
//        expense2.setDate(LocalDate.of(2022, 2, 1));
//        expense2.setIsReported(true);
//        expense2.setIsHidden(false);
//        expenses.add(expense2);
//
//        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenses);
//
//        HashMap<String, Float> totalAmountByYear = new HashMap<>();
//        totalAmountByYear.put("2022", 300F);
//
//        HashMap<String, Float> yearlyReimbursementRatio = new HashMap<>();
//        yearlyReimbursementRatio.put("2022", 0.5F);
//
//        when(categoryService.getTotalAmountByYearForAllCategories()).thenReturn(totalAmountByYear);
//        when(categoryService.getYearlyReimbursementRatioForAllCategories()).thenReturn(yearlyReimbursementRatio);
//
//        // Act
//        HashMap<String, Object> analyticsData = categoryService.getYearlyCategoryAnalyticsForAllCategories();
//
//        // Assert
//        assertEquals(2, analyticsData.size());
//        assertEquals(totalAmountByYear, analyticsData.get("categoryTotalAmountByYear"));
//        assertEquals(yearlyReimbursementRatio, analyticsData.get("yearlyReimbursementRatio"));
//        verify(categoryService).getTotalAmountByYearForAllCategories();
//        verify(categoryService).getYearlyReimbursementRatioForAllCategories();
//    }



//    @Test
//    void testGetMonthlyCategoryAnalyticsForAllCategories() {
//        // Arrange
//        Long year = 2022L;
//
//        HashMap<String, Float> totalAmountByMonth = new HashMap<>();
//        totalAmountByMonth.put("Jan", 100F);
//
//        HashMap<String, Float> monthlyReimbursementRatio = new HashMap<>();
//        monthlyReimbursementRatio.put("Jan", 0.5F);
//
//        when(categoryService.getTotalAmountByMonthForAllCategories(year)).thenReturn(totalAmountByMonth);
//        when(categoryService.getMonthlyReimbursementRatioForAllCategories(year)).thenReturn(monthlyReimbursementRatio);
//
//        // Act
//        HashMap<String, Object> analyticsData = categoryService.getMonthlyCategoryAnalyticsForAllCategories(year);
//
//        // Assert
//        assertEquals(2, analyticsData.size());
//        assertEquals(totalAmountByMonth, analyticsData.get("categoryTotalAmountByMonth"));
//        assertEquals(monthlyReimbursementRatio, analyticsData.get("monthlyReimbursementRatio"));
//        verify(categoryService).getTotalAmountByMonthForAllCategories(year);
//        verify(categoryService).getMonthlyReimbursementRatioForAllCategories(year);
//    }
}
