package com.nineleaps.expensemanagementproject.config;


import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    private static final String SECRET_KEY = "secret";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 10000;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 20000;
    private JwtUtil jwtUtil;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        response = mock(HttpServletResponse.class);
    }


    @Test
    void generateTokens_ValidArguments_ShouldReturnResponseEntityWithTokens() {
        // Arrange
        String emailId = "test@example.com";
        Long employeeId = 123L;
        String name = "John Doe";
        String imageUrl = "https://example.com/image.jpg";
        String role = "ROLE_USER";
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Create a mock of the JwtUtil class
        JwtUtil jwtUtilMock = mock(JwtUtil.class);

        // Mock the behavior of the generateTokens() method to set the headers
        when(jwtUtilMock.generateTokens(emailId, employeeId, name,  response)).thenAnswer(invocation -> {
            response.setHeader("Access_Token", "access-token-value");
            response.setHeader("Refresh_Token", "refresh-token-value");
            return ResponseEntity.ok().build();
        });

        // Act
        ResponseEntity<?> responseEntity = jwtUtilMock.generateTokens(emailId, employeeId, name, response);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("access-token-value", response.getHeader("Access_Token"));
        assertEquals("refresh-token-value", response.getHeader("Refresh_Token"));
    }



    @Test
    void validateToken_ValidToken_ShouldReturnTrue() {
        // Arrange
        String token = "valid-token";

        // Mock the JwtUtil class
        JwtUtil jwtUtil = mock(JwtUtil.class);

        // Set up the mock to return true when validateToken() is called with the valid token
        when(jwtUtil.validateToken(token)).thenReturn(true);

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        Assertions.assertTrue(isValid);
    }



    @Test
    void validateToken_InvalidToken_ShouldReturnFalse() {
        // Arrange
        String token = "invalid-token";

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        Assertions.assertFalse(isValid);
    }

    @Test
    void getClaimsFromToken_ValidToken_ShouldReturnClaims() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        Claims expectedClaims = mock(Claims.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getClaimsFromToken(token)).thenReturn(expectedClaims);

        // Act
        Claims claims = jwtUtil.getClaimsFromToken(token);

        // Assert
        assertNotNull(claims);
        assertEquals(expectedClaims, claims);
    }


}