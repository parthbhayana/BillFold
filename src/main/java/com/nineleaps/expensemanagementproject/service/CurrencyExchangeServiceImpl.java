package com.nineleaps.expensemanagementproject.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeServiceImpl implements ICurrencyExchange {

    @Override
    public double getExchangeRate(String baseCurrency, String date) {

        String baseURL = "https://api.freecurrencyapi.com/v1/historical?apikey=0bM6MPvQNZk9vZmlzY3t8MCs30rcROub0soX77mB&date_from=" + date + "&date_to=" + date + "&currencies=INR&base_currency=";
        String apiURL = baseURL + baseCurrency;
        System.out.println(apiURL);
        double exchangeValue = 0;
        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            String json = response.toString();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(json);
                JsonNode dataNode = rootNode.get("data");
                if (dataNode != null && dataNode.has(date)) {
                    JsonNode innerDataNode = dataNode.get(date);
                    if (innerDataNode.has("INR")) {
                        exchangeValue = innerDataNode.get("INR").asDouble();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exchangeValue;
    }
}
