package com.nineleaps.expensemanagementproject.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reports")
public class Reports {

	@Id
	@Column(name = "reportid", nullable = false)
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private long reportId;

	@Column(name = "report_title")
	private String reportTitle;

	@Column(name = "report_comments")
	private String reportComments;

	@Column(name = "report_status")
	private Boolean reportStatus;

	@JsonIgnore
	@OneToMany(mappedBy = "reports", cascade = CascadeType.ALL)
	private List<Expense> expenseList = new ArrayList<>();

//	public Reports(long reportId, String reportTitle, String reportComments, Boolean reportStatus,
//			List<Expense> expenseList) {
//		super();
//		this.reportId = reportId;
//		this.reportTitle = reportTitle;
//		this.reportComments = reportComments;
//		this.reportStatus = reportStatus;
//		this.expenseList = expenseList;
//	}

	public Reports(long reportId, String reportTitle, String reportComments, Boolean reportStatus) {
		super();
		this.reportId = reportId;
		this.reportTitle = reportTitle;
		this.reportComments = reportComments;
		this.reportStatus = reportStatus;
	}

	public Reports() {
		// TODO Auto-generated constructor stub
	}

	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportComments() {
		return reportComments;
	}

	public void setReportComments(String reportComments) {
		this.reportComments = reportComments;
	}

	public Boolean getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Boolean reportStatus) {
		this.reportStatus = reportStatus;
	}

//	public List<Expense> getExpenseList() {
//		return expenseList;
//	}
//
//	public void setExpenseList(List<Expense> expenseList) {
//		this.expenseList = expenseList;
//	}

}
