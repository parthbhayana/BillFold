package com.nineleaps.expensemanagementproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@SuppressWarnings("unused")
	@Autowired
	private JwtUtil jwtUtil;

	public static String getEmailFromJwt(String jwt) {
		DecodedJWT decodedJWT = JWT.decode(jwt);
		return decodedJWT.getClaim("email").asString();

	}

	@Autowired
	private IEmployeeService userServices;

	@GetMapping("/verify_employee")
	public String getUserByEmail(String emailToVerify) {
		Employee user = userServices.getUserByEmail(emailToVerify);
		if (user != null) {
			return "User verifed : " + emailToVerify;
		} else
			return "User not found. Please contact your administrator !!!";
	}
}