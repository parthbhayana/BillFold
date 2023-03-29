package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Employee.Details.Employee.entity.EmployeeDetails;
import com.Employee.Details.Employee.repository.EmployeeRepository;
import com.Employee.Details.Employee.service.IEmployeeService;

@Service
public class EmployeeServiceImpl implements IEmployeeService{
	
	@Autowired
	private EmployeeRepository employeerepository;
	
	@Override
	public List<EmployeeDetails> getAllEmployeeDetails(){
		return employeerepository.findAll();
	}
	
	@Override
	public EmployeeDetails saveEmployeeDetails(EmployeeDetails employeedetailsEntity) {
		return employeerepository.save(employeedetailsEntity);
	}
	
	@Override
	public EmployeeDetails getEmployeeDetailsById(Long empId){
		return employeerepository.findById(empId).get();
	}
	
	@Override
	public void deleteEmployeeDetailsById(Long empId) {
		employeerepository.deleteById(empId);
	}
	
	@Override
	public EmployeeDetails updateEmployeeDetails(EmployeeDetails employeeentity) {
		return employeerepository.save(employeeentity);
	}
}
