package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private EmployeeRepository employeerepository;

	@Override
	public List<Employee> getAllEmployeeDetails() {
		return employeerepository.findAll();
	}

	@Override
	public Employee saveEmployeeDetails(Employee employeedetailsEntity) {
		return employeerepository.save(employeedetailsEntity);
	}

	@Override
	public Employee getEmployeeDetailsById(Long empId) {
		return employeerepository.findById(empId).get();
	}

	@Override
	public void deleteEmployeeDetailsById(Long empId) {
		employeerepository.deleteById(empId);
	}

	@Override
	public Employee updateEmployeeDetails(Employee newemployee, Long employeeId) {
		Employee employee = getEmployeeDetailsById(employeeId);
		employee.setDesignation(newemployee.getDesignation());
		employee.setEmployeeEmail(newemployee.getEmployeeEmail());
		employee.setFirstName(newemployee.getFirstName());
		employee.setLastName(newemployee.getLastName());
		employee.setMiddleName(newemployee.getMiddleName());
		employee.setReportingManagerEmail(newemployee.getReportingManagerEmail());
		return employeerepository.save(employee);
	}

	@Override
	public Employee getEmployeeByEmail(String emailToVerify) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee getUserByEmail(String emailToVerify) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> getAllUser() {
		// TODO Auto-generated method stub
		return employeerepository.findAll();
	}

	@Override
	public Employee insertuser(Employee newUser) {
		// TODO Auto-generated method stub
		return employeerepository.save(newUser);
	}

	@Override
	public Employee findByEmailId(String emailId) {
		// TODO Auto-generated method stub
		return employeerepository.findByEmployeeEmail(emailId);
//		return null;
	}

	@Override
	public void hideEmployee(Long empId) {
		Boolean hidden = true;
		Employee emp = getEmployeeDetailsById(empId);
		emp.setIsHidden(hidden);
		employeerepository.save(emp);
	}

}