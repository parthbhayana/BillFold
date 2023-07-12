package com.nineleaps.expensemanagementproject.repository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ExpenseRepositoryTests {

    @Mock
    private ExpenseRepository expenseRepository;

    private Expense expense;

    @Before
    public void setUp() {
        expense = new Expense();
        expense.setExpenseId(1L);
    }

    @Test
    public void testFindByReports() {
        List<Expense> retrievedExpenses = expenseRepository.findByReports(new Reports());
        assertNotNull(retrievedExpenses);
        assertEquals(0, retrievedExpenses.size());
    }

    // Write more test cases for other methods if present

}
