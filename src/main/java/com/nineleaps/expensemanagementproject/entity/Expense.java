package com.nineleaps.expensemanagementproject.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

@Getter
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

	@Column(name = "amount", nullable = false)
	private Double amount;

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
	private Double amountApproved;

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
	@Column(name = "file", nullable = true)
	private byte[] file;

	@Column(name = "file_name")
	@ApiModelProperty(hidden = true)
	private String fileName;

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

	public Expense(Long expenseId, String merchantName, LocalDate date, LocalDateTime dateCreated, Double amount,
				   String description, String categoryDescription, Boolean isReported, Boolean isHidden, String reportTitle,
				   Double amountApproved, FinanceApprovalStatus financeApprovalStatus,
				   ManagerApprovalStatusExpense managerApprovalStatusExpense, Boolean potentialDuplicate, byte[] file,
				   String fileName, Employee employee, Reports reports, Category category) {
		super();
		this.expenseId = expenseId;
		this.merchantName = merchantName;
		this.date = date;
		this.dateCreated = dateCreated;
		this.amount = amount;
		this.description = description;
		this.categoryDescription = categoryDescription;
		this.isReported = isReported;
		this.isHidden = isHidden;
		this.reportTitle = reportTitle;
		this.amountApproved = amountApproved;
		this.financeApprovalStatus = financeApprovalStatus;
		this.managerApprovalStatusExpense = managerApprovalStatusExpense;
		this.potentialDuplicate = potentialDuplicate;
		this.file = file;
		this.fileName = fileName;
		this.employee = employee;
		this.reports = reports;
		this.category = category;
	}

	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public void setIsReported(Boolean isReported) {
		this.isReported = isReported;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public void setAmountApproved(Double amountApproved) {
		this.amountApproved = amountApproved;
	}

	public void setFinanceApprovalStatus(FinanceApprovalStatus financeApprovalStatus) {
		this.financeApprovalStatus = financeApprovalStatus;
	}

	public void setManagerApprovalStatusExpense(ManagerApprovalStatusExpense managerApprovalStatusExpense) {
		this.managerApprovalStatusExpense = managerApprovalStatusExpense;
	}

	public void setPotentialDuplicate(Boolean potentialDuplicate) {
		this.potentialDuplicate = potentialDuplicate;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void setReports(Reports reports) {
		this.reports = reports;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}