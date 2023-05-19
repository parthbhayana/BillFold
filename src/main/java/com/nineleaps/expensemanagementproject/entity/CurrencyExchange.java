package com.nineleaps.expensemanagementproject.entity;

import java.math.BigDecimal;

public class CurrencyExchange {

	private int id;
	private String from;
	private String to;
	private BigDecimal exchangeRate;
	
	public CurrencyExchange() {
		// TODO Auto-generated constructor stub
	}

	public CurrencyExchange(int id, String from, String to, BigDecimal exchangeRate) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.exchangeRate = exchangeRate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	
	
}
