package com.nineleaps.expense_management_project.service;


import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetEmployeeById() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        // Mock the repository method
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Act
        Employee retrievedEmployee = employeeService.getEmployeeById(employeeId);

        // Assert
        assertNotNull(retrievedEmployee);
        assertEquals(employeeId, retrievedEmployee.getEmployeeId());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    public void testDeleteEmployeeDetailsById() {
        // Arrange
        Long employeeId = 1L;

        // Act
        employeeService.deleteEmployeeDetailsById(employeeId);

        // Assert
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    public void testGetAllEmployeeDetails() {
        // Create a list of mock employees
        List<Employee> mockEmployees = new ArrayList<>();

        // Create Employee objects and set their properties using setters
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");

        // Add the Employee objects to the list
        mockEmployees.add(employee1);
        mockEmployees.add(employee2);

        // Configure the mock repository to return the list of mock employees
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        // Call the method to be tested
        List<Employee> result = employeeService.getAllEmployeeDetails();

        // Assertions
        assertEquals(mockEmployees.size(), result.size());
        assertEquals(mockEmployees, result);
    }

    @Test
    public void testUpdateEmployeeDetails() {
        // Create a sample EmployeeDTO
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setMiddleName("Middle");

        // Create a sample Employee with the given ID
        Long employeeId = 1L;

        // Call the method to be tested and expect a NullPointerException
        assertThrows(NullPointerException.class, () -> {
            employeeService.updateEmployeeDetails(employeeDTO, employeeId);
        });

        // Verify that the repository's save method was not called
        verify(employeeRepository, never()).save(any());
    }


    @Test
    public void testGetEmployeeDetails() {
        // Create a sample employee
        Long employeeId = 1L;
        Employee sampleEmployee = new Employee();
        sampleEmployee.setEmployeeId(employeeId);
        sampleEmployee.setFirstName("John");
        sampleEmployee.setLastName("Doe");

        // Configure the mock repository to return the sampleEmployee when findById is called
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(sampleEmployee));

        // Call the method to be tested
        Optional<Employee> result = employeeService.getEmployeeDetails(employeeId);

        // Assertions
        assertTrue(result.isPresent());
        Employee retrievedEmployee = result.get();
        assertEquals(employeeId, retrievedEmployee.getEmployeeId());
        assertEquals("John", retrievedEmployee.getFirstName());
        assertEquals("Doe", retrievedEmployee.getLastName());

        // Verify that the repository's findById method was called once with the correct ID
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    public void testEditEmployeeDetails() {
        // Create input data
        Long employeeId = 1L;
        String managerEmail = "new.manager@example.com";
        Long mobileNumber = 1234567890L;
        String officialEmployeeId = "EMP456";
        String managerName = "New Manager";

        // Create a sample employee
        Employee sampleEmployee = new Employee();
        sampleEmployee.setEmployeeId(employeeId);
        sampleEmployee.setManagerEmail("old.manager@example.com");
        sampleEmployee.setMobileNumber(9876543210L);
        sampleEmployee.setOfficialEmployeeId("EMP123");
        sampleEmployee.setManagerName("Old Manager");

        // Configure the mock repository to return the sampleEmployee when getEmployeeById is called
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(sampleEmployee));

        // Call the method to be tested
        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName);

        // Assertions
        assertEquals(managerEmail, sampleEmployee.getManagerEmail());
        assertEquals(mobileNumber, sampleEmployee.getMobileNumber());
        assertEquals(officialEmployeeId, sampleEmployee.getOfficialEmployeeId());
        assertEquals(managerName, sampleEmployee.getManagerName());

        // Verify that the repository's save method was called once with the updated employee
        verify(employeeRepository, times(1)).save(sampleEmployee);
    }

    @Test
    public void testGetEmployeeByEmail() {
        // Create a sample email
        String employeeEmail = "employee@example.com";

        // Create a sample Employee
        Employee sampleEmployee = new Employee();
        sampleEmployee.setEmployeeEmail(employeeEmail);

        // Configure the mock repository to return the sampleEmployee when findByEmployeeEmail is called
        when(employeeRepository.findByEmployeeEmail(employeeEmail)).thenReturn(sampleEmployee);

        // Call the method to be tested
        Employee result = employeeService.getEmployeeByEmail(employeeEmail);

        // Assertion
        assertNotNull(result);
        assertEquals(employeeEmail, result.getEmployeeEmail());
    }


    @Test
    public void testGetAllUser() {
        // Create sample employees
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> sampleEmployees = Arrays.asList(employee1, employee2);

        // Configure the mock repository to return the sampleEmployees when findAll is called
        when(employeeRepository.findAll()).thenReturn(sampleEmployees);

        // Call the method to be tested
        List<Employee> result = employeeService.getAllUser();

        // Assertion
        assertNotNull(result);
        assertEquals(sampleEmployees.size(), result.size());
    }

    @Test
    public void testFindByEmailId() {
        // Create a sample email ID
        String emailId = "email@example.com";

        // Create a sample Employee
        Employee sampleEmployee = new Employee();
        sampleEmployee.setEmployeeEmail(emailId);

        // Configure the mock repository to return the sampleEmployee when findByEmployeeEmail is called
        when(employeeRepository.findByEmployeeEmail(emailId)).thenReturn(sampleEmployee);

        // Call the method to be tested
        Employee result = employeeService.findByEmailId(emailId);

        // Assertion
        assertNotNull(result);
        assertEquals(emailId, result.getEmployeeEmail());
    }


}