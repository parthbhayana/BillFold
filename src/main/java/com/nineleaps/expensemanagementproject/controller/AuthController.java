package com.nineleaps.expensemanagementproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.dto.LoginRequest;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private JwtUtil jwtUtil;

	public static String getEmailFromJwt(String jwt) {
		DecodedJWT decodedJWT = JWT.decode(jwt);
		return decodedJWT.getClaim("email").asString();
	}

	@Autowired
	private IEmployeeService userServices;

	// fetch the email from the token given to verify the user -whether present in
	// db or not
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
		// try {
		// // authenticate user
		// authenticationManager.authenticate(new
		// UsernamePasswordAuthenticationToken(loginRequest.getEmailId(), ""));
		// } catch (BadCredentialsException e) {
		// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
		// }
		// generate JWT token
		Employee user = userServices.findByEmailId(loginRequest.getEmailId());

		String token = jwtUtil.generateToken(user.getEmployeeEmail(), user.getFirstName());
		return ResponseEntity.ok(new JwtResponse(token));
	}

	// method to check if the email is present in db or not
	@GetMapping("/verifyEmployee")
	public String getUserByEmail(String emailToVerify) {
		Employee user = userServices.getUserByEmail(emailToVerify);
		if (user != null) {
			return "User verifed : " + emailToVerify;
		} else
			return "User not found. Please contact your administrator !!!";

	}

}
