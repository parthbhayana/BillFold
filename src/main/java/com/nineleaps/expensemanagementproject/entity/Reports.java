package com.nineleaps.expensemanagementproject.entity;

import java.time.LocalDateTime;
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
import javax.persistence.Lob;
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

	@Column(name = "report_description")
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
	private LocalDateTime dateSubmitted;

	@Column(name = "date_created")
	@ApiModelProperty(hidden = true)
	private LocalDateTime dateCreated;

	@Column(name = "date_manager_action")
	@ApiModelProperty(hidden = true)
	private LocalDateTime managerActionDate;

	@Column(name = "date_finance_action")
	@ApiModelProperty(hidden = true)
	private LocalDateTime financeActionDate;

	@Column(name = "currency")
	@ApiModelProperty(hidden = true)
	private String currency;

	@Column(name = "total_amount_INR")
	@ApiModelProperty(hidden = true)
	private float totalAmountINR;

	@Column(name = "total_amount_currency")
	@ApiModelProperty(hidden = true)
	private float totalAmountCurrency;

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
	private FinanceApprovalStatus financeapprovalstatus;// = FinanceApprovalStatus.PENDING;

	@Column(name = "manager_approval_status", nullable = true)
	@ApiModelProperty(hidden = true)
	@Enumerated(EnumType.STRING)
	private ManagerApprovalStatus managerapprovalstatus;// = ManagerApprovalStatus.PENDING;

	@Lob
	@Column(name = "pdf_file", nullable = true)
	@ApiModelProperty(hidden = true)
	private byte[] pdfFile;

	@JsonIgnore
	@OneToMany(mappedBy = "reports", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	private final List<Expense> expenseList = new ArrayList<>();

	public Reports() {

	}

	public Reports(Long reportId, Long employeeId, String employeeName, String officialEmployeeId, String reportTitle,
			String reportDescription, String managerComments, String financeComments, Boolean isSubmitted,
			String employeeMail, LocalDateTime dateSubmitted, LocalDateTime dateCreated,
			LocalDateTime managerActionDate, LocalDateTime financeActionDate, String currency, float totalAmountINR,
			float totalAmountCurrency, Boolean isHidden, String managerEmail, String managerReviewTime,
			FinanceApprovalStatus financeapprovalstatus, ManagerApprovalStatus managerapprovalstatus, byte[] pdfFile) {
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
		this.totalAmountCurrency = totalAmountCurrency;
		this.isHidden = isHidden;
		this.managerEmail = managerEmail;
		this.managerReviewTime = managerReviewTime;
		this.financeapprovalstatus = financeapprovalstatus;
		this.managerapprovalstatus = managerapprovalstatus;
		this.pdfFile = pdfFile;
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

	public Boolean getSubmitted() {
		return isSubmitted;
	}

	public void setSubmitted(Boolean submitted) {
		isSubmitted = submitted;
	}

	public Boolean getHidden() {
		return isHidden;
	}

	public void setHidden(Boolean hidden) {
		isHidden = hidden;
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

	public LocalDateTime getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(LocalDateTime dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getManagerActionDate() {
		return managerActionDate;
	}

	public void setManagerActionDate(LocalDateTime managerActionDate) {
		this.managerActionDate = managerActionDate;
	}

	public LocalDateTime getFinanceActionDate() {
		return financeActionDate;
	}

	public void setFinanceActionDate(LocalDateTime financeActionDate) {
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

	public float getTotalAmountCurrency() {
		return totalAmountCurrency;
	}

	public void setTotalAmountCurrency(float totalAmountCurrency) {
		this.totalAmountCurrency = totalAmountCurrency;
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

	public void setFinanceapprovalstatus(FinanceApprovalStatus financeapprovalstatus) {
		this.financeapprovalstatus = financeapprovalstatus;
	}

	public ManagerApprovalStatus getManagerapprovalstatus() {
		return managerapprovalstatus;
	}

	public void setManagerapprovalstatus(ManagerApprovalStatus managerapprovalstatus) {
		this.managerapprovalstatus = managerapprovalstatus;
	}

	public byte[] getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(byte[] pdfFile) {
		this.pdfFile = pdfFile;
	}

}