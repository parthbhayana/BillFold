package com.nineleaps.expensemanagementproject.firebase;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PushNotificationResponseTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        int status = 200;
        String message = "Success";

        // Act
        PushNotificationResponse response = new PushNotificationResponse(status, message);

        // Assert
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
    }

    @Test
    void testSetters() {
        // Arrange
        PushNotificationResponse response = new PushNotificationResponse();

        // Act
        int status = 400;
        String message = "Bad Request";

        response.setStatus(status);
        response.setMessage(message);

        // Assert
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
    }
}