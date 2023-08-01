package com.nineleaps.expensemanagementproject.DTO;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class EmployeeDTOTest {

    @Test
     void testEmployeeDTOConstructor() {
        String employeeEmail = "employee@example.com";
        String firstName = "John";
        String officialEmployeeId = "E12345";
        Long mobileNumber = 1234567890L;
        String middleName = "Michael";
        String lastName = "Doe";
        String managerEmail = "manager@example.com";
        String managerName = "Jane Smith";

        EmployeeDTO employeeDTO = new EmployeeDTO(
                employeeEmail, firstName, officialEmployeeId, mobileNumber, middleName, lastName, managerEmail, managerName);

        assertEquals(employeeEmail, employeeDTO.getEmployeeEmail());
        assertEquals(firstName, employeeDTO.getFirstName());
        assertEquals(officialEmployeeId, employeeDTO.getOfficialEmployeeId());
        assertEquals(mobileNumber, employeeDTO.getMobileNumber());
        assertEquals(middleName, employeeDTO.getMiddleName());
        assertEquals(lastName, employeeDTO.getLastName());
        assertEquals(managerEmail, employeeDTO.getManagerEmail());
        assertEquals(managerName, employeeDTO.getManagerName());
    }

    @Test
    void testGetSetMethods() {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        assertNull(employeeDTO.getEmployeeEmail());
        assertNull(employeeDTO.getFirstName());
        assertNull(employeeDTO.getOfficialEmployeeId());
        assertNull(employeeDTO.getMobileNumber());
        assertNull(employeeDTO.getMiddleName());
        assertNull(employeeDTO.getLastName());
        assertNull(employeeDTO.getManagerEmail());
        assertNull(employeeDTO.getManagerName());

        String employeeEmail = "employee@example.com";
        String firstName = "John";
        String officialEmployeeId = "E12345";
        Long mobileNumber = 1234567890L;
        String middleName = "Michael";
        String lastName = "Doe";
        String managerEmail = "manager@example.com";
        String managerName = "Jane Smith";

        employeeDTO.setEmployeeEmail(employeeEmail);
        employeeDTO.setFirstName(firstName);
        employeeDTO.setOfficialEmployeeId(officialEmployeeId);
        employeeDTO.setMobileNumber(mobileNumber);
        employeeDTO.setMiddleName(middleName);
        employeeDTO.setLastName(lastName);
        employeeDTO.setManagerEmail(managerEmail);
        employeeDTO.setManagerName(managerName);

        assertEquals(employeeEmail, employeeDTO.getEmployeeEmail());
        assertEquals(firstName, employeeDTO.getFirstName());
        assertEquals(officialEmployeeId, employeeDTO.getOfficialEmployeeId());
        assertEquals(mobileNumber, employeeDTO.getMobileNumber());
        assertEquals(middleName, employeeDTO.getMiddleName());
        assertEquals(lastName, employeeDTO.getLastName());
        assertEquals(managerEmail, employeeDTO.getManagerEmail());
        assertEquals(managerName, employeeDTO.getManagerName());
    }
}
