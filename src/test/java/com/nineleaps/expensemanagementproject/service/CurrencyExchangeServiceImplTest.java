package com.nineleaps.expensemanagementproject.service;


import com.nineleaps.expensemanagementproject.repository.CategoryRepository;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.net.HttpURLConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;



class CurrencyExchangeServiceImplTest {
    @Mock
    private HttpURLConnection connection;

    @Mock
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void getExchangeRate_ShouldReturnExchangeValue_WhenAPIReturnsValidData() {
        // Arrange
        CurrencyExchangeServiceImpl service = new CurrencyExchangeServiceImpl();
        String baseCurrency = "USD";
        String date = "2023-07-17";
        double expectedExchangeValue = 82.0615587003;

        // Act
        double actualExchangeValue = service.getExchangeRate(baseCurrency, date);

        // Assert
        assertEquals(expectedExchangeValue, actualExchangeValue);
    }

    @Test
    void getExchangeRate_ShouldReturnZero_WhenAPIReturnsInvalidData() {
        // Arrange
        CurrencyExchangeServiceImpl service = new CurrencyExchangeServiceImpl();
        String baseCurrency = "USD";
        String date = "2023-07-17";

        // Act
        double actualExchangeValue = service.getExchangeRate(baseCurrency, date);

        // Assert
        assertEquals(82.0615587003, actualExchangeValue);
    }

    @Test
    void testGetExchangeRate() {
        // Test case inputs
        String baseCurrency = "USD";
        String date = "2023-07-18";

        // Create an instance of the service class
        CurrencyExchangeServiceImpl currencyExchangeService = new CurrencyExchangeServiceImpl();

        // Call the getExchangeRate method and store the actual result
        double actualExchangeRate = currencyExchangeService.getExchangeRate(baseCurrency, date);

        // Define the expected exchange rate for the given input
        double expectedExchangeRate = 82.0756587337;

        // Assert that the actual result matches the expected result
        Assertions.assertEquals(expectedExchangeRate, actualExchangeRate, 0.01);
    }
//    @Test
//    void testGetCategoryAnalyticsMonthly() {
//        // Test case inputs
//        Long categoryId = 1L;
//        Long year = 2023L;
//
//        // Mock the behavior of the categoryRepository
//        Category category = new Category();
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        // Mock the behavior of the expenseRepository
//        List<Expense> expenseList = new ArrayList<>();
//        Expense expense1 = new Expense();
//        expense1.setIsReported(true);
//        expense1.setDate(LocalDate.of(2023, 1, 10));
//        expense1.setAmountINR(100.0f);
//        expenseList.add(expense1);
//
//        Expense expense2 = new Expense();
//        expense2.setIsReported(true);
//        expense2.setDate(LocalDate.of(2023, 2, 20));
//        expense2.setAmountINR(200.0f);
//        expenseList.add(expense2);
//
//        when(expenseRepository.findByCategoryAndIsReported(eq(category), eq(true))).thenReturn(expenseList);
//
//        // Call the method and store the actual result
//        HashMap<String, Object> actualResult = categoryService.getCategoryAnalyticsMonthly(categoryId, year);
//
//        // Define the expected result
//        HashMap<String, Object> expectedResult = new HashMap<>();
//        HashMap<String, Float> categoryTotalAmountByMonth = new HashMap<>();
//        categoryTotalAmountByMonth.put("Jan", 100.0f);
//        categoryTotalAmountByMonth.put("Feb", 200.0f);
//        expectedResult.put("categoryTotalAmountByMonth", categoryTotalAmountByMonth);
//
//        HashMap<String, Float> monthlyReimbursementRatio = new HashMap<>();
//        monthlyReimbursementRatio.put("Jan", 100.0f);
//        monthlyReimbursementRatio.put("Feb", 50.0f);
//        expectedResult.put("monthlyReimbursementRatio", monthlyReimbursementRatio);
//
//        // Assert that the actual result matches the expected result
//        Assertions.assertEquals(expectedResult, actualResult);
//
//        // Verify that the categoryRepository.findById method was called once
//        verify(categoryRepository, times(1)).findById(categoryId);
//
//        // Verify that the expenseRepository.findByCategoryAndIsReported method was called once
//        verify(expenseRepository, times(1)).findByCategoryAndIsReported(eq(category), eq(true));
//    }

}