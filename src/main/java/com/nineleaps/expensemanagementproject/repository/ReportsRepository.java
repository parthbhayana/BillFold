package com.nineleaps.expensemanagementproject.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.FinanceApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.ManagerApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.Reports;

public interface ReportsRepository extends JpaRepository<Reports, Long> {

	Reports getReportByReportId(Long employeeid);

	Optional<Reports> findById(Long id);

	List<Reports> getReportsByEmployeeIdAndManagerapprovalstatusAndIsSubmittedAndIsHidden(Long employeeId,
			ManagerApprovalStatus managerStatus, Boolean a, Boolean b);

	List<Reports> getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Long employeeId, Boolean a, Boolean b);

	List<Reports> findByManagerEmail(String managerEmail);

	List<Reports> findByManagerEmailAndManagerapprovalstatusAndIsSubmittedAndIsHidden(String managerEmail,
			ManagerApprovalStatus managerStatus, Boolean a, Boolean b);

	List<Reports> findByManagerEmailAndIsSubmittedAndIsHidden(String managerEmail, Boolean a, Boolean b);

	List<Reports> findByManagerapprovalstatusAndFinanceapprovalstatusAndIsSubmittedAndIsHidden(
			ManagerApprovalStatus managerStatus, FinanceApprovalStatus financeStatus, Boolean a, Boolean b);

	List<Reports> findByManagerEmailAndDateSubmittedBetween(String managerEmail, LocalDate startDate,
			LocalDate endDate);

	List<Reports> findByDateSubmittedBetween(LocalDate startDate, LocalDate endDate);

}
