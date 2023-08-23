package com.nineleaps.expensemanagementproject.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

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
	private LocalDate date;

	@Column(name = "date_created")
	@ApiModelProperty(hidden = true)
	private LocalDateTime dateCreated;

	@Column(name = "currency")
	private String currency;

	@Column(name = "currency_symbol")
	@ApiModelProperty(hidden = true)
	private String currencySymbol;

	@Column(name = "amount", nullable = false)
	private Float amount;

	@Column(name = "amount_INR", nullable = false)
	@ApiModelProperty(hidden = true)
	private float amountINR;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "category")
	@ApiModelProperty(hidden = true)
	private String categoryDescription;

	@Column(name = "is_reported", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isReported = false;

	@Column(name = "is_hidden", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isHidden = false;

	@Column(name = "report_title")
	@ApiModelProperty(hidden = true)
	private String reportTitle;

	@Column(name = "amount_approved")
	@ApiModelProperty(hidden = true)
	private Float amountApproved;

	@Column(name = "amount_approved_INR")
	@ApiModelProperty(hidden = true)
	private Double amountApprovedINR;

	@Column(name = "finance_approval_status", nullable = true)
	@ApiModelProperty(hidden = true)
	@Enumerated(EnumType.STRING)
	private FinanceApprovalStatus financeApprovalStatus;

	@Column(name = "manager_approval_status", nullable = true)
	@ApiModelProperty(hidden = true)
	@Enumerated(EnumType.STRING)
	private ManagerApprovalStatusExpense managerApprovalStatusExpense;

	@Column(name = "potential_duplicate")
	@ApiModelProperty(hidden = true)
	private Boolean potentialDuplicate = false;

	@Lob
	@Column(name = "supporting_documents", nullable = true)
	private byte[] supportingDocuments;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "report_id")
	private Reports reports;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id")
	private Category category;

	public Expense() {

	}

	public Expense(Long expenseId, String merchantName, LocalDate date, LocalDateTime dateCreated, String currency,
			String currencySymbol, Float amount, float amountINR, String description, String categoryDescription,
			Boolean isReported, Boolean isHidden, String reportTitle, Float amountApproved, Double amountApprovedINR,
			FinanceApprovalStatus financeApprovalStatus, ManagerApprovalStatusExpense managerApprovalStatusExpense,
			Boolean potentialDuplicate, byte[] supportingDocuments, Employee employee, Reports reports,
			Category category) {
		super();
		this.expenseId = expenseId;
		this.merchantName = merchantName;
		this.date = date;
		this.dateCreated = dateCreated;
		this.currency = currency;
		this.currencySymbol = currencySymbol;
		this.amount = amount;
		this.amountINR = amountINR;
		this.description = description;
		this.categoryDescription = categoryDescription;
		this.isReported = isReported;
		this.isHidden = isHidden;
		this.reportTitle = reportTitle;
		this.amountApproved = amountApproved;
		this.amountApprovedINR = amountApprovedINR;
		this.financeApprovalStatus = financeApprovalStatus;
		this.managerApprovalStatusExpense = managerApprovalStatusExpense;
		this.potentialDuplicate = potentialDuplicate;
		this.supportingDocuments = supportingDocuments;
		this.employee = employee;
		this.reports = reports;
		this.category = category;
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

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public float getAmountINR() {
		return amountINR;
	}

	public void setAmountINR(float amountINR) {
		this.amountINR = amountINR;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
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

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public Float getAmountApproved() {
		return amountApproved;
	}

	public void setAmountApproved(Float amountApproved) {
		this.amountApproved = amountApproved;
	}

	public Double getAmountApprovedINR() {
		return amountApprovedINR;
	}

	public void setAmountApprovedINR(Double amountApprovedINR) {
		this.amountApprovedINR = amountApprovedINR;
	}

	public FinanceApprovalStatus getFinanceApprovalStatus() {
		return financeApprovalStatus;
	}

	public void setFinanceApprovalStatus(FinanceApprovalStatus financeApprovalStatus) {
		this.financeApprovalStatus = financeApprovalStatus;
	}

	public ManagerApprovalStatusExpense getManagerApprovalStatusExpense() {
		return managerApprovalStatusExpense;
	}

	public void setManagerApprovalStatusExpense(ManagerApprovalStatusExpense managerApprovalStatusExpense) {
		this.managerApprovalStatusExpense = managerApprovalStatusExpense;
	}

	public Boolean getPotentialDuplicate() {
		return potentialDuplicate;
	}

	public void setPotentialDuplicate(Boolean potentialDuplicate) {
		this.potentialDuplicate = potentialDuplicate;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
