package com.nineleaps.expense_management_project.config;

import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.Date;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    // Secret key for signing JWTs (Should be kept secret)
    private static final String SECRET_KEY = generateRandomSecretKey();

    // Expiration times for access and refresh tokens
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 10;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 30;

    // Method to generate a random secret key
    static String generateRandomSecretKey() {
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

    // Method to generate access and refresh tokens
    public ResponseEntity<TokenResponse> generateTokens(String emailId, Long employeeId, String role,
                                                        HttpServletResponse response) {
        String accessToken = generateToken(emailId, employeeId, role, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = generateToken(emailId, employeeId, role, REFRESH_TOKEN_EXPIRATION_TIME);

        // Set tokens in response headers
        response.setHeader("Access_Token", accessToken);
        response.setHeader("Refresh_Token", refreshToken);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    // Method to generate a JWT token
    String generateToken(String emailId, Long employeeId, String role, long expirationTime) {
        return Jwts.builder()
                .setSubject(emailId)
                .claim("EmployeeID", employeeId)
                .claim("Role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Method to validate a JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to get claims from a JWT token
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // Inner class representing the token response
    public static class TokenResponse {
        private final String accessToken;
        private final String refreshToken;

        public TokenResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        @SuppressWarnings("unused")
        public String getAccessToken() {
            return accessToken;
        }

        @SuppressWarnings("unused")
        public String getRefreshToken() {
            return refreshToken;
        }
    }
}
