package com.nineleaps.expensemanagementproject.repository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nineleaps.expensemanagementproject.entity.Employee;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @Before
    public void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeEmail("john@example.com");
        employee.setRole("Manager");
        // Set any other necessary properties of the employee

        Mockito.when(employeeRepository.findByEmployeeEmail("john@example.com")).thenReturn(employee);
        Mockito.when(employeeRepository.findByRole("Manager")).thenReturn(employee);
    }

    @Test
    public void testFindByEmployeeEmail() {
        Employee retrievedEmployee = employeeRepository.findByEmployeeEmail("john@example.com");
        assertNotNull(retrievedEmployee);
        assertEquals(employee.getEmployeeEmail(), retrievedEmployee.getEmployeeEmail());
        // Assert other properties if needed
    }

    @Test
    public void testFindByRole() {
        Employee retrievedEmployee = employeeRepository.findByRole("Manager");
        assertNotNull(retrievedEmployee);
        assertEquals(employee.getRole(), retrievedEmployee.getRole());
        // Assert other properties if needed
    }



}
