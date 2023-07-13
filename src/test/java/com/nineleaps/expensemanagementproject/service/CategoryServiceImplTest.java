//package com.nineleaps.expensemanagementproject.service;
//
//import com.nineleaps.expensemanagementproject.entity.Category;
//import com.nineleaps.expensemanagementproject.entity.Expense;
//import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
//import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//class CategoryServiceImplTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private ExpenseRepository expenseRepository;
//
//    private CategoryServiceImpl categoryService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        categoryService = new CategoryServiceImpl(categoryRepository, expenseRepository);
//    }
//
//    @Test
//    void deleteCategoryById() {
//        // Arrange
//        Long categoryId = 1L;
//
//        // Act
//        categoryService.deleteCategoryById(categoryId);
//
//        // Assert
//        verify(categoryRepository, times(1)).deleteById(categoryId);
//    }
//
//    @Test
//    void updateCategory() {
//        // Arrange
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setCategoryDescription("Category Name");
//
//        when(categoryRepository.save(category)).thenReturn(category);
//
//        // Act
//        Category updatedCategory = categoryService.updateCategory(category);
//
//        // Assert
//        assertNotNull(updatedCategory);
//        assertEquals(category.getCategoryId(), updatedCategory.getCategoryId());
//        assertEquals(category.getCategoryDescription(), updatedCategory.getCategoryDescription());
//        verify(categoryRepository, times(1)).save(category);
//    }
//
//    @Test
//    void getCategoryById_existingCategory() {
//        // Arrange
//        Long categoryId = 1L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//        category.setCategoryDescription("Category Name");
//
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        // Act
//        Category retrievedCategory = categoryService.getCategoryById(categoryId);
//
//        // Assert
//        assertNotNull(retrievedCategory);
//        assertEquals(category.getCategoryId(), retrievedCategory.getCategoryId());
//        assertEquals(category.getCategoryName(), retrievedCategory.getCategoryName());
//        verify(categoryRepository, times(1)).findById(categoryId);
//    }
//
//    @Test
//    void getCategoryById_nonExistingCategory() {
//        // Arrange
//        Long categoryId = 1L;
//
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
//
//        // Act
//        Category retrievedCategory = categoryService.getCategoryById(categoryId);
//
//        // Assert
//        assertNull(retrievedCategory);
//        verify(categoryRepository, times(1)).findById(categoryId);
//    }
//
//    @Test
//    void getAllCategories() {
//        // Arrange
//        List<Category> categories = new ArrayList<>();
//        categories.add(new Category(1L, "Category 1"));
//        categories.add(new Category(2L, "Category 2"));
//        categories.add(new Category(3L, "Category 3"));
//
//        when(categoryRepository.findAll()).thenReturn(categories);
//
//        // Act
//        List<Category> retrievedCategories = categoryService.getAllCategories();
//
//        // Assert
//        assertNotNull(retrievedCategories);
//        assertEquals(categories.size(), retrievedCategories.size());
//        for (int i = 0; i < categories.size(); i++) {
//            assertEquals(categories.get(i).getId(), retrievedCategories.get(i).getId());
//            assertEquals(categories.get(i).getCategoryName(), retrievedCategories.get(i).getCategoryName());
//        }
//        verify(categoryRepository, times(1)).findAll();
//    }
//
//    @Test
//    void addCategory() {
//        // Arrange
//        Category category = new Category();
//        category.setCategoryName("Category Name");
//
//        when(categoryRepository.save(category)).thenReturn(category);
//
//        // Act
//        Category addedCategory = categoryService.addCategory(category);
//
//        // Assert
//        assertNotNull(addedCategory);
//        assertEquals(category.getCategoryName(), addedCategory.getCategoryName());
//        verify(categoryRepository, times(1)).save(category);
//    }
//
//    @Test
//    void hideCategory() {
//        // Arrange
//        Long categoryId = 1L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//        category.setCategoryDescription("Category Name");
//        category.setIsHidden(false);
//
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//        when(categoryRepository.save(category)).thenReturn(category);
//
//        // Act
//        categoryService.hideCategory(categoryId);
//
//        // Assert
//        assertTrue(category.isHidden());
//        verify(categoryRepository, times(1)).findById(categoryId);
//        verify(categoryRepository, times(1)).save(category);
//    }
//
//    @Test
//    void getCategoryTotalAmount() {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 12, 31);
//
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setCategoryDescription("Category Name");
//
//        Expense expense1 = new Expense();
//        expense1.setExpenseId(1L);
//        expense1.setCategory(category);
//        expense1.setAmountINR(1000F);
//        expense1.setDate(LocalDate.of(2023, 1, 15));
//        expense1.setReported(true);
//
//        Expense expense2 = new Expense();
//        expense2.setId(2L);
//        expense2.setCategory(category);
//        expense2.setAmountINR(2000F);
//        expense2.setDate(LocalDate.of(2023, 2, 20));
//        expense2.setReported(true);
//
//        List<Expense> expenses = Arrays.asList(expense1, expense2);
//
//        when(expenseRepository.findByDateBetweenAndIsReported(startDate, endDate, true)).thenReturn(expenses);
//
//        // Act
//        HashMap<String, Float> categoryTotalAmountMap = categoryService.getCategoryTotalAmount(startDate, endDate);
//
//        // Assert
//        assertNotNull(categoryTotalAmountMap);
//        assertEquals(1, categoryTotalAmountMap.size());
//        assertTrue(categoryTotalAmountMap.containsKey(category.getCategoryName()));
//        assertEquals(3000F, categoryTotalAmountMap.get(category.getCategoryName()));
//        verify(expenseRepository, times(1)).findByDateBetweenAndIsReported(startDate, endDate, true);
//    }
//    @Test
//    void getCategoryTotalAmountByYearAndCategory() {
//    }
//
//    @Test
//    void getCategoryTotalAmountByMonthAndCategory() {
//    }
//
//    @Test
//    void getYearlyReimbursementRatio() {
//    }
//
//    @Test
//    void getMonthlyReimbursementRatio() {
//    }
//
//    @Test
//    void getCategoryAnalyticsYearly() {
//    }
//
//    @Test
//    void getCategoryAnalyticsMonthly() {
//    }
//
//    @Test
//    void getTotalAmountByYearForAllCategories() {
//    }
//
//    @Test
//    void getTotalAmountByMonthForAllCategories() {
//    }
//
//    @Test
//    void getYearlyReimbursementRatioForAllCategories() {
//    }
//
//    @Test
//    void getMonthlyReimbursementRatioForAllCategories() {
//    }
//
//    @Test
//    void getYearlyCategoryAnalyticsForAllCategories() {
//    }
//
//    @Test
//    void getMonthlyCategoryAnalyticsForAllCategories() {
//    }
//}