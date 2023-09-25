package com.nineleaps.expense_management_project.dto;

import com.nineleaps.expense_management_project.dto.ExpenseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseDTOTest {

    @Test
    public void testDefaultConstructor() {
        ExpenseDTO expenseDTO = new ExpenseDTO();

        // Verify that the default constructor initializes fields to their default values
        assertEquals(null, expenseDTO.getAmount());
        assertEquals(null, expenseDTO.getDescription());
        assertEquals(null, expenseDTO.getMerchantName());
        assertEquals(null, expenseDTO.getFile());
        assertEquals(null, expenseDTO.getFileName());
        assertEquals(null, expenseDTO.getDate());
    }

    @Test
    public void testParameterizedConstructor() {
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
    public void testGetterAndSetterMethods() {
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
