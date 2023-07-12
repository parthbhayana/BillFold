package com.nineleaps.expensemanagementproject.controller;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.nineleaps.expensemanagementproject.entity.Expense;
import com.nineleaps.expensemanagementproject.repository.ExpenseRepository;
import com.nineleaps.expensemanagementproject.service.IExpenseService;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseControllerTests {

    private MockMvc mockMvc;

    @Mock
    private IExpenseService expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseController expenseController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    @Test
    public void testSaveExpense() throws Exception {
        Expense expense = new Expense();
        expense.setExpenseId(1L);
        when(expenseService.addExpense(any(Expense.class), anyLong(), anyLong())).thenReturn(expense);

        mockMvc.perform(post("/insertExpenses/{employeeId}", 1L)
                        .param("categoryId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"expenseId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenseId").value(1));
    }

    @Test
    public void testGetAllExpenses() throws Exception {
        List<Expense> expenses = Arrays.asList(new Expense(), new Expense());
        when(expenseService.getAllExpenses()).thenReturn(expenses);

        mockMvc.perform(get("/showAllExpenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }



}

