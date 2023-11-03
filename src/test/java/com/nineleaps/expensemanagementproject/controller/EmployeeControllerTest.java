package com.nineleaps.expensemanagementproject.controller;


import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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


    @Test
    void testGetEmployeeById() {

        Long employeeId = 1L;
        Employee mockEmployee = new Employee();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(mockEmployee);
        Employee resultEmployee = employeeController.getEmployeeById(employeeId);
        verify(employeeService, times(1)).getEmployeeById(employeeId);
        assertEquals(mockEmployee, resultEmployee);
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

        Long employeeId = 1L;
        employeeController.deleteEmployeeById(employeeId);
        verify(employeeService, times(1)).deleteEmployeeDetailsById(employeeId);
    }

    @Test
    void testHideEmployee() {

        Long employeeId = 1L;
        employeeController.hideEmployee(employeeId);
        verify(employeeService, times(1)).hideEmployee(employeeId);
    }

    @Test
    void testSetFinanceAdmin() {

        Long employeeId = 1L;
        employeeController.setFinanceAdmin(employeeId);
        verify(employeeService, times(1)).setFinanceAdmin(employeeId);
    }

    @Test
    void testGetEmployeeDetails() {

        Long employeeId = 1L;
        Employee mockEmployee = new Employee();
        when(employeeService.getEmployeeDetails(employeeId)).thenReturn(Optional.of(mockEmployee));
        Optional<Employee> resultEmployee = employeeController.getEmployeeDetails(employeeId);
        verify(employeeService, times(1)).getEmployeeDetails(employeeId);
        assertTrue(resultEmployee.isPresent());
        assertEquals(mockEmployee, resultEmployee.get());
    }

    @Test
    void testEditEmployeeDetails() {

        employeeController.editEmployeeDetails(1L, "manager@example.com", 1234567890L, "EMP001", "Manager", "karthik", "karthik.r@nineleaps.com");
        verify(employeeService, times(1)).editEmployeeDetails(1L, "manager@example.com", 1234567890L, "EMP001", "Manager", "karthik.r@nineleaps.com", "karthik");
    }




}