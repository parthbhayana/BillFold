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

	@GetMapping("/list_employee")
	public List<Employee> getAllEmployeeDetails() {
		return employeeService.getAllEmployeeDetails();
	}

	@PostMapping("/insert_employee")
	public Employee save(@RequestBody Employee employeeentiry) {
		return employeeService.saveEmployeeDetails(employeeentiry);
	}

	@PutMapping("/update_employee/{employee_id}")
	public Employee updateEmployee(@RequestBody Employee newemployee, @PathVariable Long employeeId) {
		return employeeService.updateEmployeeDetails(newemployee, employeeId);

	}

	@GetMapping("/find_employee/{employee_id}")
	public Employee getEmployeeById(@PathVariable Long employeeId) {
		return employeeService.getEmployeeDetailsById(employeeId);

	}

	@DeleteMapping("/delete_employee/{employee_id}")
	public void deleteEmployeeById(@PathVariable Long employeeId) {
		employeeService.deleteEmployeeDetailsById(employeeId);
	}

	@PostMapping("/hide_employee/{employee_id}")
	public void hideEmployee(@PathVariable Long empId) {
		employeeService.hideEmployee(empId);
	}

	@PostMapping("/set_finance_admin")
	public Employee setFinanceAdmin(@RequestParam Long empId) {
		Boolean isAdmin = true;
		Employee emp = employeeService.getEmployeeDetailsById(empId);
		emp.setIsFinanceAdmin(isAdmin);
		return employeeRepository.save(emp);
	}

	@GetMapping("/is_admin")
	public void isFinanceAdmin(@RequestParam Long empId) {
		employeeService.isFinanceAdmin(empId);
	}
}