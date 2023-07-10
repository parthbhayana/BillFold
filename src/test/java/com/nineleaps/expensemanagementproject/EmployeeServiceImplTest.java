package com.nineleaps.expensemanagementproject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.service.EmployeeServiceImpl;

class EmployeeServiceImplTest {
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGetAllEmployeeDetails() {
        // Arrange
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> expectedEmployees = Arrays.asList(employee1, employee2);
        
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);
        
        // Act
        List<Employee> actualEmployees = employeeService.getAllEmployeeDetails();
        
        // Assert
        assertEquals(expectedEmployees.size(), actualEmployees.size());
        assertTrue(actualEmployees.containsAll(expectedEmployees));
        
        verify(employeeRepository).findAll();
    }




    @Test
    void testSaveEmployeeDetails() {
        // Arrange
        Employee employee = new Employee();

        when(employeeRepository.save(employee)).thenReturn(employee);

        // Act
        Employee savedEmployee = employeeService.saveEmployeeDetails(employee);

        // Assert
        assertEquals(employee, savedEmployee);

        verify(employeeRepository).save(employee);
    }

    @Test
    void testGetEmployeeById() {
        // Arrange
        Long employeeId = 1L;
        Employee expectedEmployee = new Employee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(expectedEmployee));

        // Act
        Employee actualEmployee = employeeService.getEmployeeById(employeeId);

        // Assert
        assertEquals(expectedEmployee, actualEmployee);

        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void testDeleteEmployeeDetailsById() {
        // Arrange
        Long employeeId = 1L;

        // Act
        employeeService.deleteEmployeeDetailsById(employeeId);

        // Assert
        verify(employeeRepository).deleteById(employeeId);
    }

//    @Test
//    void testUpdateEmployeeDetails() {
//        // Arrange
//        Long employeeId = 1L;
//        Employee employee = new Employee();
//        Employee newEmployee = new Employee();
//
//        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);
//        when(employeeRepository.save(employee)).thenReturn(employee);
//
//        // Act
//        Employee updatedEmployee = employeeService.updateEmployeeDetails(newEmployee, employeeId);
//
//        // Assert
//        assertEquals(employee, updatedEmployee);
//        assertEquals(newEmployee.getEmployeeEmail(), updatedEmployee.getEmployeeEmail());
//        assertEquals(newEmployee.getFirstName(), updatedEmployee.getFirstName());
//        assertEquals(newEmployee.getLastName(), updatedEmployee.getLastName());
//        assertEquals(newEmployee.getMiddleName(), updatedEmployee.getMiddleName());
//
//        verify(employeeRepository).save(employee);
//        verify(employeeService).getEmployeeById(employeeId);
//    }
//
//    @Test
//    void testAdditionalEmployeeDetails() {
//        // Arrange
//        Long employeeId = 1L;
//        String officialEmployeeId = "123";
//        String managerEmail = "manager@example.com";
//        Long mobileNumber = 1234567890L;
//        String managerName = "John Doe";
//        Employee employee = new Employee();
//
//        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);
//        when(employeeRepository.save(employee)).thenReturn(employee);
//
//        // Act
//        Optional<Employee> result = employeeService.additionalEmployeeDetails(
//                employeeId, officialEmployeeId, managerEmail, mobileNumber, managerName);
//
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals(employee, result.get());
//
//        verify(employeeService).getEmployeeById(employeeId);
//        verify(employeeRepository).save(employee);
//    }

    @Test
    void testGetEmployeeDetails() {
        // Arrange
        Long employeeId = 1L;
        Employee expectedEmployee = new Employee();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(expectedEmployee));

        // Act
        Optional<Employee> actualEmployee = employeeService.getEmployeeDetails(employeeId);

        // Assert
        assertTrue(actualEmployee.isPresent());
        assertEquals(expectedEmployee, actualEmployee.get());

        verify(employeeRepository).findById(employeeId);
    }

