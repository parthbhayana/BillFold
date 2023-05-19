package com.nineleaps.expensemanagementproject.dto;

public class FinanceComments {
	String financeComments;
	Long id;

	public FinanceComments(String financeComments, Long id) {
		super();
		this.financeComments = financeComments;
		this.id = id;
	}

	public FinanceComments() {
		super();
	}

	public String getFinanceComments() {
		return financeComments;
	}

	public void setFinanceComments(String financeComments) {
		this.financeComments = financeComments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}