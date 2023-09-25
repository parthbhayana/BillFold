package com.nineleaps.expense_management_project.dto;

import com.nineleaps.expense_management_project.dto.EmployeeDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeDTOTest {

    @Test
    public void testParameterizedConstructor() {
        String employeeEmail = "john@example.com";
        String firstName = "John";
        String officialEmployeeId = "E12345";
        Long mobileNumber = 1234567890L;
        String middleName = "Middle";
        String lastName = "Doe";
        String managerEmail = "manager@example.com";

        EmployeeDTO employeeDTO = new EmployeeDTO(employeeEmail, firstName, officialEmployeeId,
                mobileNumber, middleName, lastName, managerEmail);

        // Verify that the constructor initializes the fields correctly
        assertEquals(employeeEmail, employeeDTO.getEmployeeEmail());
        assertEquals(firstName, employeeDTO.getFirstName());
        assertEquals(officialEmployeeId, employeeDTO.getOfficialEmployeeId());
        assertEquals(mobileNumber, employeeDTO.getMobileNumber());
        assertEquals(middleName, employeeDTO.getMiddleName());
        assertEquals(lastName, employeeDTO.getLastName());
        assertEquals(managerEmail, employeeDTO.getManagerEmail());
    }

    @Test
    public void testAdditionalConstructor() {
        String managerName = "Manager Name";
        String hrEmail = "hr@example.com";
        String hrName = "HR Name";

        EmployeeDTO employeeDTO = new EmployeeDTO(managerName, hrEmail, hrName);

        // Verify that the additional constructor initializes manager and HR fields correctly
        assertEquals(managerName, employeeDTO.getManagerName());
        assertEquals(hrEmail, employeeDTO.getHrEmail());
        assertEquals(hrName, employeeDTO.getHrName());
    }

    @Test
    public void testGetterAndSetterMethods() {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        // Set values using setter methods
        String employeeEmail = "jane@example.com";
        employeeDTO.setEmployeeEmail(employeeEmail);
        String firstName = "Jane";
        employeeDTO.setFirstName(firstName);
        String officialEmployeeId = "E54321";
        employeeDTO.setOfficialEmployeeId(officialEmployeeId);
        Long mobileNumber = 9876543210L;
        employeeDTO.setMobileNumber(mobileNumber);
        String middleName = "MiddleName";
        employeeDTO.setMiddleName(middleName);
        String lastName = "Smith";
        employeeDTO.setLastName(lastName);
        String managerEmail = "manager2@example.com";
        employeeDTO.setManagerEmail(managerEmail);

        // Verify that the getter methods return the expected values
        assertEquals(employeeEmail, employeeDTO.getEmployeeEmail());
        assertEquals(firstName, employeeDTO.getFirstName());
        assertEquals(officialEmployeeId, employeeDTO.getOfficialEmployeeId());
        assertEquals(mobileNumber, employeeDTO.getMobileNumber());
        assertEquals(middleName, employeeDTO.getMiddleName());
        assertEquals(lastName, employeeDTO.getLastName());
        assertEquals(managerEmail, employeeDTO.getManagerEmail());
    }
}
