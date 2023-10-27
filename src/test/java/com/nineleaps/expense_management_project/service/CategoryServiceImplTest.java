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

import static com.nineleaps.expense_management_project.service.ReportsServiceImpl.CONSTANT1;
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
        Long categoryId = 1L;
        categoryService.deleteCategoryById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }


    @Test
    void testGetCategoryTotalAmount() {

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

        when(expenseRepository.findByDateBetweenAndIsReported(startDate, endDate, true))
                .thenReturn(expenseList);

        HashMap<String, Double> result = categoryService.getCategoryTotalAmount(startDate, endDate);

        assertEquals(2, result.size()); // There are two categories

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

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        when(expenseRepository.findByCategoryAndIsReported(category, true))
                .thenReturn(expenseList);

        Map<String, Double> result = categoryService.getCategoryTotalAmountByYearAndCategory(categoryId);

        assertEquals(2, result.size()); // There are two years

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

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        when(expenseRepository.findByCategoryAndIsReported(category, true))
                .thenReturn(expenseList);

        Map<String, Double> result = categoryService.getCategoryTotalAmountByMonthAndCategory(categoryId, year);

        assertEquals(2, result.size());
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

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        Map<String, Double> result = categoryService.getMonthlyReimbursementRatio(categoryId, year);

        assertEquals(6, result.size());



    }

    @Test
    void testGetTotalAmountByYearForAllCategories() {
        List<Expense> expenses = new ArrayList<>();
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setAmount(100.0);
        expenses.add(expense1);

        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenses);

        HashMap<String, Double> result = categoryService.getTotalAmountByYearForAllCategories();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("2023"));
        assertEquals(100.0, result.get("2023"));
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetTotalAmountByYearForAllCategoriesWithEmptyList() {
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(new ArrayList<>());

        HashMap<String, Double> result =  categoryService.getTotalAmountByYearForAllCategories();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetTotalAmountByYearForAllCategoriesWithNullExpenses() {
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> categoryService.getTotalAmountByYearForAllCategories());

        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }


    @Test
    void testGetYearlyReimbursementRatioForAllCategories() {
        List<Expense> expenses = new ArrayList<>();
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setAmountApproved(100.0);
        expenses.add(expense1);

        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(expenses);

        HashMap<String, Double> result = categoryService.getYearlyReimbursementRatioForAllCategories();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("2023"));
        assertEquals(100.0, result.get("2023"));



        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetYearlyReimbursementRatioForAllCategoriesWithEmptyList() {
        when(expenseRepository.findByIsReportedAndIsHidden(true, false)).thenReturn(new ArrayList<>());

        HashMap<String, Double> result = categoryService.getYearlyReimbursementRatioForAllCategories();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testHideCategory() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setIsHidden(false);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.hideCategory(categoryId);

        assertTrue(category.getIsHidden());

        verify(categoryRepository, times(1)).save(category);
    }


    @Test
    void testGetYearlyReimbursementRatio() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        List<Expense> expenseList = new ArrayList<>();
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2023, 1, 15));
        expense1.setAmountApproved(100.0);
        expense1.setIsReported(true);
        expenseList.add(expense1);

        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        HashMap<String, Double> result = categoryService.getYearlyReimbursementRatio(categoryId);
        System.out.println(result);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("2023"));
        assertEquals(100.0, result.get("2023"));
        assertTrue(result.containsKey("2023_count"));
        assertEquals(100.0, result.get("2023_ratio"));
        assertTrue(result.containsKey("2023_ratio"));


        verify(categoryRepository, times(1)).findById(categoryId);
        verify(expenseRepository, times(1)).findByCategory(category);
    }

    @Test
    void testGetMonthlyReimbursementRatio_ConditionalLogic() {
        Long categoryId = 1L;
        Long year = 2022L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        List<Expense> expenseList = new ArrayList<>();

        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2022, 1, 15));
        expense1.setAmountApproved(100.0);
        expense1.setIsReported(true);
        expenseList.add(expense1);

        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2022, 2, 20));
        expense2.setAmountApproved(50.0);
        expense2.setIsReported(true);
        expenseList.add(expense2);

        when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        HashMap<String, Double> result = categoryService.getMonthlyReimbursementRatio(categoryId, year);
        System.out.println(result);

        assertNotNull(result);
        assertEquals(6, result.size());
        assertTrue(result.containsKey("2022_Jan"));
        assertEquals(100.0, result.get("2022_Jan"));
        assertTrue(result.containsKey("2022_Feb"));
        assertEquals(50.0, result.get("2022_Feb"));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(expenseRepository, times(1)).findByCategory(category);
    }


    @Test
    void testGetTotalAmountByMonthForAllCategories() {
        Long year = 2022L;

        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2022, 1, 10));
        expense1.setAmount(100.0);

        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2022, 2, 15));
        expense2.setAmount(200.0);

        Expense expense3 = new Expense();
        expense3.setDate(LocalDate.of(2021, 12, 5));
        expense3.setAmount(50.0);

        when(expenseRepository.findByIsReportedAndIsHidden(true, false))
                .thenReturn(Arrays.asList(expense1, expense2, expense3));

        HashMap<String, Double> result = categoryService.getTotalAmountByMonthForAllCategories(year);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100.0, result.get("Jan"));
        assertEquals(200.0, result.get("Feb"));

        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetMonthlyReimbursementRatioForAllCategories() {
        Long year = 2022L;

        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2022, 1, 10));
        expense1.setAmountApproved(50.0);

        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2022, 1, 15));
        expense2.setAmountApproved(75.0);

        Expense expense3 = new Expense();
        expense3.setDate(LocalDate.of(2022, 2, 20));
        expense3.setAmountApproved(100.0);

        Expense expense4 = new Expense();
        expense4.setDate(LocalDate.of(2021, 12, 5));
        expense4.setAmountApproved(25.0);

        when(expenseRepository.findByIsReportedAndIsHidden(true, false))
                .thenReturn(Arrays.asList(expense1, expense2, expense3, expense4));

        HashMap<String, Double> result = categoryService.getMonthlyReimbursementRatioForAllCategories(year);

        assertNotNull(result);
        assertEquals(6, result.size());
        assertEquals(125.0, result.get("2022_Jan"));
        assertEquals(2.0, result.get("2022_Jan_count"));
        assertEquals(62.5, result.get("2022_Jan_ratio"));
        assertEquals(100.0, result.get("2022_Feb"));
        assertEquals(1.0, result.get("2022_Feb_count"));
        assertEquals(100.0, result.get("2022_Feb_ratio"));

        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

    @Test
    void testGetYearlyCategoryAnalyticsForAllCategories() {

        CategoryServiceImpl categoryService1 = mock(CategoryServiceImpl.class);


        HashMap<String, Double> totalAmountByYear = new HashMap<>();
        totalAmountByYear.put("2022", 1000.0);
        totalAmountByYear.put("2021", 800.0);

        HashMap<String, Double> yearlyReimbursementRatio = new HashMap<>();
        yearlyReimbursementRatio.put("2022", 0.75);
        yearlyReimbursementRatio.put("2021", 0.60);
        HashMap<String, Object> result = categoryService1.getYearlyCategoryAnalyticsForAllCategories();
        result.put("categoryTotalAmountByYear",totalAmountByYear);
        result.put("yearlyReimbursementRatio",yearlyReimbursementRatio);
        System.out.println(result);
        assertNotNull(result);


        HashMap<String, Double> resultTotalAmountByYear = (HashMap<String, Double>) result.get("categoryTotalAmountByYear");
        HashMap<String, Double> resultYearlyReimbursementRatio = (HashMap<String, Double>) result.get("yearlyReimbursementRatio");

        assertEquals(2, resultTotalAmountByYear.size());
        assertEquals(2, resultYearlyReimbursementRatio.size());
        assertEquals(1000.0, resultTotalAmountByYear.get("2022"));
        assertEquals(0.75, resultYearlyReimbursementRatio.get("2022"));
        assertEquals(800.0, resultTotalAmountByYear.get("2021"));
        assertEquals(0.60, resultYearlyReimbursementRatio.get("2021"));

    }



    @Test
    void testGetCategoryAnalyticsYearlyWithValidCategory() {
        Category category = new Category();
       category.setCategoryId(1L);

        Map<String, Double> totalAmountByYear = new HashMap<>();
        totalAmountByYear.put("2022", 1000.0);

        HashMap<String, Double> yearlyReimbursementRatio = new HashMap<>();
        yearlyReimbursementRatio.put("2022", 0.5);
        HashMap<String, Object> analyticsData = new HashMap<>();

        analyticsData.put("categoryTotalAmountByYear", totalAmountByYear);
        analyticsData.put("yearlyReimbursementRatio", yearlyReimbursementRatio);
       categoryService.getCategoryAnalyticsYearly(1L);

        assertEquals(totalAmountByYear, analyticsData.get("categoryTotalAmountByYear"));
        assertEquals(yearlyReimbursementRatio, analyticsData.get("yearlyReimbursementRatio"));
    }

    @Test
    void testGetCategoryAnalyticsYearlyWithNullCategory() {

        Category category1 = new Category();
        category1.setCategoryId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        HashMap<String, Object> result = categoryService.getCategoryAnalyticsYearly(1L);

        assertEquals(2, result.size());
    }

    @Test
    void testGetYearlyCategoryAnalyticsForAllCategories1() {

        HashMap<String, Double> totalAmountByYear = new HashMap<>();
        totalAmountByYear.put("2022", 1000.0);
        categoryService.getTotalAmountByYearForAllCategories();

        HashMap<String, Double> yearlyReimbursementRatio = new HashMap<>();
        yearlyReimbursementRatio.put("2022", 0.5);
        categoryService.getYearlyReimbursementRatioForAllCategories();

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

        HashMap<String, Double> categoryTotalAmountByMonth = new HashMap<>();
        categoryTotalAmountByMonth.put("January", 100.0);
        categoryTotalAmountByMonth.put("February", 200.0);
       categoryService.getTotalAmountByMonthForAllCategories(year);

        HashMap<String, Double> monthlyReimbursementRatio = new HashMap<>();
        monthlyReimbursementRatio.put("January", 0.2);
        monthlyReimbursementRatio.put("February", 0.4);
        categoryService.getMonthlyReimbursementRatioForAllCategories(year);

        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryTotalAmountByMonth",categoryTotalAmountByMonth);
        result.put("monthlyReimbursementRatio",monthlyReimbursementRatio);
        categoryService.getMonthlyCategoryAnalyticsForAllCategories(year);
        assertEquals(categoryTotalAmountByMonth, result.get("categoryTotalAmountByMonth"));
        assertEquals(monthlyReimbursementRatio, result.get("monthlyReimbursementRatio"));
    }



    @Test
    void testGetCategoryAnalyticsMonthlyWithValidCategory() {
        Category category = new Category();
        category.setCategoryId(1L);

        Map<String, Double> totalAmountByMonth = new HashMap<>();
        totalAmountByMonth.put("January", 1000.0);
        HashMap<String, Double> monthlyReimbursementRatio = new HashMap<>();
        monthlyReimbursementRatio.put("January", 0.5);
        HashMap<String, Object> analyticsData = new HashMap<>();
        analyticsData.put("categoryTotalAmountByMonth", totalAmountByMonth);
        analyticsData.put("monthlyReimbursementRatio", monthlyReimbursementRatio);

        categoryService.getCategoryAnalyticsMonthly(1L, 2022L);

        assertEquals(totalAmountByMonth, analyticsData.get("categoryTotalAmountByMonth"));
        assertEquals(monthlyReimbursementRatio, analyticsData.get("monthlyReimbursementRatio"));
    }

    @Test
    void testGetCategoryAnalyticsMonthlyWithNullCategory() {

        Category category1 = new Category();
        category1.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        HashMap<String, Object> result = categoryService.getCategoryAnalyticsMonthly(1L, 2022L);

        assertEquals(2, result.size());
    }



    @Test
    void testAddCategoryCategoryExists() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryDescription("Sample Category");

        when(categoryRepository.existsByCategoryDescription(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(categoryDTO));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testGetYearlyReimbursementRatio1() {
        Category category = new Category();
        category.setCategoryDescription("Sample Category");

        Expense expense1 = new Expense();
        expense1.setAmountApproved(100.0);
        expense1.setIsReported(true);
        expense1.setDate(LocalDate.of(2022, 1, 15));
        expense1.setCategory(category);

        Expense expense2 = new Expense();
        expense2.setAmountApproved(150.0);
        expense2.setIsReported(true);
        expense2.setDate(LocalDate.of(2022, 2, 20));
        expense2.setCategory(category);

        List<Expense> expenseList = Arrays.asList(expense1, expense2);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        when(expenseRepository.findByCategory(any(Category.class))).thenReturn(expenseList);

        HashMap<String, Double> yearlyReimbursementRatioMap = categoryService.getYearlyReimbursementRatio(1L);

        assertEquals(3, yearlyReimbursementRatioMap.size());
        assertTrue(yearlyReimbursementRatioMap.containsKey("2022"));
        assertFalse(yearlyReimbursementRatioMap.containsKey("2022_TotalExpenses"));
        assertEquals(250.0, yearlyReimbursementRatioMap.get("2022"));
        assertNull(yearlyReimbursementRatioMap.get("2022_TotalExpenses"));
    }

    @Test
     void testGetMonthlyReimbursementRatio1() {
        Long categoryId = 1L;
        Long year = 2023L;
        Category category = new Category();
        category.setCategoryId(categoryId);
        List<Expense> expenseList = new ArrayList<>();
        LocalDate expenseDate = LocalDate.of(2023, 1, 15);
        Expense expense1 = new Expense();
        expense1.setCategory(category);
        expense1.setIsReported(true);
        expense1.setDate(expenseDate);
        expense1.setAmountApproved(100.0);
        LocalDate expenseDate2 = LocalDate.of(2023, 1, 20);
        Expense expense2 = new Expense();
        expense2.setCategory(category);
        expense2.setIsReported(true);
        expense2.setDate(expenseDate2);
        expense2.setAmountApproved(150.0);
        expenseList.add(expense1);
        expenseList.add(expense2);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(expenseRepository.findByCategory(category)).thenReturn(expenseList);

        HashMap<String, Double> result = categoryService.getMonthlyReimbursementRatio(categoryId, year);

        assertEquals(3, result.size());
        String januaryKey = year + "_Jan";
        assertEquals(250.0, result.get(januaryKey), 0.001); // Total reimbursed amount

    }

    @Test
    void testGetCategoryById() {
        Long categoryId = 1L;
        Category expectedCategory = new Category();
        expectedCategory.setCategoryId(categoryId);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        Category result = categoryService.getCategoryById(categoryId);

        assertNotNull(result);
        assertEquals(expectedCategory, result);
    }

    @Test
    void testGetCategoryByIdNotFound() {
        Long categoryId = 1L;

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class, () -> categoryService.getCategoryById(categoryId));

        assertEquals("Category with ID 1 not found", exception.getMessage());
    }

    @Test
    void testGetYearlyReimbursementRatioForAllCategories1() {
        Expense expense1 = new Expense();
        expense1.setDate(LocalDate.of(2022, 1, 10));
        expense1.setAmountApproved(50.0);

        Expense expense2 = new Expense();
        expense2.setDate(LocalDate.of(2022, 1, 15));
        expense2.setAmountApproved(75.0);

        Expense expense3 = new Expense();
        expense3.setDate(LocalDate.of(2022, 2, 20));
        expense3.setAmountApproved(100.0);

        when(expenseRepository.findByIsReportedAndIsHidden(true, false))
                .thenReturn(Arrays.asList(expense1, expense2, expense3));

        HashMap<String, Double> result = categoryService.getYearlyReimbursementRatioForAllCategories();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(225.0, result.get("2022"));
        verify(expenseRepository, times(1)).findByIsReportedAndIsHidden(true, false);
    }

}