//    @Test
//    void testEditEmployeeDetails() {
//        // Arrange
//        Long employeeId = 1L;
//        String managerEmail = "manager@example.com";
//        Long mobileNumber = 1234567890L;
//        String officialEmployeeId = "123";
//        String managerName = "John Doe";
//        Employee employee = new Employee();
//
//        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);
//        when(employeeRepository.save(employee)).thenReturn(employee);
//
//        // Act
//        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName);
//
//        // Assert
//        assertEquals(managerEmail, employee.getManagerEmail());
//        assertEquals(mobileNumber, employee.getMobileNumber());
//        assertEquals(officialEmployeeId, employee.getOfficialEmployeeId());
//        assertEquals(managerName, employee.getManagerName());
//
//        verify(employeeService).getEmployeeById(employeeId);
//        verify(employeeRepository).save(employee);
//    }

    @Test
    void testGetEmployeeByEmail() {
        // Arrange
        String emailToVerify = "user@example.com";

        // Act
        Employee result = employeeService.getEmployeeByEmail(emailToVerify);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetUserByEmail() {
        // Arrange
        String emailToVerify = "user@example.com";

        // Act
        Employee result = employeeService.getUserByEmail(emailToVerify);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetAllUser() {
        // Arrange
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> expectedEmployees = Arrays.asList(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        // Act
        List<Employee> actualEmployees = employeeService.getAllUser();

        // Assert
        assertEquals(expectedEmployees.size(), actualEmployees.size());
        assertTrue(actualEmployees.containsAll(expectedEmployees));

        verify(employeeRepository).findAll();
    }

    @Test
    void testInsertUser() {
        // Arrange
        Employee newUser = new Employee();

        when(employeeRepository.save(newUser)).thenReturn(newUser);

        // Act
        Employee insertedUser = employeeService.insertUser(newUser);

        // Assert
        assertEquals(newUser, insertedUser);

        verify(employeeRepository).save(newUser);
    }

    @Test
    void testFindByEmailId() {
        // Arrange
        String emailId = "user@example.com";
        Employee expectedEmployee = new Employee();

        when(employeeRepository.findByEmployeeEmail(emailId)).thenReturn(expectedEmployee);

        // Act
        Employee actualEmployee = employeeService.findByEmailId(emailId);

        // Assert
        assertEquals(expectedEmployee, actualEmployee);

        verify(employeeRepository).findByEmployeeEmail(emailId);
    }

//    @Test
//    void testHideEmployee() {
//        // Arrange
//        Long employeeId = 1L;
//        Boolean hidden = true;
//        Employee employee = new Employee();
//
//        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);
//        when(employeeRepository.save(employee)).thenReturn(employee);
//
//        // Act
//        employeeService.hideEmployee(employeeId);
//
//        // Assert
//        assertEquals(hidden, employee.getIsHidden());
//
//        verify(employeeService).getEmployeeById(employeeId);
//        verify(employeeRepository).save(employee);
//    }
//
//    @Test
//    void testSetFinanceAdmin() {
//        // Arrange
//        Long employeeId = 1L;
//        Boolean isAdmin = true;
//        String role = "FINANCE_ADMIN";
//        Employee employee = new Employee();
//        List<Employee> employees = Arrays.asList(employee);
//
//        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);
//        when(employeeRepository.save(employee)).thenReturn(employee);
//        when(employeeRepository.findAll()).thenReturn(employees);
//
//        // Act
//        employeeService.setFinanceAdmin(employeeId);
//
//        // Assert
//        assertEquals(isAdmin, employee.getIsFinanceAdmin());
//        assertEquals(role, employee.getRole());
//
//        for (Employee emp : employees) {
//            if (emp.getEmployeeId() == employeeId) {
//                continue;
//            }
//            assertFalse(emp.getIsFinanceAdmin());
//            assertEquals("EMPLOYEE", emp.getRole());
//            verify(employeeRepository).save(emp);
//        }
//
//        verify(employeeService).getEmployeeById(employeeId);
//        verify(employeeRepository).save(employee);
//        verify(employeeRepository).findAll();
//    }
}