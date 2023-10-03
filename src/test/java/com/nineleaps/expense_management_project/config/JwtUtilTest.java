package com.nineleaps.expense_management_project.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateTokens() {
        // Mock HttpServletResponse
        HttpServletResponse response = mock(HttpServletResponse.class);

        ResponseEntity<JwtUtil.TokenResponse> tokenResponseEntity = jwtUtil.generateTokens("test@example.com", 123L, "ROLE_USER", response);

        assertNotNull(tokenResponseEntity);
        assertEquals(200, tokenResponseEntity.getStatusCodeValue());

        JwtUtil.TokenResponse tokenResponse = tokenResponseEntity.getBody();
        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getAccessToken());
        assertNotNull(tokenResponse.getRefreshToken());

        // Verify that tokens are set in response headers
        verify(response, times(1)).setHeader("Access_Token", tokenResponse.getAccessToken());
        verify(response, times(1)).setHeader("Refresh_Token", tokenResponse.getRefreshToken());
    }

    @Test
    void testGenerateToken() {
        String emailId = "test@example.com";
        Long employeeId = 123L;
        String role = "ROLE_USER";
        long expirationTime = 3600000L; // 1 hour in milliseconds

        String token = jwtUtil.generateToken(emailId, employeeId, role, expirationTime);

        assertNotNull(token);
    }

    @Test
    void testValidateTokenValid() {
        // Generate a valid token
        String token = jwtUtil.generateToken("test@example.com", 123L, "ROLE_USER", 3600000L);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateTokenInvalid() {
        // An invalid token (modified token)
        String invalidToken = "InvalidTokenString";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }


}