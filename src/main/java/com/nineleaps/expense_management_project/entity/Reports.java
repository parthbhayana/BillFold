package com.nineleaps.expense_management_project.entity;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModelProperty;

@Getter
@Entity
@Table(name = "reports")
public class Reports {

    @Id
    @GeneratedValue(generator = "custom_serial_number")
    @GenericGenerator(name = "custom_serial_number", strategy = "com.nineleaps.expense_management_project.entity"
            + ".CustomIdGenerator")
    @Column(name = "report_id", nullable = false)
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

    @Column(name = "manager_comments")
    @ApiModelProperty(hidden = true)
    private String managerComments;

    @Column(name = "finance_comments")
    @ApiModelProperty(hidden = true)
    private String financeComments;

    @Column(name = "is_submitted")
    @ApiModelProperty(hidden = true)
    private Boolean isSubmitted = false;

    @Column(name = "employee_mail")
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

    @Column(name = "total_amount")
    @ApiModelProperty(hidden = true)
    private float totalAmount;

    @Column(name = "total_approved_amount")
    @ApiModelProperty(hidden = true)
    private float totalApprovedAmount;

    @Column(name = "is_hidden")
    @ApiModelProperty(hidden = true)
    private Boolean isHidden = false;

    @Column(name = "manager_email")
    @ApiModelProperty(hidden = true)
    private String managerEmail;

    @Column(name = "manager_review_time")
    @ApiModelProperty(hidden = true)
    private String managerReviewTime;

    @Column(name = "finance_approval_status")
    @ApiModelProperty(hidden = true)
    @Enumerated(EnumType.STRING)
    private FinanceApprovalStatus financeApprovalStatus;

    @Column(name = "manager_approval_status")
    @ApiModelProperty(hidden = true)
    @Enumerated(EnumType.STRING)
    private ManagerApprovalStatus managerApprovalStatus;

    @Column(name = "expenses_count")
    @ApiModelProperty(hidden = true)
    private Long expensesCount;



    public Reports(Long reportId, Long employeeId, String employeeName, String officialEmployeeId, String reportTitle,
                   String managerComments, String financeComments ) {
        super();
        this.reportId = reportId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.officialEmployeeId = officialEmployeeId;
        this.reportTitle = reportTitle;
        this.managerComments = managerComments;
        this.financeComments = financeComments;



    }
    public Reports(Boolean isSubmitted, String employeeMail,
                   LocalDate dateSubmitted, LocalDate dateCreated, LocalDate managerActionDate, LocalDate financeActionDate,
                   float totalAmount){
        super();
        this.isSubmitted = isSubmitted;
        this.employeeMail = employeeMail;
        this.dateSubmitted = dateSubmitted;
        this.dateCreated = dateCreated;
        this.managerActionDate = managerActionDate;
        this.financeActionDate = financeActionDate;
        this.totalAmount = totalAmount;
    }

    public Reports(float totalApprovedAmount, Boolean isHidden, String managerEmail,
                   String managerReviewTime, FinanceApprovalStatus financeApprovalStatus,
                   ManagerApprovalStatus managerApprovalStatus, Long expensesCount)
    {
        super();
        this.totalApprovedAmount = totalApprovedAmount;
        this.isHidden = isHidden;
        this.managerEmail = managerEmail;
        this.managerReviewTime = managerReviewTime;
        this.financeApprovalStatus = financeApprovalStatus;
        this.managerApprovalStatus = managerApprovalStatus;
        this.expensesCount = expensesCount;
    }

    public Reports() {

    }


    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setOfficialEmployeeId(String officialEmployeeId) {
        this.officialEmployeeId = officialEmployeeId;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public void setManagerComments(String managerComments) {
        this.managerComments = managerComments;
    }

    public void setFinanceComments(String financeComments) {
        this.financeComments = financeComments;
    }

    public void setIsSubmitted(Boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public void setEmployeeMail(String employeeMail) {
        this.employeeMail = employeeMail;
    }

    public void setDateSubmitted(LocalDate dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setManagerActionDate(LocalDate managerActionDate) {
        this.managerActionDate = managerActionDate;
    }

    public void setFinanceActionDate(LocalDate financeActionDate) {
        this.financeActionDate = financeActionDate;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalApprovedAmount(float totalApprovedAmount) {
        this.totalApprovedAmount = totalApprovedAmount;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public void setManagerReviewTime(String managerReviewTime) {
        this.managerReviewTime = managerReviewTime;
    }

    public void setFinanceApprovalStatus(FinanceApprovalStatus financeApprovalStatus) {
        this.financeApprovalStatus = financeApprovalStatus;
    }

    public void setManagerApprovalStatus(ManagerApprovalStatus managerApprovalStatus) {
        this.managerApprovalStatus = managerApprovalStatus;
    }

    public void setExpensesCount(Long expensesCount) {
        this.expensesCount = expensesCount;
    }

}