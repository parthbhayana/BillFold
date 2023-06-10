package com.nineleaps.expensemanagementproject.config;

import javax.servlet.http.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtUtil {
	@Autowired
	private IEmployeeService userService;
	
	public void generateToken(String email, HttpServletRequest request, HttpServletResponse response) {
	    Employee employee = userService.findByEmailId(email);
	    System.out.println("roles "+employee.getRole());
	    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
	    // Update access token expiration time dynamically
	    LocalDateTime now = LocalDateTime.now();
	    LocalDateTime accessTokenExpirationTime = now.plusMinutes(1440); // Update to desired expiration time (24 hours or one day)
	    Date accessTokenExpirationDate = Date.from(accessTokenExpirationTime.atZone(ZoneId.systemDefault()).toInstant());
	    
	    String access_token = JWT.create()
	            .withSubject(employee.getEmployeeEmail())
	            .withExpiresAt(accessTokenExpirationDate)
	            .withIssuer(request.getRequestURL().toString())
	            .withClaim("Employee_Id", employee.getEmployeeId())
	            .withClaim("role", employee.getRole()) // Set the role claim
	            .sign(algorithm);

	    // Update refresh token expiration time dynamically
	    LocalDateTime refreshTokenExpirationTime = now.plusMinutes(43200); // Update to desired expiration time (30 days)
	    Date refreshTokenExpirationDate = Date.from(refreshTokenExpirationTime.atZone(ZoneId.systemDefault()).toInstant());
	    String refresh_token = JWT.create()
	            .withSubject(employee.getEmployeeEmail())
	            .withExpiresAt(refreshTokenExpirationDate)
	            .withIssuer(request.getRequestURL().toString())
	            .sign(algorithm);
	    
	    System.out.println("access_token "+ access_token);
	   
	    response.setHeader("access_token", access_token);
	    response.setHeader("refresh_token", refresh_token);
	}

}
////	private static final String SECRET_KEY = "secret";
////
////	public ResponseEntity<?> generateToken(String emailId, String name, String imageUrl, HttpServletResponse response) {
////		String token = Jwts.builder().setSubject(emailId).claim("name", name).claim("imageUrl", imageUrl)
////				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
////				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
////		System.out.println(token);
////		response.setHeader("Access_Token", token);
////		return ResponseEntity.ok(token);
////
////	}
////
////	public boolean validateToken(String token) {
////		try {
////			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
////			return true;
////		} catch (Exception e) {
////			return false;
////		}
////	}
////
////	public Claims getClaimsFromToken(String token) {
////		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
////	}
////}