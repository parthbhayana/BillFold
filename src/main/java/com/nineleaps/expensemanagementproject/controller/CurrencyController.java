package com.nineleaps.expensemanagementproject.controller;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expensemanagementproject.entity.Currency;
import com.nineleaps.expensemanagementproject.service.CurrencyExchangeServiceImpl;

@RestController
public class CurrencyController {

	@Autowired
	CurrencyExchangeServiceImpl currencyExchange;

	@GetMapping("/currencyList")
	public List<String> getCurrency() {
		return Arrays.stream(Currency.values()).map(Enum::name).collect(Collectors.toList());
	}

	@PostMapping("/getExchangeRate/{baseCurrency}")
	public double getExchangeRate(@RequestParam String baseCurrency, @RequestParam String date)  {
		return currencyExchange.getExchangeRate(baseCurrency,date);
	}

	@PostMapping("/getExchangeAmountINR/{baseCurrency}")
	public Double getExchangeAmountINR(@RequestParam Double amount, @RequestParam String baseCurrency,
									   @RequestParam String date)  {
		return currencyExchange.getExchangeAmountINR(amount,baseCurrency,date);
	}
}
