package com.nineleaps.expensemanagementproject.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private IEmployeeService employeeService;

	@GetMapping("/listemployee")
	public List<Employee> getAllEmployeeDetails() {
		return employeeService.getAllEmployeeDetails();
	}

	@PostMapping("/insertemployee")
	public Employee save(@RequestBody Employee employeeentiry) {
		return employeeService.saveEmployeeDetails(employeeentiry);
	}

	@PutMapping("/updateemployee/{employee_Id}")
	public Employee updateEmployee(@RequestBody Employee newemployee,
			@PathVariable("employee_Id") Long employeeId) {
		Employee employee = employeeService.getEmployeeDetailsById(employeeId);
		employee.setDesignation(newemployee.getDesignation());
		employee.setEmployeeEmail(newemployee.getEmployeeEmail());
		employee.setFirstName(newemployee.getFirstName());
		employee.setLastName(newemployee.getLastName());
		employee.setMiddleName(newemployee.getMiddleName());
		employee.setReportingManagerEmail(newemployee.getReportingManagerEmail());
		return employeeService.updateEmployeeDetails(employee);

	}

	@GetMapping("/findemployee/{employee_Id}")
	public Employee getEmployeeById(@PathVariable("employee_Id") Long employeeId) {
		return employeeService.getEmployeeDetailsById(employeeId);

	}

	@DeleteMapping("/deleteemployee/{employee_Id")
	public void deleteEmployeeById(@PathVariable("employee_id") Long employeeId) {
		employeeService.deleteEmployeeDetailsById(employeeId);
	}

}