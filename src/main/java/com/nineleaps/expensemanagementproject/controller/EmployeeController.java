package com.nineleaps.expensemanagementproject.controller;


/**
      EmployeeController

          Manages operations related to employee details including retrieval, modification, and addition.

          ## Endpoints

          ### GET /employee/listEmployee

          - **Description:** Fetches details of all employees.
          - **Returns:** List of Employee objects containing employee details.

          ### POST /employee/insertEmployee

          - **Description:** Saves new employee details.
          - **Request Body:** EmployeeDTO object containing employee details.
          - **Returns:** The saved Employee object.

          ### PUT /employee/updateEmployee/{employeeId}

          - **Description:** Updates employee details based on the provided ID and information.
          - **Path Variable:** employeeId (Long) - The ID of the employee to be updated.
          - **Request Body:** EmployeeDTO object containing updated employee details.
          - **Returns:** The updated Employee object.

          ### POST /employee/additionalEmployeeDetails

         - **Description:** Adds additional details for an employee.
          - **Request Parameters:**
              - employeeId (Long) - The ID of the employee.
              - officialEmployeeId (String) - Official employee ID.
              - managerEmail (String) - Manager's email.
              - mobileNumber (Long) - Mobile number.
              - managerName (String) - Manager's name.
              - hrName (String) - HR name.
              - hrEmail (String) - HR email.
          - **Returns:** Optional<Employee> containing additional details if available.

          ### GET /employee/findEmployee/{employeeId}

          - **Description:** Retrieves an employee by their ID.
          - **Path Variable:** employeeId (Long) - The ID of the employee to be retrieved.
          - **Returns:** The Employee object matching the provided ID.

          // ... (Continuing with the rest of the endpoints)

          ## Note
          - Some endpoints may require certain parameters or request bodies for successful execution.
          - Ensure proper authorization and validation for sensitive operations.
 */
import java.util.List;
import java.util.Optional;
import com.nineleaps.expensemanagementproject.DTO.EmployeeDTO;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @GetMapping("/listEmployee")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public List<Employee> getAllEmployeeDetails() {
        log.info("Fetching all employee details");

        List<Employee> employees = employeeService.getAllEmployeeDetails();

        log.info("Retrieved {} employee details", employees.size());

        return employees;
    }

    @PostMapping("/insertEmployee")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public Employee save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Inserting new employee details: {}", employeeDTO);

        Employee employee = employeeService.saveEmployeeDetails(employeeDTO);

        log.info("New employee details saved: {}", employee);

        return employee;
    }

    @PutMapping("/updateEmployee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public Employee updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long employeeId) {
        log.info("Updating employee with ID {}: {}", employeeId, employeeDTO);

        Employee updatedEmployee = employeeService.updateEmployeeDetails(employeeDTO, employeeId);

        log.info("Updated employee with ID {}: {}", employeeId, updatedEmployee);

        return updatedEmployee;
    }


    @PostMapping("/additionalEmployeeDetails")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public Optional<Employee> additionalEmployeeDetails(@RequestParam Long employeeId,
                                                        @RequestParam String officialEmployeeId,
                                                        @RequestParam String managerEmail,
                                                        @RequestParam Long mobileNumber,
                                                        @RequestParam String managerName,
                                                        @RequestParam String hrName,
                                                        @RequestParam String hrEmail) {
        log.info("Adding additional details for employee with ID: {}", employeeId);

        Optional<Employee> updatedEmployee = employeeService.additionalEmployeeDetails(employeeId, officialEmployeeId,
                managerEmail, mobileNumber, managerName, hrEmail, hrName);

        log.info("Additional details added for employee with ID {}: {}", employeeId, updatedEmployee.orElse(null));

        return updatedEmployee;
    }

    @GetMapping("/findEmployee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        log.info("Fetching employee details for ID: {}", employeeId);

        Employee employee = employeeService.getEmployeeById(employeeId);

        log.info("Retrieved employee details for ID {}: {}", employeeId, employee);

        return employee;
    }

    @DeleteMapping("/deleteEmployee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public void deleteEmployeeById(@PathVariable Long employeeId) {
        log.info("Deleting employee with ID: {}", employeeId);

        employeeService.deleteEmployeeDetailsById(employeeId);

        log.info("Employee with ID {} deleted successfully", employeeId);
    }


    @PostMapping("/hideEmployee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public void hideEmployee(@PathVariable Long employeeId) {
        log.info("Hiding employee with ID: {}", employeeId);

        employeeService.hideEmployee(employeeId);

        log.info("Employee with ID {} hidden successfully", employeeId);
    }

    @PostMapping("/setFinanceAdmin")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public void setFinanceAdmin(@RequestParam Long employeeId) {
        log.info("Setting employee with ID {} as Finance Admin", employeeId);

        employeeService.setFinanceAdmin(employeeId);

        log.info("Employee with ID {} set as Finance Admin", employeeId);
    }

    @GetMapping("/getEmployeeDetails")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public Optional<Employee> getEmployeeDetails(@RequestParam Long employeeId) {
        log.info("Fetching details for employee with ID: {}", employeeId);

        Optional<Employee> employeeDetails = employeeService.getEmployeeDetails(employeeId);

        log.info("Retrieved details for employee with ID {}: {}", employeeId, employeeDetails.orElse(null));

        return employeeDetails;
    }


    @PostMapping("/editEmployeeDetails")
    @PreAuthorize("hasAnyAuthority('FINANCE_ADMIN','EMPLOYEE')")
    public void editEmployeeDetails(Long employeeId, String managerEmail, Long mobileNumber, String officialEmployeeId,
                                    String managerName, @RequestParam String hrName,
                                    @RequestParam String hrEmail) {
        log.info("Editing details for employee with ID: {}", employeeId);

        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName, hrEmail, hrName);

        log.info("Details for employee with ID {} edited successfully", employeeId);
    }
}
