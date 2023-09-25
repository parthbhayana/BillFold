package com.nineleaps.expense_management_project.dto;

// A Data Transfer Object (DTO) class for representing employee information
public class EmployeeDTO {
	private String employeeEmail; // Employee's email
	private String firstName; // Employee's first name
	private String officialEmployeeId; // Employee's official ID
	private Long mobileNumber; // Employee's mobile number
	private String middleName; // Employee's middle name
	private String lastName; // Employee's last name
	private String managerEmail; // Manager's email
	private String managerName; // Manager's name
	private String hrEmail; // HR's email
	private String hrName; // HR's name

	// Default constructor
	public EmployeeDTO() {
	}

	// Parameterized constructor to initialize employee information
	public EmployeeDTO(String employeeEmail, String firstName, String officialEmployeeId,
					   Long mobileNumber, String middleName, String lastName, String managerEmail) {
		super();
		this.employeeEmail = employeeEmail;
		this.firstName = firstName;
		this.officialEmployeeId = officialEmployeeId;
		this.mobileNumber = mobileNumber;
		this.middleName = middleName;
		this.lastName = lastName;
		this.managerEmail = managerEmail;
	}

	// Additional constructor to initialize manager and HR information
	public EmployeeDTO(String managerName, String hrEmail, String hrName) {
		super();
		this.managerName = managerName;
		this.hrEmail = hrEmail;
		this.hrName = hrName;
	}



    // Getter method to retrieve the employee's email
	public String getEmployeeEmail() {
		return employeeEmail;
	}

	// Setter method to set the employee's email
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	// Getter method to retrieve the employee's first name
	public String getFirstName() {
		return firstName;
	}

	// Setter method to set the employee's first name
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	// Getter method to retrieve the employee's official ID
	public String getOfficialEmployeeId() {
		return officialEmployeeId;
	}

	// Setter method to set the employee's official ID
	public void setOfficialEmployeeId(String officialEmployeeId) {
		this.officialEmployeeId = officialEmployeeId;
	}

	// Getter method to retrieve the employee's mobile number
	public Long getMobileNumber() {
		return mobileNumber;
	}

	// Setter method to set the employee's mobile number
	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	// Getter method to retrieve the employee's middle name
	public String getMiddleName() {
		return middleName;
	}

	// Setter method to set the employee's middle name
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	// Getter method to retrieve the employee's last name
	public String getLastName() {
		return lastName;
	}

	// Setter method to set the employee's last name
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// Getter method to retrieve the manager's email
	public String getManagerEmail() {
		return managerEmail;
	}

	// Setter method to set the manager's email
	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	// Getter method to retrieve the manager's name
	public String getManagerName() {
		return managerName;
	}

	// Setter method to set the manager's name
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	// Getter method to retrieve the HR's email
	public String getHrEmail() {
		return hrEmail;
	}

	// Setter method to set the HR's email
	public void setHrEmail(String hrEmail) {
		this.hrEmail = hrEmail;
	}

	// Getter method to retrieve the HR's name
	public String getHrName() {
		return hrName;
	}

	// Setter method to set the HR's name
	public void setHrName(String hrName) {
		this.hrName = hrName;
	}
}
