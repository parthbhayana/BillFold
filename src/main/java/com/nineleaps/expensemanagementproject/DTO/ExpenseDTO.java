package com.nineleaps.expensemanagementproject.DTO;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ExpenseDTO {

	private Double amount;
	private String description;
	private String merchantName;
	private byte[] file;
	private String fileName;
	private LocalDate date;

	public ExpenseDTO() {

	}

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

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}