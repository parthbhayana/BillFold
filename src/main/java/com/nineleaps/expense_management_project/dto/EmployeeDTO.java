package com.nineleaps.expense_management_project.dto;

import lombok.Getter;

@Getter
public class EmployeeDTO {
	private String employeeEmail;
	private String firstName;

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setOfficialEmployeeId(String officialEmployeeId) {
		this.officialEmployeeId = officialEmployeeId;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public void setHrEmail(String hrEmail) {
		this.hrEmail = hrEmail;
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