package com.nineleaps.expensemanagementproject.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expensemanagementproject.DTO.UserDTO;
import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private IEmployeeService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private UserController userController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

//    @Test
//    public void testSendData() {
//        // Mock the request header
//        String token = "test_token";
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//
//        // Mock the decoded access token
//        DecodedJWT decodedAccessToken = mock(DecodedJWT.class);
//        when(decodedAccessToken.getSubject()).thenReturn("test@example.com");
//
//        // Mock the UserService to return the employee
//        Employee employee = new Employee();
//        employee.setEmployeeId(1L);
//        employee.setEmployeeEmail("test@example.com");
//        employee.setFirstName("John");
//        employee.setLastName("Doe");
//        employee.setImageUrl("http://example.com/image.jpg");
//        when(userService.findByEmailId("test@example.com")).thenReturn(employee);
//
//        // Mock the ResponseEntity
//        JSONObject responseJson = new JSONObject();
//        responseJson.put("employeeId", 1L);
//        responseJson.put("firstName", "John");
//        responseJson.put("lastName", "Doe");
//        responseJson.put("imageUrl", "http://example.com/image.jpg");
//        responseJson.put("email", "test@example.com");
//        ResponseEntity<JSONObject> expectedResponse = ResponseEntity.ok(responseJson);
//
//        // Call the sendData method
//        ResponseEntity<?> actualResponse = userController.sendData(request);
//
//        // Verify the response
//        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
//        assertEquals(expectedResponse.getBody(), actualResponse.getBody());
//
//        // Verify the UserService method calls
//        verify(userService, times(1)).findByEmailId("test@example.com");
//    }

//    @Test
//    public void testInsertUser() {
//        // Create a UserDTO object
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmployeeEmail("test@example.com");
//        userDTO.setImageUrl("http://example.com/image.jpg");
//        userDTO.setFirstName("John");
//        userDTO.setLastName("Doe");
//
//        // Mock the UserService to return null for findByEmailId (indicating user doesn't exist)
//        when(userService.findByEmailId("test@example.com")).thenReturn(null);
//
//        // Mock the UserService to return a newly inserted employee
//        Employee insertedEmployee = new Employee();
//        insertedEmployee.setEmployeeId(1L);
//        insertedEmployee.setEmployeeEmail("test@example.com");
//        insertedEmployee.setRole("user");
//        when(userService.insertUser(userDTO)).thenReturn(insertedEmployee);
//
//        // Mock the JwtUtil
//        JwtUtil jwtUtil = mock(JwtUtil.class);
//        JwtUtil.TokenResponse tokenResponse = new JwtUtil.TokenResponse();
//        tokenResponse.setAccessToken("test_token");
//        when(jwtUtil.generateTokens("test@example.com", 1L, "user", response)).thenReturn(ResponseEntity.ok(tokenResponse));
//
//        // Call the insertUser method
//        ResponseEntity<JwtUtil.TokenResponse> responseEntity = userController.insertUser(userDTO, response);
//
//        // Verify the response
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(tokenResponse.getAccessToken(), responseEntity.getBody().getAccessToken());
//
//        // Verify the UserService method calls
//        verify(userService, times(1)).findByEmailId("test@example.com");
//        verify(userService, times(1)).insertUser(userDTO);
//
//        // Verify the JwtUtil method calls
//        verify(jwtUtil, times(1)).generateTokens("test@example.com", 1L, "user", response);
//    }

//    @Test
//    public void testGetAllUserDetails() {
//        // Create mock employees
//        List<Employee> employees = new ArrayList<>();
//        Employee employee1 = new Employee();
//        employee1.setEmployeeId(1L);
//        employee1.setEmployeeEmail("test1@example.com");
//        Employee employee2 = new Employee();
//        employee2.setEmployeeId(2L);
//        employee2.setEmployeeEmail("test2@example.com");
//        employees.add(employee1);
//        employees.add(employee2);
//
//        // Mock the UserService to return the employees
//        when(userService.getAllUser()).thenReturn(employees);
//
//        // Call the getAllUserDetails method
//        List<Employee> actualEmployees = userController.getAllUserDetails();
//
//        // Verify the response
//        assertEquals(employees.size(), actualEmployees.size());
//        assertEquals(employees.get(0), actualEmployees.get(0));
//        assertEquals(employees.get(1), actualEmployees.get(1));
//
//        // Verify the UserService method calls
//        verify(userService, times(1)).getAllUser();
//    }
}
