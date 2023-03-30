package com.nineleaps.expensemanagementproject.entity;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="expense")
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="expenseId",nullable=false)
	private Long expenseId;
	
	@Column(name="merchantName",nullable=false)
	private String merchantName;
	
	@Column(name="date",nullable=false)
	private Date date;
	
	@Column(name="amount",nullable=false)
	private Long amount;
	
	@Column(name="description",nullable=false)
	private String description;
	
	@Column(name="attachment")
	@Enumerated(EnumType.STRING)
	private Category category;
	
	
	public Expense() {
		
	}

	

	public Expense(Long expenseId, String merchantName, Date date, Long amount, String description, Category category) {
		super();
		this.expenseId = expenseId;
		this.merchantName = merchantName;
		this.date = date;
		this.amount = amount;
		this.description = description;
		this.category = category;
	}

	public Long getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}

	
