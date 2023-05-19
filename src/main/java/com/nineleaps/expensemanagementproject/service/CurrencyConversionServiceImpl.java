package com.nineleaps.expensemanagementproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyConversionServiceImpl implements ICurrencyConversion{

	@Override
	public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.exchangeratesapi.io/latest?base=" + fromCurrency + "&symbols=" + toCurrency;
        CurrencyConversionResponse response = restTemplate.getForObject(url, CurrencyConversionResponse.class);
        double exchangeRate = response.getRates().get(toCurrency);
        return amount * exchangeRate;
    }

}
