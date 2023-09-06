package com.nineleaps.expensemanagementproject.DTO;

import java.time.LocalDate;

public class ExpenseDTO {

    private Double amount;
    private String currency;
    private String description;
    private String merchantName;
    private byte[] file;

    private String fileName;
    private LocalDate date;

    public ExpenseDTO() {
		
	}

	public ExpenseDTO(Double amount, String currency, String description, String merchantName, byte[] file,
			String fileName, LocalDate date) {
		super();
		this.amount = amount;
		this.currency = currency;
		this.description = description;
		this.merchantName = merchantName;
		this.file = file;
		this.fileName = fileName;
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
    
    

}