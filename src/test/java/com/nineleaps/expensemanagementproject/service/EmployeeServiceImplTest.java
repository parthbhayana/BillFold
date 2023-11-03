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

    @Test
    void testAdditionalEmployeeDetails() {
        // Create a sample employee and the data you want to set
        Long employeeId = 1L;
        String officialEmployeeId = "EMP123";
        String managerEmail = "manager@example.com";
        Long mobileNumber = 1234567890L;
        String managerName = "Manager Name";
        String hrEmail = "hr@example.com";
        String hrName = "HR Name";

        Employee employee = new Employee(); // Create an Employee object with default values or use a builder pattern if available.

        // Mock the behavior of the repository to return the employee when getEmployeeById is called and to save the employee.
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Call the method you want to test
        Optional<Employee> updatedEmployee = employeeService.additionalEmployeeDetails(employeeId, officialEmployeeId, managerEmail, mobileNumber, managerName, hrEmail, hrName);

        // Verify that the employee was saved
        verify(employeeRepository, times(1)).save(employee);

        // Verify that the returned employee matches the expected values
        assertTrue(updatedEmployee.isPresent());
        Employee updated = updatedEmployee.get();
        assertEquals(officialEmployeeId, updated.getOfficialEmployeeId());
        assertEquals(managerEmail, updated.getManagerEmail());
        assertEquals(mobileNumber, updated.getMobileNumber());
        assertEquals(managerName, updated.getManagerName());
        assertEquals(hrEmail, updated.getHrEmail());
        assertEquals(hrName, updated.getHrName());
    }

    @Test
    void testGetEmployeeByEmail() {
        // Create a sample email
        String employeeEmail = "employee@example.com";

        // Create a sample employee with the given email
        Employee employee = new Employee();
        employee.setEmployeeEmail(employeeEmail);

        // Mock the behavior of the repository to return the employee when findByEmployeeEmail is called
        when(employeeRepository.findByEmployeeEmail(employeeEmail)).thenReturn(employee);

        // Call the method you want to test
        Employee result = employeeService.getEmployeeByEmail(employeeEmail);

        // Verify that the correct employee is returned
        assertNotNull(result);
        assertEquals(employeeEmail, result.getEmployeeEmail());
    }

    @Test
    void testGetEmployeeByEmailNotFound() {
        // Create a sample email that does not exist in the repository
        String nonExistentEmail = "nonexistent@example.com";

        // Mock the behavior of the repository to return null when findByEmployeeEmail is called
        when(employeeRepository.findByEmployeeEmail(nonExistentEmail)).thenReturn(null);

        // Call the method you want to test
        Employee result = employeeService.getEmployeeByEmail(nonExistentEmail);

        // Verify that the result is null, indicating that the employee was not found
        assertNull(result);
    }

    @Test
    void testGetUserByEmailReturnsNull() {
        // Create an instance of your service or class that contains the method
        EmployeeServiceImpl employeeService = new EmployeeServiceImpl();

        // Call the method with any email address (it should return null regardless of the input)
        String emailToVerify = "example@example.com";
        Employee result = employeeService.getUserByEmail(emailToVerify);

        // Verify that the result is null
        assertNull(result);
    }

    @Test
    void testFindByEmailId() {
        // Create a sample email
        String emailId = "employee@example.com";

        // Create a sample employee with the given email
        Employee employee = new Employee();
        employee.setEmployeeEmail(emailId);

        // Mock the behavior of the repository to return the employee when findByEmployeeEmail is called
        when(employeeRepository.findByEmployeeEmail(emailId)).thenReturn(employee);

        // Call the method you want to test
        Employee result = employeeService.findByEmailId(emailId);

        // Verify that the correct employee is returned
        assertNotNull(result);
        assertEquals(emailId, result.getEmployeeEmail());
    }

    @Test
    void testFindByEmailIdNotFound() {
        // Create a sample email that does not exist in the repository
        String nonExistentEmail = "nonexistent@example.com";

        // Mock the behavior of the repository to return null when findByEmployeeEmail is called
        when(employeeRepository.findByEmployeeEmail(nonExistentEmail)).thenReturn(null);

        // Call the method you want to test
        Employee result = employeeService.findByEmailId(nonExistentEmail);

        // Verify that the result is null, indicating that the employee was not found
        assertNull(result);
    }

    @Test
    void testUpdateUser() {
        // Create a sample UserDTO with the data you want to update
        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeEmail("employee@example.com");
        userDTO.setImageUrl("new-image-url.jpg");
        userDTO.setFirstName("NewFirstName");
        userDTO.setMiddleName("NewMiddleName");
        userDTO.setLastName("NewLastName");
        userDTO.setFcmToken("newToken");

        // Create a sample employee with the given email
        Employee employee = new Employee();
        employee.setEmployeeEmail(userDTO.getEmployeeEmail());

        // Mock the behavior of the repository to return the employee when findByEmployeeEmail is called and to save the employee
        when(employeeRepository.findByEmployeeEmail(userDTO.getEmployeeEmail())).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Call the method you want to test
        Employee result = employeeService.updateUser(userDTO);

        // Verify that the correct employee is returned
        assertNotNull(result);
        assertEquals(userDTO.getEmployeeEmail(), result.getEmployeeEmail());
        assertEquals(userDTO.getImageUrl(), result.getImageUrl());
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        assertEquals(userDTO.getMiddleName(), result.getMiddleName());
        assertEquals(userDTO.getLastName(), result.getLastName());
        assertEquals(userDTO.getFcmToken(), result.getToken());
    }

    @Test
    void testEditEmployeeDetails() {
        // Arrange
        Long employeeId = 1L;
        String managerEmail = "newmanager@example.com";
        Long mobileNumber = 1234567890L;
        String officialEmployeeId = "EMP123";
        String managerName = "New Manager";

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Act
        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName);

        // Assert
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));

        assertEquals(managerEmail, employee.getManagerEmail());
        assertEquals(mobileNumber, employee.getMobileNumber());
        assertEquals(officialEmployeeId, employee.getOfficialEmployeeId());
        assertEquals(managerName, employee.getManagerName());
    }

    @Test
    void testEditEmployeeDetails_NullEmployee() {
        // Arrange
        Long employeeId = 1L;
        String managerEmail = "newmanager@example.com";
        Long mobileNumber = 1234567890L;
        String officialEmployeeId = "EMP123";
        String managerName = "New Manager";

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NullPointerException.class, () -> employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName));
    }

    @Test
    void testEditEmployeeDetails_EmployeeExists() {
        // Arrange
        Long employeeId = 1L;
        String managerEmail = "newmanager@example.com";
        Long mobileNumber = 1234567890L;
        String officialEmployeeId = "EMP123";
        String managerName = "New Manager";
        String hrEmail = "newhr@example.com";
        String hrName = "New HR";

        // Create a mock Employee
        Employee existingEmployee = mock(Employee.class);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        // Act
        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName, hrEmail, hrName);

        // Assert
        verify(existingEmployee).setManagerEmail(managerEmail);
        verify(existingEmployee).setMobileNumber(mobileNumber);
        verify(existingEmployee).setOfficialEmployeeId(officialEmployeeId);
        verify(existingEmployee).setManagerName(managerName);
        verify(existingEmployee).setHrEmail(hrEmail);
        verify(existingEmployee).setHrName(hrName);
        verify(employeeRepository).save(existingEmployee);
    }

    @Test
    void testEditEmployeeDetails_EmployeeDoesNotExist() {
        // Arrange
        Long employeeId = 1L;
        String managerEmail = "newmanager@example.com";
        Long mobileNumber = 1234567890L;
        String officialEmployeeId = "EMP123";
        String managerName = "New Manager";
        String hrEmail = "newhr@example.com";
        String hrName = "New HR";

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NullPointerException.class, () -> employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName, hrEmail, hrName));
    }


}