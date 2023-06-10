package com.nineleaps.expensemanagementproject.controller;

import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expensemanagementproject.config.JwtUtil;
//import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

@RestController


public class UserController {
	@Autowired
	private IEmployeeService userService;
	@Autowired
	private JwtUtil jwtUtil;
	private String email;
	JSONObject responseJson;

	@GetMapping("/listtheuser")
	public List<Employee> getAllUserDtls() {
		return userService.getAllUser();
	}

	@GetMapping("/getprofiledata")
	public ResponseEntity<?> sendData(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		 String token = authorizationHeader.substring("Bearer ".length());
         System.out.println("access_token received "+token);
         Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
         JWTVerifier verifier = JWT.require(algorithm).build();
         DecodedJWT decodedJWT = verifier.verify(token);
         String emailsubject = decodedJWT.getSubject();
		Employee employee1 = userService.findByEmailId(emailsubject);
		System.out.println(employee1.getEmployeeEmail());
		Long employeeId = employee1.getEmployeeId();

		String email = employee1.getEmployeeEmail();
		String First_name = employee1.getFirstName();
		String Last_name = employee1.getLastName();
		String Full_name = First_name + " " + Last_name;
		String imageUrl = employee1.getImageUrl();
		JSONObject responseJson = new JSONObject();
		responseJson.put("employeeId", employeeId);
		responseJson.put("firstName", First_name);
		responseJson.put("lastName", Last_name);
		responseJson.put("imageUrl", imageUrl);
		responseJson.put("email", email);

		return ResponseEntity.ok(responseJson);

	}

	@PostMapping("/theprofile")
	public ResponseEntity<?> insertuser(@RequestBody Employee newUser, HttpServletResponse response,HttpServletRequest request) {
		Employee employee = userService.findByEmailId(newUser.getEmployeeEmail());

		if (employee == null) {
			
			userService.insertuser(newUser);
//			System.out.println(newUser.getEmployeeEmail());
			email = newUser.getEmployeeEmail();
			jwtUtil.generateToken(email,request,response);
//			System.out.println("new user email" + email);
			Employee employee1 = userService.findByEmailId(email);
//			System.out.println(employee1.getEmployeeEmail());
			Long employeeId = employee1.getEmployeeId();

			String email = employee1.getEmployeeEmail();
			String First_name = employee1.getFirstName();
			String Last_name = employee1.getLastName();
			String Full_name = First_name + " " + Last_name;
			String imageUrl = employee1.getImageUrl();
			JSONObject responseJson = new JSONObject();
			responseJson.put("employeeId", employeeId);
			responseJson.put("firstName", First_name);
			responseJson.put("lastName", Last_name);
			responseJson.put("imageUrl", imageUrl);
			responseJson.put("email", email);
//			System.out.println(responseJson);
			return ResponseEntity.ok(responseJson);

		} else {
			email = employee.getEmployeeEmail();
			jwtUtil.generateToken(email, request, response);
			System.out.println(email);
			Employee employee1 = userService.findByEmailId(email);
			System.out.println(employee1.getEmployeeEmail());
			Long employeeId = employee1.getEmployeeId();

			String email = employee1.getEmployeeEmail();
			String First_name = employee1.getFirstName();
			String Last_name = employee1.getLastName();
			String Full_name = First_name + " " + Last_name;
			String imageUrl = employee1.getImageUrl();
			JSONObject responseJson = new JSONObject();
			responseJson.put("employeeId", employeeId);
			responseJson.put("firstName", First_name);
			responseJson.put("lastName", Last_name);
			responseJson.put("imageUrl", imageUrl);
			responseJson.put("email", email);
			System.out.println(responseJson);
			return ResponseEntity.ok(responseJson);
		}
		

	}

}