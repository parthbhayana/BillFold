package com.nineleaps.expense_management_project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.nineleaps.expense_management_project.dto.ReportsDTO;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.entity.Expense;
import com.nineleaps.expense_management_project.entity.Reports;
import com.nineleaps.expense_management_project.repository.CategoryRepository;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import com.nineleaps.expense_management_project.repository.ExpenseRepository;
import com.nineleaps.expense_management_project.repository.ReportsRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

public class ReportsServiceTest {


    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private ExpenseServiceImpl expenseServices;

    @Mock
    private ReportsServiceImpl reportsService;

    @Mock
    private EmployeeServiceImpl employeeServices;

    @BeforeEach
    void setUp() {
        reportsRepository = mock(ReportsRepository.class);
        expenseRepository = mock(ExpenseRepository.class);
        expenseServices = mock(ExpenseServiceImpl.class);
        reportsService = new ReportsServiceImpl();
    }

//    @Test
//    public void testGetAllReportsWithValidEmployeeId() {
//        // Create a valid employee and expected reports
//        Employee employee = new Employee();
//        employee.setEmployeeId(1L); // Set a valid employeeId
//        employee.setEmployeeEmail("employee@example.com");
//
//        Reports reports1 = new Reports();
//        Reports reports2 = new Reports();
//
//        // Create mock expenses
//        Expense expense1 = new Expense();
//        Expense expense2 = new Expense();
//
//        expense1.setReports(reports1);
//        expense2.setReports(reports2);
//
//        // Mock the behavior of employeeRepository
//        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
//
//        // Mock the behavior of expenseRepository
//        List<Expense> expenses = Arrays.asList(expense1, expense2);
//        Mockito.when(expenseRepository.findByEmployeeAndIsHidden(Mockito.any(), Mockito.eq(false), Mockito.any()))
//                .thenReturn(expenses);
//
//        // Test the getAllReports method
//        Set<Reports> result = reportsService.getAllReports(1L);
//
//        // Assertions
//        Mockito.verify(employeeRepository, Mockito.times(1)).findById(1L);
//        Mockito.verify(expenseRepository, Mockito.times(1))
//                .findByEmployeeAndIsHidden(Mockito.any(), Mockito.eq(false), Mockito.any());
//        Mockito.verifyNoMoreInteractions(employeeRepository, expenseRepository);
//
//        // Check if the result contains the expected reports
//        Set<Reports> expectedReports = new HashSet<>(Arrays.asList(reports1, reports2));
//        org.junit.jupiter.api.Assertions.assertEquals(expectedReports, result);
//    }
//
//    @Test
//    public void testAddReport() {
//        // Mock employee data
//
//        Employee employee = new Employee();
//        employee.setEmployeeId(1L);
//        employee.setEmployeeEmail("employee@example.com");
//        employee.setManagerEmail("manager@example.com");
//        employee.setFirstName("John");
//        employee.setLastName("Doe");
//        employee.setOfficialEmployeeId("EMP123");
//
//        // Mock expense data
//        List<Long> expenseIds = new ArrayList<>();
//        expenseIds.add(1L);
//        expenseIds.add(2L);
//
//        Expense expense1 = new Expense();
//        expense1.setAmount(100.0);
//
//        Expense expense2 = new Expense();
//        expense2.setAmount(200.0);
//
//        List<Expense> expenses = new ArrayList<>();
//        expenses.add(expense1);
//        expenses.add(expense2);
//
//        // Mock ReportsDTO
//        ReportsDTO reportsDTO = new ReportsDTO();
//        reportsDTO.setReportTitle("Expense Report");
//
//        // Mock employee service behavior
//        Mockito.when(employeeServices.getEmployeeById(1L)).thenReturn(employee);
//
//        // Mock expense repository behavior
//        Mockito.when(expenseRepository.findAllById(expenseIds)).thenReturn(expenses);
//
//        // Mock expense service behavior
//        Mockito.when(expenseServices.getExpenseByReportId(Mockito.anyLong())).thenReturn(expenses);
//
//        // Mock the save method of reportsRepository
//        Mockito.when(reportsRepository.save(Mockito.any())).thenReturn(new Reports());
//
//        // Call the method to be tested
//        Reports result = reportsService.addReport(reportsDTO, employee.getEmployeeId(), expenseIds);
//
//        // Assertions
//        Mockito.verify(employeeServices, Mockito.times(1)).getEmployeeById(employee.getEmployeeId());
//        Mockito.verify(expenseRepository, Mockito.times(1)).findAllById(expenseIds);
//        Mockito.verify(expenseServices, Mockito.times(1)).getExpenseByReportId(Mockito.anyLong());
//        Mockito.verify(reportsRepository, Mockito.times(1)).save(Mockito.any());
//
//        // Verify that the result is not null
//        assertEquals(result.getEmployeeId(), employee.getEmployeeId());
//    }
//



}
