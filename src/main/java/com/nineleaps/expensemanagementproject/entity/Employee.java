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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@Column(name = "designation")
	private String designation;
	@Column(name = "manager_mail")
	private String reportingManagerEmail;
	
	public String getImageUrl() {
		return imageUrl;
	}
	private String subject;
	private String message;
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Column(name="employee_image_url")
	private String imageUrl;

	@JsonIgnore
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<Expense> expenseList = new ArrayList<>();
	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public Long getEmployeeId() {
		return employeeId;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Expense> getExpenseList() {
		return expenseList;
	}

	public void setExpenseList(List<Expense> expenseList) {
		this.expenseList = expenseList;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Employee(Long employeeId, String firstName, String middleName, String lastName, String employeeEmail,
			String designation, String reportingManagerEmail, String subject, String message, String imageUrl,
			List<Expense> expenseList) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.employeeEmail = employeeEmail;
		this.designation = designation;
		this.reportingManagerEmail = reportingManagerEmail;
		this.subject = subject;
		this.message = message;
		this.imageUrl = imageUrl;
		this.expenseList = expenseList;
	}


//public Employee(Long employeeId, String firstName, String middleName, String lastName, String employeeEmail,
//		String designation, String reportingManagerEmail, String imageUrl, List<Expense> expenseList) {
//	super();
//	this.employeeId = employeeId;
//	this.firstName = firstName;
//	this.middleName = middleName;
//	this.lastName = lastName;
//	this.employeeEmail = employeeEmail;
//	this.designation = designation;
//	this.reportingManagerEmail = reportingManagerEmail;
//	this.imageUrl = imageUrl;
//	this.expenseList = expenseList;
//}
//
//	public void setEmployeeId(Long employeeId) {
//		this.employeeId = employeeId;
//	}
//	public String getFirstName() {
//		return firstName;
//	}
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//	public String getMiddleName() {
//		return middleName;
//	}
//	public void setMiddleName(String middleName) {
//		this.middleName = middleName;
//	}
//	public String getLastName() {
//		return lastName;
//	}
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//	public String getEmployeeEmail() {
//		return employeeEmail;
//	}
//	public void setEmployeeEmail(String employeeEmail) {
//		this.employeeEmail = employeeEmail;
//	}
//	public String getDesignation() {
//		return designation;
//	}
//	public void setDesignation(String designation) {
//		this.designation = designation;
//	}
//	public String getReportingManagerEmail() {
//		return reportingManagerEmail;
//	}
//	public void setReportingManagerEmail(String reportingManagerEmail) {
//		this.reportingManagerEmail = reportingManagerEmail;
//	}
//	public List<Expense> getExpenseList() {
//		return expenseList;
//	}
//	public void setExpenseList(List<Expense> expenseList) {
//		this.expenseList = expenseList;
//	}


}