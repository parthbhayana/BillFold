package com.nineleaps.expensemanagementproject.controller;

import static com.nineleaps.expensemanagementproject.config.JwtUtil.ACCESS_TOKEN_EXPIRATION_TIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.DTO.UserDTO;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmailService;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @MockBean
    private IEmailService iEmailService;

    @MockBean
    private IEmployeeService iEmployeeService;

    @MockBean
    private JwtUtil jwtUtil;


    @Autowired
    private UserController userController;



    @Test
    void testGetAllUserDetails() throws Exception {
        // Arrange
        when(iEmployeeService.getAllUser()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/listTheUser");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testSendDataWithValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String validToken = generateToken(); // Replace with a valid token
        String validEmail = "employee@example.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(validEmail);
        Employee expectedEmployee = new Employee();
        expectedEmployee.setEmployeeId(1L);
        expectedEmployee.setEmployeeEmail(validEmail);
        expectedEmployee.setFirstName("John");
        expectedEmployee.setLastName("Doe");
        expectedEmployee.setImageUrl("image.jpg");

        when(iEmployeeService.findByEmailId(validEmail)).thenReturn(expectedEmployee);

        ResponseEntity<JSONObject> responseEntity = userController.sendData(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JSONObject responseBody = responseEntity.getBody();
        Assertions.assertNotNull(responseBody);
        assertEquals(1L, responseBody.get("employeeId"));
        assertEquals("John", responseBody.get("firstName"));
        assertEquals("Doe", responseBody.get("lastName"));
        assertEquals("image.jpg", responseBody.get("imageUrl"));
        assertEquals(validEmail, responseBody.get("email"));
    }

    @Test
    void testSendDataWithMissingAuthorizationHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<JSONObject> responseEntity = userController.sendData(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        Assertions.assertNull(responseEntity.getBody());
    }

    @Test
    void testRegenerateTokenWithValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String validToken =  generateToken(); // Replace with a valid token
        String validEmail = "employee@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.isRefreshTokenExpired(validToken)).thenReturn(false);

        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(validEmail);

        Employee expectedEmployee = new Employee();
        expectedEmployee.setEmployeeEmail(validEmail);
        expectedEmployee.setEmployeeId(1L);
        expectedEmployee.setRole("EMPLOYEE");

        when(iEmployeeService.findByEmailId(validEmail)).thenReturn(expectedEmployee);

        String expectedAccessToken = "newAccessToken"; // Replace with the expected access token
        when(jwtUtil.generateToken(validEmail, 1L, "EMPLOYEE", ACCESS_TOKEN_EXPIRATION_TIME)).thenReturn(expectedAccessToken);

        userController.regenerateToken(request, response);

        verify(response).setHeader("Access_Token", expectedAccessToken);
    }

    @Test
    void testRegenerateTokenWithExpiredToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String expiredToken = "expiredToken"; // Replace with an expired token

        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredToken);
        when(jwtUtil.isRefreshTokenExpired(expiredToken)).thenReturn(true);

        userController.regenerateToken(request, response);

        // Verify that the response is not modified in this case (no Access_Token header is set)
        verify(response, never()).setHeader(eq("Access_Token"), anyString());
    }

    @Test
    void testInsertUserNewUser() throws MessagingException {
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setEmployeeEmail("newuser@example.com");
        String validToken =  generateToken();



        when(iEmployeeService.findByEmailId(newUserDTO.getEmployeeEmail())).thenReturn(null);

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        JwtUtil.TokenResponse tokenResponse = new JwtUtil.TokenResponse(validToken,validToken);

        ResponseEntity<JwtUtil.TokenResponse> responseEntity= ResponseEntity.ok(tokenResponse);
        when(jwtUtil.generateTokens(newUserDTO.getEmployeeEmail(),1L, "EMPLOYEE", mockResponse)).thenReturn(responseEntity);
        userController.insertUser(newUserDTO, mockResponse);

        verify(iEmployeeService).insertUser(newUserDTO); // Ensure insertUser method is called


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testInsertUserExistingUser() throws MessagingException {
        UserDTO existingUserDTO = new UserDTO();
        existingUserDTO.setEmployeeEmail("existinguser@example.com");
        HttpServletResponse response = mock(HttpServletResponse.class);
        Employee existingEmployee = new Employee(); // An existing employee with the same email
        existingEmployee.setEmployeeId(1L);
        existingEmployee.setEmployeeEmail("existinguser@example.com");
        when(iEmployeeService.findByEmailId(existingUserDTO.getEmployeeEmail())).thenReturn(existingEmployee);

        jwtUtil.generateTokens(existingEmployee.getEmployeeEmail(),existingEmployee.getEmployeeId(), "EMPLOYEE", response);

        ResponseEntity<JwtUtil.TokenResponse> responseEntity = userController.insertUser(existingUserDTO, mock(HttpServletResponse.class));

        verify(iEmployeeService).updateUser(existingUserDTO); // Ensure updateUser method is called

        assertEquals("existinguser@example.com", existingEmployee.getEmployeeEmail());
    }


//
    private String generateToken() {
        return Jwts.builder()
                .setSubject("employee@example.com")
                .claim("EmployeeID", 1L)
                .claim("Role", "EMPLOYEE")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtUtil.ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, generateRandomSecretKey())
                .compact();
    }

    private static String generateRandomSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(keyBytes);

        // Convert the random bytes to a hexadecimal string
        StringBuilder keyBuilder = new StringBuilder();
        for (byte b : keyBytes) {
            keyBuilder.append(String.format("%02x", b));
        }

        return keyBuilder.toString();
    }

}

