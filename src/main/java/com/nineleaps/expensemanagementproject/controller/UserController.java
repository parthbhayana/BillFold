package com.nineleaps.expensemanagementproject.controller;

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
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping()
public class UserController {
    @Autowired
    private IEmployeeService userService;
    @SuppressWarnings("unused")
    @Autowired
    private JwtUtil jwtUtil;
    private String email;
    JSONObject responseJson;

    @GetMapping("/list_the_user")
    public List<Employee> getAllUserDtls() {
        return userService.getAllUser();
    }

	
	@SuppressWarnings("unchecked")
	@GetMapping("/get_profile_data")
	public ResponseEntity<?> sendData(HttpServletRequest request) {
		String authorisationHeader = request.getHeader(AUTHORIZATION);
		String token = authorisationHeader.substring("Bearer ".length());
		DecodedJWT decodedAccessToken = JWT.decode(token);
        String employeeEmailFromToken = decodedAccessToken.getSubject();
		Employee employee1 = userService.findByEmailId(employeeEmailFromToken);
		System.out.println(employee1.getEmployeeEmail());
		Long employeeId = employee1.getEmployeeId();
		String email = employee1.getEmployeeEmail();
		String First_name = employee1.getFirstName();
		String Last_name = employee1.getLastName();
		@SuppressWarnings("unused")
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

	@PostMapping("/the_profile")
	public ResponseEntity<?> insertUser(@RequestBody Employee newUser, HttpServletResponse response) {
		Employee employee = userService.findByEmailId(newUser.getEmployeeEmail());
		if (employee == null) {
			userService.insertUser(newUser);
			employee = userService.findByEmailId(newUser.getEmployeeEmail());
			System.out.println(employee.getEmployeeEmail());
			email = employee.getEmployeeEmail();
			System.out.println("new user email" + email);
			System.out.println(employee.getEmployeeId());
			ResponseEntity<?> tokenResponse = jwtUtil.generateTokens(email, employee.getEmployeeId(),
				    employee.getFirstName(), employee.getImageUrl(), employee.getRole(), response);
			return tokenResponse;
		} else {
			email = employee.getEmployeeEmail();
			System.out.println(email);
			System.out.println(employee.getEmployeeId());
			ResponseEntity<?> tokenResponse = jwtUtil.generateTokens(email, employee.getEmployeeId(),
				    employee.getFirstName(), employee.getImageUrl(), employee.getRole(), response);
			return tokenResponse;
		}
	}
}