package com.nineleaps.expense_management_project.config;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import io.jsonwebtoken.*;


import java.security.SecureRandom;
import java.util.Date;


import static org.mockito.Mockito.mock;

class JwtUtilTest {


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


    @Test
    void testIsRefreshTokenExpiredWithFutureExpiration() {
        JwtUtil jwtUtil = new JwtUtil();
        String refreshToken = generateTokenWithFutureExpiration();

        boolean isExpired = jwtUtil.isRefreshTokenExpired(refreshToken);

        assertFalse(isExpired);
    }

    @Test
    void testIsRefreshTokenExpiredWithPastExpiration() {
        JwtUtil jwtUtil = new JwtUtil();
        String refreshToken = generateTokenWithPastExpiration();

        boolean isExpired = jwtUtil.isRefreshTokenExpired(refreshToken);

        assertFalse(isExpired);
    }

    @Test
    void testIsAccessTokenExpiredWithFutureExpiration() {
        JwtUtil jwtUtil = new JwtUtil();
        String accessToken = generateTokenWithFutureExpiration();

        boolean isExpired = jwtUtil.isAccessTokenExpired(accessToken);

        assertFalse(isExpired);
    }

    @Test
    void testIsAccessTokenExpiredWithPastExpiration() {
        JwtUtil jwtUtil = new JwtUtil();
        String accessToken = generateTokenWithPastExpiration();

        boolean isExpired = jwtUtil.isAccessTokenExpired(accessToken);

        assertFalse(isExpired);
    }

    // Helper methods to generate tokens with future and past expiration dates
    private String generateTokenWithFutureExpiration() {
        return generateToken("yokes.e2nineleaps.com",1L,"EMPLOYEE",System.currentTimeMillis() + 100000);
    }

    private String generateTokenWithPastExpiration() {
        return generateToken("yokes.e2nineleaps.com",1L,"EMPLOYEE",System.currentTimeMillis() - 100000);
    }

    public String generateToken(String emailId, Long employeeId, String role, long expirationTime) {
        String secretkey = generateRandomSecretKey();
        return Jwts.builder()
                .setSubject(emailId)
                .claim("EmployeeID", employeeId)
                .claim("Role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretkey)
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