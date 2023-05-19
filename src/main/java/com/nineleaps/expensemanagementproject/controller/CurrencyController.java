package com.nineleaps.expensemanagementproject.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineleaps.expensemanagementproject.service.CurrencyExchangeServiceImpl;

@RestController
public class CurrencyController {

	@Autowired
	CurrencyExchangeServiceImpl currencyExchange;

	@PostMapping("/getexchangerate/{baseCurrency}")
	public double getExchangeRate(@PathVariable String baseCurrency) throws IOException {
		return currencyExchange.getExchangeRate(baseCurrency);
	}

}
