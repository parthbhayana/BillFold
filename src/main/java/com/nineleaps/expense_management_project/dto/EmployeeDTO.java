package com.nineleaps.expense_management_project.dto;

import lombok.Getter;

@Getter
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


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}


}