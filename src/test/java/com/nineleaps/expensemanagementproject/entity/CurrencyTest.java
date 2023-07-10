package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void testValues() {
        Currency[] currencies = Currency.values();

        assertNotNull(currencies);
        assertEquals(33, currencies.length);

        assertEquals(Currency.EUR, currencies[0]);
        assertEquals(Currency.USD, currencies[1]);

        assertEquals(Currency.ZAR, currencies[32]);
    }

    @Test
    void testValueOf() {
        Currency currency = Currency.valueOf("EUR");
        assertEquals(Currency.EUR, currency);

        currency = Currency.valueOf("USD");
        assertEquals(Currency.USD, currency);

        currency = Currency.valueOf("ZAR");
        assertEquals(Currency.ZAR, currency);
    }
}