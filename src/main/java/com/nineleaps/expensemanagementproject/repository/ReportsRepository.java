package com.nineleaps.expensemanagementproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nineleaps.expensemanagementproject.entity.Reports;

public interface ReportsRepository extends JpaRepository<Reports, Long>{

	Reports getReportByReportId(Long employeeid);
	
	
}
