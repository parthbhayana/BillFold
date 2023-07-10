package com.nineleaps.expensemanagementproject.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    private Employee employee;
    @BeforeEach
    void setUp() {
        Long employeeId = 1L;
        String officialEmployeeId = "E001";
        String firstName = "John";
        String middleName = "Doe";
        String lastName = "Smith";
        String employeeEmail = "john@example.com";
        String managerEmail = "manager@example.com";
        String managerName = "Manager";
        Long mobileNumber = 1234567890L;
        Boolean isFinanceAdmin = false;
        String imageUrl = "https://example.com/image.jpg";
        Boolean isHidden = false;
        List<Expense> expenseList = new ArrayList<>();
        String role = "EMPLOYEE";

        employee = new Employee(employeeId, officialEmployeeId, firstName, middleName, lastName, employeeEmail,
                managerEmail, managerName, mobileNumber, isFinanceAdmin, imageUrl, isHidden, expenseList, role);
    }

    @Test
    void getEmployeeId() {
        Long employeeId = 1L;
        assertEquals(employeeId, employee.getEmployeeId());
    }

    @Test
    void setEmployeeId() {
        Long newEmployeeId = 2L;
        employee.setEmployeeId(newEmployeeId);
        assertEquals(newEmployeeId, employee.getEmployeeId());
    }

    @Test
    void getOfficialEmployeeId() {
        String officialEmployeeId = "E001";
        assertEquals(officialEmployeeId, employee.getOfficialEmployeeId());
    }

    @Test
    void setOfficialEmployeeId() {
        String newOfficialEmployeeId = "E002";
        employee.setOfficialEmployeeId(newOfficialEmployeeId);
        assertEquals(newOfficialEmployeeId, employee.getOfficialEmployeeId());
    }

    @Test
    void getFirstName() {
        String firstName = "John";
        assertEquals(firstName, employee.getFirstName());
    }

    @Test
    void setFirstName() {
        String newFirstName = "Jane";
        employee.setFirstName(newFirstName);
        assertEquals(newFirstName, employee.getFirstName());
    }

    @Test
    void getMiddleName() {
        String middleName = "Doe";
        assertEquals(middleName, employee.getMiddleName());
    }

    @Test
    void setMiddleName() {
        String newMiddleName = "Sue";
        employee.setMiddleName(newMiddleName);
        assertEquals(newMiddleName, employee.getMiddleName());
    }

    @Test
    void getLastName() {
        String lastName = "Smith";
        assertEquals(lastName, employee.getLastName());
    }

    @Test
    void setLastName() {
        String newLastName = "Johnson";
        employee.setLastName(newLastName);
        assertEquals(newLastName, employee.getLastName());
    }

    @Test
    void getEmployeeEmail() {
        String employeeEmail = "john@example.com";
        assertEquals(employeeEmail, employee.getEmployeeEmail());
    }

    @Test
    void setEmployeeEmail() {
        String newEmployeeEmail = "jane@example.com";
        employee.setEmployeeEmail(newEmployeeEmail);
        assertEquals(newEmployeeEmail, employee.getEmployeeEmail());
    }

    @Test
    void getManagerEmail() {
        String managerEmail = "manager@example.com";
        assertEquals(managerEmail, employee.getManagerEmail());
    }

    @Test
    void setManagerEmail() {
        String newManagerEmail = "newmanager@example.com";
        employee.setManagerEmail(newManagerEmail);
        assertEquals(newManagerEmail, employee.getManagerEmail());
    }

    @Test
    void setManagerName() {
        String newManagerName = "New Manager";
        employee.setManagerName(newManagerName);
        assertEquals(newManagerName, employee.getManagerName());
    }

    @Test
    void getMobileNumber() {
        Long mobileNumber = 1234567890L;
        assertEquals(mobileNumber, employee.getMobileNumber());
    }

    @Test
    void setMobileNumber() {
        Long newMobileNumber = 9876543210L;
        employee.setMobileNumber(newMobileNumber);
        assertEquals(newMobileNumber, employee.getMobileNumber());
    }

    @Test
    void getIsFinanceAdmin() {
        Boolean isFinanceAdmin = false;
        assertFalse(employee.getIsFinanceAdmin());
    }

    @Test
    void setIsFinanceAdmin() {
        employee.setIsFinanceAdmin(true);
        assertTrue(employee.getIsFinanceAdmin());
    }

    @Test
    void getImageUrl() {
        String imageUrl = "https://example.com/image.jpg";
        assertEquals(imageUrl, employee.getImageUrl());
    }

    @Test
    void setImageUrl() {
        String imageUrl = "https://example.com/image.jpg";
        employee.setImageUrl(imageUrl);
        assertEquals(imageUrl, employee.getImageUrl());
    }

    @Test
    void getIsHidden() {
        assertFalse(employee.getIsHidden());
    }

    @Test
    void setIsHidden() {
        employee.setIsHidden(true);
        assertTrue(employee.getIsHidden());
    }

    @Test
    void getExpenseList() {
        assertNotNull(employee.getExpenseList());
        assertTrue(employee.getExpenseList().isEmpty());
    }

    @Test
    void setExpenseList() {
        Expense expense = new Expense();
        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        employee.setExpenseList(expenseList);
        assertEquals(expenseList, employee.getExpenseList());
    }

    @Test
    void getRole() {
        assertEquals("EMPLOYEE", employee.getRole());
    }

    @Test
    void setRole() {
        String role = "MANAGER";
        employee.setRole(role);
        assertEquals(role, employee.getRole());
    }
}