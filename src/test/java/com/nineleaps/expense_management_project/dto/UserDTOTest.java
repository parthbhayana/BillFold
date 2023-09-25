package com.nineleaps.expense_management_project.dto;

import com.nineleaps.expense_management_project.dto.UserDTO;
import com.nineleaps.expense_management_project.entity.Employee;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserDTOTest {

    @Test
    public void testDefaultConstructor() {
        UserDTO userDTO = new UserDTO();

        // Verify that the default constructor initializes fields to their default values
        assertEquals(null, userDTO.getEmployeeEmail());
        assertEquals(null, userDTO.getImageUrl());
        assertEquals(null, userDTO.getFirstName());
        assertEquals(null, userDTO.getMiddleName());
        assertEquals(null, userDTO.getLastName());
        assertEquals(null, userDTO.getFcmToken());
    }

    @Test
    public void testParameterizedConstructor() {
        String employeeEmail = "user@example.com";
        String imageUrl = "https://example.com/profile.jpg";
        String firstName = "John";
        String middleName = "Doe";
        String lastName = "Smith";
        String fcmToken = "fcmToken123";

        UserDTO userDTO = new UserDTO(employeeEmail, imageUrl, firstName, middleName, lastName, fcmToken);

        // Verify that the parameterized constructor initializes the fields correctly
        assertEquals(employeeEmail, userDTO.getEmployeeEmail());
        assertEquals(imageUrl, userDTO.getImageUrl());
        assertEquals(firstName, userDTO.getFirstName());
        assertEquals(middleName, userDTO.getMiddleName());
        assertEquals(lastName, userDTO.getLastName());
        assertEquals(fcmToken, userDTO.getFcmToken());
    }

    @Test
    public void testGetterAndSetterMethods() {
        UserDTO userDTO = new UserDTO();

        // Set values using setter methods
        String employeeEmail = "user@example.com";
        String imageUrl = "https://example.com/profile.jpg";
        String firstName = "John";
        String middleName = "Doe";
        String lastName = "Smith";
        String fcmToken = "fcmToken123";

        userDTO.setEmployeeEmail(employeeEmail);
        userDTO.setImageUrl(imageUrl);
        userDTO.setFirstName(firstName);
        userDTO.setMiddleName(middleName);
        userDTO.setLastName(lastName);
        userDTO.setFcmToken(fcmToken);

        // Verify that the getter methods return the expected values
        assertEquals(employeeEmail, userDTO.getEmployeeEmail());
        assertEquals(imageUrl, userDTO.getImageUrl());
        assertEquals(firstName, userDTO.getFirstName());
        assertEquals(middleName, userDTO.getMiddleName());
        assertEquals(lastName, userDTO.getLastName());
        assertEquals(fcmToken, userDTO.getFcmToken());
    }



}
