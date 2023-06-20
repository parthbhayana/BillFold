package com.nineleaps.expensemanagementproject.config;

import javax.servlet.http.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "67c3f3e463358fb9cb3fb8538e04c05b6c1707563df66d2fb8a990ee04b22019";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 10;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 30;


    public ResponseEntity<?> generateTokens(String emailId, Long employeeId, String name, String imageUrl, String role,
                                            HttpServletResponse response) {
        String accessToken = generateToken(emailId, employeeId, role, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = generateToken(emailId, employeeId, role, REFRESH_TOKEN_EXPIRATION_TIME);

        response.setHeader("Access_Token", accessToken);
        response.setHeader("Refresh_Token", refreshToken);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    private String generateToken(String emailId, Long employeeId, String role, long expirationTime) {
        return Jwts.builder()
                .setSubject(emailId)
                .claim("EmployeeID", employeeId)
                .claim("Role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private static class TokenResponse {
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