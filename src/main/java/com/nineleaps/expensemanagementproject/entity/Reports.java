package com.nineleaps.expensemanagementproject.entity;

import java.sql.Date;
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

	@Column(name = "manager_email")
	@ApiModelProperty(hidden = true)
	private String managerEmail;

	@Column(name = "date_submitted")
	private Date dateSubmitted;

	@Column(name = "date_created")
	private Date dateCreated;

	@Column(name = "total_amount")
	@ApiModelProperty(hidden = true)
	private Long totalAmount;

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
	private List<Expense> expenseList = new ArrayList<>();

	public Reports() {
		// TODO Auto-generated constructor stub
	}

	public Reports(Long reportId, String reportTitle, String reportDescription, String managerComments,
			String financeComments, Boolean isSubmitted, String managerEmail, Date dateSubmitted, Date dateCreated,
			Long totalAmount, FinanceApprovalStatus financeapprovalstatus,
			ManagerApprovalStatus managerapprovalstatus) {
		super();
		this.reportId = reportId;
		this.reportTitle = reportTitle;
		this.reportDescription = reportDescription;
		this.managerComments = managerComments;
		this.financeComments = financeComments;
		this.isSubmitted = isSubmitted;
		this.managerEmail = managerEmail;
		this.dateSubmitted = dateSubmitted;
		this.dateCreated = dateCreated;
		this.totalAmount = totalAmount;
		this.financeapprovalstatus = financeapprovalstatus;
		this.managerapprovalstatus = managerapprovalstatus;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public Boolean getIsSubmitted() {
		return isSubmitted;
	}

	public void setIsSubmitted(Boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
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

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float amt) {
		this.totalAmount = (long) amt;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
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

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

//	public List<Expense> getExpenseList() {
//		return expenseList;
//	}
//
//	public void setExpenseList(List<Expense> expenseList) {
//		this.expenseList = expenseList;
//	}

}
