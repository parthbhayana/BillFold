package com.nineleaps.expensemanagementproject.entity;

import java.sql.Date;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "expense")
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "expense_id", nullable = false)
	private Long expenseId;
	@Column(name = "merchant_Name", nullable = false)
	private String merchantName;
	@Column(name = "date", nullable = false)
	private Date date;
	@Column(name = "amount", nullable = false)
	private Long amount;
	@Column(name = "description", nullable = false)
	private String description;
//	 @Lob
//	 @Column(name = "supporting_document", nullable=true)
//	 private byte[] supportingDocument;
	 
	 
	 @Lob
	 @Column(name = "supporting_documents", nullable=true)
	 private byte[] supportingDocuments;

	@Enumerated(EnumType.STRING)
	private Category category;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_empid")
	private Employee employee;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "report_id")
	private Reports reports;

//	@ManyToOne
//	@JoinColumn(name = "fk_reportid")
//	@JsonIgnore
//	private Reports reports;





	


	


	public Expense(Long expenseId, String merchantName, Date date, Long amount, String description,
		byte[] supportingDocuments, Category category, Employee employee, Reports reports) {
	super();
	this.expenseId = expenseId;
	this.merchantName = merchantName;
	this.date = date;
	this.amount = amount;
	this.description = description;
	this.supportingDocuments = supportingDocuments;
	this.category = category;
	this.employee = employee;
	this.reports = reports;
}
	


	public Expense(String merchantName, Date date, Long amount, String description, byte[] supportingDocument,
			Category category, Employee employee, Reports reports) {
		super();
		this.merchantName = merchantName;
		this.date = date;
		this.amount = amount;
		this.description = description;
		this.supportingDocuments = supportingDocuments;
		this.category = category;
		this.employee = employee;
		this.reports = reports;
	}





	public byte[] getSupportingDocuments() {
		return supportingDocuments;
	}



	public void setSupportingDocuments(byte[] supportingDocuments) {
		this.supportingDocuments = supportingDocuments;
	}



	public Expense() {

	}

	public Long getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Reports getReports() {
		return reports;
	}

	public void setReports(Reports reports) {
		this.reports = reports;
	}

}
