package com.nineleaps.expense_management_project.dto;

public class EmployeeDTO {
	private String employeeEmail;
	private String firstName;
	private String officialEmployeeId;
	private Long mobileNumber;
	private String middleName;
	private String lastName;
	private String managerEmail;
	private String managerName;
	private String hrEmail;
	private String hrName;


	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getOfficialEmployeeId() {
		return officialEmployeeId;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}


	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}


	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public String getManagerName() {
		return managerName;
	}

	public String getHrEmail() {
		return hrEmail;
	}

	public String getHrName() {
		return hrName;
	}



}