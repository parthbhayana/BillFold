package com.nineleaps.expensemanagementproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

class UserControllerTest {

    @Mock
    private IEmployeeService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUserDtls_Success() {
        // Arrange
        List<Employee> users = new ArrayList<>();
        users.add(new Employee());
        users.add(new Employee());

        when(userService.getAllUser()).thenReturn(users);

        // Act
        List<Employee> result = userController.getAllUserDtls();

        // Assert
        assertEquals(users, result);
        verify(userService).getAllUser();
    }

//    @Test
//    void sendData_Success() {
//        // Arrange
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        String authorizationHeader = "Bearer <token>";
//        String token = "<token>";
//        String employeeEmail = "user@example.com";
//        Long employeeId = 1L;
//        String firstName = "John";
//        String lastName = "Doe";
//        String imageUrl = "https://example.com/profile.jpg";
//        JSONObject responseJson = new JSONObject();
//        responseJson.put("employeeId", employeeId);
//        responseJson.put("firstName", firstName);
//        responseJson.put("lastName", lastName);
//        responseJson.put("imageUrl", imageUrl);
//        responseJson.put("email", employeeEmail);
//
//        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
//        when(jwtUtil.decodeToken(token)).thenReturn(employeeEmail);
//        when(userService.findByEmailId(employeeEmail)).thenReturn(new Employee(employeeId, firstName, lastName, imageUrl, employeeEmail));
//        ResponseEntity<?> expectedResponse = ResponseEntity.ok(responseJson);
//
//        // Act
//        ResponseEntity<?> result = userController.sendData(request);
//
//        // Assert
//        assertEquals(expectedResponse, result);
//        verify(request).getHeader("Authorization");
//        verify(jwtUtil).decodeToken(token);
//        verify(userService).findByEmailId(employeeEmail);
//    }

//    @Test
//    void insertUser_NewUser_Success() {
//        // Arrange
//        Employee newUser = new Employee(1L, "John", "Doe", "user@example.com");
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        Employee employee = null;
//        String email = "user@example.com";
//        Long employeeId = 1L;
//        String firstName = "John";
//        String imageUrl = "https://example.com/profile.jpg";
//        String role = "ROLE_USER";
//        ResponseEntity<?> tokenResponse = ResponseEntity.ok().build();
//
//        when(userService.findByEmailId(email)).thenReturn(employee);
//        when(userService.insertUser(newUser)).thenReturn(employee);
//        when(jwtUtil.generateTokens(email, employeeId, firstName, imageUrl, role, response)).thenReturn(tokenResponse);
//
//        // Act
//        ResponseEntity<?> result = userController.insertUser(newUser, response);
//
//        // Assert
//        assertEquals(tokenResponse, result);
//        verify(userService).findByEmailId(email);
//        verify(userService).insertUser(newUser);
//        verify(jwtUtil).generateTokens(email, employeeId, firstName, imageUrl, role, response);
//    }
//
//    @Test
//    void insertUser_ExistingUser_Success() {
//        // Arrange
//        Employee newUser = new Employee(1L, "John", "Doe", "user@example.com");
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        Employee employee = new Employee(1L, "John", "Doe", "user@example.com");
//        String email = "user@example.com";
//        Long employeeId = 1L;
//        String firstName = "John";
//        String imageUrl = "https://example.com/profile.jpg";
//        String role = "ROLE_USER";
//        ResponseEntity<?> tokenResponse = ResponseEntity.ok().build();
//
//        when(userService.findByEmailId(email)).thenReturn(employee);
//        when(jwtUtil.generateTokens(email, employeeId, firstName, imageUrl, role, response)).thenReturn(tokenResponse);
//
//        // Act
//        ResponseEntity<?> result = userController.insertUser(newUser, response);
//
//        // Assert
//        assertEquals(tokenResponse, result);
//        verify(userService).findByEmailId(email);
//        verify(jwtUtil).generateTokens(email, employeeId, firstName, imageUrl, role, response);
//    }
}
