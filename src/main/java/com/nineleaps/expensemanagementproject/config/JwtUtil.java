package com.nineleaps.expensemanagementproject.config;

import javax.servlet.http.*;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	private static final String SECRET_KEY = "secret";

	public ResponseEntity<?> generateToken(String emailId, String name, String imageUrl, HttpServletResponse response) {
		String token = Jwts.builder().setSubject(emailId).claim("name", name).claim("imageUrl", imageUrl)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
		System.out.println(token);
		response.setHeader("Access_Token", token);
		return ResponseEntity.ok(token);

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
}