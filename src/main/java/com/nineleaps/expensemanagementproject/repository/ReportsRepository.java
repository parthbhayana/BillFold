package com.nineleaps.expensemanagementproject.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nineleaps.expensemanagementproject.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.FinanceApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.ManagerApprovalStatus;
import com.nineleaps.expensemanagementproject.entity.Reports;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public interface ReportsRepository extends JpaRepository<Reports, Long> {

	@PersistenceContext
	EntityManager entityManager = null;

	public default Long findLatestReportSerialNumber() {
		String queryString = "SELECT MAX(CAST(SUBSTRING(r.reportId, 9) AS DECIMAL)) FROM Report r";
		Query query = entityManager.createQuery(queryString);
		Long latestSerialNumber = (Long) query.getSingleResult();
		return latestSerialNumber != null ? latestSerialNumber + 1 : 1L;
	}

	Reports getReportByReportId(Long reportId);

	Optional<Reports> findById(Long reportId);

	List<Reports> getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Long employeeId,
																						  ManagerApprovalStatus managerStatus, Boolean a, Boolean b);

	List<Reports> getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Long employeeId, Boolean a, Boolean b);

	List<Reports> findByManagerEmail(String managerEmail);

	List<Reports> findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(String managerEmail,
																					  ManagerApprovalStatus managerStatus, Boolean a, Boolean b);

	List<Reports> findByManagerEmailAndIsSubmittedAndIsHidden(String managerEmail, Boolean a, Boolean b);

	List<Reports> findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
			ManagerApprovalStatus managerStatus, FinanceApprovalStatus financeStatus, Boolean a, Boolean b);


	List<Reports> findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(String managerEmail, LocalDate startDate,
																											 LocalDate endDate, ManagerApprovalStatus managerStatus, Boolean a, Boolean b);

	List<Reports> findByDateSubmittedBetween(LocalDate startDate, LocalDate endDate);


	List<Reports> findBymanagerApprovalStatus(ManagerApprovalStatus managerApprovalStatus);

	List<Reports> getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(Long employeeId,
																			ManagerApprovalStatus managerApprovalStatus, boolean b);


	List<Reports> findByManagerEmailAndManagerApprovalStatusAndIsHidden(String managerEmail,
																		ManagerApprovalStatus managerApprovalStatus, boolean b);


	List<Reports> findByfinanceApprovalStatus(FinanceApprovalStatus financeApprovalStatus);

	List<Reports> findByDateSubmittedBetweenAndFinanceApprovalStatus(LocalDate startDate, LocalDate endDate,
																	 FinanceApprovalStatus financeApprovalStatus);


}