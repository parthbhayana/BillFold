package com.nineleaps.expensemanagementproject.service;

public interface ICurrencyConversion {

	double convertCurrency(double amount, String fromCurrency, String toCurrency);

}
