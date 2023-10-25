package com.nineleaps.expense_management_project.dto;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class UserDTOTest {

    @Test
    void testDefaultConstructor() {
        UserDTO userDTO = new UserDTO();

        // Verify that the default constructor initializes fields to their default values
        assertNull(userDTO.getEmployeeEmail());
        assertNull(userDTO.getImageUrl());
        assertNull(userDTO.getFirstName());
        assertNull(userDTO.getMiddleName());
        assertNull(userDTO.getLastName());
        assertNull(userDTO.getFcmToken());
    }

    @Test
    void testSetFcmToken() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        String fcmToken = "fcmToken123";

        // Act
        userDTO.setFcmToken(fcmToken);

        // Assert
        assertEquals(fcmToken, userDTO.getFcmToken());
   }
}