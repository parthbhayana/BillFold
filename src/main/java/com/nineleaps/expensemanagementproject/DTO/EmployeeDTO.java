package com.nineleaps.expensemanagementproject.DTO;

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

	public EmployeeDTO() {
	}

	public EmployeeDTO(String employeeEmail, String firstName, String officialEmployeeId, Long mobileNumber,
					   String middleName, String lastName, String managerEmail, String managerName, String hrEmail,
					   String hrName) {
		super();
		this.employeeEmail = employeeEmail;
		this.firstName = firstName;
		this.officialEmployeeId = officialEmployeeId;
		this.mobileNumber = mobileNumber;
		this.middleName = middleName;
		this.lastName = lastName;
		this.managerEmail = managerEmail;
		this.managerName = managerName;
		this.hrEmail = hrEmail;
		this.hrName = hrName;
	}

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

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
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

}