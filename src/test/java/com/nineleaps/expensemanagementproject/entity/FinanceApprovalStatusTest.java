package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinanceApprovalStatusTest {

    @Test
    void values() {
        FinanceApprovalStatus[] statuses = FinanceApprovalStatus.values();
        assertEquals(4, statuses.length);
        assertArrayEquals(new FinanceApprovalStatus[] {
                FinanceApprovalStatus.REIMBURSED,
                FinanceApprovalStatus.REJECTED,
                FinanceApprovalStatus.PENDING,
                FinanceApprovalStatus.APPROVED
        }, statuses);
    }

    @Test
    void valueOf() {
        FinanceApprovalStatus reimbursed = FinanceApprovalStatus.valueOf("REIMBURSED");
        assertEquals(FinanceApprovalStatus.REIMBURSED, reimbursed);

        FinanceApprovalStatus rejected = FinanceApprovalStatus.valueOf("REJECTED");
        assertEquals(FinanceApprovalStatus.REJECTED, rejected);

        FinanceApprovalStatus pending = FinanceApprovalStatus.valueOf("PENDING");
        assertEquals(FinanceApprovalStatus.PENDING, pending);

        FinanceApprovalStatus approved = FinanceApprovalStatus.valueOf("APPROVED");
        assertEquals(FinanceApprovalStatus.APPROVED, approved);
    }
}