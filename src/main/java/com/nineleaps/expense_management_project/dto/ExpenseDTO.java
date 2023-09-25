package com.nineleaps.expense_management_project.dto;

import java.time.LocalDate;

// A Data Transfer Object (DTO) class for representing expense information
public class ExpenseDTO {
	private Double amount; // Expense amount
	private String description; // Expense description
	private String merchantName; // Merchant name
	private byte[] file; // Expense file data (e.g., receipt image)
	private String fileName; // Expense file name
	private LocalDate date; // Expense date

	// Default constructor
	public ExpenseDTO() {

	}

	// Parameterized constructor to initialize expense information
	public ExpenseDTO(Double amount, String description, String merchantName, byte[] file, String fileName,
					  LocalDate date) {
		super();
		this.amount = amount;
		this.description = description;
		this.merchantName = merchantName;
		this.file = file;
		this.fileName = fileName;
		this.date = date;
	}

	// Getter method to retrieve the expense amount
	public Double getAmount() {
		return amount;
	}

	// Setter method to set the expense amount
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	// Getter method to retrieve the expense description
	public String getDescription() {
		return description;
	}

	// Setter method to set the expense description
	public void setDescription(String description) {
		this.description = description;
	}

	// Getter method to retrieve the merchant name
	public String getMerchantName() {
		return merchantName;
	}

	// Setter method to set the merchant name
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	// Getter method to retrieve the expense file data
	public byte[] getFile() {
		return file;
	}

	// Setter method to set the expense file data
	public void setFile(byte[] file) {
		this.file = file;
	}

	// Getter method to retrieve the expense file name
	public String getFileName() {
		return fileName;
	}

	// Setter method to set the expense file name
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// Getter method to retrieve the expense date
	public LocalDate getDate() {
		return date;
	}

	// Setter method to set the expense date
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
