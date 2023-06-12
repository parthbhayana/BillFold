package com.nineleaps.expensemanagementproject.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	@Column(name = "emp_Id")
	private Long employeeId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "emp_mail")
	private String employeeEmail;

	@Column(name = "is_finance_admin", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isFinanceAdmin = false;

	@Column(name = "employee_image_url")
	@ApiModelProperty(hidden = true)
	private String imageUrl;

	@Column(name = "is_hidden", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isHidden = false;

	@JsonIgnore
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<Expense> expenseList = new ArrayList<>();
	
	@Column(name="has_role")
	private String role="EMPLOYEE";

	public Employee() {
	}

	public Employee(Long employeeId, String firstName, String middleName, String lastName, String employeeEmail,
			Boolean isFinanceAdmin, String imageUrl, Boolean isHidden, List<Expense> expenseList,String role) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.employeeEmail = employeeEmail;
		this.isFinanceAdmin = isFinanceAdmin;
		this.imageUrl = imageUrl;
		this.isHidden = isHidden;
		this.expenseList = expenseList;
		this.role=role;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public Boolean getIsFinanceAdmin() {
		return isFinanceAdmin;
	}

	public void setIsFinanceAdmin(Boolean isFinanceAdmin) {
		this.isFinanceAdmin = isFinanceAdmin;
	}

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
	

}