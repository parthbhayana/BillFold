package com.nineleaps.expensemanagementproject.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expensemanagementproject.entity.Currency;
import com.nineleaps.expensemanagementproject.service.CurrencyExchangeServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.HttpURLConnection;
import java.net.URL;
import static org.mockito.Mockito.*;

@SpringBootTest
class CurrencyControllerTest {

    private final CurrencyController currencyController = new CurrencyController();

    @Mock
    private CurrencyExchangeServiceImpl currencyExchangeService;

    @Mock
    private HttpURLConnection httpURLConnection;
    @Test
    void testGetCurrency() {
        // Call the controller method
        List<String> result = currencyController.getCurrency();

        // Expected currency list
        List<String> expected = Arrays.stream(Currency.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // Verify the expected result
        assertEquals(expected, result);
    }


//    @Test
//    void testGetExchangeRate() throws IOException, IOException {
//        // Mock input parameters
//        String baseCurrency = "USD";
//        String date = "2023-07-18";
//
//        // Mock the expected result
//        double expected = 1.23;
//
//        // Create an instance of the CurrencyExchangeServiceImpl
//        CurrencyExchangeServiceImpl currencyExchangeService = new CurrencyExchangeServiceImpl();
//
//        // Mock the behavior of the URL and connection
//        URL url = mock(URL.class);
//        when(url.openConnection()).thenReturn(httpURLConnection);
//        when(httpURLConnection.getInputStream()).thenReturn(createMockInputStream("{\"data\": {\"" + date + "\": {\"INR\": " + expected + "}}}"));
//
//        // Mock the behavior of the ObjectMapper
//        ObjectMapper objectMapper = mock(ObjectMapper.class);
//        JsonNode rootNode = mock(JsonNode.class);
//        JsonNode dataNode = mock(JsonNode.class);
//        JsonNode innerDataNode = mock(JsonNode.class);
//
//        when(objectMapper.readTree(anyString())).thenReturn(rootNode);
//        when(rootNode.get("data")).thenReturn(dataNode);
//        when(dataNode.get(date)).thenReturn(innerDataNode);
//        when(innerDataNode.get("INR")).thenReturn(mock(JsonNode.class));
//        when(innerDataNode.get("INR").asDouble()).thenReturn(expected);
//
//        // Set the ObjectMapper mock in the CurrencyExchangeServiceImpl
//        currencyExchangeService.setObjectMapper(objectMapper);
//
//        // Call the service method
//        double result = currencyExchangeService.getExchangeRate(baseCurrency, date);
//
//        // Verify the interactions and expected behavior
//        assertEquals(expected, result);
//        verify(url).openConnection();
//        verify(httpURLConnection).disconnect();
//        verify(httpURLConnection).getInputStream();
//        verify(objectMapper).readTree(anyString());
//        verify(rootNode).get("data");
//        verify(dataNode).get(date);
//        verify(innerDataNode).get("INR");
//        verify(innerDataNode).get("INR").asDouble();
//    }
//    private InputStream createMockInputStream(String input) {
//        return new ByteArrayInputStream(input.getBytes());
//    }
}
