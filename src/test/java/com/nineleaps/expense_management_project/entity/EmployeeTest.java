package com.nineleaps.expense_management_project.entity;

import com.nineleaps.expense_management_project.controller.EmployeeController;
import com.nineleaps.expense_management_project.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeTest {

    @InjectMocks
    private Employee employee;

    @Mock
    private Expense expense;

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetEmployeeId() {
        Long employeeId = 1L;
        employee.setEmployeeId(employeeId);
        assertEquals(employeeId, employee.getEmployeeId());
    }

    @Test
    void testGetOfficialEmployeeId() {
        String officialEmployeeId = "EMP001";
        employee.setOfficialEmployeeId(officialEmployeeId);
        assertEquals(officialEmployeeId, employee.getOfficialEmployeeId());
    }

    @Test
    void testGetFirstName() {
        String firstName = "John";
        employee.setFirstName(firstName);
        assertEquals(firstName, employee.getFirstName());
    }

    @Test
    void testGetMiddleName() {
        String middleName = "James";
        employee.setMiddleName(middleName);
        assertEquals(middleName, employee.getMiddleName());
    }

    @Test
    void testGetLastName() {
        String lastName = "Doe";
        employee.setLastName(lastName);
        assertEquals(lastName, employee.getLastName());
    }

    @Test
    void testGetEmployeeEmail() {
        String employeeEmail = "john.doe@example.com";
        employee.setEmployeeEmail(employeeEmail);
        assertEquals(employeeEmail, employee.getEmployeeEmail());
    }

    @Test
    void testGetManagerEmail() {
        String managerEmail = "manager@example.com";
        employee.setManagerEmail(managerEmail);
        assertEquals(managerEmail, employee.getManagerEmail());
    }

    @Test
    void testGetManagerName() {
        String managerName = "Manager Name";
        employee.setManagerName(managerName);
        assertEquals(managerName, employee.getManagerName());
    }

    @Test
    void testGetHrName() {
        String hrName = "HR Name";
        employee.setHrName(hrName);
        assertEquals(hrName, employee.getHrName());
    }

    @Test
    void testGetHrEmail() {
        String hrEmail = "hr@example.com";
        employee.setHrEmail(hrEmail);
        assertEquals(hrEmail, employee.getHrEmail());
    }

    @Test
    void testGetLndName() {
        String lndName = "LND Name";
        employee.setLndName(lndName);
        assertEquals(lndName, employee.getLndName());
    }

    @Test
    void testGetLndEmail() {
        String lndEmail = "lnd@example.com";
        employee.setLndEmail(lndEmail);
        assertEquals(lndEmail, employee.getLndEmail());
    }

    @Test
    void testGetMobileNumber() {
        Long mobileNumber = 1234567890L;
        employee.setMobileNumber(mobileNumber);
        assertEquals(mobileNumber, employee.getMobileNumber());
    }

    @Test
    void testGetIsFinanceAdmin() {
        Boolean isFinanceAdmin = true;
        employee.setIsFinanceAdmin(isFinanceAdmin);
        assertEquals(isFinanceAdmin, employee.getIsFinanceAdmin());
    }

    @Test
    void testGetImageUrl() {
        String imageUrl = "http://example.com/image.jpg";
        employee.setImageUrl(imageUrl);
        assertEquals(imageUrl, employee.getImageUrl());
    }

    @Test
    void testGetIsHidden() {
        Boolean isHidden = true;
        employee.setIsHidden(isHidden);
        assertEquals(isHidden, employee.getIsHidden());
    }

    @Test
    void testGetExpenseList() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);
        employee.setExpenseList(expenses);
        assertEquals(expenses, employee.getExpenseList());
    }

    @Test
    void testGetRole() {
        String role = "ADMIN";
        employee.setRole(role);
        assertEquals(role, employee.getRole());
    }

    @Test
    void testGetToken() {
        String token = "abc123";
        employee.setToken(token);
        assertEquals(token, employee.getToken());
    }

    @Test
    void testEmployeeConstructor() {
        // Arrange
        Long employeeId = 1L;
        String officialEmployeeId = "EMP001";
        String firstName = "John";
        String middleName = "Doe";
        String lastName = "Smith";
        String employeeEmail = "john.smith@example.com";
        String managerEmail = "manager@example.com";

        // Act
        Employee employee = new Employee(
                employeeId, officialEmployeeId, firstName, middleName, lastName, employeeEmail, managerEmail);

        // Assert
        assertNotNull(employee);
        assertEquals(employeeId, employee.getEmployeeId());
        assertEquals(officialEmployeeId, employee.getOfficialEmployeeId());
        assertEquals(firstName, employee.getFirstName());
        assertEquals(middleName, employee.getMiddleName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(employeeEmail, employee.getEmployeeEmail());
        assertEquals(managerEmail, employee.getManagerEmail());
    }

    @Test
    void testEmployeeConstructorWithAdditionalParameters() {
        // Arrange
        String imageUrl = "http://example.com/image.jpg";
        Boolean isHidden = true;
        List<Expense> expenseList = new ArrayList<>(); // Create an empty list or add sample expenses
        String role = "Manager";
        String token = "abc123token";

        // Act
        Employee employee = new Employee(imageUrl, isHidden, expenseList, role, token);

        // Assert
        assertNotNull(employee);
        assertEquals(imageUrl, employee.getImageUrl());
        assertEquals(isHidden, employee.getIsHidden());
        assertEquals(expenseList, employee.getExpenseList());
        assertEquals(role, employee.getRole());
        assertEquals(token, employee.getToken());
    }

    @Test
    void testUpdateEmployee() {
        // Create a sample EmployeeDTO
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        employeeDTO.setEmployeeEmail("john@example.com");

        // Create a sample Employee entity
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setEmployeeEmail("john@example.com");

        // Mock the service method to return the updated Employee
        when(employeeService.updateEmployeeDetails(employeeDTO, 1L)).thenReturn(employee);

        // Call the method
        Employee updatedEmployee = employeeController.updateEmployee(employeeDTO, 1L);

        // Verify the response
        assertEquals(employee, updatedEmployee);

        // Verify that the service method was called with the correct arguments
        verify(employeeService).updateEmployeeDetails(employeeDTO, 1L);
    }

    @Test
    void testSave() {
        // Create a sample EmployeeDTO
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        employeeDTO.setEmployeeEmail("john@example.com");

        // Create a sample Employee entity
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setEmployeeEmail("john@example.com");

        // Mock the service method to return the saved Employee
        when(employeeService.saveEmployeeDetails(employeeDTO)).thenReturn(employee);

        // Call the method
        Employee savedEmployee = employeeController.save(employeeDTO);

        // Verify the response
        assertEquals(employee, savedEmployee);

        // Verify that the service method was called with the correct arguments
        verify(employeeService).saveEmployeeDetails(employeeDTO);
    }

}