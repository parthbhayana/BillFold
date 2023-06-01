package com.nineleaps.expensemanagementproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.config.JwtUtil;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

@RestController
@RequestMapping()

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
	public ResponseEntity<?> sendData() {

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

		return ResponseEntity.ok(responseJson);

	}

	@PostMapping("/theprofile")
	public void insertuser(@RequestBody Employee newUser, HttpServletResponse response) {
		Employee employee = userService.findByEmailId(newUser.getEmployeeEmail());

		if (employee == null) {
			userService.insertuser(newUser);
			System.out.println(newUser.getEmployeeEmail());
			email = newUser.getEmployeeEmail();
			System.out.println("new user email" + email);

		} else {

			email = employee.getEmployeeEmail();
			System.out.println(email);

		}

	}

}