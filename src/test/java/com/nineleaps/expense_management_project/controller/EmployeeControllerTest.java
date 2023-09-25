package com.nineleaps.expense_management_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.service.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private IEmployeeService employeeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void testGetAllEmployeeDetails() {
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);
        employee1.setFirstName("John");

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);
        employee2.setFirstName("Jane");

        employees.add(employee1);
        employees.add(employee2);

        when(employeeService.getAllEmployeeDetails()).thenReturn(employees);

        List<Employee> response = employeeController.getAllEmployeeDetails();

        assertEquals(employees, response);
    }

//    @Test
//    void testSaveEmployeeDetails() {
//        EmployeeDTO employeeDTO = new EmployeeDTO("John",  "john@example.com", "manager@example.com");
//        Employee savedEmployee = new Employee();
//        savedEmployee.setEmployeeId(1L);
//        savedEmployee.setFirstName("John");
//        savedEmployee.setLastName("Doe");
//        savedEmployee.setEmployeeEmail("john@example.com");
//        savedEmployee.setManagerEmail("manager@example.com");
//
//        when(employeeService.saveEmployeeDetails(employeeDTO)).thenReturn(savedEmployee);
//
//        Employee responseEntity = employeeController.save(employeeDTO);
//
//
//        assertEquals(HttpStatus.OK, responseEntity.getToken());
//        assertEquals(savedEmployee, responseEntity.getRole());
//    }

    @Test
    void testGetEmployeeById() {
        // Create a mock Employee object to return from the service
        Long employeeId = 1L;
        Employee mockEmployee = new Employee();
        // Set properties of the mockEmployee as needed

        // Mock the service method to return the mockEmployee
        when(employeeService.getEmployeeById(employeeId)).thenReturn(mockEmployee);

        // Call the getEmployeeById method in the controller
        Employee resultEmployee = employeeController.getEmployeeById(employeeId);

        // Verify that the service method was called with the correct argument
        verify(employeeService, times(1)).getEmployeeById(employeeId);

        // Verify that the returned Employee matches the mockEmployee
        assertEquals(mockEmployee, resultEmployee);
    }

    @Test
    void testUpdateEmployee() {
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO("John",  "john@example.com", "manager@example.com");
        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeId(employeeId);
        updatedEmployee.setFirstName("Updated John");
        updatedEmployee.setLastName("Updated Doe");
        updatedEmployee.setEmployeeEmail("john@example.com");
        updatedEmployee.setManagerEmail("updated-manager@example.com");

        when(employeeService.updateEmployeeDetails(employeeDTO, employeeId)).thenReturn(updatedEmployee);

        Employee response = employeeController.updateEmployee(employeeDTO, employeeId);

        assertEquals(updatedEmployee, response);
    }



    @Test
    void testAdditionalEmployeeDetails() {
        Long employeeId = 1L;
        String officialEmployeeId = "EMP123";
        String managerEmail = "manager@example.com";
        Long mobileNumber = 1234567890L;
        String managerName = "Manager";
        String hrName = "HR";
        String hrEmail = "hr@example.com";

        Employee additionalDetailsEmployee = new Employee();
        additionalDetailsEmployee.setEmployeeId(employeeId);
        additionalDetailsEmployee.setOfficialEmployeeId(officialEmployeeId);
        additionalDetailsEmployee.setManagerEmail(managerEmail);
        additionalDetailsEmployee.setMobileNumber(mobileNumber);
        additionalDetailsEmployee.setManagerName(managerName);
        additionalDetailsEmployee.setHrName(hrName);
        additionalDetailsEmployee.setHrEmail(hrEmail);

        when(employeeService.additionalEmployeeDetails(employeeId, officialEmployeeId, managerEmail, mobileNumber,
                managerName, hrEmail, hrName)).thenReturn(Optional.of(additionalDetailsEmployee));

        Optional<Employee> response = employeeController.additionalEmployeeDetails(employeeId, officialEmployeeId,
                managerEmail, mobileNumber, managerName, hrName, hrEmail);

        assertTrue(response.isPresent());
        assertEquals(additionalDetailsEmployee, response.get());
    }

    @Test
    void testDeleteEmployeeById() {
        // Specify the employeeId to be deleted
        Long employeeId = 1L;

        // Call the deleteEmployeeById method in the controller
        employeeController.deleteEmployeeById(employeeId);

        // Verify that the service method was called with the correct argument
        verify(employeeService, times(1)).deleteEmployeeDetailsById(employeeId);
    }

    @Test
    void testHideEmployee() {
        // Specify the employeeId to hide
        Long employeeId = 1L;

        // Call the hideEmployee method in the controller
        employeeController.hideEmployee(employeeId);

        // Verify that the service method was called with the correct argument
        verify(employeeService, times(1)).hideEmployee(employeeId);
    }

    @Test
    void testSetFinanceAdmin() {
        // Specify the employeeId to set as a finance admin
        Long employeeId = 1L;

        // Call the setFinanceAdmin method in the controller
        employeeController.setFinanceAdmin(employeeId);

        // Verify that the service method was called with the correct argument
        verify(employeeService, times(1)).setFinanceAdmin(employeeId);
    }

    @Test
    void testGetEmployeeDetails() {
        // Specify the employeeId for which to retrieve details
        Long employeeId = 1L;

        // Create a mock Employee object to return from the service
        Employee mockEmployee = new Employee();
        // Set properties of the mockEmployee as needed

        // Mock the service method to return an Optional containing the mockEmployee
        when(employeeService.getEmployeeDetails(employeeId)).thenReturn(Optional.of(mockEmployee));

        // Call the getEmployeeDetails method in the controller
        Optional<Employee> resultEmployee = employeeController.getEmployeeDetails(employeeId);

        // Verify that the service method was called with the correct argument
        verify(employeeService, times(1)).getEmployeeDetails(employeeId);

        // Verify that the returned Employee matches the mockEmployee
        assertTrue(resultEmployee.isPresent());
        assertEquals(mockEmployee, resultEmployee.get());
    }

 }
