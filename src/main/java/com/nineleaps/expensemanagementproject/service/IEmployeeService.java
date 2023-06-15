package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.nineleaps.expensemanagementproject.entity.Employee;

public interface IEmployeeService {

	public List<Employee> getAllEmployeeDetails();

	public Employee saveEmployeeDetails(Employee employee);

	public Employee getEmployeeDetailsById(Long employeeId);

	public void deleteEmployeeDetailsById(Long employeeId);

	public Employee updateEmployeeDetails(Employee newemployee, Long employeeId);

	public Employee getEmployeeByEmail(String emailToVerify);

	public void hideEmployee(Long employeeId);

	public Employee getUserByEmail(String emailToVerify);

	public List<Employee> getAllUser();

	public Employee insertUser(Employee newUser);

	Employee insertuser(Employee newUser);

	public Employee findByEmailId(String emailId);

	public void isFinanceAdmin(Long employeeId);

	public void setFinanceAdmin(Long employeeId);


}