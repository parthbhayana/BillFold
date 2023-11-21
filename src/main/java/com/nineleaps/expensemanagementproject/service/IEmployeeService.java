package com.nineleaps.expensemanagementproject.service;

import java.util.List;
import java.util.Optional;

import com.nineleaps.expensemanagementproject.DTO.EmployeeDTO;
import com.nineleaps.expensemanagementproject.DTO.UserDTO;
import com.nineleaps.expensemanagementproject.entity.Employee;

public interface IEmployeeService {

	 List<Employee> getAllEmployeeDetails();

	 Employee saveEmployeeDetails(EmployeeDTO employee);

	 Employee getEmployeeById(Long employeeId);

	 void deleteEmployeeDetailsById(Long employeeId);

	 Employee updateEmployeeDetails(EmployeeDTO newemployee, Long employeeId);

	 Employee getEmployeeByEmail(String employeeEmail);

	 void hideEmployee(Long employeeId);

	 Employee getUserByEmail(String emailToVerify);

	 List<Employee> getAllUser();

	 Employee insertUser(UserDTO newUser);

	 Employee updateUser(UserDTO newUser);

	 Employee insertuser(Employee newUser);

	public Employee findByEmailId(String emailId);

	 void setFinanceAdmin(Long employeeId);



	 Optional<Employee> additionalEmployeeDetails(Long employeeId, String officialEmployeeId, String managerEmail,
														Long mobileNumber, String managerName, String hrEmail, String hrName);

	 Optional<Employee> getEmployeeDetails(Long employeeId);

	 void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId, String managerName);

	 void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId, String managerName, String hrEmail, String hrName);
}