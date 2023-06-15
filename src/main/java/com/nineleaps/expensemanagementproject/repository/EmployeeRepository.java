package com.nineleaps.expensemanagementproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Employee findByEmployeeEmail(String employeeEmail);

	Employee findByRole(String string);

	

	

}