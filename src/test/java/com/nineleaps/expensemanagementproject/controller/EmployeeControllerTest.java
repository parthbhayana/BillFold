package com.nineleaps.expensemanagementproject.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nineleaps.expensemanagementproject.DTO.EmployeeDTO;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeControllerTest {

    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployeeDetails() {
        // Create a list of employees
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee());
        employeeList.add(new Employee());
        employeeList.add(new Employee());

        // Mock the service method
        when(employeeService.getAllEmployeeDetails()).thenReturn(employeeList);

        // Call the controller method
        List<Employee> result = employeeController.getAllEmployeeDetails();

        // Verify the result
        assertEquals(3, result.size());
        verify(employeeService, times(1)).getAllEmployeeDetails();
    }

    @Test
    void testSaveEmployeeDetails() {
        // Create a sample employeeDTO
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeEmail("test@example.com");
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");

        // Create a sample employee entity
        Employee employee = new Employee();
        employee.setEmployeeEmail("test@example.com");
        employee.setFirstName("John");
        employee.setLastName("Doe");

        // Mock the service method
        when(employeeService.saveEmployeeDetails(employeeDTO)).thenReturn(employee);

        // Call the controller method
        Employee result = employeeController.save(employeeDTO);

        // Verify the result
        assertEquals(employee, result);
        verify(employeeService, times(1)).saveEmployeeDetails(employeeDTO);
    }

    @Test
    void testUpdateEmployeeDetails() {
        // Create a sample employeeDTO
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeEmail("test@example.com");
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");

        // Create a sample employee entity
        Employee employee = new Employee();
        employee.setEmployeeEmail("test@example.com");
        employee.setFirstName("John");
        employee.setLastName("Doe");

        // Mock the service method
        when(employeeService.updateEmployeeDetails(employeeDTO, 1L)).thenReturn(employee);

        // Call the controller method
        Employee result = employeeController.updateEmployee(employeeDTO, 1L);

        // Verify the result
        assertEquals(employee, result);
        verify(employeeService, times(1)).updateEmployeeDetails(employeeDTO, 1L);
    }


    @Test
    void testGetEmployeeById() {
        // Create a sample employee
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        // Mock the service method
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        // Call the controller method
        Employee result = employeeController.getEmployeeById(1L);

        // Verify the result
        assertEquals(employee, result);
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void testDeleteEmployeeById() {
        // Call the controller method
        employeeController.deleteEmployeeById(1L);

        // Verify the method call
        verify(employeeService, times(1)).deleteEmployeeDetailsById(1L);
    }

    @Test
    void testHideEmployee() {
        // Call the controller method
        employeeController.hideEmployee(1L);

        // Verify the method call
        verify(employeeService, times(1)).hideEmployee(1L);
    }

    @Test
    void testSetFinanceAdmin() {
        // Call the controller method
        employeeController.setFinanceAdmin(1L);

        // Verify the method call
        verify(employeeService, times(1)).setFinanceAdmin(1L);
    }

    @Test
    void testGetEmployeeDetails() {
        // Create a sample employee
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        // Mock the service method
        when(employeeService.getEmployeeDetails(1L)).thenReturn(Optional.of(employee));

        // Call the controller method
        Optional<Employee> result = employeeController.getEmployeeDetails(1L);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
        verify(employeeService, times(1)).getEmployeeDetails(1L);
    }

    @Test
    void testEditEmployeeDetails() {
        // Call the controller method
        employeeController.editEmployeeDetails(1L, "manager@example.com", 1234567890L, "EMP001", "Manager");

        // Verify the method call
        verify(employeeService, times(1)).editEmployeeDetails(1L, "manager@example.com", 1234567890L, "EMP001", "Manager");
    }



}
