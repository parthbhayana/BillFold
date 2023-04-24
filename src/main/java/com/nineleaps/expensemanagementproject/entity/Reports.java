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

	@Column(name = "report_comments",nullable = true)
//	@ApiModelProperty(hidden = true)
	private String reportComments;

	@Column(name = "is_aprooved",nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isAprooved=false;

	@Column(name = "is_submitted",nullable = true)
	@ApiModelProperty(hidden = true)
	private Boolean isSubmitted=false;

	@JsonIgnore
	@OneToMany(mappedBy = "reports", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
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

	public Reports(long reportId, String reportTitle, String reportDescription, String reportComments,
			Boolean isAprooved, Boolean isSubmitted) {
		super();
		this.reportId = reportId;
		this.reportTitle = reportTitle;
		this.reportDescription = reportDescription;
		this.reportComments = reportComments;
		this.isAprooved = isAprooved;
		this.isSubmitted = isSubmitted;
	}

	public Reports() {
		// TODO Auto-generated constructor stub
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

	public String getReportComments() {
		return reportComments;
	}

	public void setReportComments(String reportComments) {
		this.reportComments = reportComments;
	}

	public Boolean getIsAprooved() {
		return isAprooved;
	}

	public void setIsAprooved(Boolean isAprooved) {
		this.isAprooved = isAprooved;
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

//	public List<Expense> getExpenseList() {
//		return expenseList;
//	}
//
//	public void setExpenseList(List<Expense> expenseList) {
//		this.expenseList = expenseList;
//	}

}
