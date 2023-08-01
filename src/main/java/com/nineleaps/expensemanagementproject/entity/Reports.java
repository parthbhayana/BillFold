package com.nineleaps.expensemanagementproject.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "reports")
public class Reports {

	@Id
	@Column(name = "report_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	private Long reportId;

	@Column(name = "employee_id")
	@ApiModelProperty(hidden = true)
	private Long employeeId;

	@Column(name = "employee_name")
	@ApiModelProperty(hidden = true)
	private String employeeName;

	@Column(name = "official_employee_id")
	@ApiModelProperty(hidden = true)
	private String officialEmployeeId;

	@Column(name = "report_title", nullable = false)
	private String reportTitle;

	@Column(name = "report_description", nullable = true)
	private String reportDescription;

	@Column(name = "manager_comments")
	@ApiModelProperty(hidden = true)
	private String managerComments;

	@Column(name = "finance_comments")
	@ApiModelProperty(hidden = true)
	private String financeComments;

	@Column(name = "is_submitted", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isSubmitted = false;

	@Column(name = "employee_mail", nullable = true)
	@ApiModelProperty(hidden = true)
	private String employeeMail;

	@Column(name = "date_submitted")
	@ApiModelProperty(hidden = true)
	private LocalDate dateSubmitted;

	@Column(name = "date_created")
	@ApiModelProperty(hidden = true)
	private LocalDate dateCreated;

	@Column(name = "date_manager_action")
	@ApiModelProperty(hidden = true)
	private LocalDate managerActionDate;

	@Column(name = "date_finance_action")
	@ApiModelProperty(hidden = true)
	private LocalDate financeActionDate;

	@Column(name = "currency")
	@ApiModelProperty(hidden = true)
	private String currency;

	@Column(name = "total_amount_INR")
	@ApiModelProperty(hidden = true)
	private float totalAmountINR;

	@Column(name = "total_approved_amount_INR")
	@ApiModelProperty(hidden = true)
	private float totalApprovedAmountINR;

	@Column(name = "total_amount_currency")
	@ApiModelProperty(hidden = true)
	private float totalAmountCurrency;

	@Column(name = "total_approved_amount_currency")
	@ApiModelProperty(hidden = true)
	private float totalApprovedAmountCurrency;

	@Column(name = "is_hidden", nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isHidden = false;

	@Column(name = "manager_email")
	@ApiModelProperty(hidden = true)
	private String managerEmail;

	@Column(name = "manager_review_time")
	@ApiModelProperty(hidden = true)
	private String managerReviewTime;

	@Column(name = "finance_approval_status", nullable = true)
	@ApiModelProperty(hidden = true)
	@Enumerated(EnumType.STRING)
	private FinanceApprovalStatus financeapprovalstatus;

	@Column(name = "manager_approval_status", nullable = true)
	@ApiModelProperty(hidden = true)
	@Enumerated(EnumType.STRING)
	private ManagerApprovalStatus managerapprovalstatus;

	@JsonIgnore
	@OneToMany(mappedBy = "reports", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private final List<Expense> expenseList = new ArrayList<>();

	public Reports() {

	}

	public Reports(Long reportId, Long employeeId, String employeeName, String officialEmployeeId, String reportTitle,
			String reportDescription, String managerComments, String financeComments, Boolean isSubmitted,
			String employeeMail, LocalDate dateSubmitted, LocalDate dateCreated, LocalDate managerActionDate,
			LocalDate financeActionDate, String currency, float totalAmountINR, float totalApprovedAmountINR,
			float totalAmountCurrency, float totalApprovedAmountCurrency, Boolean isHidden, String managerEmail,
			String managerReviewTime, FinanceApprovalStatus financeapprovalstatus,
			ManagerApprovalStatus managerapprovalstatus) {
		super();
		this.reportId = reportId;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.officialEmployeeId = officialEmployeeId;
		this.reportTitle = reportTitle;
		this.reportDescription = reportDescription;
		this.managerComments = managerComments;
		this.financeComments = financeComments;
		this.isSubmitted = isSubmitted;
		this.employeeMail = employeeMail;
		this.dateSubmitted = dateSubmitted;
		this.dateCreated = dateCreated;
		this.managerActionDate = managerActionDate;
		this.financeActionDate = financeActionDate;
		this.currency = currency;
		this.totalAmountINR = totalAmountINR;
		this.totalApprovedAmountINR = totalApprovedAmountINR;
		this.totalAmountCurrency = totalAmountCurrency;
		this.totalApprovedAmountCurrency = totalApprovedAmountCurrency;
		this.isHidden = isHidden;
		this.managerEmail = managerEmail;
		this.managerReviewTime = managerReviewTime;
		this.financeapprovalstatus = financeapprovalstatus;
		this.managerapprovalstatus = managerapprovalstatus;

	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getOfficialEmployeeId() {
		return officialEmployeeId;
	}

	public void setOfficialEmployeeId(String officialEmployeeId) {
		this.officialEmployeeId = officialEmployeeId;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportDescription() {
		return reportDescription;
	}

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	public String getFinanceComments() {
		return financeComments;
	}

	public void setFinanceComments(String financeComments) {
		this.financeComments = financeComments;
	}

	public Boolean getIsSubmitted() {
		return isSubmitted;
	}

	public void setIsSubmitted(Boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	public String getEmployeeMail() {
		return employeeMail;
	}

	public void setEmployeeMail(String employeeMail) {
		this.employeeMail = employeeMail;
	}

	public LocalDate getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(LocalDate dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDate getManagerActionDate() {
		return managerActionDate;
	}

	public void setManagerActionDate(LocalDate managerActionDate) {
		this.managerActionDate = managerActionDate;
	}

	public LocalDate getFinanceActionDate() {
		return financeActionDate;
	}

	public void setFinanceActionDate(LocalDate financeActionDate) {
		this.financeActionDate = financeActionDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public float getTotalAmountINR() {
		return totalAmountINR;
	}

	public void setTotalAmountINR(float totalAmountINR) {
		this.totalAmountINR = totalAmountINR;
	}

	public float getTotalApprovedAmountINR() {
		return totalApprovedAmountINR;
	}

	public void setTotalApprovedAmountINR(float totalApprovedAmountINR) {
		this.totalApprovedAmountINR = totalApprovedAmountINR;
	}

	public float getTotalAmountCurrency() {
		return totalAmountCurrency;
	}

	public void setTotalAmountCurrency(float totalAmountCurrency) {
		this.totalAmountCurrency = totalAmountCurrency;
	}

	public float getTotalApprovedAmountCurrency() {
		return totalApprovedAmountCurrency;
	}

	public void setTotalApprovedAmountCurrency(float totalApprovedAmountCurrency) {
		this.totalApprovedAmountCurrency = totalApprovedAmountCurrency;
	}

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public String getManagerReviewTime() {
		return managerReviewTime;
	}

	public void setManagerReviewTime(String managerReviewTime) {
		this.managerReviewTime = managerReviewTime;
	}

	public FinanceApprovalStatus getFinanceapprovalstatus() {
		return financeapprovalstatus;
	}

	public void setFinanceApprovalStatus(FinanceApprovalStatus financeapprovalstatus) {
		this.financeapprovalstatus = financeapprovalstatus;
	}

	public ManagerApprovalStatus getManagerapprovalstatus() {
		return managerapprovalstatus;
	}

	public void setManagerApprovalStatus(ManagerApprovalStatus managerapprovalstatus) {
		this.managerapprovalstatus = managerapprovalstatus;
	}

}
