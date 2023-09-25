package com.nineleaps.expense_management_project.controller;

import java.util.List;
import java.util.Optional;

import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.service.IEmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    // Endpoint to retrieve a list of all employee details
    @GetMapping("/listEmployee")
    public List<Employee> getAllEmployeeDetails() {
        return employeeService.getAllEmployeeDetails();
    }

    // Endpoint to insert a new employee
    @PostMapping("/insertEmployee")
    public Employee save(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.saveEmployeeDetails(employeeDTO);
    }

    // Endpoint to update an employee's details by their ID
    @PutMapping("/updateEmployee/{employeeId}")
    public Employee updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long employeeId) {
        return employeeService.updateEmployeeDetails(employeeDTO, employeeId);
    }

    // Endpoint to add additional details for an employee
    @PostMapping("/additionalEmployeeDetails")
    public Optional<Employee> additionalEmployeeDetails(@RequestParam Long employeeId,
                                                        @RequestParam String officialEmployeeId,
                                                        @RequestParam String managerEmail,
                                                        @RequestParam Long mobileNumber,
                                                        @RequestParam String managerName,
                                                        @RequestParam String hrName,
                                                        @RequestParam String hrEmail) {
        return employeeService.additionalEmployeeDetails(employeeId, officialEmployeeId, managerEmail, mobileNumber,
                managerName, hrEmail, hrName);
    }

    // Endpoint to find an employee by their ID
    @GetMapping("/findEmployee/{employeeId}")
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    // Endpoint to delete an employee by their ID
    @DeleteMapping("/deleteEmployee/{employeeId}")
    public void deleteEmployeeById(@PathVariable Long employeeId) {
        employeeService.deleteEmployeeDetailsById(employeeId);
    }

    // Endpoint to hide an employee by their ID
    @PostMapping("/hideEmployee/{employeeId}")
    public void hideEmployee(@PathVariable Long employeeId) {
        employeeService.hideEmployee(employeeId);
    }

    // Endpoint to set an employee as a finance admin
    @PostMapping("/setFinanceAdmin")
    public void setFinanceAdmin(@RequestParam Long employeeId) {
        employeeService.setFinanceAdmin(employeeId);
    }

    // Endpoint to get employee details by their ID
    @GetMapping("/getEmployeeDetails")
    public Optional<Employee> getEmployeeDetails(@RequestParam Long employeeId) {
        return employeeService.getEmployeeDetails(employeeId);
    }

    // Endpoint to edit an employee's details
    @PostMapping("/editEmployeeDetails")
    public void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId,
                                    String managerName, @RequestParam String hrName,
                                    @RequestParam String hrEmail) {
        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName, hrEmail, hrName);
    }
}
