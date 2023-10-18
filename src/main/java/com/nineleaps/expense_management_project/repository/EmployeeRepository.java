package com.nineleaps.expense_management_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expense_management_project.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Employee findByEmployeeEmail(String employeeEmail);

	Employee findByRole(String string);



}