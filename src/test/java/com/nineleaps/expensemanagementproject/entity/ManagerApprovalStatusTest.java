package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerApprovalStatusTest {

    @Test
    void values() {
        ManagerApprovalStatus[] statuses = ManagerApprovalStatus.values();
        assertEquals(5, statuses.length);
        assertArrayEquals(new ManagerApprovalStatus[] {
                ManagerApprovalStatus.APPROVED,
                ManagerApprovalStatus.REJECTED,
                ManagerApprovalStatus.PENDING,
                ManagerApprovalStatus.ACTION_REQUIRED,
                ManagerApprovalStatus.PARTIALLY_APPROVED
        }, statuses);
    }

    @Test
    void valueOf() {
        ManagerApprovalStatus approved = ManagerApprovalStatus.valueOf("APPROVED");
        assertEquals(ManagerApprovalStatus.APPROVED, approved);

        ManagerApprovalStatus rejected = ManagerApprovalStatus.valueOf("REJECTED");
        assertEquals(ManagerApprovalStatus.REJECTED, rejected);

        ManagerApprovalStatus pending = ManagerApprovalStatus.valueOf("PENDING");
        assertEquals(ManagerApprovalStatus.PENDING, pending);

        ManagerApprovalStatus actionRequired = ManagerApprovalStatus.valueOf("ACTION_REQUIRED");
        assertEquals(ManagerApprovalStatus.ACTION_REQUIRED, actionRequired);

        ManagerApprovalStatus partiallyApproved = ManagerApprovalStatus.valueOf("PARTIALLY_APPROVED");
        assertEquals(ManagerApprovalStatus.PARTIALLY_APPROVED, partiallyApproved);
    }
}