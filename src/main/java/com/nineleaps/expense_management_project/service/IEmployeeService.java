package com.nineleaps.expense_management_project.service;

import java.util.List;
import java.util.Optional;
import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import com.nineleaps.expense_management_project.dto.UserDTO;
import com.nineleaps.expense_management_project.entity.Employee;

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



	 Employee findByEmailId(String emailId);

	 void setFinanceAdmin(Long employeeId);


	 Optional<Employee> additionalEmployeeDetails(Long employeeId, String officialEmployeeId, String managerEmail,
														Long mobileNumber, String managerName, String hrEmail, String hrName);

	 Optional<Employee> getEmployeeDetails(Long employeeId);

	 void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId, String managerName);

	 void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId, String managerName, String hrEmail, String hrName);
}