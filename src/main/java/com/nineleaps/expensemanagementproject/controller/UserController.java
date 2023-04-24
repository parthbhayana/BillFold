package com.nineleaps.expensemanagementproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

@RestController
@RequestMapping()

public class UserController {
	@Autowired
	private IEmployeeService userService;

	@GetMapping("/api/v1/listtheuser")
	public List<Employee> getAllUserDtls() {
		return userService.getAllUser();
	}

	@PostMapping("/api/v1/theprofile")
	public Employee insertuser(@RequestBody Employee newUser) {
		Employee employee = userService.findByEmailId(newUser.getEmployeeEmail());
		if (employee == null) {
			return userService.insertuser(newUser);
		}
		return employee;
	}

}
