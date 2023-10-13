package com.nineleaps.expense_management_project.dto;

import lombok.Getter;

@Getter
public class EmployeeDTO {
	private String employeeEmail;
	private String firstName;

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getOfficialEmployeeId() {
		return officialEmployeeId;
	}

	public void setOfficialEmployeeId(String officialEmployeeId) {
		this.officialEmployeeId = officialEmployeeId;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getHrEmail() {
		return hrEmail;
	}

	public void setHrEmail(String hrEmail) {
		this.hrEmail = hrEmail;
	}

	public String getHrName() {
		return hrName;
	}

	public void setHrName(String hrName) {
		this.hrName = hrName;
	}

	private String officialEmployeeId;
	private Long mobileNumber;
	private String middleName;
	private String lastName;
	private String managerEmail;
	private String managerName;
	private String hrEmail;
	private String hrName;


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}


}