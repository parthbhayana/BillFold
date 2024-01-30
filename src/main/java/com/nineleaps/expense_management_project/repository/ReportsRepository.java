package com.nineleaps.expense_management_project.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nineleaps.expense_management_project.entity.FinanceApprovalStatus;
import com.nineleaps.expense_management_project.entity.ManagerApprovalStatus;
import com.nineleaps.expense_management_project.entity.Reports;

public interface ReportsRepository extends JpaRepository<Reports, Long> {


    Reports getReportByReportId(Long reportId);

    Optional<Reports> findById(Long reportId);

    List<Reports> getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Long employeeId,
                                                                                          ManagerApprovalStatus managerStatus,
                                                                                          Boolean a, Boolean b);

    List<Reports> getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Long employeeId, Boolean a, Boolean b);


    List<Reports> findByManagerEmailAndManagerApprovalStatusAndIsSubmittedAndIsHidden(String managerEmail,
                                                                                      ManagerApprovalStatus managerStatus,
                                                                                      Boolean a, Boolean b);


    List<Reports> findByManagerApprovalStatusAndFinanceApprovalStatusAndIsSubmittedAndIsHidden(
            ManagerApprovalStatus managerStatus, FinanceApprovalStatus financeStatus, Boolean a, Boolean b);


    List<Reports> findByManagerEmailAndDateSubmittedBetweenAndManagerApprovalStatusAndIsSubmittedAndIsHidden(
            String managerEmail, LocalDate startDate, LocalDate endDate, ManagerApprovalStatus managerStatus, Boolean a,
            Boolean b);

    List<Reports> findByDateSubmittedBetween(LocalDate startDate, LocalDate endDate);


    List<Reports> getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(Long employeeId,
                                                                            ManagerApprovalStatus managerApprovalStatus,
                                                                            boolean b);


    List<Reports> findByManagerEmailAndManagerApprovalStatusAndIsHidden(String managerEmail,
                                                                        ManagerApprovalStatus managerApprovalStatus,
                                                                        boolean b);


    List<Reports> findByfinanceApprovalStatus(FinanceApprovalStatus financeApprovalStatus);

    List<Reports> findByDateSubmittedBetweenAndFinanceApprovalStatus(LocalDate startDate, LocalDate endDate,
                                                                     FinanceApprovalStatus financeApprovalStatus);

    Page<Reports> getReportsByEmployeeIdAndIsSubmittedAndIsHidden(Long employeeId, boolean isSubmitted, boolean isHidden, Pageable pageable);

    Page<Reports> getReportsByEmployeeIdAndManagerApprovalStatusAndIsSubmittedAndIsHidden(Long employeeId, ManagerApprovalStatus managerApprovalStatus, boolean isSubmitted, boolean isHidden, Pageable pageable);

    Page<Reports> getReportsByEmployeeIdAndManagerApprovalStatusAndIsHidden(Long employeeId, ManagerApprovalStatus managerApprovalStatus, boolean isHidden, Pageable pageable);




}