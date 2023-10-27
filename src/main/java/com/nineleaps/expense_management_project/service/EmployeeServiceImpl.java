package com.nineleaps.expense_management_project.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import com.nineleaps.expense_management_project.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> getAllEmployeeDetails() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee saveEmployeeDetails(EmployeeDTO employeeDTO) {
		Employee employee=new Employee();
		employee.setEmployeeEmail(employeeDTO.getEmployeeEmail());
		employee.setFirstName(employeeDTO.getFirstName());
		employee.setLastName(employeeDTO.getLastName());
		employee.setManagerEmail(employeeDTO.getManagerEmail());
		employee.setManagerName(employeeDTO.getManagerName());
		employee.setHrEmail(employeeDTO.getHrEmail());
		employee.setHrName(employeeDTO.getHrName());
		employee.setMiddleName(employeeDTO.getMiddleName());
		employee.setMobileNumber(employeeDTO.getMobileNumber());
		employee.setOfficialEmployeeId(employeeDTO.getOfficialEmployeeId());
		return employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployeeById(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId).orElse(null);

		if (employee == null) {
			throw new NullPointerException("Employee with ID " + employeeId + " not found");
		}

		return employee;
	}



	@Override
	public void deleteEmployeeDetailsById(Long employeeId) {
		employeeRepository.deleteById(employeeId);
	}

	@Override
	public Employee updateEmployeeDetails(EmployeeDTO employeeDTO, Long employeeId) {
		Employee employee = getEmployeeById(employeeId);
		employee.setEmployeeEmail(employeeDTO.getEmployeeEmail());
		employee.setFirstName(employeeDTO.getFirstName());
		employee.setLastName(employeeDTO.getLastName());
		employee.setMiddleName(employeeDTO.getMiddleName());
		return employeeRepository.save(employee);
	}


	@Override
	public Optional<Employee> additionalEmployeeDetails(Long employeeId, String officialEmployeeId, String managerEmail, Long mobileNumber, String managerName, String hrEmail, String hrName) {
		Employee employee = getEmployeeById(employeeId);
		employee.setOfficialEmployeeId(officialEmployeeId);
		employee.setManagerEmail(managerEmail);
		employee.setMobileNumber(mobileNumber);
		employee.setManagerName(managerName);
		employee.setHrName(hrName);
		employee.setHrEmail(hrEmail);
		employeeRepository.save(employee);
		return getEmployeeDetails(employeeId);
	}

	@Override
	public Optional<Employee> getEmployeeDetails(Long employeeId) {
		return employeeRepository.findById(employeeId);
	}

	@Override
	public void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber,
									String officialEmployeeId, String managerName) {
		Employee employee = getEmployeeById(employeeId);
		employee.setManagerEmail(managerEmail);
		employee.setMobileNumber(mobileNumber);
		employee.setOfficialEmployeeId(officialEmployeeId);
		employee.setManagerName(managerName);
		employeeRepository.save(employee);
	}

	@Override
	public void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId, String managerName, String hrEmail, String hrName) {
		Employee employee = getEmployeeById(employeeId);
		employee.setManagerEmail(managerEmail);
		employee.setMobileNumber(mobileNumber);
		employee.setOfficialEmployeeId(officialEmployeeId);
		employee.setManagerName(managerName);
		employee.setHrEmail(hrEmail);
		employee.setHrName(hrName);
		employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployeeByEmail(String employeeEmail) {
		return employeeRepository.findByEmployeeEmail(employeeEmail);
	}

	@Override
	public Employee getUserByEmail(String emailToVerify) {
		return null;
	}

	@Override
	public List<Employee> getAllUser() {
		return employeeRepository.findAll();
	}



	@Override
	public Employee findByEmailId(String emailId) {
		return employeeRepository.findByEmployeeEmail(emailId);
	}

	@Override
	public void hideEmployee(Long employeeId) {
		Boolean hidden = true;
		Employee employee = getEmployeeById(employeeId);
		employee.setIsHidden(hidden);
		employeeRepository.save(employee);
	}

	@Override
	public void setFinanceAdmin(Long employeeId) {
		Boolean isAdmin = true;
		String role = "FINANCE_ADMIN";
		Employee employee = getEmployeeById(employeeId);
		employee.setIsFinanceAdmin(isAdmin);
		employee.setRole(role);

		employeeRepository.save(employee);

//		List<Employee> emp = employeeRepository.findAll();
//		Boolean isAdmins = false;
//		String roles = "EMPLOYEE";
//		for (Employee emp1 : emp) {
//			if (Objects.equals(emp1.getEmployeeId(), employeeId))
//				continue;
//			emp1.setIsFinanceAdmin(isAdmins);
//			emp1.setRole(roles);
//			employeeRepository.save(emp1);
//		}

	}

	@Override
	public Employee insertUser(UserDTO userDTO) {
		Employee employee = new Employee();
		employee.setEmployeeEmail(userDTO.getEmployeeEmail());
		employee.setImageUrl(userDTO.getImageUrl());
		employee.setFirstName(userDTO.getFirstName());
		employee.setMiddleName(userDTO.getMiddleName());
		employee.setLastName(userDTO.getLastName());
		employee.setToken(userDTO.getFcmToken());
		return employeeRepository.save(employee);
	}

	@Override
	public Employee updateUser(UserDTO userDTO) {
		Employee employee = findByEmailId(userDTO.getEmployeeEmail());
		employee.setEmployeeEmail(userDTO.getEmployeeEmail());
		employee.setImageUrl(userDTO.getImageUrl());
		employee.setFirstName(userDTO.getFirstName());
		employee.setMiddleName(userDTO.getMiddleName());
		employee.setLastName(userDTO.getLastName());
		employee.setToken(userDTO.getFcmToken());
		return employeeRepository.save(employee);
	}
}