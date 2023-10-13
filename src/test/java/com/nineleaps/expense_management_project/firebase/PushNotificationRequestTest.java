package com.nineleaps.expense_management_project.firebase;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PushNotificationRequestTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String title = "Test Title";
        String topic = "Test Topic";
        String message = "Test Message";
        String token = "Test Token";

        // Act
        PushNotificationRequest request = new PushNotificationRequest(title, topic, message, token);

        // Assert
        assertEquals(title, request.getTitle());
        assertEquals(topic, request.getTopic());
        assertEquals(message, request.getMessage());
        assertEquals(token, request.getToken());
    }

    @Test
    void testSetters() {
        // Arrange
        PushNotificationRequest request = new PushNotificationRequest();

        // Act
        String title = "New Title";
        String topic = "New Topic";
        String message = "New Message";
        String token = "New Token";

        request.setTitle(title);
        request.setTopic(topic);
        request.setMessage(message);
        request.setToken(token);

        // Assert
        assertEquals(title, request.getTitle());
        assertEquals(topic, request.getTopic());
        assertEquals(message, request.getMessage());
        assertEquals(token, request.getToken());
    }
}