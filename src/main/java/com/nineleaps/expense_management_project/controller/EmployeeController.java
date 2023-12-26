package com.nineleaps.expense_management_project.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expense_management_project.entity.Employee;
import com.nineleaps.expense_management_project.service.IEmployeeService;

@RestController
@RequestMapping("/employee")
@ApiResponses(
        value = {
                @ApiResponse(code = 200, message = "SUCCESS"),
                @ApiResponse(code = 401, message = "UNAUTHORIZED"),
                @ApiResponse(code = 403, message = "ACCESS_FORBIDDEN"),
                @ApiResponse(code = 404, message = "NOT_FOUND"),
                @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
        })

public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @GetMapping("/listEmployee")
    public List<Employee> getAllEmployeeDetails() {
        return employeeService.getAllEmployeeDetails();
    }

    @PostMapping("/insertEmployee")
    public Employee save(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.saveEmployeeDetails(employeeDTO);
    }

    @PutMapping("/updateEmployee/{employeeId}")
    public Employee updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long employeeId) {
        return employeeService.updateEmployeeDetails(employeeDTO, employeeId);
    }

    @PostMapping("/additionalEmployeeDetails")
    public Optional<Employee> additionalEmployeeDetails(@RequestParam Long employeeId,
                                                        @RequestParam String officialEmployeeId,
                                                        @RequestParam String managerEmail,
                                                        @RequestParam Long mobileNumber,
                                                        @RequestParam String managerName, @RequestParam String hrName,
                                                        @RequestParam String hrEmail) {
        return employeeService.additionalEmployeeDetails(employeeId, officialEmployeeId, managerEmail, mobileNumber,
                managerName, hrEmail, hrName);
    }

    @GetMapping("/findEmployee/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            return ResponseEntity.ok(employee);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteEmployee/{employeeId}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long employeeId) {
        try {
            employeeService.deleteEmployeeDetailsById(employeeId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/hideEmployee/{employeeId}")
    public ResponseEntity<Void> hideEmployee(@PathVariable Long employeeId) {
        try {
            employeeService.hideEmployee(employeeId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/setFinanceAdmin")
    public void setFinanceAdmin(@RequestParam Long employeeId) {
        employeeService.setFinanceAdmin(employeeId);
    }

    @GetMapping("/getEmployeeDetails")
    public Optional<Employee> getEmployeeDetails(@RequestParam Long employeeId) {
        return employeeService.getEmployeeDetails(employeeId);
    }

    @PostMapping("/editEmployeeDetails")
    public void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId,
                                    String managerName, @RequestParam String hrName,
                                    @RequestParam String hrEmail) {
        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName, hrEmail, hrName);
    }
}