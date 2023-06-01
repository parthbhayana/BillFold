package com.nineleaps.expensemanagementproject.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Reports;

public interface ReportsRepository extends JpaRepository<Reports, Long> {

	Reports getReportByReportId(Long employeeid);

	Optional<Reports> findById(Long id);

	List<Reports> findByManagerEmail(String managerEmail);

	List<Reports> findByDateSubmittedBetween(LocalDate startDate, LocalDate endDate);
	
	List<Reports> findByManagerEmailAndDateSubmittedBetween(String managerEmail, LocalDate startDate , LocalDate endDate);
}
