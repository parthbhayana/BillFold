package com.nineleaps.expensemanagementproject.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "currency")
public class CurrencyConversion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	@Column(name = "currency_id", nullable = false)
	private int id;

	@Column(name = "client_currency")
	private String from;

	@Column(name = "to_currency_INR")
	private String to;

	@Column(name = "quantity")
	private BigDecimal quantity;

	@Column(name = "exchange_rate")
	private BigDecimal exchangeRate;

	@Column(name = "calculated_amount")
	private BigDecimal calculatedAmount;

	public CurrencyConversion() {
		// TODO Auto-generated constructor stub
	}

	public CurrencyConversion(int id, String from, String to, BigDecimal quantity, BigDecimal exchangeRate,
			BigDecimal totalAmount) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.quantity = quantity;
		this.exchangeRate = exchangeRate;
		this.calculatedAmount = totalAmount;
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

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getTotalAmount() {
		return calculatedAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.calculatedAmount = totalAmount;
	}

}
