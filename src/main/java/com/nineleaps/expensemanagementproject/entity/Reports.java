package com.nineleaps.expensemanagementproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "report")
public class Reports {
	
	@Id
	@Column(name = "reportid", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long reportId;

	@Column(name = "report_title")
	private String reportTitle;
	
	@Column(name = "report_comments")
	private String reportComments;
	
//	reportstatus
}
