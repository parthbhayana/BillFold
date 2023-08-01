package com.nineleaps.expensemanagementproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.nineleaps.expensemanagementproject.DTO.EmployeeDTO;
import com.nineleaps.expensemanagementproject.DTO.UserDTO;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testGetAllEmployeeDetails() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employees.add(new Employee());

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployeeDetails();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testSaveEmployeeDetails() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeEmail("john.doe@example.com");
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");

        Employee savedEmployee = new Employee();
        savedEmployee.setEmployeeEmail("john.doe@example.com");
        savedEmployee.setFirstName("John");
        savedEmployee.setLastName("Doe");

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.saveEmployeeDetails(employeeDTO);

        assertEquals("john.doe@example.com", result.getEmployeeEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(employeeRepository, times(1)).save(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testGetEmployeeById_ExistingId() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(employeeId);

        Assertions.assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());

        verify(employeeRepository, times(1)).findById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testGetEmployeeById_NonExistingId() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Employee result = employeeService.getEmployeeById(employeeId);

        Assertions.assertNull(result);

        verify(employeeRepository, times(1)).findById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testDeleteEmployeeDetailsById() {
        Long employeeId = 1L;

        employeeService.deleteEmployeeDetailsById(employeeId);

        verify(employeeRepository, times(1)).deleteById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testUpdateEmployeeDetails() {
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeEmail("john.doe@example.com");
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");

        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(employeeId);
        existingEmployee.setEmployeeEmail("jane.smith@example.com");
        existingEmployee.setFirstName("Jane");
        existingEmployee.setLastName("Smith");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeId(employeeId);
        updatedEmployee.setEmployeeEmail("john.doe@example.com");
        updatedEmployee.setFirstName("John");
        updatedEmployee.setLastName("Doe");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployeeDetails(employeeDTO, employeeId);

        Assertions.assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());
        assertEquals("john.doe@example.com", result.getEmployeeEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository);
    }


    @Test
    void testGetEmployeeDetails_ExistingId() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeDetails(employeeId);

        assertTrue(result.isPresent());
        assertEquals(employeeId, result.get().getEmployeeId());

        verify(employeeRepository, times(1)).findById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testGetEmployeeDetails_NonExistingId() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeDetails(employeeId);

        assertFalse(result.isPresent());

        verify(employeeRepository, times(1)).findById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testEditEmployeeDetails() {
        Long employeeId = 1L;
        String managerEmail = "manager@example.com";
        Long mobileNumber = 1234567890L;
        String officialEmployeeId = "EMP001";
        String managerName = "John Doe";

        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName);

        assertEquals(managerEmail, existingEmployee.getManagerEmail());
        assertEquals(mobileNumber, existingEmployee.getMobileNumber());
        assertEquals(officialEmployeeId, existingEmployee.getOfficialEmployeeId());
        assertEquals(managerName, existingEmployee.getManagerName());

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void hideEmployee_ShouldHideEmployee() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setIsHidden(false);
        Employee savedEmployee = new Employee();
        savedEmployee.setEmployeeId(employeeId);
        savedEmployee.setIsHidden(true);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(savedEmployee);
        // Act
        employeeService.hideEmployee(employeeId);
        // Assert
        assertTrue(savedEmployee.getIsHidden());
    }

    @Test
    void setFinanceAdmin_ShouldSetFinanceAdminAndRoles() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setIsFinanceAdmin(false);
        employee.setRole("EMPLOYEE");
        Employee savedEmployee = new Employee();
        savedEmployee.setEmployeeId(employeeId);
        savedEmployee.setIsFinanceAdmin(true);
        savedEmployee.setRole("FINANCE_ADMIN");
        List<Employee> allEmployees = Arrays.asList(employee, savedEmployee);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(savedEmployee);
        when(employeeRepository.findAll()).thenReturn(allEmployees);
        // Act
        employeeService.setFinanceAdmin(employeeId);
        // Assert
        assertTrue(savedEmployee.getIsFinanceAdmin());
        assertEquals("FINANCE_ADMIN", savedEmployee.getRole());
        for (Employee emp : allEmployees) {
            if (!emp.getEmployeeId().equals(employeeId)) {
                assertFalse(emp.getIsFinanceAdmin());
                assertEquals("EMPLOYEE", emp.getRole());
            }
        }

    }
    @Test
    void insertUser_ShouldSaveEmployee() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeEmail("john.doe@example.com");
        userDTO.setImageUrl("https://example.com/profile.jpg");
        userDTO.setFirstName("John");
        userDTO.setMiddleName("Middle");
        userDTO.setLastName("Doe");
        Employee savedEmployee = new Employee();
        savedEmployee.setOfficialEmployeeId(String.valueOf(1L));
        savedEmployee.setEmployeeEmail(userDTO.getEmployeeEmail());
        savedEmployee.setImageUrl(userDTO.getImageUrl());
        savedEmployee.setFirstName(userDTO.getFirstName());
        savedEmployee.setMiddleName(userDTO.getMiddleName());
        savedEmployee.setLastName(userDTO.getLastName());
        when(employeeRepository.save(ArgumentMatchers.any(Employee.class))).thenReturn(savedEmployee);
        // Act
        Employee result = employeeService.insertUser(userDTO);
        // Assert
        assertEquals(savedEmployee, result);
    }
    @Test
    void getAllUser_ShouldReturnAllUsers() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);
        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);
        List<Employee> expectedUsers = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(expectedUsers);
        // Act
        List<Employee> actualUsers = employeeService.getAllUser();
        // Assert
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
    }
}