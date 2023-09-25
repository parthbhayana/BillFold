package com.nineleaps.expense_management_project.service;

import java.util.List;
import java.util.Optional;

import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import com.nineleaps.expense_management_project.dto.UserDTO;
import com.nineleaps.expense_management_project.entity.Employee;

public interface IEmployeeService {

	public List<Employee> getAllEmployeeDetails();

	public Employee saveEmployeeDetails(EmployeeDTO employee);

	public Employee getEmployeeById(Long employeeId);

	public void deleteEmployeeDetailsById(Long employeeId);

	public Employee updateEmployeeDetails(EmployeeDTO newemployee, Long employeeId);

	public Employee getEmployeeByEmail(String employeeEmail);

	public void hideEmployee(Long employeeId);

	public Employee getUserByEmail(String emailToVerify);

	public List<Employee> getAllUser();

	public Employee insertUser(UserDTO newUser);

	public Employee updateUser(UserDTO newUser);



	public Employee findByEmailId(String emailId);

	public void setFinanceAdmin(Long employeeId);


	public Optional<Employee> additionalEmployeeDetails(Long employeeId, String officialEmployeeId, String managerEmail,
														Long mobileNumber, String managerName, String hrEmail, String hrName);

	public Optional<Employee> getEmployeeDetails(Long employeeId);

	public void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId, String managerName);

	public void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId, String managerName, String hrEmail, String hrName);
}