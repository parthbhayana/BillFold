package com.nineleaps.expense_management_project.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import com.nineleaps.expense_management_project.config.JwtUtil;
import com.nineleaps.expense_management_project.service.IEmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.service.IEmployeeService;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private IEmployeeService employeeService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private IEmailService emailService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUserDetails() {
        // Create sample data
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> userList = Arrays.asList(employee1, employee2);

        // Mock the behavior of the employeeService to return the sample user list
        when(employeeService.getAllUser()).thenReturn(userList);

        // Call the controller method to get all user details
        List<Employee> result = userController.getAllUserDetails();

        // Verify that the result matches the sample user list
        assertThat(result, is(userList));
    }

}