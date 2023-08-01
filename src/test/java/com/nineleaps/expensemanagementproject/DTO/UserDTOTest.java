package com.nineleaps.expensemanagementproject.DTO;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        String employeeEmail = "john.doe@example.com";
        String imageUrl = "http://example.com/image.jpg";
        String firstName = "John";
        String middleName = "Middle";
        String lastName = "Doe";

        // Act
        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeEmail(employeeEmail);
        userDTO.setImageUrl(imageUrl);
        userDTO.setFirstName(firstName);
        userDTO.setMiddleName(middleName);
        userDTO.setLastName(lastName);

        // Assert
        Assertions.assertEquals(employeeEmail, userDTO.getEmployeeEmail());
        Assertions.assertEquals(imageUrl, userDTO.getImageUrl());
        Assertions.assertEquals(firstName, userDTO.getFirstName());
        Assertions.assertEquals(middleName, userDTO.getMiddleName());
        Assertions.assertEquals(lastName, userDTO.getLastName());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String employeeEmail = "john.doe@example.com";
        String imageUrl = "http://example.com/image.jpg";
        String firstName = "John";
        String middleName = "Middle";
        String lastName = "Doe";
        String fcm_token = "dIgqC2CYTDWBMfNMuRZAk_:APA91bHG1W_sMNDMuI_QwYsBc3FhwOQtOIimJxb0zqMOWd1FzJYdez2q-HkEp2wkQcw7ojDqt9wAHLXdpMs6EfvGvwtRqGByDByyQbt-j9v5H1GXNb9LBo7kEJKdsZzg3ndeuPsuH_KK";

        // Act
        UserDTO userDTO = new UserDTO(employeeEmail, imageUrl, firstName, middleName, lastName, fcm_token);

        // Assert
        Assertions.assertEquals(employeeEmail, userDTO.getEmployeeEmail());
        Assertions.assertEquals(imageUrl, userDTO.getImageUrl());
        Assertions.assertEquals(firstName, userDTO.getFirstName());
        Assertions.assertEquals(middleName, userDTO.getMiddleName());
        Assertions.assertEquals(lastName, userDTO.getLastName());
        Assertions.assertEquals(fcm_token , userDTO.getFcmToken());
    }

    @Test
    void testEmptyConstructor() {
        // Arrange

        // Act
        UserDTO userDTO = new UserDTO();

        // Assert
        Assertions.assertNull(userDTO.getEmployeeEmail());
        Assertions.assertNull(userDTO.getImageUrl());
        Assertions.assertNull(userDTO.getFirstName());
        Assertions.assertNull(userDTO.getMiddleName());
        Assertions.assertNull(userDTO.getLastName());
    }

    @Test
    void testSetNullValues() {
        // Arrange
        String employeeEmail = null;
        String imageUrl = null;
        String firstName = null;
        String middleName = null;
        String lastName = null;

        // Act
        UserDTO userDTO = new UserDTO();
        userDTO.setEmployeeEmail(employeeEmail);
        userDTO.setImageUrl(imageUrl);
        userDTO.setFirstName(firstName);
        userDTO.setMiddleName(middleName);
        userDTO.setLastName(lastName);

        // Assert
        Assertions.assertNull(userDTO.getEmployeeEmail());
        Assertions.assertNull(userDTO.getImageUrl());
        Assertions.assertNull(userDTO.getFirstName());
        Assertions.assertNull(userDTO.getMiddleName());
        Assertions.assertNull(userDTO.getLastName());
    }
}

