package com.nineleaps.expensemanagementproject.DTO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeDTOTest {

    @Test
    void testGetEmployeeEmail() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeEmail("test@example.com");
        assertEquals("test@example.com", employeeDTO.getEmployeeEmail());
    }

    @Test
    void testSetEmployeeEmail() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeEmail("test@example.com");
        assertEquals("test@example.com", employeeDTO.getEmployeeEmail());
    }

    @Test
    void testGetFirstName() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        assertEquals("John", employeeDTO.getFirstName());
    }

    @Test
    void testSetFirstName() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        assertEquals("John", employeeDTO.getFirstName());
    }


    @Test
    void testGetHrName() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setHrName("HR Manager");
        assertEquals("HR Manager", employeeDTO.getHrName());
    }

    @Test
    void testSetHrName() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setHrName("HR Manager");
        assertEquals("HR Manager", employeeDTO.getHrName());
    }

    @Test
    void testSetOfficialEmployeeId() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String officialEmployeeId = "EMP123";

        // Act
        employeeDTO.setOfficialEmployeeId(officialEmployeeId);

        // Assert
        assertEquals(officialEmployeeId, employeeDTO.getOfficialEmployeeId());
    }

    @Test
    void testSetMobileNumber() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Long mobileNumber = 1234567890L;

        // Act
        employeeDTO.setMobileNumber(mobileNumber);

        // Assert
        assertEquals(mobileNumber, employeeDTO.getMobileNumber());
    }

    @Test
    void testSetManagerName() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String managerName = "John Doe";

        // Act
        employeeDTO.setManagerName(managerName);

        // Assert
        assertEquals(managerName, employeeDTO.getManagerName());
    }

    @Test
    void testSetHrEmail() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String hrEmail = "hr@example.com";

        // Act
        employeeDTO.setHrEmail(hrEmail);

        // Assert
        assertEquals(hrEmail, employeeDTO.getHrEmail());
    }

    @Test
    void testSetMiddleName() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String middleName = "Middle";

        // Act
        employeeDTO.setMiddleName(middleName);

        // Assert
        assertEquals(middleName, employeeDTO.getMiddleName());
    }

    @Test
    void testSetManagerEmail() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String managerEmail = "manager@example.com";

        // Act
        employeeDTO.setManagerEmail(managerEmail);

        // Assert
        assertEquals(managerEmail, employeeDTO.getManagerEmail());
    }
}
