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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private IEmployeeService employeeService;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@GetMapping("/listemployee")
	public List<Employee> getAllEmployeeDetails() {
		return employeeService.getAllEmployeeDetails();
	}

	@PostMapping("/insertemployee")
	public Employee save(@RequestBody Employee employeeentiry) {
		return employeeService.saveEmployeeDetails(employeeentiry);
	}

	@PutMapping("/updateemployee/{employee_Id}")
	public Employee updateEmployee(@RequestBody Employee newemployee, @PathVariable("employee_Id") Long employeeId) {
		return employeeService.updateEmployeeDetails(newemployee, employeeId);

	}

	@GetMapping("/findemployee/{employee_Id}")
	public Employee getEmployeeById(@PathVariable("employee_Id") Long employeeId) {
		return employeeService.getEmployeeDetailsById(employeeId);

	}

	@DeleteMapping("/deleteemployee/{employee_Id}")
	public void deleteEmployeeById(@PathVariable("employee_id") Long employeeId) {
		employeeService.deleteEmployeeDetailsById(employeeId);
	}
	
	@PostMapping("/hideemployee/{empId}")
	public void hideEmployee(@PathVariable Long empId) {
		employeeService.hideEmployee(empId);
	}
	
	@PostMapping("/setfinanceadmin")
	public Employee setFinanceAdmin(@RequestParam Long empId) {
	String admin = "FINANCE_ADMIN";
	boolean isAdmin=true;
	Employee emp = employeeService.getEmployeeDetailsById(empId);
	emp.setRole(admin);
	emp.setIsFinanceAdmin(isAdmin);
	return employeeRepository.save(emp);
	}
	
	@GetMapping("/isadmin")
//	@GetMapping("/isadmin/{empId}")
	public void isFinanceAdmin(@RequestParam Long empId) {
		employeeService.isFinanceAdmin(empId);
	}
}