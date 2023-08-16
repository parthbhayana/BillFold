package com.nineleaps.expensemanagementproject.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	@Column(name = "employee_id")
	private Long employeeId;

	@Column(name = "official_id")
	private String officialEmployeeId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "employee_email")
	private String employeeEmail;

	@Column(name = "manager_email")
	private String managerEmail;

	@Column(name = "manager_name")
	private String managerName;

	@Column(name = "hr_name")
	private String hrName;

	@Column(name = "hr_email")
	private String hrEmail;

	@Column(name = "mobile_number")
	private Long mobileNumber;

	@Column(name = "is_admin", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isFinanceAdmin = false;

	@Column(name = "image_url")
	@ApiModelProperty(hidden = true)
	private String imageUrl;

	@Column(name = "is_hidden", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isHidden = false;

	@JsonIgnore
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<Expense> expenseList = new ArrayList<>();

	@Column(name = "role")
	@ApiModelProperty(hidden = true)
	private String role = "EMPLOYEE";

	@Column(name = "fcm_token")
	@ApiModelProperty(hidden = true)
	private String token;

	public Employee() {

	}

//	public Employee(Long employeeId, String officialEmployeeId, String firstName, String middleName, String lastName,
//					String employeeEmail, String managerEmail, String managerName, Long mobileNumber, Boolean isFinanceAdmin,
//					String imageUrl, Boolean isHidden, List<Expense> expenseList, String role) {
//		super();
//		this.employeeId = employeeId;
//		this.officialEmployeeId = officialEmployeeId;
//		this.firstName = firstName;
//		this.middleName = middleName;
//		this.lastName = lastName;
//		this.employeeEmail = employeeEmail;
//		this.managerEmail = managerEmail;
//		this.managerName = managerName;
//		this.mobileNumber = mobileNumber;
//		this.isFinanceAdmin = isFinanceAdmin;
//		this.imageUrl = imageUrl;
//		this.isHidden = isHidden;
//		this.expenseList = expenseList;
//		this.role = role;
//	}


	public Employee(Long employeeId, String officialEmployeeId, String firstName, String middleName, String lastName, String employeeEmail, String managerEmail, String managerName, Long mobileNumber, Boolean isFinanceAdmin, String imageUrl, Boolean isHidden, List<Expense> expenseList, String role, String token) {
		this.employeeId = employeeId;
		this.officialEmployeeId = officialEmployeeId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.employeeEmail = employeeEmail;
		this.managerEmail = managerEmail;
		this.managerName = managerName;
		this.mobileNumber = mobileNumber;
		this.isFinanceAdmin = isFinanceAdmin;
		this.imageUrl = imageUrl;
		this.isHidden = isHidden;
		this.expenseList = expenseList;
		this.role = role;
		this.token = token;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getOfficialEmployeeId() {
		return officialEmployeeId;
	}

	public void setOfficialEmployeeId(String officialEmployeeId) {
		this.officialEmployeeId = officialEmployeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
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

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Boolean getIsFinanceAdmin() {
		return isFinanceAdmin;
	}

	public void setIsFinanceAdmin(Boolean isFinanceAdmin) {
		this.isFinanceAdmin = isFinanceAdmin;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public List<Expense> getExpenseList() {
		return expenseList;
	}

	public void setExpenseList(List<Expense> expenseList) {
		this.expenseList = expenseList;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}