package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> getAllEmployeeDetails() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee saveEmployeeDetails(Employee employeedetailsEntity) {
		return employeeRepository.save(employeedetailsEntity);
	}

	@Override
	public Employee getEmployeeDetailsById(Long empId) {
		return employeeRepository.findById(empId).get();
	}

	@Override
	public void deleteEmployeeDetailsById(Long empId) {
		employeeRepository.deleteById(empId);
	}

	@Override
	public Employee updateEmployeeDetails(Employee newemployee, Long employeeId) {
		Employee employee = getEmployeeDetailsById(employeeId);
		employee.setEmployeeEmail(newemployee.getEmployeeEmail());
		employee.setFirstName(newemployee.getFirstName());
		employee.setLastName(newemployee.getLastName());
		employee.setMiddleName(newemployee.getMiddleName());
		return employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployeeByEmail(String emailToVerify) {
		return null;
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
	public Employee insertuser(Employee newUser) {
		return employeeRepository.save(newUser);
	}

	@Override
	public Employee findByEmailId(String emailId) {
		return employeeRepository.findByEmployeeEmail(emailId);
	}

	@Override
	public void hideEmployee(Long empId) {
		Boolean hidden = true;
		Employee emp = getEmployeeDetailsById(empId);
		emp.setIsHidden(hidden);
		employeeRepository.save(emp);
	}

	@Override
	public Boolean isFinanceAdmin(Long empId) {
		Employee emp = getEmployeeDetailsById(empId);
		Boolean isAdmin = emp.getIsFinanceAdmin();
		return isAdmin;
	}

	@Override
	public void setFinanceAdmin(Long empId) {
		Boolean isAdmin = true;
		String role="FINANCE_ADMIN";
		Employee emp = getEmployeeDetailsById(empId);
		emp.setIsFinanceAdmin(isAdmin);
		emp.setRole(role);
		employeeRepository.save(emp);
		
	}

}