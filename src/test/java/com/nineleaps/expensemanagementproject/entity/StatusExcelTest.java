package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusExcelTest {

    @Test
    void values() {
        StatusExcel[] statuses = StatusExcel.values();
        assertEquals(4, statuses.length);
        assertEquals(StatusExcel.ALL, statuses[0]);
        assertEquals(StatusExcel.REIMBURSED, statuses[1]);
        assertEquals(StatusExcel.COMPLETED, statuses[2]);
    }

    @Test
    void valueOf() {
        StatusExcel statusAll = StatusExcel.valueOf("ALL");
        StatusExcel statusReimbursed = StatusExcel.valueOf("REIMBURSED");
        StatusExcel statusPending = StatusExcel.valueOf("PENDING");

        assertEquals(StatusExcel.ALL, statusAll);
        assertEquals(StatusExcel.REIMBURSED, statusReimbursed);
        assertEquals(StatusExcel.PENDING, statusPending);
    }
}