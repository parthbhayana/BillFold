package com.nineleaps.expensemanagementproject.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
	@ApiModelProperty(hidden = true)
	private LocalDate date;

	@Column(name = "created_time")
	@ApiModelProperty(hidden = true)
	private LocalTime time;

	@Column(name = "currency")
	private String currency;

	@Column(name = "amount", nullable = false)
	private Long amount;

	@Column(name = "amount_INR", nullable = false)
	@ApiModelProperty(hidden = true)
	private float amountINR;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "category")
	@ApiModelProperty(hidden = true)
	private String catDescription;

	@Column(name = "is_reported", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isReported = false;

	@Column(name = "is_hidden", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isHidden = false;

	@Column(name = "report_title")
	@ApiModelProperty(hidden = true)
	private String reportTitle;

	@Lob
	@Column(name = "supporting_documents", nullable = true)
	private byte[] supportingDocuments;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "emp_id")
	private Employee employee;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "report_id")
	private Reports reports;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id")
	private Category categoryfinance;

	public Expense() {

	}

	public Expense(Long expenseId, String merchantName, LocalDate date, LocalTime time, String currency, Long amount,
			float amountINR, String description, String catDescription, Boolean isReported, Boolean isHidden,
			String reportTitle, byte[] supportingDocuments, Employee employee, Reports reports,
			Category categoryfinance) {
		super();
		this.expenseId = expenseId;
		this.merchantName = merchantName;
		this.date = date;
		this.time = time;
		this.currency = currency;
		this.amount = amount;
		this.amountINR = amountINR;
		this.description = description;
		this.catDescription = catDescription;
		this.isReported = isReported;
		this.isHidden = isHidden;
		this.reportTitle = reportTitle;
		this.supportingDocuments = supportingDocuments;
		this.employee = employee;
		this.reports = reports;
		this.categoryfinance = categoryfinance;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public byte[] getSupportingDocuments() {
		return supportingDocuments;
	}

	public void setSupportingDocuments(byte[] supportingDocuments) {
		this.supportingDocuments = supportingDocuments;
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

	public Category getCategoryfinance() {
		return categoryfinance;
	}

	public void setCategoryfinance(Category categoryfinance) {
		this.categoryfinance = categoryfinance;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public float getAmountINR() {
		return amountINR;
	}

	public void setAmountINR(float amountINR) {
		this.amountINR = amountINR;
	}

}