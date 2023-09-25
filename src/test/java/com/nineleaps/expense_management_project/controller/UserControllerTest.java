package com.nineleaps.expense_management_project.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expense_management_project.config.JwtUtil;
import com.nineleaps.expense_management_project.dto.UserDTO;
import com.nineleaps.expense_management_project.service.IEmailService;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.service.IEmployeeService;

import javax.mail.MessagingException;
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

//    @Test
//    public void testInsertUser_NewUser() throws MessagingException {
//        // Create a sample UserDTO
//        UserDTO sampleUserDTO = new UserDTO();
//        sampleUserDTO.setEmployeeEmail("newuser@example.com");
//
//        // Mock the behavior of employeeService.findByEmailId to return null (indicating a new user)
//        when(employeeService.findByEmailId(sampleUserDTO.getEmployeeEmail())).thenReturn(null);
//
//        // Create a sample Employee object for the new user
//        Employee newUser = new Employee();
//        newUser.setEmployeeEmail(sampleUserDTO.getEmployeeEmail());
//        newUser.setEmployeeId(1L);
//        newUser.setRole("EMPLOYEE");
//
//        // Mock the behavior of employeeService.insertUser to return the new user
//        when(employeeService.insertUser(sampleUserDTO)).thenReturn(newUser);
//
//        // Create a sample JWT token response
//        JwtUtil.TokenResponse sampleTokenResponse = new JwtUtil.TokenResponse("accessToken", "refreshToken");
//
//        // Mock the behavior of jwtUtil.generateTokens to return the sample token response
//        when(jwtUtil.generateTokens(newUser.getEmployeeEmail(), newUser.getEmployeeId(), newUser.getRole(), new MockHttpServletResponse()))
//                .thenReturn(ResponseEntity.ok(sampleTokenResponse));
//
//        // Call the controller method to insert the new user
//        ResponseEntity<JwtUtil.TokenResponse> response = userController.insertUser(sampleUserDTO, new MockHttpServletResponse());
//
//        // Verify that the response is OK (status code 200)
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//
//        // Verify that the response contains the expected token response
//        assertThat(response.getBody(), is(sampleTokenResponse));
//
//        // Verify that the welcomeEmail method was called with the new user's email
//        verify(emailService).welcomeEmail(sampleUserDTO.getEmployeeEmail());
//    }
//
//    @Test
//    public void testInsertUser_ExistingUser() throws MessagingException {
//        // Create a sample UserDTO
//        UserDTO sampleUserDTO = new UserDTO();
//        sampleUserDTO.setEmployeeEmail("existinguser@example.com");
//
//        // Create a sample existing user
//        Employee existingUser = new Employee();
//        existingUser.setEmployeeEmail(sampleUserDTO.getEmployeeEmail());
//
//        // Mock the behavior of employeeService.findByEmailId to return the existing user
//        when(employeeService.findByEmailId(sampleUserDTO.getEmployeeEmail())).thenReturn(existingUser);
//
//        // Create a sample JWT token response
//        JwtUtil.TokenResponse sampleTokenResponse = new JwtUtil.TokenResponse("accessToken", "refreshToken");
//
//        // Mock the behavior of jwtUtil.generateTokens to return the sample token response
//        when(jwtUtil.generateTokens(existingUser.getEmployeeEmail(), existingUser.getEmployeeId(), existingUser.getRole(), new MockHttpServletResponse()))
//                .thenReturn(ResponseEntity.ok(sampleTokenResponse));
//
//        // Call the controller method to update the existing user
//        ResponseEntity<JwtUtil.TokenResponse> response = userController.insertUser(sampleUserDTO, new MockHttpServletResponse());
//
//        // Verify that the response is OK (status code 200)
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//
//        // Verify that the response contains the expected token response
//        assertThat(response.getBody(), is(sampleTokenResponse));
//
//        // Verify that the updateUser method was called with the existing user's data
//        verify(employeeService).updateUser(sampleUserDTO);
//    }




}
