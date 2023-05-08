package com.nineleaps.expensemanagementproject.dto;

public class ManagerComments {
	String managerComments;
	Long id;

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ManagerComments(String managerComments, Long id) {
		super();
		this.managerComments = managerComments;
		this.id = id;
	}

	public ManagerComments() {
		super();
		// TODO Auto-generated constructor stub
	}

}