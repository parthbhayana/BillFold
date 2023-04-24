package com.nineleaps.expensemanagementproject.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_Id")
	@ApiModelProperty(hidden = true)
	private Long employeeId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "emp_mail")
	private String employeeEmail;

	@Column(name = "designation")
	private String designation;

	@Column(name = "manager_mail")
	private String reportingManagerEmail;

//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "fk_empid",referencedColumnName = "emp_id")
//	private List<Expense >expense;

	@JsonIgnore
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private List<Expense> expenseList = new ArrayList<>();

	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public Employee(Long employeeId, String firstName, String middleName, String lastName, String email,
			String designation, String reportingManagerEmail, List<Expense> expenseList) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.employeeEmail = email;
		this.designation = designation;
		this.reportingManagerEmail = reportingManagerEmail;
		this.expenseList = expenseList;
	}

	public Employee(Long employeeId, String firstName, String middleName, String lastName, String email,
			String designation, String reportingManagerEmail) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.employeeEmail = email;
		this.designation = designation;
		this.reportingManagerEmail = reportingManagerEmail;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getReportingManagerEmail() {
		return reportingManagerEmail;
	}

	public void setReportingManagerEmail(String reportingManagerEmail) {
		this.reportingManagerEmail = reportingManagerEmail;
	}

	public List<Expense> getExpenseList() {
		return expenseList;
	}

	public void setExpenseList(List<Expense> expenseList) {
		this.expenseList = expenseList;
	}

}