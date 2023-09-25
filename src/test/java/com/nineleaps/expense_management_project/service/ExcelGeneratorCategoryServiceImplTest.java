package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.nineleaps.expense_management_project.entity.Category;
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcelGeneratorCategoryServiceImplTest {

    @InjectMocks
    private ExcelGeneratorCategoryServiceImpl excelGeneratorService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGenerateExcel() throws Exception {
        // Mock data
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        List<Category> categories = new ArrayList<>();

        Category category1 = new Category();
        category1.setCategoryDescription("Category 1");

        Category category2 = new Category();
        category2.setCategoryDescription("Category 2");

        categories.add(category1);
        categories.add(category2);

        HashMap<String, Double> categoryAmountMap = new HashMap<>();
        categoryAmountMap.put("Category 1", 1000.0);
        categoryAmountMap.put("Category 2", 2000.0);

        // Call the method
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        excelGeneratorService.generateExcel(excelStream, startDate, endDate);

        // Assertions (you may need to write specific assertions based on your requirements)
        assertNotNull(excelStream);
    }



    @Test
    void testGenerateExcelAndSendEmail() throws Exception {
        // Mock data and behavior
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        categories.add(new Category());

        when(expenseRepository.findByDateBetween(eq(startDate), eq(endDate))).thenReturn(new ArrayList<>());

        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();

        // Call the method
        String result = excelGeneratorService.generateExcelAndSendEmail(null, startDate, endDate);

        // Assertions
        assertEquals("No data available for the selected period.So, Email can't be sent!", result);

        // Verify behavior
        verify(expenseRepository, times(1)).findByDateBetween(eq(startDate), eq(endDate));
    }



}
