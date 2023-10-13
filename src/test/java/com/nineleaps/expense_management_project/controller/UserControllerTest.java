package com.nineleaps.expense_management_project.controller;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expense_management_project.config.JwtUtil;
import com.nineleaps.expense_management_project.dto.UserDTO;
import com.nineleaps.expense_management_project.service.IEmailService;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.service.IEmployeeService;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @MockBean
    private IEmailService iEmailService;

    @MockBean
    private IEmployeeService iEmployeeService;

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

//    @Test
//    public void testGetAllUserDetails() {
//
//        Employee employee1 = new Employee();
//        Employee employee2 = new Employee();
//        List<Employee> userList = Arrays.asList(employee1, employee2);
//
//        when(employeeService.getAllUser()).thenReturn(userList);
//
//        List<Employee> result = userController.getAllUserDetails();
//
//        assertThat(result, is(userList));
//    }



    @Test
    public void testRegenerateTokenExpiredRefreshToken() {
        // Mock the request and response
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        ((MockHttpServletRequest) request).addHeader("Authorization", "Bearer your-expired-refresh-token-here");

        // Mock the JWTUtil methods
        Mockito.when(jwtUtil.isRefreshTokenExpired(Mockito.anyString())).thenReturn(true);

        // Call the controller method
        userController.regenerateToken(request, response);

        // Verify that the response headers are not set for an expired token
        // Add assertions to validate the response
    }

    @Test
    public void testGetAllUserDetails() {
        // Create a sample list of Employee objects
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmployeeEmail("john@example.com");

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmployeeEmail("jane@example.com");

        employees.add(employee1);
        employees.add(employee2);

        // Mock the behavior of employeeService
        when(employeeService.getAllUser()).thenReturn(employees);

        // Call the controller method
        List<Employee> result = userController.getAllUserDetails();

        // Verify the result
        assertEquals(employees, result);
    }



}