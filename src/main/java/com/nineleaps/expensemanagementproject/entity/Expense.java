package com.nineleaps.expensemanagementproject.entity;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "expense")
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "expense_id", nullable = false)
	@ApiModelProperty(hidden = true)
	private Long expenseId;

	@Column(name = "merchant_Name", nullable = false)
	private String merchantName;

	@Column(name = "date", nullable = false)
	private Date date;

	@Column(name = "amount", nullable = false)
	private Long amount;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "category")
	@ApiModelProperty(hidden = true)
	private String catDescription;

	@Column(name = "is_reported", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isReported = false;

//	@Column(name = "manager_email")
//	@ApiModelProperty(hidden = true)
//	private String managerEmail;

	@Lob
	@Column(name = "supporting_documents", nullable = true)
	private byte[] supportingDocuments;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "fk_empid")
	private Employee employee;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "report_id")
	private Reports reports;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id")
	private CategoryFinance categoryfinance;

//	public Expense(String merchantName, Date date, Long amount, String description, byte[] supportingDocuments,
//			Employee employee, Reports reports, CategoryFinance categoryfinance, String catDescription) {
//		super();
//		this.merchantName = merchantName;
//		this.date = date;
//		this.amount = amount;
//		this.description = description;
//		this.supportingDocuments = supportingDocuments;
//		this.employee = employee;
//		this.reports = reports;
//		this.categoryfinance = categoryfinance;
//		this.catDescription = catDescription;
//	}
//	
//	

	public Expense(String merchantName, Date date, Long amount, String description, String catDescription,
			Boolean isReported, byte[] supportingDocuments, Employee employee, Reports reports,
			CategoryFinance categoryfinance) {
		super();
		this.merchantName = merchantName;
		this.date = date;
		this.amount = amount;
		this.description = description;
		this.catDescription = catDescription;
		this.isReported = isReported;
//		this.managerEmail = managerEmail;
		this.supportingDocuments = supportingDocuments;
		this.employee = employee;
		this.reports = reports;
		this.categoryfinance = categoryfinance;
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

	public CategoryFinance getCategoryfinance() {
		return categoryfinance;
	}

	public void setCategoryfinance(CategoryFinance categoryfinance) {
		this.categoryfinance = categoryfinance;
	}

	public String getCatDescription() {
		return catDescription;
	}

	public void setCatDescription(String catDescription) {
		this.catDescription = catDescription;
	}

	public Boolean getIsReported() {
		return isReported;
	}

	public void setIsReported(Boolean isReported) {
		this.isReported = isReported;
	}

}
