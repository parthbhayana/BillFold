package com.nineleaps.expensemanagementproject.DTO;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseDTOTest {

    @Test
    void testExpenseDTOConstructor() {
        Double amount = (double) 1000L;
        String currency = "USD";
        String description = "Test Expense";
        String merchantName = "Test Merchant";
        String fileName = "Test File";
        byte[] file = new byte[]{1, 2, 3};
        LocalDate date = LocalDate.now();

        ExpenseDTO expenseDTO = new ExpenseDTO(amount, currency, description, merchantName, file, fileName, date);

        assertEquals(amount, expenseDTO.getAmount());
        assertEquals(currency, expenseDTO.getCurrency());
        assertEquals(description, expenseDTO.getDescription());
        assertEquals(merchantName, expenseDTO.getMerchantName());
        assertEquals(fileName, expenseDTO.getFileName());
        assertArrayEquals(file, expenseDTO.getFile());
        assertEquals(date, expenseDTO.getDate());
    }

    @Test
    void testGetSetMethods() {
        ExpenseDTO expenseDTO = new ExpenseDTO();

        assertNull(expenseDTO.getAmount());
        assertNull(expenseDTO.getCurrency());
        assertNull(expenseDTO.getDescription());
        assertNull(expenseDTO.getMerchantName());
        assertNull(expenseDTO.getFile());
        assertNull(expenseDTO.getFileName());
        assertNull(expenseDTO.getDate());

        Double amount = (double) 1000L;
        String currency = "USD";
        String description = "Test Expense";
        String merchantName = "Test Merchant";
        String fileName = "Test File";
        byte[] file = new byte[]{1, 2, 3};
        LocalDate date = LocalDate.now();

        expenseDTO.setAmount(amount);
        expenseDTO.setCurrency(currency);
        expenseDTO.setDescription(description);
        expenseDTO.setMerchantName(merchantName);
        expenseDTO.setFile(file);
        expenseDTO.setDate(date);

        assertEquals(amount, expenseDTO.getAmount());
        assertEquals(currency, expenseDTO.getCurrency());
        assertEquals(description, expenseDTO.getDescription());
        assertEquals(merchantName, expenseDTO.getMerchantName());
        assertArrayEquals(file, expenseDTO.getFile());
        assertEquals(date, expenseDTO.getDate());
    }
}
