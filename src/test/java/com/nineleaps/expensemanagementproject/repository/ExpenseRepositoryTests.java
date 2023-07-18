package com.nineleaps.expensemanagementproject.repository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.entity.Reports;


@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ExpenseRepositoryTests {

    @Mock
    private ExpenseRepository expenseRepository;

    @Before
    public void setUp() {
        Expense expense = new Expense();
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
