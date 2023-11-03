package com.nineleaps.expensemanagementproject.config;


import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {


    private static final String SECRET_KEY = generateRandomSecretKey();
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 10;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 30;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


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

    public ResponseEntity<TokenResponse> generateTokens(String emailId, Long employeeId,  String role,
                                                        HttpServletResponse response) {


        String accessToken = generateToken(emailId, employeeId, role, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = generateToken(emailId, employeeId, role, REFRESH_TOKEN_EXPIRATION_TIME);

        response.setHeader("Access_Token", accessToken);
        response.setHeader("Refresh_Token", refreshToken);
        logger.info("Refresh token generated: {}", refreshToken);
        logger.info("Access token generated: {}", accessToken);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    public String generateToken(String emailId, Long employeeId, String role, long expirationTime) {
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

    public boolean isRefreshTokenExpired(String refreshToken){
        DecodedJWT decodedJWT = JWT.decode(refreshToken);
        Date expirationDate = decodedJWT.getExpiresAt();
        return expirationDate.before(new Date());
    }
    public boolean isAccessTokenExpired(String accessToken) {
        DecodedJWT decodedJWT = JWT.decode(accessToken);
        Date expirationDate = decodedJWT.getExpiresAt();
        return expirationDate.before(new Date());
    }

}