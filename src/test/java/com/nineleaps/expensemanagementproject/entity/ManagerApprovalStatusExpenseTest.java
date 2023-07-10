package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerApprovalStatusExpenseTest {

    @Test
    void values() {
        ManagerApprovalStatusExpense[] statuses = ManagerApprovalStatusExpense.values();
        assertEquals(4, statuses.length);
        assertArrayEquals(new ManagerApprovalStatusExpense[] {
                ManagerApprovalStatusExpense.APPROVED,
                ManagerApprovalStatusExpense.REJECTED,
                ManagerApprovalStatusExpense.PENDING,
                ManagerApprovalStatusExpense.PARTIALLY_APPROVED
        }, statuses);
    }

    @Test
    void valueOf() {
        ManagerApprovalStatusExpense approved = ManagerApprovalStatusExpense.valueOf("APPROVED");
        assertEquals(ManagerApprovalStatusExpense.APPROVED, approved);

        ManagerApprovalStatusExpense rejected = ManagerApprovalStatusExpense.valueOf("REJECTED");
        assertEquals(ManagerApprovalStatusExpense.REJECTED, rejected);

        ManagerApprovalStatusExpense pending = ManagerApprovalStatusExpense.valueOf("PENDING");
        assertEquals(ManagerApprovalStatusExpense.PENDING, pending);

        ManagerApprovalStatusExpense partiallyApproved = ManagerApprovalStatusExpense.valueOf("PARTIALLY_APPROVED");
        assertEquals(ManagerApprovalStatusExpense.PARTIALLY_APPROVED, partiallyApproved);
    }
}