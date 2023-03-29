package com.nineleaps.expensemanagementproject.service;

import java.util.List;

import com.Employee.Details.Employee.entity.EmployeeDetails;

public interface IEmployeeService {

	public List<EmployeeDetails> getAllEmployeeDetails();

	public EmployeeDetails saveEmployeeDetails(EmployeeDetails employeedetailsEntiry);

	public EmployeeDetails getEmployeeDetailsById(Long empId);

	public void deleteEmployeeDetailsById(Long empId);

	public EmployeeDetails updateEmployeeDetails(EmployeeDetails employeeentity);
}
