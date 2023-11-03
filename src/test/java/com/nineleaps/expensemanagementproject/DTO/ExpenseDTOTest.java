package com.nineleaps.expensemanagementproject.DTO;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExpenseDTOTest {

    @Test
    void testDefaultConstructor() {
        ExpenseDTO expenseDTO = new ExpenseDTO();

        // Verify that the default constructor initializes fields to their default values
        assertNull(expenseDTO.getAmount());
        assertNull(expenseDTO.getDescription());
        assertNull(expenseDTO.getMerchantName());
        assertNull(expenseDTO.getFile());
        assertNull(expenseDTO.getFileName());
        assertNull(expenseDTO.getDate());
    }

    @Test
    void testParameterizedConstructor() {
        Double amount = 100.0;
        String description = "Expense description";
        String merchantName = "Merchant Inc.";
        byte[] file = new byte[]{1, 2, 3};
        String fileName = "receipt.jpg";
        LocalDate date = LocalDate.of(2023, 9, 21);

        ExpenseDTO expenseDTO = new ExpenseDTO(amount, description, merchantName, file, fileName, date);

        // Verify that the parameterized constructor initializes fields correctly
        assertEquals(amount, expenseDTO.getAmount());
        assertEquals(description, expenseDTO.getDescription());
        assertEquals(merchantName, expenseDTO.getMerchantName());
        assertEquals(file, expenseDTO.getFile());
        assertEquals(fileName, expenseDTO.getFileName());
        assertEquals(date, expenseDTO.getDate());
    }

    @Test
    void testGetterAndSetterMethods() {
        ExpenseDTO expenseDTO = new ExpenseDTO();

        // Set values using setter methods
        Double amount = 200.0;
        expenseDTO.setAmount(amount);
        String description = "Updated description";
        expenseDTO.setDescription(description);
        String merchantName = "Updated Merchant Inc.";
        expenseDTO.setMerchantName(merchantName);
        byte[] file = new byte[]{4, 5, 6};
        expenseDTO.setFile(file);
        String fileName = "updated_receipt.jpg";
        expenseDTO.setFileName(fileName);
        LocalDate date = LocalDate.of(2023, 9, 22);
        expenseDTO.setDate(date);

        // Verify that the getter methods return the expected values
        assertEquals(amount, expenseDTO.getAmount());
        assertEquals(description, expenseDTO.getDescription());
        assertEquals(merchantName, expenseDTO.getMerchantName());
        assertEquals(file, expenseDTO.getFile());
        assertEquals(fileName, expenseDTO.getFileName());
        assertEquals(date, expenseDTO.getDate());
    }
}