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

import com.nineleaps.expensemanagementproject.controller.EmployeeController;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTests {

    private MockMvc mockMvc;

    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testGetAllEmployeeDetails() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeService.getAllEmployeeDetails()).thenReturn(employees);

        mockMvc.perform(get("/employee/listEmployee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testSaveEmployeeDetails() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        when(employeeService.saveEmployeeDetails(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/employee/insertEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1));
    }


}
