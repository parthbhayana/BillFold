package com.nineleaps.expensemanagementproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.net.URL;

public interface ICurrencyExchange {

	public double getExchangeRate(String baseCurrency,String date);
}
