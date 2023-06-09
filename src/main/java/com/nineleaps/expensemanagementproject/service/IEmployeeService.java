package com.nineleaps.expensemanagementproject.service;

import java.util.List;
import java.util.Optional;

import com.nineleaps.expensemanagementproject.entity.Employee;

public interface IEmployeeService {

	public List<Employee> getAllEmployeeDetails();

	public Employee saveEmployeeDetails(Employee employee);

	public Employee getEmployeeById(Long employeeId);

	public void deleteEmployeeDetailsById(Long employeeId);

	public Employee updateEmployeeDetails(Employee newemployee, Long employeeId);

	public Employee getEmployeeByEmail(String emailToVerify);

	public void hideEmployee(Long employeeId);

	public Employee getUserByEmail(String emailToVerify);

	public List<Employee> getAllUser();

	public Employee insertUser(Employee newUser);

	public Employee insertuser(Employee newUser);

	public Employee findByEmailId(String emailId);

	public void setFinanceAdmin(Long employeeId);

	public Optional<Employee> additionalEmployeeDetails(Long employeeId, String officialEmployeeId, String managerEmail,
			Long mobileNumber, String managerName);

	public Optional<Employee> getEmployeeDetails(Long employeeId);

	public void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId, String managerName);
}