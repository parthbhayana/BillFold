package com.nineleaps.expensemanagementproject.DTO;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseDTOTest {

    @Test
    void testExpenseDTOConstructor() {
        Long amount = 1000L;
        String currency = "USD";
        String description = "Test Expense";
        String merchantName = "Test Merchant";
        byte[] supportingDocuments = new byte[]{1, 2, 3};
        LocalDate date = LocalDate.now();

        ExpenseDTO expenseDTO = new ExpenseDTO(amount, currency, description, merchantName, supportingDocuments, date);

        assertEquals(amount, expenseDTO.getAmount());
        assertEquals(currency, expenseDTO.getCurrency());
        assertEquals(description, expenseDTO.getDescription());
        assertEquals(merchantName, expenseDTO.getMerchantName());
        assertArrayEquals(supportingDocuments, expenseDTO.getSupportingDocuments());
        assertEquals(date, expenseDTO.getDate());
    }

    @Test
    void testGetSetMethods() {
        ExpenseDTO expenseDTO = new ExpenseDTO();

        assertNull(expenseDTO.getAmount());
        assertNull(expenseDTO.getCurrency());
        assertNull(expenseDTO.getDescription());
        assertNull(expenseDTO.getMerchantName());
        assertNull(expenseDTO.getSupportingDocuments());
        assertNull(expenseDTO.getDate());

        Long amount = 1000L;
        String currency = "USD";
        String description = "Test Expense";
        String merchantName = "Test Merchant";
        byte[] supportingDocuments = new byte[]{1, 2, 3};
        LocalDate date = LocalDate.now();

        expenseDTO.setAmount(amount);
        expenseDTO.setCurrency(currency);
        expenseDTO.setDescription(description);
        expenseDTO.setMerchantName(merchantName);
        expenseDTO.setSupportingDocuments(supportingDocuments);
        expenseDTO.setDate(date);

        assertEquals(amount, expenseDTO.getAmount());
        assertEquals(currency, expenseDTO.getCurrency());
        assertEquals(description, expenseDTO.getDescription());
        assertEquals(merchantName, expenseDTO.getMerchantName());
        assertArrayEquals(supportingDocuments, expenseDTO.getSupportingDocuments());
        assertEquals(date, expenseDTO.getDate());
    }
}
