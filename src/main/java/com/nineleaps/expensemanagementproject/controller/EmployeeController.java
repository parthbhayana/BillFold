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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.expensemanagementproject.entity.Employee;
import com.nineleaps.expensemanagementproject.service.IEmployeeService;

@RestController
@RequestMapping("/employee")
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
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeService.getEmployeeById(employeeId);

    }

    @DeleteMapping("/deleteEmployee/{employeeId}")
    public void deleteEmployeeById(@PathVariable Long employeeId) {
        employeeService.deleteEmployeeDetailsById(employeeId);
    }

    @PostMapping("/hideEmployee/{employeeId}")
    public void hideEmployee(@PathVariable Long employeeId) {
        employeeService.hideEmployee(employeeId);
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
                                    String managerName,@RequestParam String hrName,
                                    @RequestParam String hrEmail) {
        employeeService.editEmployeeDetails(employeeId, managerEmail, mobileNumber, officialEmployeeId, managerName, hrEmail, hrName);
    }



}